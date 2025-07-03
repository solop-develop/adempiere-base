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

package org.spin.pos.process;

import java.sql.Timestamp;
import org.compiere.process.SvrProcess;

/** Generated Process for (POS Sales Detail and Collection)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class inf_POS_Sales_Detail_And_CollectionAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "inf_POS_Sales_Detail_And_Collection";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "POS Sales Detail and Collection";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54543;
	/**	Parameter Name for Organization	*/
	public static final String AD_ORG_ID = "AD_Org_ID";
	/**	Parameter Name for POS Terminal	*/
	public static final String C_POS_ID = "C_POS_ID";
	/**	Parameter Name for Business Partner 	*/
	public static final String C_BPARTNER_ID = "C_BPartner_ID";
	/**	Parameter Name for Date Invoiced	*/
	public static final String DATEINVOICED = "DateInvoiced";
	/**	Parameter Value for Organization	*/
	private int orgId;
	/**	Parameter Value for POS Terminal	*/
	private int pOSId;
	/**	Parameter Value for Business Partner 	*/
	private int bPartnerId;
	/**	Parameter Value for Date Invoiced	*/
	private Timestamp dateInvoiced;
	/**	Parameter Value for Date Invoiced(To)	*/
	private Timestamp dateInvoicedTo;

	@Override
	protected void prepare() {
		orgId = getParameterAsInt(AD_ORG_ID);
		pOSId = getParameterAsInt(C_POS_ID);
		bPartnerId = getParameterAsInt(C_BPARTNER_ID);
		dateInvoiced = getParameterAsTimestamp(DATEINVOICED);
		dateInvoicedTo = getParameterToAsTimestamp(DATEINVOICED);
	}

	/**	 Getter Parameter Value for Organization	*/
	protected int getOrgId() {
		return orgId;
	}

	/**	 Setter Parameter Value for Organization	*/
	protected void setOrgId(int orgId) {
		this.orgId = orgId;
	}

	/**	 Getter Parameter Value for POS Terminal	*/
	protected int getPOSId() {
		return pOSId;
	}

	/**	 Setter Parameter Value for POS Terminal	*/
	protected void setPOSId(int pOSId) {
		this.pOSId = pOSId;
	}

	/**	 Getter Parameter Value for Business Partner 	*/
	protected int getBPartnerId() {
		return bPartnerId;
	}

	/**	 Setter Parameter Value for Business Partner 	*/
	protected void setBPartnerId(int bPartnerId) {
		this.bPartnerId = bPartnerId;
	}

	/**	 Getter Parameter Value for Date Invoiced	*/
	protected Timestamp getDateInvoiced() {
		return dateInvoiced;
	}

	/**	 Setter Parameter Value for Date Invoiced	*/
	protected void setDateInvoiced(Timestamp dateInvoiced) {
		this.dateInvoiced = dateInvoiced;
	}

	/**	 Getter Parameter Value for Date Invoiced(To)	*/
	protected Timestamp getDateInvoicedTo() {
		return dateInvoicedTo;
	}

	/**	 Setter Parameter Value for Date Invoiced(To)	*/
	protected void setDateInvoicedTo(Timestamp dateInvoicedTo) {
		this.dateInvoicedTo = dateInvoicedTo;
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