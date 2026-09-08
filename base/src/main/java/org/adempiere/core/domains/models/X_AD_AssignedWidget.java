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

/** Generated Model for AD_AssignedWidget
 *  @author Adempiere (generated)
 *  @version Release 3.9.4 - $Id$ */
public class X_AD_AssignedWidget extends PO implements I_AD_AssignedWidget, I_Persistent
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260908L;

    /** Standard Constructor */
    public X_AD_AssignedWidget (Properties ctx, int AD_AssignedWidget_ID, String trxName)
    {
      super (ctx, AD_AssignedWidget_ID, trxName);
      /** if (AD_AssignedWidget_ID == 0)
        {
			setAD_AssignedWidget_ID (0);
        } */
    }

    /** Load Constructor */
    public X_AD_AssignedWidget (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_AD_AssignedWidget[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Assigned Widget.
		@param AD_AssignedWidget_ID Assigned Widget	  */
	public void setAD_AssignedWidget_ID (int AD_AssignedWidget_ID)
	{
		if (AD_AssignedWidget_ID < 1)
			set_ValueNoCheck (COLUMNNAME_AD_AssignedWidget_ID, null);
		else
			set_ValueNoCheck (COLUMNNAME_AD_AssignedWidget_ID, Integer.valueOf(AD_AssignedWidget_ID));
	}

	/** Get Assigned Widget.
		@return Assigned Widget	  */
	public int getAD_AssignedWidget_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_AssignedWidget_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_DashboardInstance getAD_DashboardInstance() throws RuntimeException
    {
		return (I_AD_DashboardInstance)MTable.get(getCtx(), I_AD_DashboardInstance.Table_Name)
			.getPO(getAD_DashboardInstance_ID(), get_TrxName());	}

	/** Set Dashboard Instance.
		@param AD_DashboardInstance_ID Dashboard Instance	  */
	public void setAD_DashboardInstance_ID (int AD_DashboardInstance_ID)
	{
		if (AD_DashboardInstance_ID < 1)
			set_Value (COLUMNNAME_AD_DashboardInstance_ID, null);
		else
			set_Value (COLUMNNAME_AD_DashboardInstance_ID, Integer.valueOf(AD_DashboardInstance_ID));
	}

	/** Get Dashboard Instance.
		@return Dashboard Instance	  */
	public int getAD_DashboardInstance_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_AD_DashboardInstance_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_AD_TemplateZone getAD_TemplateZone() throws RuntimeException
    {
		return (I_AD_TemplateZone)MTable.get(getCtx(), I_AD_TemplateZone.Table_Name)
			.getPO(getAD_TemplateZone_ID(), get_TrxName());	}

	/** Set Template Zone.
		@param AD_TemplateZone_ID Template Zone	  */
	public void setAD_TemplateZone_ID (int AD_TemplateZone_ID)
	{
		if (AD_TemplateZone_ID < 1)
			set_Value (COLUMNNAME_AD_TemplateZone_ID, null);
		else
			set_Value (COLUMNNAME_AD_TemplateZone_ID, Integer.valueOf(AD_TemplateZone_ID));
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

	/** Set Configuration JSON.
		@param ConfigJSON
		Widget configuration stored as JSON
	  */
	public void setConfigJSON (String ConfigJSON)
	{
		set_Value (COLUMNNAME_ConfigJSON, ConfigJSON);
	}

	/** Get Configuration JSON.
		@return Widget configuration stored as JSON
	  */
	public String getConfigJSON ()
	{
		return (String)get_Value(COLUMNNAME_ConfigJSON);
	}

	/** Set Display Order.
		@param DisplayOrder
		Order of the widget within its zone
	  */
	public void setDisplayOrder (int DisplayOrder)
	{
		set_Value (COLUMNNAME_DisplayOrder, Integer.valueOf(DisplayOrder));
	}

	/** Get Display Order.
		@return Order of the widget within its zone
	  */
	public int getDisplayOrder ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DisplayOrder);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Report.
		@param Report_ID
		Identifier of the chart or report to render
	  */
	public void setReport_ID (int Report_ID)
	{
		set_Value (COLUMNNAME_Report_ID, Integer.valueOf(Report_ID));
	}

	/** Get Report.
		@return Identifier of the chart or report to render
	  */
	public int getReport_ID ()
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_Report_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** ReportType AD_Reference_ID=53377 */
	public static final int REPORTTYPE_AD_Reference_ID=53377;
	/** Area Chart = AC */
	public static final String REPORTTYPE_AreaChart = "AC";
	/** Bar Chart = BC */
	public static final String REPORTTYPE_BarChart = "BC";
	/** Stacked Area Chart = AS */
	public static final String REPORTTYPE_StackedAreaChart = "AS";
	/** 3D Bar Chart = B3 */
	public static final String REPORTTYPE_3DBarChart = "B3";
	/** Stacked Bar Chart = BS */
	public static final String REPORTTYPE_StackedBarChart = "BS";
	/** 3D Stacked Bar Chart = B4 */
	public static final String REPORTTYPE_3DStackedBarChart = "B4";
	/** Line Chart = LC */
	public static final String REPORTTYPE_LineChart = "LC";
	/** 3D Line Chart = L3 */
	public static final String REPORTTYPE_3DLineChart = "L3";
	/** Waterfall Chart = WC */
	public static final String REPORTTYPE_WaterfallChart = "WC";
	/** Pie Chart = PC */
	public static final String REPORTTYPE_PieChart = "PC";
	/** 3D Pie Chart = P3 */
	public static final String REPORTTYPE_3DPieChart = "P3";
	/** Ring Chart = RC */
	public static final String REPORTTYPE_RingChart = "RC";
	/** Portlet Chart = PT */
	public static final String REPORTTYPE_PortletChart = "PT";
	/** Table Chart = TB */
	public static final String REPORTTYPE_TableChart = "TB";
	/** Gauge Chart = GU */
	public static final String REPORTTYPE_GaugeChart = "GU";
	/** Portlet Custom Chart = P5 */
	public static final String REPORTTYPE_PortletCustomChart = "P5";
	/** Set Report Type.
		@param ReportType Report Type	  */
	public void setReportType (String ReportType)
	{

		set_Value (COLUMNNAME_ReportType, ReportType);
	}

	/** Get Report Type.
		@return Report Type	  */
	public String getReportType ()
	{
		return (String)get_Value(COLUMNNAME_ReportType);
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
