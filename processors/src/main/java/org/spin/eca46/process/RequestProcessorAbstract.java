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

package org.spin.eca46.process;

import org.compiere.process.SvrProcess;

/** Generated Process for (Request Processor)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class RequestProcessorAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "ECA46_Run_Request_Processor";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Request Processor";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54660;
	/**	Parameter Name for Request Processor	*/
	public static final String R_REQUESTPROCESSOR_ID = "R_RequestProcessor_ID";
	/**	Parameter Value for Request Processor	*/
	private int requestProcessorId;

	@Override
	protected void prepare() {
		requestProcessorId = getParameterAsInt(R_REQUESTPROCESSOR_ID);
	}

	/**	 Getter Parameter Value for Request Processor	*/
	protected int getRequestProcessorId() {
		return requestProcessorId;
	}

	/**	 Setter Parameter Value for Request Processor	*/
	protected void setRequestProcessorId(int requestProcessorId) {
		this.requestProcessorId = requestProcessorId;
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