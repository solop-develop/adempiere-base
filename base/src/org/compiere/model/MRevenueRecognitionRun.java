/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.                                     *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net                                                  *
 * or https://github.com/adempiere/adempiere/blob/develop/license.html        *
 *****************************************************************************/
package org.compiere.model;

import org.adempiere.core.domains.models.X_C_RevenueRecognition_Run;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.exceptions.PeriodClosedException;
import org.compiere.process.DocAction;
import org.compiere.process.DocOptions;
import org.compiere.process.DocumentEngine;
import org.compiere.process.DocumentReversalEnabled;
import org.compiere.recognition.support.AccrualRecognition;
import org.compiere.recognition.support.IRecognitionRevenue;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;

import java.io.File;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/** Generated Model for C_RevenueRecognition_Run
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class MRevenueRecognitionRun extends X_C_RevenueRecognition_Run implements DocAction, DocumentReversalEnabled, DocOptions {

	/**
	 *
	 */
	private static final long serialVersionUID = 20250708L;
	/**	Process Message 			*/
	private String processMsg = null;

    /** Standard Constructor */
    public MRevenueRecognitionRun(Properties ctx, int C_RevenueRecognition_Run_ID, String trxName)
    {
      super (ctx, C_RevenueRecognition_Run_ID, trxName);
    }

    /** Load Constructor */
    public MRevenueRecognitionRun(Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

	public MRevenueRecognitionRun(MRevenueRecognitionPlan plan, String transactionName) {
		this(plan.getCtx(), 0, transactionName);
		setClientOrg(plan);
		setC_RevenueRecognition_Plan_ID(plan.getC_RevenueRecognition_Plan_ID());
		setRecognizedAmt(Env.ZERO);
		setSourceRecognizedAmt(Env.ZERO);
	}

	/**
	 * 	Get Document Info
	 *	@return document info (untranslated)
	 */
	public String getDocumentInfo()
	{
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		return dt.getName() + " " + getDocumentNo();
	}	//	getDocumentInfo

	/**
	 * 	Create PDF
	 *	@return File or null
	 */
	public File createPDF ()
	{
		try
		{
			File temp = File.createTempFile(get_TableName() + get_ID() +"_", ".pdf");
			return createPDF (temp);
		}
		catch (Exception e)
		{
			log.severe("Could not create PDF - " + e.getMessage());
		}
		return null;
	}	//	getPDF

	/**
	 * 	Create PDF file
	 *	@param file output file
	 *	@return file if success
	 */
	public File createPDF (File file)
	{
	//	ReportEngine re = ReportEngine.get (getCtx(), ReportEngine.INVOICE, getC_Invoice_ID());
	//	if (re == null)
			return null;
	//	return re.getPDF(file);
	}	//	createPDF

	
	/**************************************************************************
	 * 	Process document
	 *	@param processAction document action
	 *	@return true if performed
	 */
	public boolean processIt (String processAction)
	{
		m_processMsg = null;
		DocumentEngine engine = new DocumentEngine (this, getDocStatus());
		return engine.processIt (processAction, getDocAction());
	}	//	processIt
	
	/**	Process Message 			*/
	private String		m_processMsg = null;
	/**	Just Prepared Flag			*/
	private boolean		m_justPrepared = false;
	private boolean isReversal;

	/**
	 * 	Unlock Document.
	 * 	@return true if success 
	 */
	public boolean unlockIt()
	{
		log.info("unlockIt - " + toString());
	//	setProcessing(false);
		return true;
	}	//	unlockIt
	
	/**
	 * 	Invalidate Document
	 * 	@return true if success 
	 */
	public boolean invalidateIt()
	{
		log.info("invalidateIt - " + toString());
		setDocAction(DOCACTION_Prepare);
		return true;
	}	//	invalidateIt

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if (getC_DocType_ID() <= 0) {
			Optional<MDocType> doctypeOptional = Arrays.stream(MDocType.getOfDocBaseType(getCtx(), "RRR")).min((docType1, docType2) -> Boolean.compare(docType2.isDefault(), docType1.isDefault()));
			doctypeOptional.ifPresent(docType -> setC_DocType_ID(docType.getC_DocType_ID()));
			if (getC_DocType_ID() <= 0)
				throw new AdempiereException("@C_DocType_ID@ @FillMandatory@");

		}
		if(getC_RevenueRecognition_ID() <= 0) {
			MRevenueRecognitionPlan plan = getRevenueRecognitionPlan();
			setC_RevenueRecognition_ID(plan.getC_RevenueRecognition_ID());
		}
		return super.beforeSave(newRecord);
	}

	/**
	 *	Prepare Document
	 * 	@return new status (In Progress or Invalid) 
	 */
	public String prepareIt()
	{
		log.info(toString());
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());

		//	Std Period open?
		if (!MPeriod.isOpen(getCtx(), getDateDoc(), dt.getDocBaseType(), getAD_Org_ID()))
		{
			m_processMsg = "@PeriodClosed@";
			return DocAction.STATUS_Invalid;
		}
		runRecognition();
		//	Add up Amounts
		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_PREPARE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		m_justPrepared = true;
		if (!DOCACTION_Complete.equals(getDocAction()))
			setDocAction(DOCACTION_Complete);
		return DocAction.STATUS_InProgress;
	}	//	prepareIt

	private void runRecognition() {
		if(isReversal()) {
			return;
		}
		MRevenueRecognitionPlan recognitionPlan = new MRevenueRecognitionPlan(getCtx(), getC_RevenueRecognition_Plan_ID(), get_TrxName());
		MRevenueRecognition recognition = (MRevenueRecognition) recognitionPlan.getC_RevenueRecognition();
		IRecognitionRevenue recognitionEngine = null;
		if(recognition.getRecognitionType().equals(MRevenueRecognition.RECOGNITIONTYPE_Accrual)) {
			recognitionEngine = new AccrualRecognition();
		} else {
			throw new AdempiereException("@RecognitionType@ Unimplemented");
		}
		String message = recognitionEngine.run(this);
		if(message != null) {
			addDescription(Msg.parseTranslation(getCtx(), message));
			saveEx();
		}
		recognitionPlan.updateRecognizedAmount(TimeUtil.getDayTime(getDateDoc(), new Timestamp(System.currentTimeMillis())));
	}

	public void addDescription(String description) {
		if(getDescription() != null) {
			setDescription(getDescription() + Env.NL + description);
		} else {
			setDescription(description);
		}
	}

	/**
	 * 	Approve Document
	 * 	@return true if success 
	 */
	public boolean  approveIt()
	{
		log.info("approveIt - " + toString());
		setIsApproved(true);
		return true;
	}	//	approveIt
	
	/**
	 * 	Reject Approval
	 * 	@return true if success 
	 */
	public boolean rejectIt()
	{
		log.info("rejectIt - " + toString());
		setIsApproved(false);
		return true;
	}	//	rejectIt

	public MRevenueRecognitionPlan getRevenueRecognitionPlan() {
		return (MRevenueRecognitionPlan) getC_RevenueRecognition_Plan();
	}

	public MRevenueRecognition getRevenueRecognition(){
		MRevenueRecognitionPlan plan = getRevenueRecognitionPlan();
		if(plan != null) {
			return (MRevenueRecognition) plan.getC_RevenueRecognition();
		}
		return null;
	}
	
	/**
	 * 	Complete Document
	 * 	@return new status (Complete, In Progress, Invalid, Waiting ..)
	 */
	public String completeIt()
	{
		//	Re-Check
		if (!m_justPrepared)
		{
			String status = prepareIt();
			if (!DocAction.STATUS_InProgress.equals(status))
				return status;
		}

		m_processMsg = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_BEFORE_COMPLETE);
		if (m_processMsg != null)
			return DocAction.STATUS_Invalid;
		
		//	Implicit Approval
		if (!isApproved())
			approveIt();
		log.info(toString());
		//
		
		//	User Validation
		String valid = ModelValidationEngine.get().fireDocValidate(this, ModelValidator.TIMING_AFTER_COMPLETE);
		if (valid != null)
		{
			m_processMsg = valid;
			return DocAction.STATUS_Invalid;
		}
		//	Set Definitive Document No
		setDefiniteDocumentNo();

		setProcessed(true);
		setDocAction(DOCACTION_Close);
		return DocAction.STATUS_Completed;
	}	//	completeIt
	
	/**
	 * 	Set the definite document number after completed
	 */
	private void setDefiniteDocumentNo() {
		MDocType dt = MDocType.get(getCtx(), getC_DocType_ID());
		if (dt.isOverwriteDateOnComplete()) {
			setDateDoc(new Timestamp(System.currentTimeMillis()));
		}
		if (dt.isOverwriteSeqOnComplete()) {
			String value = null;
			int index = p_info.getColumnIndex("C_DocType_ID");
			if (index == -1)
				index = p_info.getColumnIndex("C_DocTypeTarget_ID");
			if (index != -1)		//	get based on Doc Type (might return null)
				value = DB.getDocumentNo(get_ValueAsInt(index), get_TrxName(), true);
			if (value != null) {
				setDocumentNo(value);
			}
		}
	}

	/**
	 * 	Void Document.
	 * 	Same as Close.
	 * 	@return true if success 
	 */
	public boolean voidIt()
	{
		log.info(toString());
		boolean retValue = false;
		if (DOCSTATUS_Closed.equals(getDocStatus())
				|| DOCSTATUS_Reversed.equals(getDocStatus())
				|| DOCSTATUS_Voided.equals(getDocStatus()))
		{
			processMsg = "Document Closed: " + getDocStatus();
			setDocAction(DOCACTION_None);
			return false;
		}

		//	Not Processed
		if (DOCSTATUS_Drafted.equals(getDocStatus())
				|| DOCSTATUS_Invalid.equals(getDocStatus())
				|| DOCSTATUS_InProgress.equals(getDocStatus())
				|| DOCSTATUS_Approved.equals(getDocStatus()))

		{
			processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_VOID);
			if (processMsg != null)
				return false;

			//	Set lines to 0
			setRecognizedAmt(Env.ZERO);
			setSourceRecognizedAmt(Env.ZERO);
			addDescription(Msg.getMsg(getCtx(), "Voided"));
			retValue = true;
		}
		else
		{
			boolean accrual = false;
			try
			{
				MPeriod.testPeriodOpen(getCtx(), getDateDoc(), MPeriodControl.DOCBASETYPE_PaymentAllocation, getAD_Org_ID());
			}
			catch (PeriodClosedException e)
			{
				accrual = true;
			}

			if (accrual)
				return reverseAccrualIt();
			else
				return reverseCorrectIt();
		}

		// After Void
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_VOID);
		if (processMsg != null)
			return false;

		setDocAction(DOCACTION_None);

		return retValue;
	}	//	voidIt

	/**
	 * 	Close Document.
	 * 	Cancel not delivered Qunatities
	 * 	@return true if success
	 */
	public boolean closeIt()
	{
		log.info(toString());
		// Before Close
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_CLOSE);
		if (processMsg != null)
			return false;

		setDocAction(DOCACTION_None);

		// After Close
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_CLOSE);
		if (processMsg != null)
			return false;

		return true;
	}	//	closeIt

	/**
	 * 	Reverse Correction
	 * 	@return true if success
	 */
	public boolean reverseCorrectIt()
	{
		log.info(toString());
		// Before reverseCorrect
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSECORRECT);
		if (processMsg != null)
			return false;

		MRevenueRecognitionRun reversal = reverseIt(true);
		if (reversal == null)
			return false;

		// After reverseCorrect
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSECORRECT);
		if (processMsg != null)
			return false;

		setDocAction(DOCACTION_None);
		return true;
	}	//	reverseCorrectionIt

	/**
	 * 	Reverse Accrual - none
	 * 	@return false
	 */
	public boolean reverseAccrualIt()
	{
		log.info(toString());
		// Before reverseAccrual
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_BEFORE_REVERSEACCRUAL);
		if (processMsg != null)
			return false;

		MRevenueRecognitionRun reversal = reverseIt(true);
		if (reversal == null)
			return false;
		// After reverseAccrual
		processMsg = ModelValidationEngine.get().fireDocValidate(this,ModelValidator.TIMING_AFTER_REVERSEACCRUAL);
		if (processMsg != null)
			return false;

		setDocAction(DOCACTION_None);
		return true;
	}	//	reverseAccrualIt

	@Override
	public MRevenueRecognitionRun reverseIt(boolean b) {
		log.info("reverseCorrectIt - " + toString());
		return reverseIt(TimeUtil.getDay(System.currentTimeMillis()));
	}

	public MRevenueRecognitionRun reverseIt(Timestamp date) {
		MRevenueRecognitionRun reverse = new MRevenueRecognitionRun(getCtx(), 0, get_TrxName());
		if(date != null) {
			reverse.setDateDoc(date);
		} else {
			reverse.setDateDoc(getDateDoc());
		}
		PO.copyValues(this, reverse);
		reverse.setRecognizedAmt(getRecognizedAmt().negate());
		reverse.setSourceRecognizedAmt(getSourceRecognizedAmt().negate());
		reverse.setReversal(true);
		reverse.setDocStatus(DOCSTATUS_Drafted);
		reverse.setDocAction(DOCACTION_Complete);
		reverse.saveEx();
		if(!reverse.processIt(DOCACTION_Complete)) {
			throw new AdempiereException(reverse.getProcessMsg());
		}
		reverse.saveEx();
		reverse.setDocStatus(DOCSTATUS_Reversed);
		reverse.setDocAction(DOCACTION_None);
		reverse.setReversal_ID(getC_RevenueRecognition_Run_ID());
		reverse.saveEx();
		setReversal_ID(reverse.getC_RevenueRecognition_Run_ID());
		setDocStatus(DOCSTATUS_Reversed);
		setDocAction(DOCACTION_None);
		saveEx();
		return reverse;
	}

	@Override
	public boolean isReversal() {
		return getReversal_ID() > 0 || isReversal;
	}

	@Override
	public void setReversal(boolean isReversal) {
		this.isReversal = isReversal;
	}

	/** 
	 * 	Re-activate
	 * 	@return true if success 
	 */
	public boolean reActivateIt()
	{
		log.info("reActivateIt - " + toString());
		setProcessed(false);
		return true;
	}	//	reActivateIt
	
	
	/*************************************************************************
	 * 	Get Summary
	 *	@return Summary of Document
	 */
	public String getSummary()
	{
		StringBuffer sb = new StringBuffer();
		sb.append(getDocumentNo());
	//	sb.append(": ")
	//		.append(Msg.translate(getCtx(),"TotalLines")).append("=").append(getTotalLines())
	//		.append(" (#").append(getLines(false).length).append(")");
		//	 - Description
		if (getDescription() != null && !getDescription().isEmpty())
			sb.append(" - ").append(getDescription());
		return sb.toString();
	}	//	getSummary

	/**
	 * 	Get Process Message
	 *	@return clear text error message
	 */
	public String getProcessMsg()
	{
		return m_processMsg;
	}	//	getProcessMsg
	
	/**
	 * 	Get Document Owner (Responsible)
	 *	@return AD_User_ID
	 */
	public int getDoc_User_ID()
	{
	//	return getSalesRep_ID();
		return getCreatedBy();
	}	//	getDoc_User_ID

	/**
	 * 	Get Document Approval Amount
	 *	@return amount
	 */
	public BigDecimal getApprovalAmt()
	{
		return getRecognizedAmt();	//getTotalLines();
	}	//	getApprovalAmt

	/**
	 * 	Get Document Currency
	 *	@return C_Currency_ID
	 */
	public int getC_Currency_ID() {
		MRevenueRecognitionPlan plan = getRevenueRecognitionPlan();
		if(plan != null) {
			return plan.getC_Currency_ID();
		}
		return 0;
	}	//	getC_Currency_ID

    @Override
    public String toString()
    {
      StringBuffer sb = new StringBuffer ("MRevenueRecognitionRun[")
        .append(getSummary()).append("]");
      return sb.toString();
    }

	@Override
	public int customizeValidActions(String docStatus, Object processing,
									 String orderType, String isSOTrx, int tableId,
									 String[] docAction, String[] options, int index) {
		//	Valid Document Action
		if (Table_ID == tableId) {
			if (docStatus.equals(DocumentEngine.STATUS_Drafted)
					|| docStatus.equals(DocumentEngine.STATUS_InProgress)
					|| docStatus.equals(DocumentEngine.STATUS_Invalid)) {
				options[index++] = DocumentEngine.ACTION_Prepare;
			}
			//	Complete                    ..  CO
			else if (docStatus.equals(DocumentEngine.STATUS_Completed)) {
				options[index++] = DocumentEngine.ACTION_Void;
				options[index++] = DocumentEngine.ACTION_ReActivate;
				options[index++] = DocumentEngine.ACTION_Close;

			} else if (docStatus.equals(DocumentEngine.STATUS_Closed)) {
				options[index++] = DocumentEngine.ACTION_None;
			}
		}
		return index;
	}
}