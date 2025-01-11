/************************************************************************************
 * Copyright (C) 2018-present E.R.P. Consultores y Asociados, C.A.                  *
 * Contributor(s): Edwin Betancourt, EdwinBetanc0urt@outlook.com                    *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.service.grpc.util.db;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.compiere.util.DB;

/**
 * Class for handle SQL Count records
 * @author Edwin Betancourt, EdwinBetanc0urt@outlook.com, https://github.com/EdwinBetanc0urt
 */
public class CountUtil {
	
	/**
	 * Count records
	 * @param sql
	 * @param tableName
	 * @param parameters
	 * @return
	 */
	public static int countRecords(String sql, String tableName, List<Object> parameters) {
		return countRecords(sql, tableName, parameters, null);
	}

	/**
	 * Count records using transaction name
	 * @param sql
	 * @param tableName
	 * @param parameters
	 * @param transactionName
	 * @return
	 */
	public static int countRecords(String sql, String tableName, List<Object> parameters, String transactionName) {
		return countRecords(sql, tableName, null, parameters, transactionName);
	}
	
	/**
	 * Count records
	 * @param sql
	 * @param tableName
	 * @param tableNameAlias
	 * @param parameters
	 * @return
	 */
	public static int countRecords(String sql, String tableName, String tableNameAlias, List<Object> parameters) {
		return countRecords(sql, tableName, tableNameAlias, parameters, null);
	}

	/**
	 * Count records using transaction name
	 * @param sql
	 * @param tableName
	 * @param tableNameAlias
	 * @param parameters
	 * @param transactionName
	 * @return
	 */
	public static int countRecords(String sql, String tableName, String tableNameAlias, List<Object> parameters, String transactionName) {
		// tableName tableName, tableName AS tableName
		String tableWithAliases = FromUtil.getPatternTableName(tableName, tableNameAlias);

		String regex = "\\s+(FROM)\\s+(" + tableWithAliases + ")"
			+ "\\s+(\\bWHERE\\b|\\bORDER\\s+BY\\b|"
			+ "((LEFT|INNER|RIGHT|FULL|SELF|CROSS)\\s+(OUTER\\s+){0,1}){0,1}JOIN)"
		;

		Pattern pattern = Pattern.compile(
			regex,
			Pattern.CASE_INSENSITIVE | Pattern.DOTALL
		);
		Matcher matcherFrom = pattern
			.matcher(sql);
		List<MatchResult> fromWhereParts = matcherFrom.results()
			.collect(
				Collectors.toList()
			)
		;
		int positionFrom = -1;
		if (fromWhereParts != null && fromWhereParts.size() > 0) {
			// MatchResult lastFrom = fromWhereParts.get(fromWhereParts.size() - 1);
			MatchResult lastFrom = fromWhereParts.get(0);
			positionFrom = lastFrom.start();
		} else {
			// without JOIN/WHERE/ORDER BY
			String regexSimply = "\\s+(FROM)\\s+(" + tableWithAliases + ")";
			Pattern patternSimply = Pattern.compile(
				regexSimply,
				Pattern.CASE_INSENSITIVE | Pattern.DOTALL
			);
			Matcher matcherFromSimply = patternSimply
				.matcher(sql);
			List<MatchResult> fromWherePartsSimply = matcherFromSimply.results()
				.collect(
					Collectors.toList()
				)
			;
			if (fromWherePartsSimply != null && fromWherePartsSimply.size() > 0) {
				// MatchResult lastFrom = fromWherePartsSimply.get(fromWherePartsSimply.size() - 1);
				MatchResult lastFrom = fromWherePartsSimply.get(0);
				positionFrom = lastFrom.start();
			} else {
				// whitout `FROM` clause
				return 0;
			}
		}

		String queryCount = "SELECT COUNT(*) " + sql.substring(positionFrom, sql.length());

		// remove order by clause
		queryCount = OrderByUtil.removeOrderBy(queryCount);

		if (parameters == null) {
			parameters = new ArrayList<Object>();
		}

		return DB.getSQLValueEx(transactionName, queryCount, parameters);
	}

}
