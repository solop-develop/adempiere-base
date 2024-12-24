/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/

package org.spin.pos.process;

import java.sql.Timestamp;
import org.compiere.process.SvrProcess;

/** Generated Process for (Generate Refund from (POS))
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public abstract class GenerateRefundFromPOSAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "P_POS_GenerateRefundFromPOS";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Generate Refund from (POS)";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54594;
	/**	Parameter Name for Payment date	*/
	public static final String PAYDATE = "PayDate";
	/**	Parameter Name for Target Document Type	*/
	public static final String C_DOCTYPETARGET_ID = "C_DocTypeTarget_ID";
	/**	Parameter Name for Bank Account	*/
	public static final String C_BANKACCOUNT_ID = "C_BankAccount_ID";
	/**	Parameter Name for Document No	*/
	public static final String DOCUMENTNO = "DocumentNo";
	/**	Parameter Value for Payment date	*/
	private Timestamp payDate;
	/**	Parameter Value for Target Document Type	*/
	private int docTypeTargetId;
	/**	Parameter Value for Bank Account	*/
	private int bankAccountId;
	/**	Parameter Value for Document No	*/
	private String documentNo;

	@Override
	protected void prepare() {
		payDate = getParameterAsTimestamp(PAYDATE);
		docTypeTargetId = getParameterAsInt(C_DOCTYPETARGET_ID);
		bankAccountId = getParameterAsInt(C_BANKACCOUNT_ID);
		documentNo = getParameterAsString(DOCUMENTNO);
	}

	/**	 Getter Parameter Value for Payment date	*/
	protected Timestamp getPayDate() {
		return payDate;
	}

	/**	 Setter Parameter Value for Payment date	*/
	protected void setPayDate(Timestamp payDate) {
		this.payDate = payDate;
	}

	/**	 Getter Parameter Value for Target Document Type	*/
	protected int getDocTypeTargetId() {
		return docTypeTargetId;
	}

	/**	 Setter Parameter Value for Target Document Type	*/
	protected void setDocTypeTargetId(int docTypeTargetId) {
		this.docTypeTargetId = docTypeTargetId;
	}

	/**	 Getter Parameter Value for Bank Account	*/
	protected int getBankAccountId() {
		return bankAccountId;
	}

	/**	 Setter Parameter Value for Bank Account	*/
	protected void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	/**	 Getter Parameter Value for Document No	*/
	protected String getDocumentNo() {
		return documentNo;
	}

	/**	 Setter Parameter Value for Document No	*/
	protected void setDocumentNo(String documentNo) {
		this.documentNo = documentNo;
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