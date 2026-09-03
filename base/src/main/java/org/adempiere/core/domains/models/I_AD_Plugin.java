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

import java.math.BigDecimal;
import java.sql.Timestamp;
import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

/** Generated Interface for AD_Plugin
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4
 */
public interface I_AD_Plugin 
{

    /** TableName=AD_Plugin */
    public static final String Table_Name = "AD_Plugin";

    /** AD_Table_ID=55199 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 4 - System 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(4);

    /** Load Meta Data */

    /** Column name ActivatedDate */
    public static final String COLUMNNAME_ActivatedDate = "ActivatedDate";

	/** Set Activated Date.
	  * Last activation timestamp
	  */
	public void setActivatedDate (Timestamp ActivatedDate);

	/** Get Activated Date.
	  * Last activation timestamp
	  */
	public Timestamp getActivatedDate();

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

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

    /** Column name AD_Plugin_ID */
    public static final String COLUMNNAME_AD_Plugin_ID = "AD_Plugin_ID";

	/** Set Plugin	  */
	public void setAD_Plugin_ID (int AD_Plugin_ID);

	/** Get Plugin	  */
	public int getAD_Plugin_ID();

    /** Column name ApiVersion */
    public static final String COLUMNNAME_ApiVersion = "ApiVersion";

	/** Set API Version.
	  * Host API version the plugin targets (PluginIdentity.apiVersion())
	  */
	public void setApiVersion (String ApiVersion);

	/** Get API Version.
	  * Host API version the plugin targets (PluginIdentity.apiVersion())
	  */
	public String getApiVersion();

    /** Column name Checksum */
    public static final String COLUMNNAME_Checksum = "Checksum";

	/** Set Checksum.
	  * SHA-256 of the JAR;
 detects same Version, different JAR across tenants
	  */
	public void setChecksum (String Checksum);

	/** Get Checksum.
	  * SHA-256 of the JAR;
 detects same Version, different JAR across tenants
	  */
	public String getChecksum();

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

    /** Column name Description */
    public static final String COLUMNNAME_Description = "Description";

	/** Set Description.
	  * Optional short description of the record
	  */
	public void setDescription (String Description);

	/** Get Description.
	  * Optional short description of the record
	  */
	public String getDescription();

    /** Column name ErrorMsg */
    public static final String COLUMNNAME_ErrorMsg = "ErrorMsg";

	/** Set Error Msg	  */
	public void setErrorMsg (String ErrorMsg);

	/** Get Error Msg	  */
	public String getErrorMsg();

    /** Column name InstalledDate */
    public static final String COLUMNNAME_InstalledDate = "InstalledDate";

	/** Set Installed Date.
	  * When install() ran against this tenant
	  */
	public void setInstalledDate (Timestamp InstalledDate);

	/** Get Installed Date.
	  * When install() ran against this tenant
	  */
	public Timestamp getInstalledDate();

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

    /** Column name IsBundled */
    public static final String COLUMNNAME_IsBundled = "IsBundled";

	/** Set Bundled.
	  * If Yes, plugin is bundled in the node and Status is ignored at runtime
	  */
	public void setIsBundled (boolean IsBundled);

	/** Get Bundled.
	  * If Yes, plugin is bundled in the node and Status is ignored at runtime
	  */
	public boolean isBundled();

    /** Column name Name */
    public static final String COLUMNNAME_Name = "Name";

	/** Set Name.
	  * Alphanumeric identifier of the entity
	  */
	public void setName (String Name);

	/** Get Name.
	  * Alphanumeric identifier of the entity
	  */
	public String getName();

    /** Column name RouteCount */
    public static final String COLUMNNAME_RouteCount = "RouteCount";

	/** Set Route Count.
	  * Informative number of routes published by the plugin (extension.routes().size())
	  */
	public void setRouteCount (int RouteCount);

	/** Get Route Count.
	  * Informative number of routes published by the plugin (extension.routes().size())
	  */
	public int getRouteCount();

    /** Column name Status */
    public static final String COLUMNNAME_Status = "Status";

	/** Set Status.
	  * Status of the currently running check
	  */
	public void setStatus (String Status);

	/** Get Status.
	  * Status of the currently running check
	  */
	public String getStatus();

    /** Column name TablePrefix */
    public static final String COLUMNNAME_TablePrefix = "TablePrefix";

	/** Set Table Prefix.
	  * Table prefix derived by PluginLifecycleService.deriveTablePrefix()
	  */
	public void setTablePrefix (String TablePrefix);

	/** Get Table Prefix.
	  * Table prefix derived by PluginLifecycleService.deriveTablePrefix()
	  */
	public String getTablePrefix();

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

    /** Column name Value */
    public static final String COLUMNNAME_Value = "Value";

	/** Set Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value);

	/** Get Search Key.
	  * Search key for the record in the format required - must be unique
	  */
	public String getValue();

    /** Column name Version */
    public static final String COLUMNNAME_Version = "Version";

	/** Set Version.
	  * Version of the table definition
	  */
	public void setVersion (String Version);

	/** Get Version.
	  * Version of the table definition
	  */
	public String getVersion();
}
