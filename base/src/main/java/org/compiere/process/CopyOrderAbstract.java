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

package org.compiere.process;

import java.sql.Timestamp;

/** Generated Process for (Quote convert)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class CopyOrderAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "C_Order QuoteCopy";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Quote convert";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 231;
	/**	Parameter Name for Order	*/
	public static final String C_ORDER_ID = "C_Order_ID";
	/**	Parameter Name for Document Type	*/
	public static final String C_DOCTYPE_ID = "C_DocType_ID";
	/**	Parameter Name for Document Date	*/
	public static final String DATEDOC = "DateDoc";
	/**	Parameter Name for Close Document	*/
	public static final String ISCLOSEDOCUMENT = "IsCloseDocument";
	/**	Parameter Value for Order	*/
	private int orderId;
	/**	Parameter Value for Document Type	*/
	private int docTypeId;
	/**	Parameter Value for Document Date	*/
	private Timestamp dateDoc;
	/**	Parameter Value for Close Document	*/
	private boolean isCloseDocument;

	@Override
	protected void prepare() {
		orderId = getParameterAsInt(C_ORDER_ID);
		docTypeId = getParameterAsInt(C_DOCTYPE_ID);
		dateDoc = getParameterAsTimestamp(DATEDOC);
		isCloseDocument = getParameterAsBoolean(ISCLOSEDOCUMENT);
	}

	/**	 Getter Parameter Value for Order	*/
	protected int getOrderId() {
		return orderId;
	}

	/**	 Setter Parameter Value for Order	*/
	protected void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	/**	 Getter Parameter Value for Document Type	*/
	protected int getDocTypeId() {
		return docTypeId;
	}

	/**	 Setter Parameter Value for Document Type	*/
	protected void setDocTypeId(int docTypeId) {
		this.docTypeId = docTypeId;
	}

	/**	 Getter Parameter Value for Document Date	*/
	protected Timestamp getDateDoc() {
		return dateDoc;
	}

	/**	 Setter Parameter Value for Document Date	*/
	protected void setDateDoc(Timestamp dateDoc) {
		this.dateDoc = dateDoc;
	}

	/**	 Getter Parameter Value for Close Document	*/
	protected boolean isCloseDocument() {
		return isCloseDocument;
	}

	/**	 Setter Parameter Value for Close Document	*/
	protected void setIsCloseDocument(boolean isCloseDocument) {
		this.isCloseDocument = isCloseDocument;
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