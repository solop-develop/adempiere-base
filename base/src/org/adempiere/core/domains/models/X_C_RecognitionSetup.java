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
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for C_RecognitionSetup
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_C_RecognitionSetup extends PO implements I_C_RecognitionSetup, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250715L;

    /** Standard Constructor */
    public X_C_RecognitionSetup (Properties ctx, int C_RecognitionSetup_ID, String trxName)
    {
      super (ctx, C_RecognitionSetup_ID, trxName);
      /** if (C_RecognitionSetup_ID == 0)
        {
			setC_RecognitionSetup_ID (0);
			setRecognitionType (null);
// 'AC'
        } */
    }

    /** Load Constructor */
    public X_C_RecognitionSetup (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 1 - Org 
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
      StringBuffer sb = new StringBuffer ("X_C_RecognitionSetup[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_Invoice getC_Invoice() throws RuntimeException
    {
		return (I_C_Invoice)MTable.get(getCtx(), I_C_Invoice.Table_Name)
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

	public I_C_Order getC_Order() throws RuntimeException
    {
		return (I_C_Order)MTable.get(getCtx(), I_C_Order.Table_Name)
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

	/** Set Revenue Recognition Setup.
		@param C_RecognitionSetup_ID 
		Revenue Recognition Setup
	  */
	public void setC_RecognitionSetup_ID (int C_RecognitionSetup_ID)
	{
		if (C_RecognitionSetup_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_RecognitionSetup_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_RecognitionSetup_ID, Integer.valueOf(C_RecognitionSetup_ID));
	}

	/** Get Revenue Recognition Setup.
		@return Revenue Recognition Setup
	  */
	public int getC_RecognitionSetup_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_RecognitionSetup_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set End Date.
		@param EndDate 
		Last effective date (inclusive)
	  */
	public void setEndDate (Timestamp EndDate)
	{
		set_Value (COLUMNNAME_EndDate, EndDate);
	}

	/** Get End Date.
		@return Last effective date (inclusive)
	  */
	public Timestamp getEndDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_EndDate);
	}

	/** Set Number of Months.
		@param NoMonths Number of Months	  */
	public void setNoMonths (int NoMonths)
	{
		set_Value (COLUMNNAME_NoMonths, Integer.valueOf(NoMonths));
	}

	/** Get Number of Months.
		@return Number of Months	  */
	public int getNoMonths () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_NoMonths);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** RecognitionType AD_Reference_ID=54496 */
	public static final int RECOGNITIONTYPE_AD_Reference_ID=54496;
	/** Sales Basis = SB */
	public static final String RECOGNITIONTYPE_SalesBasis = "SB";
	/** Completed Contract = CC */
	public static final String RECOGNITIONTYPE_CompletedContract = "CC";
	/** Percentage of Completion = PC */
	public static final String RECOGNITIONTYPE_PercentageOfCompletion = "PC";
	/** Installment = II */
	public static final String RECOGNITIONTYPE_Installment = "II";
	/** Cost Recovery = CR */
	public static final String RECOGNITIONTYPE_CostRecovery = "CR";
	/** Accrual = AC */
	public static final String RECOGNITIONTYPE_Accrual = "AC";
	/** Deferred Revenue = DR */
	public static final String RECOGNITIONTYPE_DeferredRevenue = "DR";
	/** Milestone Based = MB */
	public static final String RECOGNITIONTYPE_MilestoneBased = "MB";
	/** Set Recognition Type.
		@param RecognitionType 
		Recognition Type
	  */
	public void setRecognitionType (String RecognitionType)
	{

		set_Value (COLUMNNAME_RecognitionType, RecognitionType);
	}

	/** Get Recognition Type.
		@return Recognition Type
	  */
	public String getRecognitionType () 
	{
		return (String)get_Value(COLUMNNAME_RecognitionType);
	}

	/** Set Start Date.
		@param StartDate 
		First effective day (inclusive)
	  */
	public void setStartDate (Timestamp StartDate)
	{
		set_Value (COLUMNNAME_StartDate, StartDate);
	}

	/** Get Start Date.
		@return First effective day (inclusive)
	  */
	public Timestamp getStartDate () 
	{
		return (Timestamp)get_Value(COLUMNNAME_StartDate);
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