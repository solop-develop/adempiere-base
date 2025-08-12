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

package org.solop.process;

import org.compiere.process.SvrProcess;

import java.sql.Timestamp;

/** Generated Process for (Processor Batch Closing)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class PaymentProcessorBatchClosingAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "PP_ProcessorBatchClosing";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Processor Batch Closing";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54870;
	/**	Parameter Name for Bank Account	*/
	public static final String C_BANKACCOUNT_ID = "C_BankAccount_ID";
	/**	Parameter Name for Payment Processor	*/
	public static final String C_PAYMENTPROCESSOR_ID = "C_PaymentProcessor_ID";
	/**	Parameter Name for Store Payment Method	*/
	public static final String C_PAYMENTMETHOD_ID = "C_PaymentMethod_ID";
	/**	Parameter Name for Document Date	*/
	public static final String DATEDOC = "DateDoc";
	/**	Parameter Value for Bank Account	*/
	private int bankAccountId;
	/**	Parameter Value for Payment Processor	*/
	private int paymentProcessorId;
	/**	Parameter Value for Store Payment Method	*/
	private int paymentMethodId;
	/**	Parameter Value for Document Date	*/
	private Timestamp dateDoc;

	@Override
	protected void prepare() {
		bankAccountId = getParameterAsInt(C_BANKACCOUNT_ID);
		paymentProcessorId = getParameterAsInt(C_PAYMENTPROCESSOR_ID);
		paymentMethodId = getParameterAsInt(C_PAYMENTMETHOD_ID);
		dateDoc = getParameterAsTimestamp(DATEDOC);
	}

	/**	 Getter Parameter Value for Bank Account	*/
	protected int getBankAccountId() {
		return bankAccountId;
	}

	/**	 Setter Parameter Value for Bank Account	*/
	protected void setBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
	}

	/**	 Getter Parameter Value for Payment Processor	*/
	protected int getPaymentProcessorId() {
		return paymentProcessorId;
	}

	/**	 Setter Parameter Value for Payment Processor	*/
	protected void setPaymentProcessorId(int paymentProcessorId) {
		this.paymentProcessorId = paymentProcessorId;
	}

	/**	 Getter Parameter Value for Store Payment Method	*/
	protected int getPaymentMethodId() {
		return paymentMethodId;
	}

	/**	 Setter Parameter Value for Store Payment Method	*/
	protected void setPaymentMethodId(int paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
	}

	/**	 Getter Parameter Value for Document Date	*/
	protected Timestamp getDateDoc() {
		return dateDoc;
	}

	/**	 Setter Parameter Value for Document Date	*/
	protected void setDateDoc(Timestamp dateDoc) {
		this.dateDoc = dateDoc;
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