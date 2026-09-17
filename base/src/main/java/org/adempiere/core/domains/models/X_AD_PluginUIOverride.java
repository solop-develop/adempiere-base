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

/** Generated Model for AD_PluginUIOverride
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_PluginUIOverride extends PO implements I_AD_PluginUIOverride, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260917L;

    /** Standard Constructor */
    public X_AD_PluginUIOverride (Properties ctx, int AD_PluginUIOverride_ID, String trxName)
    {
      super (ctx, AD_PluginUIOverride_ID, trxName);
      /** if (AD_PluginUIOverride_ID == 0)
        {
			setAD_Plugin_ID (0);
			setAD_PluginUIOverride_ID (0);
			setComponentType (null);
			setIsSpecificComponent (false);
// N
			setName (null);
			setSeqNo (0);
// 10
        } */
    }

    /** Load Constructor */
    public X_AD_PluginUIOverride (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_PluginUIOverride[")
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

	public org.adempiere.core.domains.models.I_AD_Plugin getAD_Plugin() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Plugin)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Plugin.Table_Name)
			.getPO(getAD_Plugin_ID(), get_TrxName());	}

	/** Set Plugin.
		@param AD_Plugin_ID Plugin	  */
	public void setAD_Plugin_ID (int AD_Plugin_ID)
	{
		if (AD_Plugin_ID < 1) 
			set_Value (COLUMNNAME_AD_Plugin_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Plugin_ID, Integer.valueOf(AD_Plugin_ID));
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

	/** Set Component Path.
		@param ComponentPath 
		Front-end component route
	  */
	public void setComponentPath (String ComponentPath)
	{
		set_Value (COLUMNNAME_ComponentPath, ComponentPath);
	}

	/** Get Component Path.
		@return Front-end component route
	  */
	public String getComponentPath () 
	{
		return (String)get_Value(COLUMNNAME_ComponentPath);
	}

	/** ComponentType AD_Reference_ID=104 */
	public static final int COMPONENTTYPE_AD_Reference_ID=104;
	/** Window = W */
	public static final String COMPONENTTYPE_Window = "W";
	/** Task = T */
	public static final String COMPONENTTYPE_Task = "T";
	/** WorkFlow = F */
	public static final String COMPONENTTYPE_WorkFlow = "F";
	/** Process = P */
	public static final String COMPONENTTYPE_Process = "P";
	/** Report = R */
	public static final String COMPONENTTYPE_Report = "R";
	/** Form = X */
	public static final String COMPONENTTYPE_Form = "X";
	/** Workbench = B */
	public static final String COMPONENTTYPE_Workbench = "B";
	/** Smart Browse = S */
	public static final String COMPONENTTYPE_SmartBrowse = "S";
	/** Set Component Type.
		@param ComponentType 
		Component Type for a Bill of Material or Formula
	  */
	public void setComponentType (String ComponentType)
	{

		set_Value (COLUMNNAME_ComponentType, ComponentType);
	}

	/** Get Component Type.
		@return Component Type for a Bill of Material or Formula
	  */
	public String getComponentType () 
	{
		return (String)get_Value(COLUMNNAME_ComponentType);
	}

	/** Set Specific Component.
		@param IsSpecificComponent 
		Targets one artifact vs a group
	  */
	public void setIsSpecificComponent (boolean IsSpecificComponent)
	{
		set_Value (COLUMNNAME_IsSpecificComponent, Boolean.valueOf(IsSpecificComponent));
	}

	/** Get Specific Component.
		@return Targets one artifact vs a group
	  */
	public boolean isSpecificComponent () 
	{
		Object oo = get_Value(COLUMNNAME_IsSpecificComponent);
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

	/** Set Sequence.
		@param SeqNo 
		Method of ordering records; lowest number comes first
	  */
	public void setSeqNo (int SeqNo)
	{
		set_Value (COLUMNNAME_SeqNo, Integer.valueOf(SeqNo));
	}

	/** Get Sequence.
		@return Method of ordering records; lowest number comes first
	  */
	public int getSeqNo () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SeqNo);
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