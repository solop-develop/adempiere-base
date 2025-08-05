/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2015 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.eca62.setup;

import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClientInfo;
import org.compiere.util.Env;
import org.spin.model.MADAppRegistration;
import org.spin.model.MADAppSupport;
import org.spin.util.ISetupDefinition;

/**
 * Create App Registration to Connect to local minio
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class Deploy implements ISetupDefinition {

	private static final String APP_TYPE = "AS3";
	
	@Override
	public String doIt(Properties context, String transactionName) {
		//	create App Registration
		MADAppRegistration appRegistration = createConnection(context, transactionName);

		//	Set App Registration to Client Info
		configClient(context, appRegistration, transactionName);

		return "@AD_SetupDefinition_ID@ @Ok@";
	}

	private MADAppRegistration createConnection(Properties context, String transactionName) {
		MADAppRegistration aws3Connection = MADAppRegistration.getByApplicationType(context, APP_TYPE, transactionName);
		if(aws3Connection == null
				|| aws3Connection.getAD_AppRegistration_ID() <= 0) {
			MADAppSupport aws3Support = MADAppSupport.getByApplicationType(context, APP_TYPE, transactionName);
			if(aws3Support == null
					|| aws3Support.getAD_AppSupport_ID() <= 0) {
				throw new AdempiereException("@AD_AppSupport_ID@ @EMail@ @NotFound@");
			}
			aws3Connection = new MADAppRegistration(context, 0, transactionName);
			aws3Connection.setValue("AWS3");
			aws3Connection.setApplicationType(APP_TYPE);
			aws3Connection.setAD_AppSupport_ID(aws3Support.getAD_AppSupport_ID());
			aws3Connection.setName("Default AWS3 Minio File Storage");
			aws3Connection.setVersionNo("1.0");
			aws3Connection.setHost("http://0.0.0.0");
			aws3Connection.setPort(9000);
			aws3Connection.saveEx();
		}
		return aws3Connection;
	}

	private void configClient(Properties context, MADAppRegistration aws3Connection, String transactionName) {
		int clientId = Env.getAD_Client_ID(context);
		MClientInfo clientInfo = MClientInfo.get(context, clientId, transactionName);
		clientInfo.setFileHandler_ID(
			aws3Connection.getAD_AppRegistration_ID()
		);
		clientInfo.saveEx();
	}

}
