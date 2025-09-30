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

package org.solop.freight.process;

import org.compiere.process.SvrProcess;

/** Generated Process for (Generate AP Invoice from Freight Order Header)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public abstract class GenerateAPIFromFreightHeaderAbstract extends SvrProcess {
	/** Process Value 	*/
	private static final String VALUE_FOR_PROCESS = "SOLOP_P_GenerateAPIFromFreightHeader";
	/** Process Name 	*/
	private static final String NAME_FOR_PROCESS = "Generate AP Invoice from Freight Order Header";
	/** Process Id 	*/
	private static final int ID_FOR_PROCESS = 54711;
	/**	Parameter Name for Overwrite Freight Cost Rule	*/
	public static final String ISOVERWRITEFREIGHTCOSTRULE = "IsOverwriteFreightCostRule";
	/**	Parameter Name for Freight Category	*/
	public static final String M_FREIGHTCATEGORY_ID = "M_FreightCategory_ID";
	/**	Parameter Value for Overwrite Freight Cost Rule	*/
	private boolean isOverwriteFreightCostRule;
	/**	Parameter Value for Freight Category	*/
	private int freightCategoryId;

	@Override
	protected void prepare() {
		isOverwriteFreightCostRule = getParameterAsBoolean(ISOVERWRITEFREIGHTCOSTRULE);
		freightCategoryId = getParameterAsInt(M_FREIGHTCATEGORY_ID);
	}

	/**	 Getter Parameter Value for Overwrite Freight Cost Rule	*/
	protected boolean isOverwriteFreightCostRule() {
		return isOverwriteFreightCostRule;
	}

	/**	 Setter Parameter Value for Overwrite Freight Cost Rule	*/
	protected void setIsOverwriteFreightCostRule(boolean isOverwriteFreightCostRule) {
		this.isOverwriteFreightCostRule = isOverwriteFreightCostRule;
	}

	/**	 Getter Parameter Value for Freight Category	*/
	protected int getFreightCategoryId() {
		return freightCategoryId;
	}

	/**	 Setter Parameter Value for Freight Category	*/
	protected void setFreightCategoryId(int freightCategoryId) {
		this.freightCategoryId = freightCategoryId;
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