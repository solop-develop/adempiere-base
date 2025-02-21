/** Copyright (C) 2023, e-Evolution , http://e-evolution.com This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
  * Public License along with this program. If not, see <http://www.gnu.org/licenses/>. Email: victor.perez@e-evolution.com , http://www.e-evolution.com , http://github.com/e-Evolution Created by
  * victor.perez@e-evolution.com , www.e-evolution.com
  */

package org
package eevolution
package context
package service
package infrastructure
package domain
package entities

import org.adempiere.core.domains.models.*
import org.compiere.model.*
import org.compiere.model.ModelValidator.*
import org.compiere.process.{DocAction, DocOptions, DocumentEngine}
import org.compiere.util.{DB, Env}
import org.eevolution.context.service.infrastructure.domain.entities.*
import org.eevolution.manufacturing.model.MPPPeriodDefinition

import java.io.File
import java.sql.{ResultSet, Timestamp}
import java.util
import java.util.{Comparator, Properties}

/** Contract Entity
  *
  * @param ctx
  * @param id
  * @param rs
  * @param trxName
  */
class MSContract(
  ctx:     Properties,
  id:      Int,
  rs:      ResultSet,
  trxName: String)
    extends X_S_Contract(ctx: Properties, id: Int, trxName: String) with DocAction with DocOptions {

  def this(
    ctx:     Properties,
    id:      Int,
    trxName: String
  ) = {
    this(ctx, id, null, trxName)
    load(id, trxName)
  }

  def this(
    ctx:     Properties,
    rs:      ResultSet,
    trxName: String
  ) = {
    this(ctx, 999999999, rs, trxName)
    load(rs)
  }

  def compare[A](
    obj1:                A,
    obj2:                A
  )(implicit comparator: Comparator[A]
  ) = comparator.compare(obj1, obj2)

  override protected def beforeSave(newRecord: Boolean): Boolean = return true

  override protected def afterSave(
    newRecord: Boolean,
    success:   Boolean
  ): Boolean = return true

  override protected def beforeDelete(): Boolean = return true

  def getDocumentInfo(): String = {
    val documentType = MDocType.get(getCtx, getC_DocType_ID)
    return s"${documentType.getName}   $getDocumentNo"
  }

  /** Process Message */
  private var processMessage = ""

  /** Just Prepared Flag */
  private var justPrepared = false

  override def processIt(processAction: String): Boolean = {
    val engine = new DocumentEngine(this, getDocStatus)
    engine.processIt(processAction, getDocAction)
  }

  override def unlockIt(): Boolean = {
    log.info(s"unlockIt -  $toString")
    return true
  }

  override def invalidateIt(): Boolean = {
    log.info(s"invalidateIt - $toString")
    setDocAction(X_S_Contract.DOCACTION_Prepare)
    return true
  }

  override def prepareIt(): String = {
    log.info(toString)
    processMessage = ModelValidationEngine.get().fireDocValidate(this, TIMING_BEFORE_PREPARE)
    if processMessage != null then return DocAction.STATUS_Invalid

    val dt = MDocType.get(getCtx, getC_DocType_ID)

    //	Std Period open?
    if !MPeriod.isOpen(getCtx, getDateDoc, dt.getDocBaseType, getAD_Org_ID) then {
      processMessage = "@PeriodClosed@"
      return DocAction.STATUS_Invalid
    }

    val contractLines = getLines
    if contractLines.isEmpty then {
      processMessage = "@NoLines@"
      return DocAction.STATUS_Invalid
    } else {
      var totalLines = Env.ZERO
      contractLines.forEach { contractLine =>
        totalLines = totalLines.add(contractLine.getLineNetAmt)
      }
      setTotalLines(totalLines)
      setGrandTotal(totalLines)
    }

    //	Add up Amounts
    processMessage = ModelValidationEngine.get.fireDocValidate(this, TIMING_AFTER_PREPARE)
    if processMessage != null then return DocAction.STATUS_Invalid
    justPrepared = true;
    if !(X_S_Contract.DOCACTION_Complete == getDocAction) then setDocAction(X_S_Contract.DOCACTION_Complete)
    return DocAction.STATUS_InProgress
  }

  override def approveIt(): Boolean = {
    log.info(s"approveIt - $toString")
    setIsApproved(true)
    return true
  }

  override def rejectIt(): Boolean = {
    log.info(s"rejectIt - $toString")
    setIsApproved(false)
    return true
  }

  override def completeIt(): String = {
    //	Re-Check
    if !justPrepared then {
      val status: String = prepareIt
      if !(DocAction.STATUS_InProgress == status) then return status
    }

    processMessage = ModelValidationEngine.get.fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE)
    if processMessage != null then {
      return DocAction.STATUS_Invalid
    }

    //	Implicit Approval
    if !isApproved then {
      approveIt
    }
    log.info(toString)
    //
    if getPP_PeriodDefinition_ID > 0 then creteTimeSheetReportExpense()

    //	User Validation
    val valid: String = ModelValidationEngine.get.fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE)
    if valid != null then {
      processMessage = valid
      return DocAction.STATUS_Invalid
    }
    //	Set Definitive Document No
    setDefiniteDocumentNo()
    setProcessed(true)
    setDocAction(X_S_Contract.DOCACTION_Close)
    return DocAction.STATUS_Completed
  }

  def creteTimeSheetReportExpense(): Unit = {

    var lineNo = 10
    val periodDefinition = new MPPPeriodDefinition(getCtx, getPP_PeriodDefinition_ID, get_TrxName())
    getLines.forEach(contractLine =>
      periodDefinition.getPeriodsOrderBy(I_PP_Period.COLUMNNAME_PeriodNo).forEach { period =>
        val timeReport = new MTimeExpense(getCtx, 0, get_TrxName())
        timeReport.set_ValueOfColumn("S_Contract_ID", get_ID)
        timeReport.setC_BPartner_ID(getC_BPartner_ID)
        timeReport.setDateReport(period.getStartDate)
        timeReport.set_ValueOfColumn("PP_PeriodDefinition_ID", periodDefinition.get_ID)
        timeReport.set_ValueOfColumn("PP_Period_ID", period.get_ID)
        timeReport.set_ValueOfColumn("PP_Calendar_ID", periodDefinition.getPP_Calendar_ID)
        timeReport.setDescription(contractLine.getDescription)
        timeReport.setM_PriceList_ID(getM_PriceList_ID)
        timeReport.setM_Warehouse_ID(getM_Warehouse_ID)
        timeReport.set_ValueOfColumn("C_Project_ID", getC_Project_ID)
        timeReport.set_ValueOfColumn("C_Activity_ID", getC_Activity_ID)
        timeReport.set_ValueOfColumn("C_Campaign_ID", getC_Campaign_ID)
        timeReport.setAD_Org_ID(getAD_Org_ID)
        timeReport.set_ValueOfColumn("AD_OrgTrx_ID", getAD_OrgTrx_ID)
        timeReport.set_ValueOfColumn("User1_ID", getUser1_ID)
        timeReport.set_ValueOfColumn("User2_ID", getUser1_ID)
        timeReport.set_ValueOfColumn("User3_ID", getUser1_ID)
        timeReport.set_ValueOfColumn("User4_ID", getUser1_ID)
        timeReport.setDocStatus(DocAction.ACTION_Complete)
        timeReport.setDocAction(DocAction.STATUS_Drafted)
        timeReport.set_ValueOfColumn("IsSOTrx", isSOTrx)
        timeReport.saveEx

        val timeReportLine = new MTimeExpenseLine(getCtx, 0, get_TableName())
        timeReportLine.setS_TimeExpense_ID(timeReport.get_ID())
        timeReportLine.set_ValueOfColumn("S_ContractLine_ID", contractLine.get_ID)
        timeReportLine.setLine(lineNo)
        timeReportLine.set_ValueOfColumn("PP_Calendar_ID", periodDefinition.getPP_Calendar_ID)
        timeReportLine.set_ValueOfColumn("PP_PeriodDefinition_ID", periodDefinition.get_ID())
        timeReportLine.set_ValueOfColumn("PP_Period_ID", period.get_ID)
        timeReportLine.setDateExpense(period.getStartDate)
        timeReportLine.setM_Product_ID(contractLine.getM_Product_ID)
        timeReportLine.setQty(contractLine.getQtyOrdered)
        timeReportLine.setC_BPartner_ID(contractLine.getC_BPartner_ID)
        timeReportLine.setDescription(contractLine.getDescription)
        if isSOTrx then {
          timeReportLine.setIsInvoiced(true)
          timeReportLine.setInvoicePrice(contractLine.getPriceActual)
        } else timeReportLine.setExpenseAmt(contractLine.getPriceActual)

        timeReportLine.setC_Currency_ID(contractLine.getC_Currency_ID)
        timeReportLine.setC_Tax_ID(contractLine.getC_Tax_ID)
        timeReportLine.setLineNetAmt(contractLine.getLineNetAmt)
        timeReportLine.setC_Activity_ID(contractLine.getC_Activity_ID)
        timeReportLine.setC_Campaign_ID(contractLine.getC_Campaign_ID)
        timeReportLine.setAD_Org_ID(contractLine.getAD_Org_ID)
        timeReportLine.set_ValueOfColumn("AD_OrgTrx_ID", contractLine.getAD_OrgTrx_ID)
        timeReportLine.set_ValueOfColumn("User1_ID", contractLine.getUser1_ID)
        timeReportLine.set_ValueOfColumn("User2_ID", contractLine.getUser1_ID)
        timeReportLine.set_ValueOfColumn("User3_ID", contractLine.getUser1_ID)
        timeReportLine.set_ValueOfColumn("User4_ID", contractLine.getUser1_ID)
        timeReportLine.set_ValueOfColumn("C_Project_ID", contractLine.getC_Project_ID)
        timeReportLine.setC_ProjectPhase_ID(contractLine.getC_ProjectPhase_ID)
        timeReportLine.setC_ProjectTask_ID(contractLine.getC_ProjectTask_ID)
        timeReportLine.saveEx()
        lineNo = lineNo + 10
      }
    )
  }

  private def setDefiniteDocumentNo(): Unit = {
    val documentType = MDocType.get(getCtx, getC_DocType_ID)
    if documentType.isOverwriteDateOnComplete then setDateDoc(new Timestamp(System.currentTimeMillis))
    if documentType.isOverwriteSeqOnComplete then {
      var value = ""
      var index = p_info.getColumnIndex(I_C_DocType.COLUMNNAME_C_DocType_ID)
      if index != -1 then value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName, true)
      if value != null then setDocumentNo(value)
    }
  }

  override def voidIt(): Boolean = {
    log.info(s"voidIt - $toString")
    return closeIt
  }

  override def closeIt(): Boolean = {
    log.info(s"closeIt -  $toString")
    setDocAction(X_S_Contract.DOCACTION_None)
    return true
  }

  override def reverseCorrectIt(): Boolean = {
    log.info(s"reverseCorrectIt - $toString")
    return false
  }

  override def reverseAccrualIt(): Boolean = {
    log.info(s"reverseAccrualIt - $toString")
    return false
  }

  override def reActivateIt(): Boolean = {
    log.info("reActivateIt - " + toString)
    // Before reActivate
    processMessage = ModelValidationEngine.get.fireDocValidate(this, ModelValidator.TIMING_BEFORE_REACTIVATE)
    if processMessage != null then return false
    val documentType = MDocType.get(getCtx, getC_DocType_ID)

    // After reActivate
    processMessage = ModelValidationEngine.get.fireDocValidate(this, ModelValidator.TIMING_AFTER_REACTIVATE)
    if processMessage != null then {
      return false
    }

    setDocAction(DocAction.ACTION_Complete)
    setProcessed(false)
    setPosted(false)
    return true
  }

  override def customizeValidActions(
    docStatus:   String,
    processing:  Any,
    orderType:   String,
    isSOTrx:     String,
    AD_Table_ID: Int,
    docAction:   Array[String],
    options:     Array[String],
    index:       Int
  ): Int = {
    //	Valid Document Action
    var seqIndex = index
    if AD_Table_ID == I_S_Contract.Table_ID then {
      if docStatus == DocAction.STATUS_Drafted
        || docStatus == DocAction.STATUS_InProgress
        || docStatus == DocAction.STATUS_Invalid
      then {
        options {
          seqIndex += 1;
          seqIndex - 1
        } = DocAction.ACTION_Prepare
      } else { //	Complete                    ..  CO
        if docStatus == DocAction.STATUS_Completed then {
          options {
            seqIndex += 1;
            seqIndex - 1
          } = DocAction.ACTION_Void
          options {
            seqIndex += 1;
            seqIndex - 1
          } = DocAction.ACTION_ReActivate
          options {
            seqIndex += 1;
            seqIndex - 1
          } = DocAction.ACTION_Close
        } else if docStatus == DocAction.STATUS_Closed then {
          options {
            seqIndex += 1;
            seqIndex - 1
          } = DocAction.ACTION_None
        }
      }
    }
    seqIndex
  }

  override def getSummary: String = {
    val sb = new StringBuffer
    sb.append(getDocumentNo)
    if getDescription != null && getDescription.length > 0 then sb.append(" - ").append(getDescription)
    return sb.toString
  }

  def createPDF(file: File): File =
    // val reportEngine = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
    // if (re == null)
    null
  //	createPDF//	return re.getPDF(file);

  override def createPDF(): File = {
    try {
      val temp = File.createTempFile(get_TableName + get_ID + "_", ".pdf")
      return createPDF(temp)
    } catch {
      case e: Exception =>
        log.severe("Could not create PDF - " + e.getMessage)
    }
    return null
  }

  override def getProcessMsg: String = processMessage

  override def getDoc_User_ID: Int = getCreatedBy

  override def getApprovalAmt: java.math.BigDecimal = java.math.BigDecimal.ZERO

  def getLines: util.List[MSContractLine] = {
    val whereClause = s"${I_S_ContractLine.COLUMNNAME_S_Contract_ID}=?"
    val contractLines = new Query(getCtx, I_S_ContractLine.Table_Name, whereClause, get_TrxName())
      .setParameters(get_ID())
      .list[MSContractLine]
    contractLines
  }

}
