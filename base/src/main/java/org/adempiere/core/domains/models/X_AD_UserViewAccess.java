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

/** Generated Model for AD_UserViewAccess
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_UserViewAccess extends PO implements I_AD_UserViewAccess, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260825L;

    /** Standard Constructor */
    public X_AD_UserViewAccess (Properties ctx, int AD_UserViewAccess_ID, String trxName)
    {
      super (ctx, AD_UserViewAccess_ID, trxName);
      /** if (AD_UserViewAccess_ID == 0)
        {
			setAccessType (null);
// R
			setAD_UserViewAccess_ID (0);
			setAD_UserView_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AD_UserViewAccess (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_UserViewAccess[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** AccessType AD_Reference_ID=54741 */
	public static final int ACCESSTYPE_AD_Reference_ID=54741;
	/** Read = R */
	public static final String ACCESSTYPE_Read = "R";
	/** Write = W */
	public static final String ACCESSTYPE_Write = "W";
	/** Admin = M */
	public static final String ACCESSTYPE_Admin = "M";
	/** Set Access Type.
		@param AccessType 
		Permission granted
	  */
	public void setAccessType (String AccessType)
	{

		set_Value (COLUMNNAME_AccessType, AccessType);
	}

	/** Get Access Type.
		@return Permission granted
	  */
	public String getAccessType () 
	{
		return (String)get_Value(COLUMNNAME_AccessType);
	}

	public org.adempiere.core.domains.models.I_AD_Role getAD_Role() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_Role)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_Role.Table_Name)
			.getPO(getAD_Role_ID(), get_TrxName());	}

	/** Set Role.
		@param AD_Role_ID 
		Responsibility Role
	  */
	public void setAD_Role_ID (int AD_Role_ID)
	{
		if (AD_Role_ID < 0) 
			set_Value (COLUMNNAME_AD_Role_ID, null);
		else 
			set_Value (COLUMNNAME_AD_Role_ID, Integer.valueOf(AD_Role_ID));
	}

	/** Get Role.
		@return Responsibility Role
	  */
	public int getAD_Role_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_Role_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_AD_User getAD_User() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_User)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_User.Table_Name)
			.getPO(getAD_User_ID(), get_TrxName());	}

	/** Set User/Contact.
		@param AD_User_ID 
		User within the system - Internal or Business Partner Contact
	  */
	public void setAD_User_ID (int AD_User_ID)
	{
		if (AD_User_ID < 1) 
			set_Value (COLUMNNAME_AD_User_ID, null);
		else 
			set_Value (COLUMNNAME_AD_User_ID, Integer.valueOf(AD_User_ID));
	}

	/** Get User/Contact.
		@return User within the system - Internal or Business Partner Contact
	  */
	public int getAD_User_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_User_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set View Access.
		@param AD_UserViewAccess_ID 
		Users and roles a view is shared with, and the permission granted
	  */
	public void setAD_UserViewAccess_ID (int AD_UserViewAccess_ID)
	{
		if (AD_UserViewAccess_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_AD_UserViewAccess_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_AD_UserViewAccess_ID, Integer.valueOf(AD_UserViewAccess_ID));
	}

	/** Get View Access.
		@return Users and roles a view is shared with, and the permission granted
	  */
	public int getAD_UserViewAccess_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_UserViewAccess_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_AD_UserView getAD_UserView() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_AD_UserView)MTable.get(getCtx(), org.adempiere.core.domains.models.I_AD_UserView.Table_Name)
			.getPO(getAD_UserView_ID(), get_TrxName());	}

	/** Set User View.
		@param AD_UserView_ID 
		User's saved display configuration over a form: filter, grouping, sorting, columns and view mode, serialized as a querystring
	  */
	public void setAD_UserView_ID (int AD_UserView_ID)
	{
		if (AD_UserView_ID < 1) 
			set_Value (COLUMNNAME_AD_UserView_ID, null);
		else 
			set_Value (COLUMNNAME_AD_UserView_ID, Integer.valueOf(AD_UserView_ID));
	}

	/** Get User View.
		@return User's saved display configuration over a form: filter, grouping, sorting, columns and view mode, serialized as a querystring
	  */
	public int getAD_UserView_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_UserView_ID);
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