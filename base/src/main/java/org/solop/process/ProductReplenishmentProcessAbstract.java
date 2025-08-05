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

/** Generated Process for (Product Replenishment Process)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class ProductReplenishmentProcessAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "ProductReplenishmentProcess";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Product Replenishment Process";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54795;
	/**	Parameter Name for Create	*/
	public static final String REPLENISHMENTCREATE = "ReplenishmentCreate";
	/**	Parameter Name for Document Type	*/
	public static final String C_DOCTYPE_ID = "C_DocType_ID";
	/**	Parameter Name for Business Partner 	*/
	public static final String C_BPARTNER_ID = "C_BPartner_ID";
	/**	Parameter Value for Create	*/
	private String replenishmentCreate;
	/**	Parameter Value for Document Type	*/
	private int docTypeId;
	/**	Parameter Value for Business Partner 	*/
	private int bPartnerId;

	@Override
	protected void prepare() {
		replenishmentCreate = getParameterAsString(REPLENISHMENTCREATE);
		docTypeId = getParameterAsInt(C_DOCTYPE_ID);
		bPartnerId = getParameterAsInt(C_BPARTNER_ID);
	}

	/**	 Getter Parameter Value for Create	*/
	protected String getReplenishmentCreate() {
		return replenishmentCreate;
	}

	/**	 Setter Parameter Value for Create	*/
	protected void setReplenishmentCreate(String replenishmentCreate) {
		this.replenishmentCreate = replenishmentCreate;
	}

	/**	 Getter Parameter Value for Document Type	*/
	protected int getDocTypeId() {
		return docTypeId;
	}

	/**	 Setter Parameter Value for Document Type	*/
	protected void setDocTypeId(int docTypeId) {
		this.docTypeId = docTypeId;
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