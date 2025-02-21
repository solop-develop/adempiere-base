/** Copyright (C) 2023, e-Evolution , http://e-evolution.com This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
  * Public License along with this program. If not, see <http://www.gnu.org/licenses/>. Email: victor.perez@e-evolution.com , http://www.e-evolution.com , http://github.com/e-Evolution Created by
  * victor.perez@e-evolution.com , www.e-evolution.com
  */

package org.eevolution.context.service.domain.validators

import java.util.logging.Level


import org.compiere.Adempiere
import org.adempiere.core.domains.models.*
import org.compiere.model.*
import org.compiere.util.*

class ContractModelValidator extends ModelValidator {
  
  val withoutErrors = ""

  override def initialize(
    engine: ModelValidationEngine,
    client: MClient
  ): Unit = {

    engine.addModelChange(I_C_Invoice.Table_Name, this)
    engine.addModelChange(I_C_InvoiceLine.Table_Name, this)
    engine.addModelChange(I_C_OrderLine.Table_Name, this)
    engine.addModelChange(I_M_InOut.Table_Name, this)
    engine.addModelChange(I_M_InOutLine.Table_Name, this)
  }

  override def getAD_Client_ID: Int = {
    val clientId = Env.getAD_Client_ID(Env.getCtx)
    clientId
  }

  override def login(
    orgIc:  Int,
    roleId: Int,
    userId: Int
  ): String = withoutErrors

  override def modelChange(
    instance:  PO,
    typeEvent: Int
  ): String =
    instance match {
      case _
          if I_M_InOut.Table_ID == instance.get_Table_ID()
            && instance.get_ColumnIndex("S_Contract_ID") > 0
            && ModelValidator.TYPE_AFTER_NEW == typeEvent =>
        val shipment = instance.asInstanceOf[MInOut]
        if shipment.getC_Order_ID > 0 then {
          val contractId =
            getContractId(shipment.getC_Order_ID, shipment.get_TableName())
          if contractId > 0 then {
            shipment.set_ValueOfColumn("S_Contract_ID", contractId)
            shipment.saveEx()
          }
        }
        withoutErrors
      case _
          if I_M_InOutLine.Table_ID == instance.get_Table_ID()
            && instance.get_ColumnIndex("S_ContractLine_ID") > 0
            && ModelValidator.TYPE_AFTER_NEW == typeEvent =>
        val shipmentLine = instance.asInstanceOf[MInOutLine]
        if shipmentLine.getC_OrderLine_ID > 0 then {
          val contractLineId = getContractLineId(
            shipmentLine.getC_OrderLine_ID,
            shipmentLine.get_TableName()
          )
          if contractLineId > 0 then {
            shipmentLine.set_ValueOfColumn("S_ContractLine_ID", contractLineId)
            shipmentLine.saveEx()
          }
        }
        withoutErrors
      case _
          if I_C_Invoice.Table_ID == instance.get_Table_ID()
            && instance.get_ColumnIndex("S_Contract_ID") > 0
            && ModelValidator.TYPE_AFTER_NEW == typeEvent =>
        val invoice = instance.asInstanceOf[MInvoice]
        if invoice.getC_Order_ID > 0 then {
          val contractId =
            getContractId(invoice.getC_Order_ID, invoice.get_TrxName())
          if contractId > 0 then {
            invoice.set_ValueOfColumn("S_Contract_ID", contractId)
            invoice.saveEx()
          }
        }
        withoutErrors
      case _
          if I_C_InvoiceLine.Table_ID == instance.get_Table_ID()
            && instance.get_ColumnIndex("S_ContractLine_ID") > 0
            && ModelValidator.TYPE_AFTER_NEW == typeEvent =>
        val invoiceLine = instance.asInstanceOf[MInvoiceLine]
        if invoiceLine.getC_OrderLine_ID > 0 then {
          val contractLineId = getContractLineId(
            invoiceLine.getC_OrderLine_ID,
            invoiceLine.get_TableName()
          )
          if contractLineId > 0 then {
            invoiceLine.set_ValueOfColumn("S_ContractLine_ID", contractLineId)
            invoiceLine.saveEx()
          }
        }
        withoutErrors
      case _ => withoutErrors
    }

  override def docValidate(
    instance: PO,
    timing:   Int
  ): String =
    instance match {
      // Validate reverse Payment
      case _ if I_C_Order.Table_ID == instance.get_Table_ID && ModelValidator.TIMING_AFTER_COMPLETE == timing =>
        withoutErrors
      case _ if I_C_Invoice.Table_ID == instance.get_Table_ID && ModelValidator.TIMING_AFTER_COMPLETE == timing =>
        withoutErrors
      case _ if I_S_Contract.Table_ID == instance.get_Table_ID && ModelValidator.TIMING_AFTER_COMPLETE == timing =>
        withoutErrors
      case _ => withoutErrors
    }

  private def getContractId(
    orderId: Int,
    trxName: String
  ): Int =
    DB.getSQLValue(
      trxName,
      "SELECT S_Contract_ID FROM C_Order WHERE C_Order_ID = ?",
      orderId
    )

  private def getContractLineId(
    orderLineId: Int,
    trxName:     String
  ): Int =
    DB.getSQLValue(
      trxName,
      "SELECT S_ContractLine_ID FROM C_OrderLine WHERE C_OrderLine_ID = ?",
      orderLineId
    )

}

object ContractModelValidator {

  def main(args: Array[String]): Unit = {
    Adempiere.startup(false)
    Ini.setProperty(Ini.P_UID, "SuperUser")
    Ini.setProperty(Ini.P_PWD, "System")
    Ini.setProperty(Ini.P_ROLE, "GardenWorld Admin")
    Ini.setProperty(Ini.P_CLIENT, "GardenWorld")
    Ini.setProperty(Ini.P_ORG, "HQ")
    Ini.setProperty(Ini.P_WAREHOUSE, "HQ Warehouse")
    Ini.setProperty(Ini.P_LANGUAGE, "English")
    val login = new Login(Env.getCtx)
    login.batchLogin
    CLogMgt.setLevel(Level.SEVERE)
  }

}
