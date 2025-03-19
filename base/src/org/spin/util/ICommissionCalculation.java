/************************************************************************************
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                     *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                     *
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
package org.spin.util;

import java.util.Properties;

/**
 * This interface represent a contract of commission type calculation, you can implement your own business logic
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public interface ICommissionCalculation {
	
	/**
	 * Process Commission by Sales Representative
	 * @param context
	 * @param businessPartnerId
	 * @param commissionId
	 * @param commissionRunId
	 * @param transactionName
	 */
	public void processCommission(Properties context, int businessPartnerId, int commissionId, int commissionRunId, String transactionName);
}
