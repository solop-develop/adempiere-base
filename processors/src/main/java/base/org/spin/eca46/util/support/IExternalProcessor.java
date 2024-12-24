/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                      *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                      *
 * This program is free software: you can redistribute it and/or modify              *
 * it under the terms of the GNU General Public License as published by              *
 * the Free Software Foundation, either version 3 of the License, or                 *
 * (at your option) any later version.                                               *
 * This program is distributed in the hope that it will be useful,                   *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                    *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the                     *
 * GNU General Public License for more details.                                      *
 * You should have received a copy of the GNU General Public License                 *
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.eca46.util.support;
import org.spin.util.support.IAppSupport;

/**
 * 	Wrapper for Alert Processor
 * 	@author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public interface IExternalProcessor extends IAppSupport {
	
	/**
	 * Export a specific processor
	 * @param processor
	 */
	public String exportProcessor(IProcessorEntity processor);
	
	/**
	 * Set token for connect with ADempiere
	 * @param token
	 */
	public void setTokenAccess(String token);
	
	/**
	 * Set host for connect with ADempiere
	 * @param host
	 */
	public void setHost(String host);
}
