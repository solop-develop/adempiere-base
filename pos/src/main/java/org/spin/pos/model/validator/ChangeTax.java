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

import java.util.Arrays;

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.core.domains.models.I_C_OrderLine;
import org.compiere.model.MClient;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.util.CLogger;
import org.spin.pos.util.PointOfSalesImprovementsChanges;

/**
 * Change tax for Sales Order
 * @author Yamel Senih ysenih@erpya.com
 *
 */
public class ChangeTax implements ModelValidator {

	/** Logger */
	private static CLogger log = CLogger.getCLogger(ChangeTax.class);
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
		engine.addModelChange(I_C_Order.Table_Name, this);
		engine.addModelChange(I_C_OrderLine.Table_Name, this);
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
		if(type == TYPE_BEFORE_NEW
				|| type == TYPE_BEFORE_CHANGE) {
			if(entity.get_TableName().equals(I_C_OrderLine.Table_Name)) {
				//	For Sales Orders
				if(entity.is_new()
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_M_Product_ID)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_C_Charge_ID)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_QtyEntered)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_PriceEntered)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_Discount)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_C_BPartner_ID)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_C_UOM_ID)
						|| entity.is_ValueChanged(I_C_OrderLine.COLUMNNAME_C_Tax_ID)) {
					MOrderLine orderLine = (MOrderLine) entity;
					MOrder salesOrder = orderLine.getParent();
					if(salesOrder.isSOTrx()
							&& !salesOrder.isReturnOrder()) {
						MDocType documentType = MDocType.get(entity.getCtx(), salesOrder.getC_DocTypeTarget_ID());
						boolean isTaxExempt = documentType.get_ValueAsBoolean(PointOfSalesImprovementsChanges.COLUMNNAME_IsTaxExempt);
						if(isTaxExempt) {
							PointOfSalesImprovementsChanges.changeTax(orderLine, isTaxExempt);
						}
					}
				}
			} else if(entity.get_TableName().equals(I_C_Order.Table_Name)) {
				if(entity.is_new()
						|| entity.is_ValueChanged(I_C_Order.COLUMNNAME_C_DocTypeTarget_ID)
						|| entity.is_ValueChanged(I_C_Order.COLUMNNAME_C_BPartner_ID)
						|| entity.is_ValueChanged(I_C_Order.COLUMNNAME_M_PriceList_ID)
						|| entity.is_ValueChanged(I_C_Order.COLUMNNAME_M_Warehouse_ID)) {
					MOrder order = (MOrder) entity;
					MDocType documentType = MDocType.get(entity.getCtx(), order.getC_DocTypeTarget_ID());
					boolean isTaxExempt = documentType.get_ValueAsBoolean(PointOfSalesImprovementsChanges.COLUMNNAME_IsTaxExempt);
					if(order.isSOTrx()
							&& !order.isReturnOrder()) {
						Arrays.asList(order.getLines())
						.forEach(orderLine -> {
							PointOfSalesImprovementsChanges.changeTax(orderLine, isTaxExempt);
							orderLine.saveEx();
						});
					}
				}
			}
		}
		return null;
	}

	@Override
	public String docValidate(PO entity, int timing) {
		return null;
	}
}
