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
package org.adempiere.core.domains.models;

import org.compiere.model.MTable;
import org.compiere.util.KeyNamePair;

import java.math.BigDecimal;
import java.sql.Timestamp;

/** Generated Interface for C_Project_Performance
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4
 */
public interface I_C_Project_Performance 
{

    /** TableName=C_Project_Performance */
    public static final String Table_Name = "C_Project_Performance";

    /** AD_Table_ID=2000120 */
    public static final int Table_ID = MTable.getTable_ID(Table_Name);

    KeyNamePair Model = new KeyNamePair(Table_ID, Table_Name);

    /** AccessLevel = 3 - Client - Org 
     */
    BigDecimal accessLevel = BigDecimal.valueOf(3);

    /** Load Meta Data */

    /** Column name AD_Client_ID */
    public static final String COLUMNNAME_AD_Client_ID = "AD_Client_ID";

	/** Get Client.
	  * Client/Tenant for this installation.
	  */
	public int getAD_Client_ID();

    /** Column name AD_Org_ID */
    public static final String COLUMNNAME_AD_Org_ID = "AD_Org_ID";

	/** Set Organization.
	  * Organizational entity within client
	  */
	public void setAD_Org_ID (int AD_Org_ID);

	/** Get Organization.
	  * Organizational entity within client
	  */
	public int getAD_Org_ID();

    /** Column name CostAmt */
    public static final String COLUMNNAME_CostAmt = "CostAmt";

	/** Set Cost Value.
	  * Value with Cost
	  */
	public void setCostAmt (BigDecimal CostAmt);

	/** Get Cost Value.
	  * Value with Cost
	  */
	public BigDecimal getCostAmt();

    /** Column name costamtinherited */
    public static final String COLUMNNAME_costamtinherited = "costamtinherited";

	/** Set costamtinherited	  */
	public void setcostamtinherited (BigDecimal costamtinherited);

	/** Get costamtinherited	  */
	public BigDecimal getcostamtinherited();

    /** Column name CostAmtLL */
    public static final String COLUMNNAME_CostAmtLL = "CostAmtLL";

	/** Set Cost Value LL.
	  * Value with Cost Lower Level
	  */
	public void setCostAmtLL (BigDecimal CostAmtLL);

	/** Get Cost Value LL.
	  * Value with Cost Lower Level
	  */
	public BigDecimal getCostAmtLL();

    /** Column name costamtvolumeinherited */
    public static final String COLUMNNAME_costamtvolumeinherited = "costamtvolumeinherited";

	/** Set costamtvolumeinherited	  */
	public void setcostamtvolumeinherited (BigDecimal costamtvolumeinherited);

	/** Get costamtvolumeinherited	  */
	public BigDecimal getcostamtvolumeinherited();

    /** Column name costamtweightinherited */
    public static final String COLUMNNAME_costamtweightinherited = "costamtweightinherited";

	/** Set costamtweightinherited	  */
	public void setcostamtweightinherited (BigDecimal costamtweightinherited);

	/** Get costamtweightinherited	  */
	public BigDecimal getcostamtweightinherited();

    /** Column name CostDiffExecution */
    public static final String COLUMNNAME_CostDiffExecution = "CostDiffExecution";

	/** Set CostDiffExecution	  */
	public void setCostDiffExecution (BigDecimal CostDiffExecution);

	/** Get CostDiffExecution	  */
	public BigDecimal getCostDiffExecution();

    /** Column name costdiffexecutionll */
    public static final String COLUMNNAME_costdiffexecutionll = "costdiffexecutionll";

	/** Set costdiffexecutionll	  */
	public void setcostdiffexecutionll (BigDecimal costdiffexecutionll);

	/** Get costdiffexecutionll	  */
	public BigDecimal getcostdiffexecutionll();

    /** Column name CostExtrapolated */
    public static final String COLUMNNAME_CostExtrapolated = "CostExtrapolated";

	/** Set CostExtrapolated	  */
	public void setCostExtrapolated (BigDecimal CostExtrapolated);

	/** Get CostExtrapolated	  */
	public BigDecimal getCostExtrapolated();

    /** Column name costextrapolatedinherited */
    public static final String COLUMNNAME_costextrapolatedinherited = "costextrapolatedinherited";

	/** Set costextrapolatedinherited	  */
	public void setcostextrapolatedinherited (BigDecimal costextrapolatedinherited);

	/** Get costextrapolatedinherited	  */
	public BigDecimal getcostextrapolatedinherited();

    /** Column name costextrapolatedll */
    public static final String COLUMNNAME_costextrapolatedll = "costextrapolatedll";

	/** Set costextrapolatedll	  */
	public void setcostextrapolatedll (BigDecimal costextrapolatedll);

	/** Get costextrapolatedll	  */
	public BigDecimal getcostextrapolatedll();

    /** Column name costextrapolatedvolinherited */
    public static final String COLUMNNAME_costextrapolatedvolinherited = "costextrapolatedvolinherited";

	/** Set costextrapolatedvolinherited	  */
	public void setcostextrapolatedvolinherited (BigDecimal costextrapolatedvolinherited);

	/** Get costextrapolatedvolinherited	  */
	public BigDecimal getcostextrapolatedvolinherited();

    /** Column name costextrapolatedwghtinherited */
    public static final String COLUMNNAME_costextrapolatedwghtinherited = "costextrapolatedwghtinherited";

	/** Set costextrapolatedwghtinherited	  */
	public void setcostextrapolatedwghtinherited (BigDecimal costextrapolatedwghtinherited);

	/** Get costextrapolatedwghtinherited	  */
	public BigDecimal getcostextrapolatedwghtinherited();

    /** Column name CostIssueInventory */
    public static final String COLUMNNAME_CostIssueInventory = "CostIssueInventory";

	/** Set CostIssueInventory	  */
	public void setCostIssueInventory (BigDecimal CostIssueInventory);

	/** Get CostIssueInventory	  */
	public BigDecimal getCostIssueInventory();

    /** Column name costissueinventoryll */
    public static final String COLUMNNAME_costissueinventoryll = "costissueinventoryll";

	/** Set costissueinventoryll	  */
	public void setcostissueinventoryll (BigDecimal costissueinventoryll);

	/** Get costissueinventoryll	  */
	public BigDecimal getcostissueinventoryll();

    /** Column name CostIssueProduct */
    public static final String COLUMNNAME_CostIssueProduct = "CostIssueProduct";

	/** Set CostIssueProduct	  */
	public void setCostIssueProduct (BigDecimal CostIssueProduct);

	/** Get CostIssueProduct	  */
	public BigDecimal getCostIssueProduct();

    /** Column name costissueproductll */
    public static final String COLUMNNAME_costissueproductll = "costissueproductll";

	/** Set costissueproductll	  */
	public void setcostissueproductll (BigDecimal costissueproductll);

	/** Get costissueproductll	  */
	public BigDecimal getcostissueproductll();

    /** Column name CostIssueResource */
    public static final String COLUMNNAME_CostIssueResource = "CostIssueResource";

	/** Set CostIssueResource	  */
	public void setCostIssueResource (BigDecimal CostIssueResource);

	/** Get CostIssueResource	  */
	public BigDecimal getCostIssueResource();

    /** Column name costissueresourcell */
    public static final String COLUMNNAME_costissueresourcell = "costissueresourcell";

	/** Set costissueresourcell	  */
	public void setcostissueresourcell (BigDecimal costissueresourcell);

	/** Get costissueresourcell	  */
	public BigDecimal getcostissueresourcell();

    /** Column name CostIssueSum */
    public static final String COLUMNNAME_CostIssueSum = "CostIssueSum";

	/** Set CostIssueSum	  */
	public void setCostIssueSum (BigDecimal CostIssueSum);

	/** Get CostIssueSum	  */
	public BigDecimal getCostIssueSum();

    /** Column name costissuesumll */
    public static final String COLUMNNAME_costissuesumll = "costissuesumll";

	/** Set costissuesumll	  */
	public void setcostissuesumll (BigDecimal costissuesumll);

	/** Get costissuesumll	  */
	public BigDecimal getcostissuesumll();

    /** Column name CostNotInvoiced */
    public static final String COLUMNNAME_CostNotInvoiced = "CostNotInvoiced";

	/** Set CostNotInvoiced	  */
	public void setCostNotInvoiced (BigDecimal CostNotInvoiced);

	/** Get CostNotInvoiced	  */
	public BigDecimal getCostNotInvoiced();

    /** Column name costnotinvoicedll */
    public static final String COLUMNNAME_costnotinvoicedll = "costnotinvoicedll";

	/** Set costnotinvoicedll	  */
	public void setcostnotinvoicedll (BigDecimal costnotinvoicedll);

	/** Get costnotinvoicedll	  */
	public BigDecimal getcostnotinvoicedll();

    /** Column name CostPlanned */
    public static final String COLUMNNAME_CostPlanned = "CostPlanned";

	/** Set CostPlanned	  */
	public void setCostPlanned (BigDecimal CostPlanned);

	/** Get CostPlanned	  */
	public BigDecimal getCostPlanned();

    /** Column name costplannedinherited */
    public static final String COLUMNNAME_costplannedinherited = "costplannedinherited";

	/** Set costplannedinherited	  */
	public void setcostplannedinherited (BigDecimal costplannedinherited);

	/** Get costplannedinherited	  */
	public BigDecimal getcostplannedinherited();

    /** Column name costplannedll */
    public static final String COLUMNNAME_costplannedll = "costplannedll";

	/** Set costplannedll	  */
	public void setcostplannedll (BigDecimal costplannedll);

	/** Get costplannedll	  */
	public BigDecimal getcostplannedll();

    /** Column name costplannedvolumeinherited */
    public static final String COLUMNNAME_costplannedvolumeinherited = "costplannedvolumeinherited";

	/** Set costplannedvolumeinherited	  */
	public void setcostplannedvolumeinherited (BigDecimal costplannedvolumeinherited);

	/** Get costplannedvolumeinherited	  */
	public BigDecimal getcostplannedvolumeinherited();

    /** Column name costplannedweightinherited */
    public static final String COLUMNNAME_costplannedweightinherited = "costplannedweightinherited";

	/** Set costplannedweightinherited	  */
	public void setcostplannedweightinherited (BigDecimal costplannedweightinherited);

	/** Get costplannedweightinherited	  */
	public BigDecimal getcostplannedweightinherited();

    /** Column name C_Project_ID */
    public static final String COLUMNNAME_C_Project_ID = "C_Project_ID";

	/** Set Project.
	  * Financial Project
	  */
	public void setC_Project_ID (int C_Project_ID);

	/** Get Project.
	  * Financial Project
	  */
	public int getC_Project_ID();

	public I_C_Project getC_Project() throws RuntimeException;

    /** Column name c_project_parent_id */
    public static final String COLUMNNAME_c_project_parent_id = "c_project_parent_id";

	/** Set c_project_parent_id	  */
	public void setc_project_parent_id (int c_project_parent_id);

	/** Get c_project_parent_id	  */
	public int getc_project_parent_id();

	public I_C_Project getc_project_parent() throws RuntimeException;

    /** Column name C_Project_Performance_ID */
    public static final String COLUMNNAME_C_Project_Performance_ID = "C_Project_Performance_ID";

	/** Set C_Project_Performance	  */
	public void setC_Project_Performance_ID (int C_Project_Performance_ID);

	/** Get C_Project_Performance	  */
	public int getC_Project_Performance_ID();

    /** Column name Created */
    public static final String COLUMNNAME_Created = "Created";

	/** Get Created.
	  * Date this record was created
	  */
	public Timestamp getCreated();

    /** Column name CreatedBy */
    public static final String COLUMNNAME_CreatedBy = "CreatedBy";

	/** Get Created By.
	  * User who created this records
	  */
	public int getCreatedBy();

    /** Column name DateLastRun */
    public static final String COLUMNNAME_DateLastRun = "DateLastRun";

	/** Set Date last run.
	  * Date the process was last run.
	  */
	public void setDateLastRun (Timestamp DateLastRun);

	/** Get Date last run.
	  * Date the process was last run.
	  */
	public Timestamp getDateLastRun();

    /** Column name grossmargin */
    public static final String COLUMNNAME_grossmargin = "grossmargin";

	/** Set grossmargin	  */
	public void setgrossmargin (BigDecimal grossmargin);

	/** Get grossmargin	  */
	public BigDecimal getgrossmargin();

    /** Column name grossmarginll */
    public static final String COLUMNNAME_grossmarginll = "grossmarginll";

	/** Set grossmarginll	  */
	public void setgrossmarginll (BigDecimal grossmarginll);

	/** Get grossmarginll	  */
	public BigDecimal getgrossmarginll();

    /** Column name grossmargintotal */
    public static final String COLUMNNAME_grossmargintotal = "grossmargintotal";

	/** Set grossmargintotal	  */
	public void setgrossmargintotal (BigDecimal grossmargintotal);

	/** Get grossmargintotal	  */
	public BigDecimal getgrossmargintotal();

    /** Column name IsActive */
    public static final String COLUMNNAME_IsActive = "IsActive";

	/** Set Active.
	  * The record is active in the system
	  */
	public void setIsActive (boolean IsActive);

	/** Get Active.
	  * The record is active in the system
	  */
	public boolean isActive();

    /** Column name Margin */
    public static final String COLUMNNAME_Margin = "Margin";

	/** Set Margin %.
	  * Margin for a product as a percentage
	  */
	public void setMargin (BigDecimal Margin);

	/** Get Margin %.
	  * Margin for a product as a percentage
	  */
	public BigDecimal getMargin();

    /** Column name projectofferedrevenueplanned */
    public static final String COLUMNNAME_projectofferedrevenueplanned = "projectofferedrevenueplanned";

	/** Set projectofferedrevenueplanned	  */
	public void setprojectofferedrevenueplanned (BigDecimal projectofferedrevenueplanned);

	/** Get projectofferedrevenueplanned	  */
	public BigDecimal getprojectofferedrevenueplanned();

    /** Column name projectpricelistrevenueplanned */
    public static final String COLUMNNAME_projectpricelistrevenueplanned = "projectpricelistrevenueplanned";

	/** Set projectpricelistrevenueplanned	  */
	public void setprojectpricelistrevenueplanned (BigDecimal projectpricelistrevenueplanned);

	/** Get projectpricelistrevenueplanned	  */
	public BigDecimal getprojectpricelistrevenueplanned();

    /** Column name revenueamt */
    public static final String COLUMNNAME_revenueamt = "revenueamt";

	/** Set revenueamt	  */
	public void setrevenueamt (BigDecimal revenueamt);

	/** Get revenueamt	  */
	public BigDecimal getrevenueamt();

    /** Column name revenueamtll */
    public static final String COLUMNNAME_revenueamtll = "revenueamtll";

	/** Set revenueamtll	  */
	public void setrevenueamtll (BigDecimal revenueamtll);

	/** Get revenueamtll	  */
	public BigDecimal getrevenueamtll();

    /** Column name revenueextrapolated */
    public static final String COLUMNNAME_revenueextrapolated = "revenueextrapolated";

	/** Set revenueextrapolated	  */
	public void setrevenueextrapolated (BigDecimal revenueextrapolated);

	/** Get revenueextrapolated	  */
	public BigDecimal getrevenueextrapolated();

    /** Column name revenueextrapolatedll */
    public static final String COLUMNNAME_revenueextrapolatedll = "revenueextrapolatedll";

	/** Set revenueextrapolatedll	  */
	public void setrevenueextrapolatedll (BigDecimal revenueextrapolatedll);

	/** Get revenueextrapolatedll	  */
	public BigDecimal getrevenueextrapolatedll();

    /** Column name revenuenotinvoiced */
    public static final String COLUMNNAME_revenuenotinvoiced = "revenuenotinvoiced";

	/** Set revenuenotinvoiced	  */
	public void setrevenuenotinvoiced (BigDecimal revenuenotinvoiced);

	/** Get revenuenotinvoiced	  */
	public BigDecimal getrevenuenotinvoiced();

    /** Column name revenuenotinvoicedll */
    public static final String COLUMNNAME_revenuenotinvoicedll = "revenuenotinvoicedll";

	/** Set revenuenotinvoicedll	  */
	public void setrevenuenotinvoicedll (BigDecimal revenuenotinvoicedll);

	/** Get revenuenotinvoicedll	  */
	public BigDecimal getrevenuenotinvoicedll();

    /** Column name revenueplanned */
    public static final String COLUMNNAME_revenueplanned = "revenueplanned";

	/** Set revenueplanned	  */
	public void setrevenueplanned (BigDecimal revenueplanned);

	/** Get revenueplanned	  */
	public BigDecimal getrevenueplanned();

    /** Column name revenueplannedll */
    public static final String COLUMNNAME_revenueplannedll = "revenueplannedll";

	/** Set revenueplannedll	  */
	public void setrevenueplannedll (BigDecimal revenueplannedll);

	/** Get revenueplannedll	  */
	public BigDecimal getrevenueplannedll();

    /** Column name Updated */
    public static final String COLUMNNAME_Updated = "Updated";

	/** Get Updated.
	  * Date this record was updated
	  */
	public Timestamp getUpdated();

    /** Column name UpdatedBy */
    public static final String COLUMNNAME_UpdatedBy = "UpdatedBy";

	/** Get Updated By.
	  * User who updated this records
	  */
	public int getUpdatedBy();

    /** Column name UUID */
    public static final String COLUMNNAME_UUID = "UUID";

	/** Set Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID);

	/** Get Immutable Universally Unique Identifier.
	  * Immutable Universally Unique Identifier
	  */
	public String getUUID();
}
