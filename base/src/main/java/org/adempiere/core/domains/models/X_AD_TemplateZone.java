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

/** Generated Model for AD_TemplateZone
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_TemplateZone extends PO implements I_AD_TemplateZone, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260908L;

    /** Standard Constructor */
    public X_AD_TemplateZone (Properties ctx, int AD_TemplateZone_ID, String trxName)
    {
      super (ctx, AD_TemplateZone_ID, trxName);
      /** if (AD_TemplateZone_ID == 0)
        {
			setAD_TemplateZone_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AD_TemplateZone (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_TemplateZone[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_AD_DashboardTemplate getAD_DashboardTemplate() throws RuntimeException
    {
		return (I_AD_DashboardTemplate)MTable.get(getCtx(), I_AD_DashboardTemplate.Table_Name)
			.getPO(getAD_DashboardTemplate_ID(), get_TrxName());	}

	/** Set Dashboard Template.
		@param AD_DashboardTemplate_ID Dashboard Template	  */
	public void setAD_DashboardTemplate_ID (int AD_DashboardTemplate_ID)
	{
		if (AD_DashboardTemplate_ID < 1)
			set_Value (COLUMNNAME_AD_DashboardTemplate_ID, null);
		else
			set_Value (COLUMNNAME_AD_DashboardTemplate_ID, Integer.valueOf(AD_DashboardTemplate_ID));
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

	/** Set Template Zone.
		@param AD_TemplateZone_ID Template Zone	  */
	public void setAD_TemplateZone_ID (int AD_TemplateZone_ID)
	{
		if (AD_TemplateZone_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AD_TemplateZone_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_TemplateZone_ID, Integer.valueOf(AD_TemplateZone_ID));
	}

	/** Get Template Zone.
		@return Template Zone	  */
	public int getAD_TemplateZone_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_TemplateZone_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Allowed Types.
		@param AllowedTypes
		Comma-separated list of allowed chart types
	  */
	public void setAllowedTypes (String AllowedTypes)
	{
		set_Value (COLUMNNAME_AllowedTypes, AllowedTypes);
	}

	/** Get Allowed Types.
		@return Comma-separated list of allowed chart types
	  */
	public String getAllowedTypes ()
	{
		return (String)get_Value(COLUMNNAME_AllowedTypes);
	}

	/** Set Height.
		@param Height
		Height required
	  */
	public void setHeight (int Height)
	{
		set_Value (COLUMNNAME_Height, Integer.valueOf(Height));
	}

	/** Get Height.
		@return Height required
	  */
	public int getHeight ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Height);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Max Items.
		@param MaxItems
		Maximum number of widgets allowed in the zone
	  */
	public void setMaxItems (int MaxItems)
	{
		set_Value (COLUMNNAME_MaxItems, Integer.valueOf(MaxItems));
	}

	/** Get Max Items.
		@return Maximum number of widgets allowed in the zone
	  */
	public int getMaxItems ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MaxItems);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Min Items.
		@param MinItems
		Minimum number of widgets required in the zone
	  */
	public void setMinItems (int MinItems)
	{
		set_Value (COLUMNNAME_MinItems, Integer.valueOf(MinItems));
	}

	/** Get Min Items.
		@return Minimum number of widgets required in the zone
	  */
	public int getMinItems ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_MinItems);
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

	/** Set Position X.
		@param PosX
		Horizontal position of the zone in the grid
	  */
	public void setPosX (int PosX)
	{
		set_Value (COLUMNNAME_PosX, Integer.valueOf(PosX));
	}

	/** Get Position X.
		@return Horizontal position of the zone in the grid
	  */
	public int getPosX ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PosX);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Position Y.
		@param PosY
		Vertical position of the zone in the grid
	  */
	public void setPosY (int PosY)
	{
		set_Value (COLUMNNAME_PosY, Integer.valueOf(PosY));
	}

	/** Get Position Y.
		@return Vertical position of the zone in the grid
	  */
	public int getPosY ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_PosY);
		if (ii == null)
			 return 0;
		return ii.intValue();
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

	/** Set Width.
		@param Width
		Width required
	  */
	public void setWidth (int Width)
	{
		set_Value (COLUMNNAME_Width, Integer.valueOf(Width));
	}

	/** Get Width.
		@return Width required
	  */
	public int getWidth ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Width);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}
}
