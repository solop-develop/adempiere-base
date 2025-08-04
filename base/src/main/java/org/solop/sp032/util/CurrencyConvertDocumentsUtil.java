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
 * Copyright (C) 2003-2019 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.solop.sp032.util;

import org.solop.sp032.model.MSP032Conversion;
import org.adempiere.core.domains.models.I_C_Invoice;
import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.core.domains.models.I_C_Payment;
import org.compiere.util.Util;

import java.util.List;

/**
 * Added for handle custom columns for ADempiere core
 * @author Carlos Parada, cparada@erpya.com, ERPCyA http://www.erpya.com
 */
public class CurrencyConvertDocumentsUtil {
	/**Expedient Table Name*/
	public static final String Expedient_TableName = "ECA28_Expedient";
	/**Expedient Primary Column Name*/
	public static final String ColumnName_SP009_Expedient_ID = "SP009_Expedient_ID";
	/**	Generated Automatic Currency Type */
	public static final String COLUMNNAME_SP032_GeneratedCurrencyType = "SP032_IsGeneratedCurrencyType";
	/**Parent Conversion Type*/
	public static final String COLUMNNAME_SP032_ParentCType_ID = "SP032_ParentCType_ID";
	/**Negotiated Currency Rate*/
	public static final String COLUMNNAME_SP032_NegotiatedRate = "SP032_NegotiatedRate";
	/**Negotiated Conversion Date*/
	public static final String COLUMNNAME_SP032_NegotiatedDate= "SP032_NegotiatedDate";
	/**Accounting Date*/
	public static final String COLUMNNAME_DateAcct = "DateAcct";
	/**Interval of Yeas for Valid To Conversion Rate*/
	public static final int TIME_Interval= 100;

	public static String getDocumentDateColumnName(String tableName) {
		if(Util.isEmpty(tableName)) {
			return COLUMNNAME_DateAcct;
		}
		if(List.of(I_C_Order.Table_Name, I_C_Invoice.Table_Name, I_C_Payment.Table_Name).contains(tableName)) {
			return I_C_Order.COLUMNNAME_DateAcct;
		}
		return MSP032Conversion.COLUMNNAME_DateDoc;
	}
	
}
