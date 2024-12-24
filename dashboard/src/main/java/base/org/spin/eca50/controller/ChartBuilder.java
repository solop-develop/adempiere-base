/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * This program is free software; you can redistribute it and/or modify it           *
 * under the terms version 2 or later of the GNU General Public License as published *
 * by the Free Software Foundation. This program is distributed in the hope          *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied        *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                  *
 * See the GNU General Public License for more details.                              *
 * You should have received a copy of the GNU General Public License along           *
 * with this program; if not, write to the Free Software Foundation, Inc.,           *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                            *
 * For the text or an alternative of this public license, you may reach us           *
 * Copyright (C) 2012-2023 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpya.com                                         *
 *************************************************************************************/
package org.spin.eca50.controller;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MChart;
import org.compiere.model.MChartDatasource;
import org.compiere.model.MRole;
import org.compiere.model.Query;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;
import org.spin.eca50.data.ChartDataValue;
import org.spin.eca50.data.ChartSeriesValue;
import org.spin.eca50.data.ChartValue;
import org.spin.eca50.util.ChartQueryDefinition;

/**
 * A class as controller for Chart Service
 * @author Yamel Senih at www.erpya.com
 *
 */
public class ChartBuilder {

	/**
	 * Get Data Source Query
	 * @param chartDatasourceId
	 * @param isTimeSeries
	 * @param timeUnit
	 * @param timeScope
	 * @param customParameters
	 * @return
	 */
	private static ChartQueryDefinition getDataSourceQuery(int chartDatasourceId, boolean isTimeSeries, String timeUnit, int timeScope, Map<String, Object> customParameters) {
		if(chartDatasourceId <= 0) {
			throw new AdempiereException("@FillMandatory@ @AD_ChartDatasource_ID@");
		}
		MChartDatasource datasource = new MChartDatasource(Env.getCtx(), chartDatasourceId, null);
		if (datasource == null || datasource.getAD_ChartDatasource_ID() <= 0) {
			throw new AdempiereException("@AD_ChartDatasource_ID@ @NotFound@");
		}

		String value = datasource.getValueColumn();
		String dateColumn = datasource.getDateColumn();
		String seriesColumn = datasource.getSeriesColumn();
		String name = datasource.getName();
		String where = datasource.getWhereClause();
		String fromClause = datasource.getFromClause();
		String category;
		String unit = "D";
		if (!isTimeSeries) {
			category = datasource.getCategoryColumn();
		} else {
			if (timeUnit.equals(MChart.TIMEUNIT_Week)) {
				unit = "W";
			} else if (timeUnit.equals(MChart.TIMEUNIT_Month)) {
				unit = "MM";
			} else if (timeUnit.equals(MChart.TIMEUNIT_Quarter)) {
				unit = "Q";
			} else if (timeUnit.equals(MChart.TIMEUNIT_Year)) {
				unit = "Y";
			}
			category = " TRUNC(" + dateColumn + ", '" + unit + "') ";
		}

		String series = DB.TO_STRING(name);
		boolean hasSeries = false;
		if (seriesColumn != null) {
			series = seriesColumn;
			hasSeries = true;
		}

		List<Object> parameters = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT " + value + ", " + category + ", " + series);
		sql.append(" FROM ").append(fromClause);

		// where clause
		StringBuffer whereClause = new StringBuffer(" WHERE 1=1 ");

		Timestamp currentDate = Env.getContextAsDate(Env.getCtx(), "#Date");
		Timestamp startDate = null;
		Timestamp endDate = null;

		int scope = timeScope;
		int offset = datasource.getTimeOffset();
		
		if (isTimeSeries && scope != 0) {
			offset += -scope;
			startDate = TimeUtil.getDay(TimeUtil.addDuration(currentDate, timeUnit, offset));
			endDate = TimeUtil.getDay(TimeUtil.addDuration(currentDate, timeUnit, scope));
		}

		if (startDate != null && endDate != null) {
			whereClause.append(" AND ").append(category).append(" >= ? ")
				.append("AND ").append(category).append(" <= ? ")
			;
			parameters.add(startDate);
			parameters.add(endDate);
		}
		//	Add custom parameters
		if (customParameters != null && customParameters.size() > 0) {
			customParameters.entrySet().forEach(parameter -> {
				whereClause.append(" AND ")
					.append(parameter.getKey())
				;

				// add value(s)
				Object currentValue = parameter.getValue();
				if (currentValue instanceof Collection) {
					// is multiple values to IN or BETWEEN operators
					addCollectionParameters(currentValue, parameters);
				} else {
					parameters.add(currentValue);
				}
			});
		}

		//	Add where clause
		sql.append(whereClause);

		MRole role = MRole.getDefault(Env.getCtx(), false);
		String sqlWithAccess = role.addAccessSQL(
			sql.toString(),
			null,
			MRole.SQL_FULLYQUALIFIED,
			MRole.SQL_RO
		);

		if (!Util.isEmpty(where, true)) {
			String parsedWhere = Env.parseContext(Env.getCtx(), 0, where, true);
			if (parsedWhere.trim().startsWith("WHERE")) {
				int startIndex = "WHERE".length();
				parsedWhere = parsedWhere.trim().substring(startIndex);
			}
			sqlWithAccess += " AND (" + parsedWhere.trim() + ")";
		}

		sql = new StringBuffer(sqlWithAccess);

		if (hasSeries) {
			sql.append(" GROUP BY " + series + ", " + category + " ORDER BY " + series + ", " + category);
		} else {
			sql.append(" GROUP BY " + category + " ORDER BY " + category);
		}
		return new ChartQueryDefinition(name, sql.toString(), parameters);
	}

	/**
	 * Get Chart data
	 * This method allows run all data sources and get data
	 * @param chartId
	 * @param customParameters
	 * @return
	 */
	public static ChartValue getChartData(int chartId, Map<String, Object> customParameters) {
		if(chartId <= 0) {
			throw new AdempiereException("@AD_Chart_ID@ @NotFound@");
		}
		MChart chart = new MChart(Env.getCtx(), chartId, null);
		ChartValue metrics = new ChartValue(chart.getName());
		List<ChartQueryDefinition> dataSources = new ArrayList<ChartQueryDefinition>();
		new Query(Env.getCtx(), MChartDatasource.Table_Name, MChart.COLUMNNAME_AD_Chart_ID + " = ?", null)
			.setParameters(chart.getAD_Chart_ID())
			.setOnlyActiveRecords(true)
			.getIDsAsList().forEach(chartDataSourceId -> {
				ChartQueryDefinition queryDefinition = getDataSourceQuery(
					chartDataSourceId,
					chart.isTimeSeries(), chart.getTimeUnit(), chart.getTimeScope(),
					customParameters
				);
				dataSources.add(queryDefinition);
			});
		//	Run queries
		if (dataSources != null && dataSources.size() > 0) {
			Map<String, List<Map<String, BigDecimal>>> seriesMap = new HashMap<String, List<Map<String, BigDecimal>>>();
			dataSources.forEach(dataSource -> {
				PreparedStatement pstmt = null;
				ResultSet rs = null;
				try {
					pstmt = DB.prepareStatement(dataSource.getQuery(), null);
					DB.setParameters(pstmt, dataSource.getParameters());

					rs = pstmt.executeQuery();
					while(rs.next()) {
						String key = rs.getString(2);
						String seriesName = rs.getString(3);
						if(chart.isTimeSeries()) {
							//	TODO: Define it with dates
						}
						BigDecimal amount = rs.getBigDecimal(1);

						// series values
						List<Map<String, BigDecimal>> valuesList = new ArrayList<Map<String, BigDecimal>>();
						if (seriesMap.containsKey(seriesName)) {
							valuesList = seriesMap.get(seriesName);
						}
						HashMap<String, BigDecimal> valuesMap = new HashMap<String, BigDecimal>();
						valuesMap.put(key, amount);
						valuesList.add(valuesMap);

						seriesMap.put(seriesName, valuesList);
					}
				} catch (Exception e) {
					throw new AdempiereException(e);
				} finally {
					DB.close(rs, pstmt);
					rs = null;
					pstmt = null;
				}
			});

			seriesMap.entrySet().stream().forEach(seriesEntry -> {
				ChartSeriesValue chartSerie = new ChartSeriesValue(seriesEntry.getKey());
				// each values list
				seriesEntry.getValue().stream().forEach(serie -> {
					serie.entrySet().stream().forEach(value -> {
						String name = value.getKey();
						BigDecimal amount = value.getValue();
						ChartDataValue data = new ChartDataValue(name, amount);
						// fill series with value data
						chartSerie.addData(data);
					});
				});
				metrics.addSerie(chartSerie);
			});
		}
		//	
		return metrics;
	}


	/**
	 * When filter value is a collection (List, ArrayList)
	 * @param objectColelction
	 * @param parameters
	 */
	@SuppressWarnings("unchecked")
	public static void addCollectionParameters(Object objectColelction, List<Object> parameters) {
		if (objectColelction instanceof Collection) {
			try {
				Collection<Object> collection = (Collection<Object>) objectColelction;
				// for-each loop
				for (Object rangeValue : collection) {
					parameters.add(rangeValue);
				}
			}
			catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}


	public static void main(String[] args) {
		org.compiere.Adempiere.startup(true);
		Env.setContext(Env.getCtx(), "#Date", new Timestamp(System.currentTimeMillis()));
		Env.setContext(Env.getCtx(), "#AD_Client_ID", 1000000);
		Env.setContext(Env.getCtx(), "#AD_Role_ID", 1000000);
		Map<String, Object> customParameters = new HashMap<String, Object>();
		customParameters.put("i.DateInvoiced <= ?", new Timestamp(System.currentTimeMillis()));
		ChartValue data = getChartData(1000000, customParameters);
		System.out.println(data);
	}

}
