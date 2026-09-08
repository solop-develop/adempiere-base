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
package org.adempiere.core.domains.models;

import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

import java.math.BigDecimal;
import java.sql.Timestamp;

/** Generated Interface for AD_AssignedWidget
 *  @author Adempiere (generated)
 *  @version Release 3.9.4
 */
public interface I_AD_AssignedWidget
{

    /** TableName=AD_AssignedWidget */
    public static final String Table_Name = "AD_AssignedWidget";

    /** AD_Table_ID=2000177 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_AssignedWidget_ID */
    public static final String COLUMNNAME_AD_AssignedWidget_ID = "AD_AssignedWidget_ID";

	/** Set Assigned Widget	  */
	public void setAD_AssignedWidget_ID (int AD_AssignedWidget_ID);

	/** Get Assigned Widget	  */
	public int getAD_AssignedWidget_ID();

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_DashboardInstance_ID */
    public static final String COLUMNNAME_AD_DashboardInstance_ID = "AD_DashboardInstance_ID";

	/** Set Dashboard Instance	  */
	public void setAD_DashboardInstance_ID (int AD_DashboardInstance_ID);

	/** Get Dashboard Instance	  */
	public int getAD_DashboardInstance_ID();

	public I_AD_DashboardInstance getAD_DashboardInstance() throws RuntimeException;

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name AD_TemplateZone_ID */
    public static final String COLUMNNAME_AD_TemplateZone_ID = "AD_TemplateZone_ID";

	/** Set Template Zone	  */
	public void setAD_TemplateZone_ID (int AD_TemplateZone_ID);

	/** Get Template Zone	  */
	public int getAD_TemplateZone_ID();

	public I_AD_TemplateZone getAD_TemplateZone() throws RuntimeException;

    /** Column name ConfigJSON */
    public static final String COLUMNNAME_ConfigJSON = "ConfigJSON";

	/** Set Configuration JSON.
	  * Widget configuration stored as JSON
	  */
	public void setConfigJSON (String ConfigJSON);

	/** Get Configuration JSON.
	  * Widget configuration stored as JSON
	  */
	public String getConfigJSON();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DisplayOrder */
    public static final String COLUMNNAME_DisplayOrder = "DisplayOrder";

	/** Set Display Order.
	  * Order of the widget within its zone
	  */
	public void setDisplayOrder (int DisplayOrder);

	/** Get Display Order.
	  * Order of the widget within its zone
	  */
	public int getDisplayOrder();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Report_ID */
    public static final String COLUMNNAME_Report_ID = "Report_ID";

	/** Set Report.
	  * Identifier of the chart or report to render
	  */
	public void setReport_ID (int Report_ID);

	/** Get Report.
	  * Identifier of the chart or report to render
	  */
	public int getReport_ID();

    /** Column name ReportType */
    public static final String COLUMNNAME_ReportType = "ReportType";

	/** Set Report Type	  */
	public void setReportType (String ReportType);

	/** Get Report Type	  */
	public String getReportType();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UUID */
    public static final String COLUMNNAME_UUID = "UUID";

	/** Set Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();
}
