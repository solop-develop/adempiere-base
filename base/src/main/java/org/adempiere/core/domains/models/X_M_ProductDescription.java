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

/** Generated Model for M_ProductDescription
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_M_ProductDescription extends PO implements I_M_ProductDescription, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20261008L;

    /** Standard Constructor */
    public X_M_ProductDescription (Properties ctx, int M_ProductDescription_ID, String trxName)
    {
      super (ctx, M_ProductDescription_ID, trxName);
      /** if (M_ProductDescription_ID == 0)
        {
			setDescription (null);
			setM_Product_ID (0);
			setM_ProductDescription_ID (0);
			setProductDescriptionType (null);
        } */
    }

    /** Load Constructor */
    public X_M_ProductDescription (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_ProductDescription[")
        .append(get_ID()).append("]");
      return sb.toString();
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

	public org.adempiere.core.domains.models.I_M_Product getM_Product() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_M_Product)MTable.get(getCtx(), org.adempiere.core.domains.models.I_M_Product.Table_Name)
			.getPO(getM_Product_ID(), get_TrxName());	}

	/** Set Product.
		@param M_Product_ID
		Product, Service, Item
	  */
	public void setM_Product_ID (int M_Product_ID)
	{
		if (M_Product_ID < 1)
			set_ValueNoCheck (COLUMNNAME_M_Product_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_Product_ID, Integer.valueOf(M_Product_ID));
	}

	/** Get Product.
		@return Product, Service, Item
	  */
	public int getM_Product_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_Product_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Product Description.
		@param M_ProductDescription_ID Product Description	  */
	public void setM_ProductDescription_ID (int M_ProductDescription_ID)
	{
		if (M_ProductDescription_ID < 1)
			set_ValueNoCheck (COLUMNNAME_M_ProductDescription_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_M_ProductDescription_ID, Integer.valueOf(M_ProductDescription_ID));
	}

	/** Get Product Description.
		@return Product Description	  */
	public int getM_ProductDescription_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_ProductDescription_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** ProductDescriptionType AD_Reference_ID=54825 */
	public static final int PRODUCTDESCRIPTIONTYPE_AD_Reference_ID=54825;
	/** Customer Quote = CQ */
	public static final String PRODUCTDESCRIPTIONTYPE_CustomerQuote = "CQ";
	/** E-Commerce / Catalog = EC */
	public static final String PRODUCTDESCRIPTIONTYPE_E_CommerceCatalog = "EC";
	/** Invoicing = IN */
	public static final String PRODUCTDESCRIPTIONTYPE_Invoicing = "IN";
	/** Installation = IS */
	public static final String PRODUCTDESCRIPTIONTYPE_Installation = "IS";
	/** Internal Technical = IT */
	public static final String PRODUCTDESCRIPTIONTYPE_InternalTechnical = "IT";
	/** Logistics = LG */
	public static final String PRODUCTDESCRIPTIONTYPE_Logistics = "LG";
	/** Manufacturing = MF */
	public static final String PRODUCTDESCRIPTIONTYPE_Manufacturing = "MF";
	/** Maintenance Technical = MT */
	public static final String PRODUCTDESCRIPTIONTYPE_MaintenanceTechnical = "MT";
	/** Vendor Purchase = PO */
	public static final String PRODUCTDESCRIPTIONTYPE_VendorPurchase = "PO";
	/** Quality Inspection = QC */
	public static final String PRODUCTDESCRIPTIONTYPE_QualityInspection = "QC";
	/** Warranty = WR */
	public static final String PRODUCTDESCRIPTIONTYPE_Warranty = "WR";
	/** Set Product Description Type.
		@param ProductDescriptionType Product Description Type	  */
	public void setProductDescriptionType (String ProductDescriptionType)
	{

		set_Value (COLUMNNAME_ProductDescriptionType, ProductDescriptionType);
	}

	/** Get Product Description Type.
		@return Product Description Type	  */
	public String getProductDescriptionType ()
	{
		return (String)get_Value(COLUMNNAME_ProductDescriptionType);
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
