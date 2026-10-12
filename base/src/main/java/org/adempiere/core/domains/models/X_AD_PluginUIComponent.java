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

/** Generated Model for AD_PluginUIComponent
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_PluginUIComponent extends PO implements I_AD_PluginUIComponent, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260917L;

    /** Standard Constructor */
    public X_AD_PluginUIComponent (Properties ctx, int AD_PluginUIComponent_ID, String trxName)
    {
      super (ctx, AD_PluginUIComponent_ID, trxName);
      /** if (AD_PluginUIComponent_ID == 0)
        {
			setAD_PluginUIComponent_ID (0);
			setAD_PluginUIOverride_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AD_PluginUIComponent (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_PluginUIComponent[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.adempiere.core.domains.models.I_AD_Browse getAD_Browse() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Browse)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Browse.Table_Name)
			.getPO(getAD_Browse_ID(), get_TrxName());	}

	/** Set Smart Browse.
		@param AD_Browse_ID Smart Browse	  */
	public void setAD_Browse_ID (int AD_Browse_ID)
	{
		if (AD_Browse_ID < 1) 
			set_Value (COLUMNNAME_AD_Browse_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Browse_ID, Integer.valueOf(AD_Browse_ID));
	}

	/** Get Smart Browse.
		@return Smart Browse	  */
	public int getAD_Browse_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Browse_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_AD_Form getAD_Form() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Form)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Form.Table_Name)
			.getPO(getAD_Form_ID(), get_TrxName());	}

	/** Set Special Form.
		@param AD_Form_ID 
		Special Form
	  */
	public void setAD_Form_ID (int AD_Form_ID)
	{
		if (AD_Form_ID < 1) 
			set_Value (COLUMNNAME_AD_Form_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Form_ID, Integer.valueOf(AD_Form_ID));
	}

	/** Get Special Form.
		@return Special Form
	  */
	public int getAD_Form_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Form_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Plugin UI Component.
		@param AD_PluginUIComponent_ID 
		Member of a group UI override
	  */
	public void setAD_PluginUIComponent_ID (int AD_PluginUIComponent_ID)
	{
		if (AD_PluginUIComponent_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PluginUIComponent_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PluginUIComponent_ID, Integer.valueOf(AD_PluginUIComponent_ID));
	}

	/** Get Plugin UI Component.
		@return Member of a group UI override
	  */
	public int getAD_PluginUIComponent_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PluginUIComponent_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_AD_PluginUIOverride getAD_PluginUIOverride() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_PluginUIOverride)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_PluginUIOverride.Table_Name)
			.getPO(getAD_PluginUIOverride_ID(), get_TrxName());	}

	/** Set Plugin UI Override.
		@param AD_PluginUIOverride_ID 
		Plugin UI override definition
	  */
	public void setAD_PluginUIOverride_ID (int AD_PluginUIOverride_ID)
	{
		if (AD_PluginUIOverride_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_PluginUIOverride_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_PluginUIOverride_ID, Integer.valueOf(AD_PluginUIOverride_ID));
	}

	/** Get Plugin UI Override.
		@return Plugin UI override definition
	  */
	public int getAD_PluginUIOverride_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_PluginUIOverride_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_AD_Process getAD_Process() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Process)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Process.Table_Name)
			.getPO(getAD_Process_ID(), get_TrxName());	}

	/** Set Process.
		@param AD_Process_ID 
		Process or Report
	  */
	public void setAD_Process_ID (int AD_Process_ID)
	{
		if (AD_Process_ID < 1) 
			set_Value (COLUMNNAME_AD_Process_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Process_ID, Integer.valueOf(AD_Process_ID));
	}

	/** Get Process.
		@return Process or Report
	  */
	public int getAD_Process_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Process_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_AD_Window getAD_Window() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Window)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Window.Table_Name)
			.getPO(getAD_Window_ID(), get_TrxName());	}

	/** Set Window.
		@param AD_Window_ID 
		Data entry or display window
	  */
	public void setAD_Window_ID (int AD_Window_ID)
	{
		if (AD_Window_ID < 1) 
			set_Value (COLUMNNAME_AD_Window_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Window_ID, Integer.valueOf(AD_Window_ID));
	}

	/** Get Window.
		@return Data entry or display window
	  */
	public int getAD_Window_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Window_ID);
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
}