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

package org.compiere.recognition.process;

import org.compiere.process.SvrProcess;

import java.sql.Timestamp;

/** Generated Process for (Revenue Recognition Run)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class RevenueRecognitionRunAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "Revenue_Recognition_Run";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Revenue Recognition Run";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54854;
	/**	Parameter Name for Organization	*/
	public static final String AD_ORG_ID = "AD_Org_ID";
	/**	Parameter Name for Revenue Recognition	*/
	public static final String C_REVENUERECOGNITION_ID = "C_RevenueRecognition_ID";
	/**	Parameter Name for Document Date	*/
	public static final String DATEDOC = "DateDoc";
	/**	Parameter Name for Force	*/
	public static final String ISFORCE = "IsForce";
	/**	Parameter Name for Contract	*/
	public static final String S_CONTRACT_ID = "S_Contract_ID";
	/**	Parameter Name for Project	*/
	public static final String C_PROJECT_ID = "C_Project_ID";
	/**	Parameter Name for Order	*/
	public static final String C_ORDER_ID = "C_Order_ID";
	/**	Parameter Name for Invoice	*/
	public static final String C_INVOICE_ID = "C_Invoice_ID";
	/**	Parameter Name for Business Partner 	*/
	public static final String C_BPARTNER_ID = "C_BPartner_ID";
	/**	Parameter Value for Organization	*/
	private int orgId;
	/**	Parameter Value for Revenue Recognition	*/
	private int revenueRecognitionId;
	/**	Parameter Value for Document Date	*/
	private Timestamp dateDoc;
	/**	Parameter Value for Force	*/
	private boolean isForce;
	/**	Parameter Value for Contract	*/
	private int contractId;
	/**	Parameter Value for Project	*/
	private int projectId;
	/**	Parameter Value for Order	*/
	private int orderId;
	/**	Parameter Value for Invoice	*/
	private int invoiceId;
	/**	Parameter Value for Business Partner 	*/
	private int bPartnerId;

	@Override
	protected void prepare() {
		orgId = getParameterAsInt(AD_ORG_ID);
		revenueRecognitionId = getParameterAsInt(C_REVENUERECOGNITION_ID);
		dateDoc = getParameterAsTimestamp(DATEDOC);
		isForce = getParameterAsBoolean(ISFORCE);
		contractId = getParameterAsInt(S_CONTRACT_ID);
		projectId = getParameterAsInt(C_PROJECT_ID);
		orderId = getParameterAsInt(C_ORDER_ID);
		invoiceId = getParameterAsInt(C_INVOICE_ID);
		bPartnerId = getParameterAsInt(C_BPARTNER_ID);
	}

	/**	 Getter Parameter Value for Organization	*/
	protected int getOrgId() {
		return orgId;
	}

	/**	 Setter Parameter Value for Organization	*/
	protected void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	/**	 Getter Parameter Value for Revenue Recognition	*/
	protected int getRevenueRecognitionId() {
		return revenueRecognitionId;
	}

	/**	 Setter Parameter Value for Revenue Recognition	*/
	protected void setRevenueRecognitionId(int revenueRecognitionId) {
		this.revenueRecognitionId = revenueRecognitionId;
	}

	/**	 Getter Parameter Value for Document Date	*/
	protected Timestamp getDateDoc() {
		return dateDoc;
	}

	/**	 Setter Parameter Value for Document Date	*/
	protected void setDateDoc(Timestamp dateDoc) {
		this.dateDoc = dateDoc;
	}

	/**	 Getter Parameter Value for Force	*/
	protected boolean isForce() {
		return isForce;
	}

	/**	 Setter Parameter Value for Force	*/
	protected void setIsForce(boolean isForce) {
		this.isForce = isForce;
	}

	/**	 Getter Parameter Value for Contract	*/
	protected int getContractId() {
		return contractId;
	}

	/**	 Setter Parameter Value for Contract	*/
	protected void setContractId(int contractId) {
		this.contractId = contractId;
	}

	/**	 Getter Parameter Value for Project	*/
	protected int getProjectId() {
		return projectId;
	}

	/**	 Setter Parameter Value for Project	*/
	protected void setProjectId(int projectId) {
		this.projectId = projectId;
	}

	/**	 Getter Parameter Value for Order	*/
	protected int getOrderId() {
		return orderId;
	}

	/**	 Setter Parameter Value for Order	*/
	protected void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	/**	 Getter Parameter Value for Invoice	*/
	protected int getInvoiceId() {
		return invoiceId;
	}

	/**	 Setter Parameter Value for Invoice	*/
	protected void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	/**	 Getter Parameter Value for Business Partner 	*/
	protected int getBPartnerId() {
		return bPartnerId;
	}

	/**	 Setter Parameter Value for Business Partner 	*/
	protected void setBPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	/**	 Getter Parameter Value for Process ID	*/
	public static final int getProcessId() {
		return ID_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Value	*/
	public static final String getProcessValue() {
		return VALUE_FOR_PROCESS;
	}

	/**	 Getter Parameter Value for Process Name	*/
	public static final String getProcessName() {
		return NAME_FOR_PROCESS;
	}
}