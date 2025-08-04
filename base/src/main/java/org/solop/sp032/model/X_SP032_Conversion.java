/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
/** Generated Model - DO NOT CHANGE */
package org.solop.sp032.model;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;
import org.compiere.model.*;
import org.compiere.util.Env;

/** Generated Model for SP032_Conversion
 *  @author Adempiere (generated) 
 *  @version Release 3.9.3 - $Id$ */
public class X_SP032_Conversion extends PO implements I_SP032_Conversion, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20220816L;

    /** Standard Constructor */
    public X_SP032_Conversion (Properties ctx, int SP032_Conversion_ID, String trxName)
    {
      super (ctx, SP032_Conversion_ID, trxName);
      /** if (SP032_Conversion_ID == 0)
        {
			setSP032_Conversion_ID (0);
        } */
    }

    /** Load Constructor */
    public X_SP032_Conversion (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_SP032_Conversion[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public org.adempiere.core.domains.models.I_A_Asset_Addition getA_Asset_Addition() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_A_Asset_Addition)MTable.get(getCtx(), org.adempiere.core.domains.models.I_A_Asset_Addition.Table_Name)
			.getPO(getA_Asset_Addition_ID(), get_TrxName());	}

	/** Set Asset Addition.
		@param A_Asset_Addition_ID Asset Addition	  */
	public void setA_Asset_Addition_ID (int A_Asset_Addition_ID)
	{
		if (A_Asset_Addition_ID < 1) 
			set_Value (COLUMNNAME_A_Asset_Addition_ID, null);
		else 
			set_Value (COLUMNNAME_A_Asset_Addition_ID, Integer.valueOf(A_Asset_Addition_ID));
	}

	/** Get Asset Addition.
		@return Asset Addition	  */
	public int getA_Asset_Addition_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_A_Asset_Addition_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_ConversionType getC_ConversionType() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_ConversionType)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_ConversionType.Table_Name)
			.getPO(getC_ConversionType_ID(), get_TrxName());	}

	/** Set Currency Type.
		@param C_ConversionType_ID 
		Currency Conversion Rate Type
	  */
	public void setC_ConversionType_ID (int C_ConversionType_ID)
	{
		if (C_ConversionType_ID < 1) 
			set_Value (COLUMNNAME_C_ConversionType_ID, null);
		else 
			set_Value (COLUMNNAME_C_ConversionType_ID, Integer.valueOf(C_ConversionType_ID));
	}

	/** Get Currency Type.
		@return Currency Conversion Rate Type
	  */
	public int getC_ConversionType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_ConversionType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Conversion Document.
		@param SP032_Conversion_ID Conversion Document	  */
	public void setSP032_Conversion_ID (int SP032_Conversion_ID)
	{
		if (SP032_Conversion_ID < 1)
			set_ValueNoCheck (COLUMNNAME_SP032_Conversion_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_SP032_Conversion_ID, Integer.valueOf(SP032_Conversion_ID));
	}

	/** Get Conversion Document.
		@return Conversion Document	  */
	public int getSP032_Conversion_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_SP032_Conversion_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Currency getC_Currency() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Currency)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Invoice)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Invoice.Table_Name)
			.getPO(getC_Invoice_ID(), get_TrxName());	}

	/** Set Invoice.
		@param C_Invoice_ID 
		Invoice Identifier
	  */
	public void setC_Invoice_ID (int C_Invoice_ID)
	{
		if (C_Invoice_ID < 1) 
			set_Value (COLUMNNAME_C_Invoice_ID, null);
		else 
			set_Value (COLUMNNAME_C_Invoice_ID, Integer.valueOf(C_Invoice_ID));
	}

	/** Get Invoice.
		@return Invoice Identifier
	  */
	public int getC_Invoice_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Invoice_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Order getC_Order() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Order)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Order.Table_Name)
			.getPO(getC_Order_ID(), get_TrxName());	}

	/** Set Order.
		@param C_Order_ID 
		Order
	  */
	public void setC_Order_ID (int C_Order_ID)
	{
		if (C_Order_ID < 1) 
			set_Value (COLUMNNAME_C_Order_ID, null);
		else 
			set_Value (COLUMNNAME_C_Order_ID, Integer.valueOf(C_Order_ID));
	}

	/** Get Order.
		@return Order
	  */
	public int getC_Order_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Order_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public org.adempiere.core.domains.models.I_C_Payment getC_Payment() throws RuntimeException
    {
		return (org.adempiere.core.domains.models.I_C_Payment)MTable.get(getCtx(), org.adempiere.core.domains.models.I_C_Payment.Table_Name)
			.getPO(getC_Payment_ID(), get_TrxName());	}

	/** Set Payment.
		@param C_Payment_ID 
		Payment identifier
	  */
	public void setC_Payment_ID (int C_Payment_ID)
	{
		if (C_Payment_ID < 1) 
			set_Value (COLUMNNAME_C_Payment_ID, null);
		else 
			set_Value (COLUMNNAME_C_Payment_ID, Integer.valueOf(C_Payment_ID));
	}

	/** Get Payment.
		@return Payment identifier
	  */
	public int getC_Payment_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Payment_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Document Date.
		@param DateDoc 
		Date of the Document
	  */
	public void setDateDoc (Timestamp DateDoc)
	{
		set_Value (COLUMNNAME_DateDoc, DateDoc);
	}

	/** Get Document Date.
		@return Date of the Document
	  */
	public Timestamp getDateDoc () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateDoc);
	}

	/** Set Generated Currency Type Automatically.
		@param SP032_IsGeneratedCurrencyType Generated Currency Type Automatically	  */
	public void setSP032_IsGeneratedCurrencyType (boolean SP032_IsGeneratedCurrencyType)
	{
		set_Value (COLUMNNAME_SP032_IsGeneratedCurrencyType, Boolean.valueOf(SP032_IsGeneratedCurrencyType));
	}

	/** Get Generated Currency Type Automatically.
		@return Generated Currency Type Automatically	  */
	public boolean isSP032_GeneratedCurrencyType ()
	{
		Object oo = get_Value(COLUMNNAME_SP032_IsGeneratedCurrencyType);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Negotiated Conversion Date.
		@param SP032_NegotiatedDate Negotiated Conversion Date	  */
	public void setSP032_NegotiatedDate (Timestamp SP032_NegotiatedDate)
	{
		set_Value (COLUMNNAME_SP032_NegotiatedDate, SP032_NegotiatedDate);
	}

	/** Get Negotiated Conversion Date.
		@return Negotiated Conversion Date	  */
	public Timestamp getSP032_NegotiatedDate ()
	{
		return (Timestamp)get_Value(COLUMNNAME_SP032_NegotiatedDate);
	}

	/** Set Negotiated Currency Rate.
		@param SP032_NegotiatedRate Negotiated Currency Rate	  */
	public void setSP032_NegotiatedRate (BigDecimal SP032_NegotiatedRate)
	{
		set_Value (COLUMNNAME_SP032_NegotiatedRate, SP032_NegotiatedRate);
	}

	/** Get Negotiated Currency Rate.
		@return Negotiated Currency Rate	  */
	public BigDecimal getSP032_NegotiatedRate ()
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_SP032_NegotiatedRate);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Processed.
		@param Processed 
		The document has been processed
	  */
	public void setProcessed (boolean Processed)
	{
		set_Value (COLUMNNAME_Processed, Boolean.valueOf(Processed));
	}

	/** Get Processed.
		@return The document has been processed
	  */
	public boolean isProcessed () 
	{
		Object oo = get_Value(COLUMNNAME_Processed);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
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