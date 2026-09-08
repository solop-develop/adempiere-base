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

/** Generated Model for AD_Plugin
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_Plugin extends PO implements I_AD_Plugin, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260902L;

    /** Standard Constructor */
    public X_AD_Plugin (Properties ctx, int AD_Plugin_ID, String trxName)
    {
      super (ctx, AD_Plugin_ID, trxName);
      /** if (AD_Plugin_ID == 0)
        {
			setAD_Plugin_ID (0);
			setApiVersion (null);
			setIsBundled (false);
// N
			setName (null);
			setStatus (null);
// I
			setValue (null);
			setVersion (null);
        } */
    }

    /** Load Constructor */
    public X_AD_Plugin (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_Plugin[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Activated Date.
		@param ActivatedDate 
		Last activation timestamp
	  */
	public void setActivatedDate (Timestamp ActivatedDate)
	{
		set_ValueNoCheck (COLUMNNAME_ActivatedDate, ActivatedDate);
	}

	/** Get Activated Date.
		@return Last activation timestamp
	  */
	public Timestamp getActivatedDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_ActivatedDate);
	}

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

	/** Set API Version.
		@param ApiVersion 
		Host API version the plugin targets (PluginIdentity.apiVersion())
	  */
	public void setApiVersion (String ApiVersion)
	{
		set_ValueNoCheck (COLUMNNAME_ApiVersion, ApiVersion);
	}

	/** Get API Version.
		@return Host API version the plugin targets (PluginIdentity.apiVersion())
	  */
	public String getApiVersion () 
	{
		return (String)get_Value(COLUMNNAME_ApiVersion);
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

	/** Set Description.
		@param Description 
		Optional short description of the record
	  */
	public void setDescription (String Description)
	{
		set_Value (COLUMNNAME_Description, Description);
	}

	/** Get Description.
		@return Optional short description of the record
	  */
	public String getDescription () 
	{
		return (String)get_Value(COLUMNNAME_Description);
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

	/** Set Installed Date.
		@param InstalledDate 
		When install() ran against this tenant
	  */
	public void setInstalledDate (Timestamp InstalledDate)
	{
		set_ValueNoCheck (COLUMNNAME_InstalledDate, InstalledDate);
	}

	/** Get Installed Date.
		@return When install() ran against this tenant
	  */
	public Timestamp getInstalledDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_InstalledDate);
	}

	/** Set Bundled.
		@param IsBundled 
		If Yes, plugin is bundled in the node and Status is ignored at runtime
	  */
	public void setIsBundled (boolean IsBundled)
	{
		set_ValueNoCheck (COLUMNNAME_IsBundled, Boolean.valueOf(IsBundled));
	}

	/** Get Bundled.
		@return If Yes, plugin is bundled in the node and Status is ignored at runtime
	  */
	public boolean isBundled () 
	{
		Object oo = get_Value(COLUMNNAME_IsBundled);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

	/** Set Route Count.
		@param RouteCount 
		Informative number of routes published by the plugin (extension.routes().size())
	  */
	public void setRouteCount (int RouteCount)
	{
		set_ValueNoCheck (COLUMNNAME_RouteCount, Integer.valueOf(RouteCount));
	}

	/** Get Route Count.
		@return Informative number of routes published by the plugin (extension.routes().size())
	  */
	public int getRouteCount () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_RouteCount);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Status AD_Reference_ID=54744 */
	public static final int STATUS_AD_Reference_ID=54744;
	/** Installed = I */
	public static final String STATUS_Installed = "I";
	/** Active = A */
	public static final String STATUS_Active = "A";
	/** Failed = F */
	public static final String STATUS_Failed = "F";
	/** Disabled = D */
	public static final String STATUS_Disabled = "D";
	/** Set Status.
		@param Status 
		Status of the currently running check
	  */
	public void setStatus (String Status)
	{

		set_Value (COLUMNNAME_Status, Status);
	}

	/** Get Status.
		@return Status of the currently running check
	  */
	public String getStatus () 
	{
		return (String)get_Value(COLUMNNAME_Status);
	}

	/** Set Table Prefix.
		@param TablePrefix 
		Table prefix derived by PluginLifecycleService.deriveTablePrefix()
	  */
	public void setTablePrefix (String TablePrefix)
	{
		set_ValueNoCheck (COLUMNNAME_TablePrefix, TablePrefix);
	}

	/** Get Table Prefix.
		@return Table prefix derived by PluginLifecycleService.deriveTablePrefix()
	  */
	public String getTablePrefix () 
	{
		return (String)get_Value(COLUMNNAME_TablePrefix);
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

	/** Set Search Key.
		@param Value 
		Search key for the record in the format required - must be unique
	  */
	public void setValue (String Value)
	{
		set_ValueNoCheck (COLUMNNAME_Value, Value);
	}

	/** Get Search Key.
		@return Search key for the record in the format required - must be unique
	  */
	public String getValue () 
	{
		return (String)get_Value(COLUMNNAME_Value);
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