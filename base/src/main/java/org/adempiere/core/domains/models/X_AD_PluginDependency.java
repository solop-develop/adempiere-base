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
import java.util.Properties;
import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;

/** Generated Model for AD_PluginDependency
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_PluginDependency extends PO implements I_AD_PluginDependency, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260917L;

    /** Standard Constructor */
    public X_AD_PluginDependency (Properties ctx, int AD_PluginDependency_ID, String trxName)
    {
      super (ctx, AD_PluginDependency_ID, trxName);
      /** if (AD_PluginDependency_ID == 0)
        {
			setAD_PluginDependency_ID (0);
			setAD_Plugin_ID (0);
			setDependencyType (null);
// REQ
			setDependsOnPluginValue (null);
        } */
    }

    /** Load Constructor */
    public X_AD_PluginDependency (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_PluginDependency[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Plugin Dependency.
		@param AD_PluginDependency_ID 
		Plugin dependency/compatibility rule
	  */
	public void setAD_PluginDependency_ID (int AD_PluginDependency_ID)
	{
		if (AD_PluginDependency_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PluginDependency_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PluginDependency_ID, Integer.valueOf(AD_PluginDependency_ID));
	}

	/** Get Plugin Dependency.
		@return Plugin dependency/compatibility rule
	  */
	public int getAD_PluginDependency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PluginDependency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** DependencyType AD_Reference_ID=54754 */
	public static final int DEPENDENCYTYPE_AD_Reference_ID=54754;
	/** Required = REQ */
	public static final String DEPENDENCYTYPE_Required = "REQ";
	/** Recommended = REC */
	public static final String DEPENDENCYTYPE_Recommended = "REC";
	/** Optional = OPT */
	public static final String DEPENDENCYTYPE_Optional = "OPT";
	/** Incompatible = INC */
	public static final String DEPENDENCYTYPE_Incompatible = "INC";
	/** Set Dependency Type.
		@param DependencyType 
		Dependency or conflict kind
	  */
	public void setDependencyType (String DependencyType)
	{

		set_Value (COLUMNNAME_DependencyType, DependencyType);
	}

	/** Get Dependency Type.
		@return Dependency or conflict kind
	  */
	public String getDependencyType () 
	{
		return (String)get_Value(COLUMNNAME_DependencyType);
	}

	/** Set Depends On Plugin.
		@param DependsOnPluginValue 
		Value of the required plugin
	  */
	public void setDependsOnPluginValue (String DependsOnPluginValue)
	{
		set_Value (COLUMNNAME_DependsOnPluginValue, DependsOnPluginValue);
	}

	/** Get Depends On Plugin.
		@return Value of the required plugin
	  */
	public String getDependsOnPluginValue () 
	{
		return (String)get_Value(COLUMNNAME_DependsOnPluginValue);
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

	/** Set Version Max.
		@param VersionMax 
		Inclusive maximum version
	  */
	public void setVersionMax (String VersionMax)
	{
		set_Value (COLUMNNAME_VersionMax, VersionMax);
	}

	/** Get Version Max.
		@return Inclusive maximum version
	  */
	public String getVersionMax () 
	{
		return (String)get_Value(COLUMNNAME_VersionMax);
	}

	/** Set Version Min.
		@param VersionMin 
		Inclusive minimum version
	  */
	public void setVersionMin (String VersionMin)
	{
		set_Value (COLUMNNAME_VersionMin, VersionMin);
	}

	/** Get Version Min.
		@return Inclusive minimum version
	  */
	public String getVersionMin () 
	{
		return (String)get_Value(COLUMNNAME_VersionMin);
	}
}