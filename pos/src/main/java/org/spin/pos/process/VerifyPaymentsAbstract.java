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

import org.compiere.process.SvrProcess;

/** Generated Process for (Verify Payments to Approve)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public abstract class VerifyPaymentsAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "POSVerifyPayments_Process";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Verify Payments to Approve";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54573;
	/**	Parameter Name for Payment Verified	*/
	public static final String ISPAYMENTVERIFIED = "IsPaymentVerified";
	/**	Parameter Name for Description	*/
	public static final String DESCRIPTION = "Description";
	/**	Parameter Value for Payment Verified	*/
	private String isPaymentVerified;
	/**	Parameter Value for Description	*/
	private String description;

	@Override
	protected void prepare() {
		isPaymentVerified = getParameterAsString(ISPAYMENTVERIFIED);
		description = getParameterAsString(DESCRIPTION);
	}

	/**	 Getter Parameter Value for Payment Verified	*/
	protected String getIsPaymentVerified() {
		return isPaymentVerified;
	}

	/**	 Setter Parameter Value for Payment Verified	*/
	protected void setIsPaymentVerified(String isPaymentVerified) {
		this.isPaymentVerified = isPaymentVerified;
	}

	/**	 Getter Parameter Value for Description	*/
	protected String getDescription() {
		return description;
	}

	/**	 Setter Parameter Value for Description	*/
	protected void setDescription(String description) {
		this.description = description;
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