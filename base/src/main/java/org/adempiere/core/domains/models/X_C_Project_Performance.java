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
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for C_Project_Performance
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_C_Project_Performance extends PO implements I_C_Project_Performance, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20230901L;

    /** Standard Constructor */
    public X_C_Project_Performance (Properties ctx, int C_Project_Performance_ID, String trxName)
    {
      super (ctx, C_Project_Performance_ID, trxName);
      /** if (C_Project_Performance_ID == 0)
        {
			setC_Project_ID (0);
			setC_Project_Performance_ID (0);
        } */
    }

    /** Load Constructor */
    public X_C_Project_Performance (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_C_Project_Performance[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Cost Value.
		@param CostAmt 
		Value with Cost
	  */
	public void setCostAmt (BigDecimal CostAmt)
	{
		set_Value (COLUMNNAME_CostAmt, CostAmt);
	}

	/** Get Cost Value.
		@return Value with Cost
	  */
	public BigDecimal getCostAmt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostAmt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costamtinherited.
		@param costamtinherited costamtinherited	  */
	public void setcostamtinherited (BigDecimal costamtinherited)
	{
		set_Value (COLUMNNAME_costamtinherited, costamtinherited);
	}

	/** Get costamtinherited.
		@return costamtinherited	  */
	public BigDecimal getcostamtinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costamtinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Cost Value LL.
		@param CostAmtLL 
		Value with Cost Lower Level
	  */
	public void setCostAmtLL (BigDecimal CostAmtLL)
	{
		set_Value (COLUMNNAME_CostAmtLL, CostAmtLL);
	}

	/** Get Cost Value LL.
		@return Value with Cost Lower Level
	  */
	public BigDecimal getCostAmtLL () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostAmtLL);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costamtvolumeinherited.
		@param costamtvolumeinherited costamtvolumeinherited	  */
	public void setcostamtvolumeinherited (BigDecimal costamtvolumeinherited)
	{
		set_Value (COLUMNNAME_costamtvolumeinherited, costamtvolumeinherited);
	}

	/** Get costamtvolumeinherited.
		@return costamtvolumeinherited	  */
	public BigDecimal getcostamtvolumeinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costamtvolumeinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costamtweightinherited.
		@param costamtweightinherited costamtweightinherited	  */
	public void setcostamtweightinherited (BigDecimal costamtweightinherited)
	{
		set_Value (COLUMNNAME_costamtweightinherited, costamtweightinherited);
	}

	/** Get costamtweightinherited.
		@return costamtweightinherited	  */
	public BigDecimal getcostamtweightinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costamtweightinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostDiffExecution.
		@param CostDiffExecution CostDiffExecution	  */
	public void setCostDiffExecution (BigDecimal CostDiffExecution)
	{
		set_Value (COLUMNNAME_CostDiffExecution, CostDiffExecution);
	}

	/** Get CostDiffExecution.
		@return CostDiffExecution	  */
	public BigDecimal getCostDiffExecution () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostDiffExecution);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costdiffexecutionll.
		@param costdiffexecutionll costdiffexecutionll	  */
	public void setcostdiffexecutionll (BigDecimal costdiffexecutionll)
	{
		set_Value (COLUMNNAME_costdiffexecutionll, costdiffexecutionll);
	}

	/** Get costdiffexecutionll.
		@return costdiffexecutionll	  */
	public BigDecimal getcostdiffexecutionll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costdiffexecutionll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostExtrapolated.
		@param CostExtrapolated CostExtrapolated	  */
	public void setCostExtrapolated (BigDecimal CostExtrapolated)
	{
		set_Value (COLUMNNAME_CostExtrapolated, CostExtrapolated);
	}

	/** Get CostExtrapolated.
		@return CostExtrapolated	  */
	public BigDecimal getCostExtrapolated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostExtrapolated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costextrapolatedinherited.
		@param costextrapolatedinherited costextrapolatedinherited	  */
	public void setcostextrapolatedinherited (BigDecimal costextrapolatedinherited)
	{
		set_Value (COLUMNNAME_costextrapolatedinherited, costextrapolatedinherited);
	}

	/** Get costextrapolatedinherited.
		@return costextrapolatedinherited	  */
	public BigDecimal getcostextrapolatedinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costextrapolatedinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costextrapolatedll.
		@param costextrapolatedll costextrapolatedll	  */
	public void setcostextrapolatedll (BigDecimal costextrapolatedll)
	{
		set_Value (COLUMNNAME_costextrapolatedll, costextrapolatedll);
	}

	/** Get costextrapolatedll.
		@return costextrapolatedll	  */
	public BigDecimal getcostextrapolatedll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costextrapolatedll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costextrapolatedvolinherited.
		@param costextrapolatedvolinherited costextrapolatedvolinherited	  */
	public void setcostextrapolatedvolinherited (BigDecimal costextrapolatedvolinherited)
	{
		set_Value (COLUMNNAME_costextrapolatedvolinherited, costextrapolatedvolinherited);
	}

	/** Get costextrapolatedvolinherited.
		@return costextrapolatedvolinherited	  */
	public BigDecimal getcostextrapolatedvolinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costextrapolatedvolinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costextrapolatedwghtinherited.
		@param costextrapolatedwghtinherited costextrapolatedwghtinherited	  */
	public void setcostextrapolatedwghtinherited (BigDecimal costextrapolatedwghtinherited)
	{
		set_Value (COLUMNNAME_costextrapolatedwghtinherited, costextrapolatedwghtinherited);
	}

	/** Get costextrapolatedwghtinherited.
		@return costextrapolatedwghtinherited	  */
	public BigDecimal getcostextrapolatedwghtinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costextrapolatedwghtinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostIssueInventory.
		@param CostIssueInventory CostIssueInventory	  */
	public void setCostIssueInventory (BigDecimal CostIssueInventory)
	{
		set_Value (COLUMNNAME_CostIssueInventory, CostIssueInventory);
	}

	/** Get CostIssueInventory.
		@return CostIssueInventory	  */
	public BigDecimal getCostIssueInventory () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostIssueInventory);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costissueinventoryll.
		@param costissueinventoryll costissueinventoryll	  */
	public void setcostissueinventoryll (BigDecimal costissueinventoryll)
	{
		set_Value (COLUMNNAME_costissueinventoryll, costissueinventoryll);
	}

	/** Get costissueinventoryll.
		@return costissueinventoryll	  */
	public BigDecimal getcostissueinventoryll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costissueinventoryll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostIssueProduct.
		@param CostIssueProduct CostIssueProduct	  */
	public void setCostIssueProduct (BigDecimal CostIssueProduct)
	{
		set_Value (COLUMNNAME_CostIssueProduct, CostIssueProduct);
	}

	/** Get CostIssueProduct.
		@return CostIssueProduct	  */
	public BigDecimal getCostIssueProduct () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostIssueProduct);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costissueproductll.
		@param costissueproductll costissueproductll	  */
	public void setcostissueproductll (BigDecimal costissueproductll)
	{
		set_Value (COLUMNNAME_costissueproductll, costissueproductll);
	}

	/** Get costissueproductll.
		@return costissueproductll	  */
	public BigDecimal getcostissueproductll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costissueproductll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostIssueResource.
		@param CostIssueResource CostIssueResource	  */
	public void setCostIssueResource (BigDecimal CostIssueResource)
	{
		set_Value (COLUMNNAME_CostIssueResource, CostIssueResource);
	}

	/** Get CostIssueResource.
		@return CostIssueResource	  */
	public BigDecimal getCostIssueResource () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostIssueResource);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costissueresourcell.
		@param costissueresourcell costissueresourcell	  */
	public void setcostissueresourcell (BigDecimal costissueresourcell)
	{
		set_Value (COLUMNNAME_costissueresourcell, costissueresourcell);
	}

	/** Get costissueresourcell.
		@return costissueresourcell	  */
	public BigDecimal getcostissueresourcell () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costissueresourcell);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostIssueSum.
		@param CostIssueSum CostIssueSum	  */
	public void setCostIssueSum (BigDecimal CostIssueSum)
	{
		set_Value (COLUMNNAME_CostIssueSum, CostIssueSum);
	}

	/** Get CostIssueSum.
		@return CostIssueSum	  */
	public BigDecimal getCostIssueSum () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostIssueSum);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costissuesumll.
		@param costissuesumll costissuesumll	  */
	public void setcostissuesumll (BigDecimal costissuesumll)
	{
		set_Value (COLUMNNAME_costissuesumll, costissuesumll);
	}

	/** Get costissuesumll.
		@return costissuesumll	  */
	public BigDecimal getcostissuesumll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costissuesumll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostNotInvoiced.
		@param CostNotInvoiced CostNotInvoiced	  */
	public void setCostNotInvoiced (BigDecimal CostNotInvoiced)
	{
		set_Value (COLUMNNAME_CostNotInvoiced, CostNotInvoiced);
	}

	/** Get CostNotInvoiced.
		@return CostNotInvoiced	  */
	public BigDecimal getCostNotInvoiced () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostNotInvoiced);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costnotinvoicedll.
		@param costnotinvoicedll costnotinvoicedll	  */
	public void setcostnotinvoicedll (BigDecimal costnotinvoicedll)
	{
		set_Value (COLUMNNAME_costnotinvoicedll, costnotinvoicedll);
	}

	/** Get costnotinvoicedll.
		@return costnotinvoicedll	  */
	public BigDecimal getcostnotinvoicedll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costnotinvoicedll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set CostPlanned.
		@param CostPlanned CostPlanned	  */
	public void setCostPlanned (BigDecimal CostPlanned)
	{
		set_Value (COLUMNNAME_CostPlanned, CostPlanned);
	}

	/** Get CostPlanned.
		@return CostPlanned	  */
	public BigDecimal getCostPlanned () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_CostPlanned);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costplannedinherited.
		@param costplannedinherited costplannedinherited	  */
	public void setcostplannedinherited (BigDecimal costplannedinherited)
	{
		set_Value (COLUMNNAME_costplannedinherited, costplannedinherited);
	}

	/** Get costplannedinherited.
		@return costplannedinherited	  */
	public BigDecimal getcostplannedinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costplannedinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costplannedll.
		@param costplannedll costplannedll	  */
	public void setcostplannedll (BigDecimal costplannedll)
	{
		set_Value (COLUMNNAME_costplannedll, costplannedll);
	}

	/** Get costplannedll.
		@return costplannedll	  */
	public BigDecimal getcostplannedll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costplannedll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costplannedvolumeinherited.
		@param costplannedvolumeinherited costplannedvolumeinherited	  */
	public void setcostplannedvolumeinherited (BigDecimal costplannedvolumeinherited)
	{
		set_Value (COLUMNNAME_costplannedvolumeinherited, costplannedvolumeinherited);
	}

	/** Get costplannedvolumeinherited.
		@return costplannedvolumeinherited	  */
	public BigDecimal getcostplannedvolumeinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costplannedvolumeinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set costplannedweightinherited.
		@param costplannedweightinherited costplannedweightinherited	  */
	public void setcostplannedweightinherited (BigDecimal costplannedweightinherited)
	{
		set_Value (COLUMNNAME_costplannedweightinherited, costplannedweightinherited);
	}

	/** Get costplannedweightinherited.
		@return costplannedweightinherited	  */
	public BigDecimal getcostplannedweightinherited () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_costplannedweightinherited);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	public I_C_Project getC_Project() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getC_Project_ID(), get_TrxName());	}

	/** Set Project.
		@param C_Project_ID 
		Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID)
	{
		if (C_Project_ID < 1) 
			set_Value (COLUMNNAME_C_Project_ID, null);
		else 
			set_Value (COLUMNNAME_C_Project_ID, Integer.valueOf(C_Project_ID));
	}

	/** Get Project.
		@return Financial Project
	  */
	public int getC_Project_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Project getc_project_parent() throws RuntimeException
    {
		return (I_C_Project)MTable.get(getCtx(), I_C_Project.Table_Name)
			.getPO(getc_project_parent_id(), get_TrxName());	}

	/** Set c_project_parent_id.
		@param c_project_parent_id c_project_parent_id	  */
	public void setc_project_parent_id (int c_project_parent_id)
	{
		set_Value (COLUMNNAME_c_project_parent_id, Integer.valueOf(c_project_parent_id));
	}

	/** Get c_project_parent_id.
		@return c_project_parent_id	  */
	public int getc_project_parent_id () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_c_project_parent_id);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set C_Project_Performance.
		@param C_Project_Performance_ID C_Project_Performance	  */
	public void setC_Project_Performance_ID (int C_Project_Performance_ID)
	{
		if (C_Project_Performance_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_Project_Performance_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_Project_Performance_ID, Integer.valueOf(C_Project_Performance_ID));
	}

	/** Get C_Project_Performance.
		@return C_Project_Performance	  */
	public int getC_Project_Performance_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Project_Performance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Date last run.
		@param DateLastRun 
		Date the process was last run.
	  */
	public void setDateLastRun (Timestamp DateLastRun)
	{
		set_Value (COLUMNNAME_DateLastRun, DateLastRun);
	}

	/** Get Date last run.
		@return Date the process was last run.
	  */
	public Timestamp getDateLastRun () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateLastRun);
	}

	/** Set grossmargin.
		@param grossmargin grossmargin	  */
	public void setgrossmargin (BigDecimal grossmargin)
	{
		set_Value (COLUMNNAME_grossmargin, grossmargin);
	}

	/** Get grossmargin.
		@return grossmargin	  */
	public BigDecimal getgrossmargin () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_grossmargin);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set grossmarginll.
		@param grossmarginll grossmarginll	  */
	public void setgrossmarginll (BigDecimal grossmarginll)
	{
		set_Value (COLUMNNAME_grossmarginll, grossmarginll);
	}

	/** Get grossmarginll.
		@return grossmarginll	  */
	public BigDecimal getgrossmarginll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_grossmarginll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set grossmargintotal.
		@param grossmargintotal grossmargintotal	  */
	public void setgrossmargintotal (BigDecimal grossmargintotal)
	{
		set_Value (COLUMNNAME_grossmargintotal, grossmargintotal);
	}

	/** Get grossmargintotal.
		@return grossmargintotal	  */
	public BigDecimal getgrossmargintotal () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_grossmargintotal);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Margin %.
		@param Margin 
		Margin for a product as a percentage
	  */
	public void setMargin (BigDecimal Margin)
	{
		set_Value (COLUMNNAME_Margin, Margin);
	}

	/** Get Margin %.
		@return Margin for a product as a percentage
	  */
	public BigDecimal getMargin () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_Margin);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set projectofferedrevenueplanned.
		@param projectofferedrevenueplanned projectofferedrevenueplanned	  */
	public void setprojectofferedrevenueplanned (BigDecimal projectofferedrevenueplanned)
	{
		set_Value (COLUMNNAME_projectofferedrevenueplanned, projectofferedrevenueplanned);
	}

	/** Get projectofferedrevenueplanned.
		@return projectofferedrevenueplanned	  */
	public BigDecimal getprojectofferedrevenueplanned () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_projectofferedrevenueplanned);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set projectpricelistrevenueplanned.
		@param projectpricelistrevenueplanned projectpricelistrevenueplanned	  */
	public void setprojectpricelistrevenueplanned (BigDecimal projectpricelistrevenueplanned)
	{
		set_Value (COLUMNNAME_projectpricelistrevenueplanned, projectpricelistrevenueplanned);
	}

	/** Get projectpricelistrevenueplanned.
		@return projectpricelistrevenueplanned	  */
	public BigDecimal getprojectpricelistrevenueplanned () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_projectpricelistrevenueplanned);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenueamt.
		@param revenueamt revenueamt	  */
	public void setrevenueamt (BigDecimal revenueamt)
	{
		set_Value (COLUMNNAME_revenueamt, revenueamt);
	}

	/** Get revenueamt.
		@return revenueamt	  */
	public BigDecimal getrevenueamt () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenueamt);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenueamtll.
		@param revenueamtll revenueamtll	  */
	public void setrevenueamtll (BigDecimal revenueamtll)
	{
		set_Value (COLUMNNAME_revenueamtll, revenueamtll);
	}

	/** Get revenueamtll.
		@return revenueamtll	  */
	public BigDecimal getrevenueamtll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenueamtll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenueextrapolated.
		@param revenueextrapolated revenueextrapolated	  */
	public void setrevenueextrapolated (BigDecimal revenueextrapolated)
	{
		set_Value (COLUMNNAME_revenueextrapolated, revenueextrapolated);
	}

	/** Get revenueextrapolated.
		@return revenueextrapolated	  */
	public BigDecimal getrevenueextrapolated () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenueextrapolated);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenueextrapolatedll.
		@param revenueextrapolatedll revenueextrapolatedll	  */
	public void setrevenueextrapolatedll (BigDecimal revenueextrapolatedll)
	{
		set_Value (COLUMNNAME_revenueextrapolatedll, revenueextrapolatedll);
	}

	/** Get revenueextrapolatedll.
		@return revenueextrapolatedll	  */
	public BigDecimal getrevenueextrapolatedll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenueextrapolatedll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenuenotinvoiced.
		@param revenuenotinvoiced revenuenotinvoiced	  */
	public void setrevenuenotinvoiced (BigDecimal revenuenotinvoiced)
	{
		set_Value (COLUMNNAME_revenuenotinvoiced, revenuenotinvoiced);
	}

	/** Get revenuenotinvoiced.
		@return revenuenotinvoiced	  */
	public BigDecimal getrevenuenotinvoiced () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenuenotinvoiced);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenuenotinvoicedll.
		@param revenuenotinvoicedll revenuenotinvoicedll	  */
	public void setrevenuenotinvoicedll (BigDecimal revenuenotinvoicedll)
	{
		set_Value (COLUMNNAME_revenuenotinvoicedll, revenuenotinvoicedll);
	}

	/** Get revenuenotinvoicedll.
		@return revenuenotinvoicedll	  */
	public BigDecimal getrevenuenotinvoicedll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenuenotinvoicedll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenueplanned.
		@param revenueplanned revenueplanned	  */
	public void setrevenueplanned (BigDecimal revenueplanned)
	{
		set_Value (COLUMNNAME_revenueplanned, revenueplanned);
	}

	/** Get revenueplanned.
		@return revenueplanned	  */
	public BigDecimal getrevenueplanned () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenueplanned);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set revenueplannedll.
		@param revenueplannedll revenueplannedll	  */
	public void setrevenueplannedll (BigDecimal revenueplannedll)
	{
		set_Value (COLUMNNAME_revenueplannedll, revenueplannedll);
	}

	/** Get revenueplannedll.
		@return revenueplannedll	  */
	public BigDecimal getrevenueplannedll () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_revenueplannedll);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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