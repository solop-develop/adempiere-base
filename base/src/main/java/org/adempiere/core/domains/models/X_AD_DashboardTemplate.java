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

import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for AD_DashboardTemplate
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_DashboardTemplate extends PO implements I_AD_DashboardTemplate, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260908L;

    /** Standard Constructor */
    public X_AD_DashboardTemplate (Properties ctx, int AD_DashboardTemplate_ID, String trxName)
    {
      super (ctx, AD_DashboardTemplate_ID, trxName);
      /** if (AD_DashboardTemplate_ID == 0)
        {
			setAD_DashboardTemplate_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AD_DashboardTemplate (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org
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
      StringBuffer sb = new StringBuffer ("X_AD_DashboardTemplate[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Dashboard Template.
		@param AD_DashboardTemplate_ID Dashboard Template	  */
	public void setAD_DashboardTemplate_ID (int AD_DashboardTemplate_ID)
	{
		if (AD_DashboardTemplate_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AD_DashboardTemplate_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_DashboardTemplate_ID, Integer.valueOf(AD_DashboardTemplate_ID));
	}

	/** Get Dashboard Template.
		@return Dashboard Template	  */
	public int getAD_DashboardTemplate_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_DashboardTemplate_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Grid Columns.
		@param GridColumns
		Number of columns in the dashboard grid
	  */
	public void setGridColumns (int GridColumns)
	{
		set_Value (COLUMNNAME_GridColumns, Integer.valueOf(GridColumns));
	}

	/** Get Grid Columns.
		@return Number of columns in the dashboard grid
	  */
	public int getGridColumns ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_GridColumns);
		if (ii == null)
			 return 0;
		return ii.intValue();
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
}
