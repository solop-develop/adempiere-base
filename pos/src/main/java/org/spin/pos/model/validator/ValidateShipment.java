/************************************************************************************
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, C.A.                     *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                     *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.	See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program.	If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.pos.model.validator;

import org.adempiere.core.domains.models.I_C_Payment;
import org.adempiere.core.domains.models.I_M_InOut;
import org.compiere.model.MClient;
import org.compiere.model.MInOut;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.spin.pos.util.PaymentApprovalUtil;
import org.spin.pos.util.ShipmentValidationUtil;

/**
 * Validate Shipment based on POS flag
 * @author Yamel Senih ysenih@erpya.com
 *
 */
public class ValidateShipment implements ModelValidator {

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ValidateShipment.class);
	/** Client */
	private int clientId = -1;
	
	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			clientId = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing global validator: " + this.toString());
		}
		//	Add Persistence for IsDefault values
		engine.addDocValidate(I_M_InOut.Table_Name, this);
	}
	
	@Override
	public int getAD_Client_ID() {
		return clientId;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	}

	@Override
	public String modelChange(PO entity, int type) throws Exception {
		return null;
	}

	@Override
	public String docValidate(PO entity, int timing) {
		if(timing == TIMING_AFTER_COMPLETE) {
			if(entity.get_TableName().equals(I_M_InOut.Table_Name)) {
				ShipmentValidationUtil.validateShipmentFromPOS((MInOut) entity);
			} else if(entity.get_TableName().equals(I_C_Payment.Table_Name)) {
				MPayment payment = (MPayment) entity;
				PaymentApprovalUtil.validateApprovalForPayment(payment);
			}
		}
		return null;
	}
}
