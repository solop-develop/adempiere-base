/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 or later of the                                  *
 * GNU General Public License as published                                    *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2023 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.eca56.util.support.documents;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.adempiere.core.domains.models.I_AD_Chart;
import org.adempiere.core.domains.models.I_AD_Image;
import org.adempiere.core.domains.models.I_AD_Ref_List;
import org.adempiere.core.domains.models.I_C_ElementValue;
import org.adempiere.core.domains.models.I_C_Location;
import org.adempiere.core.domains.models.I_M_AttributeSetInstance;
import org.adempiere.core.domains.models.I_M_Locator;
import org.adempiere.core.domains.models.I_S_ResourceAssignment;
import org.adempiere.core.domains.models.X_AD_Reference;
import org.compiere.model.MRefTable;
import org.compiere.model.MValRule;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * 	The util class for all documents
 * 	@author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class ReferenceUtil {

	/**
	 * Validate reference
	 * @param displayTypeId
	 * @return
	 */
	public static boolean isLookupReference(int displayTypeId) {
		if (DisplayType.isLookup(displayTypeId)
				|| DisplayType.Account == displayTypeId
				|| DisplayType.Assignment == displayTypeId
				|| DisplayType.ID == displayTypeId
				|| DisplayType.Location == displayTypeId
				|| DisplayType.Locator == displayTypeId
				|| DisplayType.Image == displayTypeId
				|| DisplayType.PAttribute == displayTypeId) {
			return true;
		}
		return false;
	}

	/**
	 * Get Context column names from context
	 * @param context
	 * @return
	 * @return List<String>
	 */
	public static List<String> getContextColumnNames(String context) {
		if (Util.isEmpty(context, true)) {
			return new ArrayList<String>();
		}
		String START = "\\@";  // A literal "(" character in regex
		String END   = "\\@";  // A literal ")" character in regex

		// Captures the word(s) between the above two character(s)
		final String COLUMN_NAME_PATTERN = START + "(#|$|\\d\\|){0,1}(\\w+)" + END;

		Pattern pattern = Pattern.compile(
			COLUMN_NAME_PATTERN,
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL
		);
		Matcher matcher = pattern.matcher(context);
		Map<String, Boolean> columnNamesMap = new HashMap<String, Boolean>();
		while(matcher.find()) {
			final String columnContext = matcher.group().replace("@", "").replace("@", "");
			columnNamesMap.put(columnContext, true);
		}
		return new ArrayList<String>(columnNamesMap.keySet());
	}

	public static ReferenceValues getReferenceDefinition(String columnName, int displayTypeId, int referenceValueId, int validationRuleId) {
		String embeddedContextColumn = null;

		if (DisplayType.Button == displayTypeId) {
			//	Reference Value
			if (referenceValueId > 0) {
				X_AD_Reference reference = new X_AD_Reference(Env.getCtx(), referenceValueId, null);
				if (reference != null && reference.getAD_Reference_ID() > 0) {
					// overwrite display type to Table or List
					if (X_AD_Reference.VALIDATIONTYPE_TableValidation.equals(reference.getValidationType())) {
						displayTypeId = DisplayType.Table;
					} else {
						displayTypeId = DisplayType.List;
					}
				}
			}
		}

		if(displayTypeId > 0 && ReferenceUtil.isLookupReference(displayTypeId)) {
//			X_AD_Reference reference = new X_AD_Reference(Env.getCtx(), displayTypeId, null);
//			MLookupInfo lookupInformation = null;
			String tableName = getTableNameFromReference(columnName, displayTypeId);
//			//	Special references
//			if(Util.isEmpty(tableName)) {
//				lookupInformation = MLookupFactory.getLookupInfo(Env.getCtx(), 0, 0, referenceId, Language.getBaseLanguage(), columnName, referenceValueId, false, null, false);
//				if(lookupInformation != null) {
//					String validationRuleValue = null;
//					if(validationRuleId > 0) {
//						MValRule validationRule = MValRule.get(Env.getCtx(), validationRuleId);
//						validationRuleValue = validationRule.getCode();
//					}
//					tableName = lookupInformation.TableName;
//					embeddedContextColumn = Optional.ofNullable(lookupInformation.Query).orElse("") 
//							+ Optional.ofNullable(lookupInformation.QueryDirect).orElse("") 
//							+ Optional.ofNullable(lookupInformation.ValidationCode).orElse("")
//							+ Optional.ofNullable(validationRuleValue).orElse("");
//				}
//			}
			String whereClauseReference = null;
			String validationRuleValue = null;
			if(validationRuleId > 0) {
				MValRule validationRule = MValRule.get(Env.getCtx(), validationRuleId);
				validationRuleValue = validationRule.getCode();
			}
			if ((displayTypeId == DisplayType.Table || displayTypeId == DisplayType.Search) && referenceValueId > 0) {
				MRefTable tableReference = MRefTable.getById(Env.getCtx(), referenceValueId);
				if(tableReference != null) {
					whereClauseReference = tableReference.getWhereClause();
				}
			}
			embeddedContextColumn = Optional.ofNullable(whereClauseReference).orElse("")
				+ Optional.ofNullable(validationRuleValue).orElse("")
			;
			return ReferenceValues.newInstance(displayTypeId, tableName, embeddedContextColumn);
		}
		return null;
	}

	/**
	 * Get Table Name for special tables
	 * @param columnName
	 * @param referenceId
	 * @return
	 */
	public static String getTableNameFromReference(String columnName, int referenceId) {
		String tableName = null;
		if(DisplayType.ID == referenceId || DisplayType.Search == referenceId
			|| DisplayType.Table == referenceId || DisplayType.TableDir == referenceId) {
			tableName = columnName.replaceAll("(_ID_To|_To_ID|_ID)$", "");
			if (columnName.endsWith("_Acct")) {
				tableName = I_C_ElementValue.Table_Name;
			}
		} else if (DisplayType.List == referenceId) {
			tableName = I_AD_Ref_List.Table_Name;
		} else if (DisplayType.Location == referenceId) {
			tableName = I_C_Location.Table_Name;
		} else if (DisplayType.Locator == referenceId) {
			tableName = I_M_Locator.Table_Name;
		} else if (DisplayType.PAttribute == referenceId) {
			tableName = I_M_AttributeSetInstance.Table_Name;
		} else if(DisplayType.Image == referenceId) {
			tableName = I_AD_Image.Table_Name;
		} else if(DisplayType.Assignment == referenceId) {
			tableName = I_S_ResourceAssignment.Table_Name;
		} else if(DisplayType.Chart == referenceId) {
			tableName = I_AD_Chart.Table_Name;
		} else if(DisplayType.Account == referenceId) {
			tableName = I_C_ElementValue.Table_Name;
		}
		return tableName;
	}

}
