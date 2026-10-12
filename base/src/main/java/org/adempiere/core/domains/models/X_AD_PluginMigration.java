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
/** Generated Model - DO NOT CHANGE */
package org.adempiere.core.domains.models;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;

/** Generated Model for AD_PluginMigration
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_PluginMigration extends PO implements I_AD_PluginMigration, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260902L;

    /** Standard Constructor */
    public X_AD_PluginMigration (Properties ctx, int AD_PluginMigration_ID, String trxName)
    {
      super (ctx, AD_PluginMigration_ID, trxName);
      /** if (AD_PluginMigration_ID == 0)
        {
			setAD_Plugin_ID (0);
			setAD_PluginMigration_ID (0);
			setScriptName (null);
			setStatus (null);
// P
			setTenantId (null);
			setVersion (null);
        } */
    }

    /** Load Constructor */
    public X_AD_PluginMigration (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 4 - System 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_AD_PluginMigration[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.adempiere.core.domains.models.I_AD_Plugin getAD_Plugin() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Plugin)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Plugin.Table_Name)
			.getPO(getAD_Plugin_ID(), get_TrxName());	}

	/** Set Plugin.
		@param AD_Plugin_ID Plugin	  */
	public void setAD_Plugin_ID (int AD_Plugin_ID)
	{
		if (AD_Plugin_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_Plugin_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_Plugin_ID, Integer.valueOf(AD_Plugin_ID));
	}

	/** Get Plugin.
		@return Plugin	  */
	public int getAD_Plugin_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Plugin_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Plugin Migration.
		@param AD_PluginMigration_ID Plugin Migration	  */
	public void setAD_PluginMigration_ID (int AD_PluginMigration_ID)
	{
		if (AD_PluginMigration_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PluginMigration_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PluginMigration_ID, Integer.valueOf(AD_PluginMigration_ID));
	}

	/** Get Plugin Migration.
		@return Plugin Migration	  */
	public int getAD_PluginMigration_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PluginMigration_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Applied Date.
		@param AppliedDate 
		When the migration was applied
	  */
	public void setAppliedDate (Timestamp AppliedDate)
	{
		set_ValueNoCheck (COLUMNNAME_AppliedDate, AppliedDate);
	}

	/** Get Applied Date.
		@return When the migration was applied
	  */
	public Timestamp getAppliedDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_AppliedDate);
	}

	/** Set Checksum.
		@param Checksum 
		SHA-256 of the JAR; detects same Version, different JAR across tenants
	  */
	public void setChecksum (String Checksum)
	{
		set_ValueNoCheck (COLUMNNAME_Checksum, Checksum);
	}

	/** Get Checksum.
		@return SHA-256 of the JAR; detects same Version, different JAR across tenants
	  */
	public String getChecksum () 
	{
		return (String)get_Value(COLUMNNAME_Checksum);
	}

	/** Set Error Msg.
		@param ErrorMsg Error Msg	  */
	public void setErrorMsg (String ErrorMsg)
	{
		set_ValueNoCheck (COLUMNNAME_ErrorMsg, ErrorMsg);
	}

	/** Get Error Msg.
		@return Error Msg	  */
	public String getErrorMsg () 
	{
		return (String)get_Value(COLUMNNAME_ErrorMsg);
	}

	/** Set Script Name.
		@param ScriptName 
		Migration script name, e.g. V1__create_probe_table.sql
	  */
	public void setScriptName (String ScriptName)
	{
		set_ValueNoCheck (COLUMNNAME_ScriptName, ScriptName);
	}

	/** Get Script Name.
		@return Migration script name, e.g. V1__create_probe_table.sql
	  */
	public String getScriptName () 
	{
		return (String)get_Value(COLUMNNAME_ScriptName);
	}

	/** Status AD_Reference_ID=54745 */
	public static final int STATUS_AD_Reference_ID=54745;
	/** Pending = P */
	public static final String STATUS_Pending = "P";
	/** Success = S */
	public static final String STATUS_Success = "S";
	/** Failed = F */
	public static final String STATUS_Failed = "F";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_ValueNoCheck (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}

	/** Set Tenant Id.
		@param TenantId 
		Tenant name as named by TenantDataSourceRegistry (tenants/ file name)
	  */
	public void setTenantId (String TenantId)
	{
		set_ValueNoCheck (COLUMNNAME_TenantId, TenantId);
	}

	/** Get Tenant Id.
		@return Tenant name as named by TenantDataSourceRegistry (tenants/ file name)
	  */
	public String getTenantId () 
	{
		return (String)get_Value(COLUMNNAME_TenantId);
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}

	/** Set Version.
		@param Version 
		Version of the table definition
	  */
	public void setVersion (String Version)
	{
		set_ValueNoCheck (COLUMNNAME_Version, Version);
	}

	/** Get Version.
		@return Version of the table definition
	  */
	public String getVersion () 
	{
		return (String)get_Value(COLUMNNAME_Version);
	}
}