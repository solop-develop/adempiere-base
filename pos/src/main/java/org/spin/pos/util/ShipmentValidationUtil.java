/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 or later of the                                  *
 * GNU General Public License as published                                    *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2019 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.pos.util;

import java.util.Arrays;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInOut;
import org.compiere.model.MOrder;
import org.compiere.model.MPOS;
import org.compiere.model.MProduct;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/**
 * Validate Shipment from order
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class ShipmentValidationUtil {
	
	/**	Confirm Only Complete Shipment	*/
	public static final String COLUMNNAME_IsConfirmCompleteShipment = "IsConfirmCompleteShipment";
	/**	Error message for shipment with missing products	*/
	public static final String MESSAGE_MissingProductsOnShipment = "MissingProductsOnShipment";
	/**	Error message for shipment with missing product	*/
	public static final String MESSAGE_MissingProductsOnShipmentLine = "MissingProductsOnShipmentLine";
	
	/**
	 * Validate shipment from Point of Sales
	 * @param shipment
	 * @return void
	 */
	public static void validateShipmentFromPOS(MInOut shipment) {
		//	Only for POS
		if(shipment.getC_POS_ID() <= 0) {
			return;
		}
		MPOS pos = MPOS.get(shipment.getCtx(), shipment.getC_POS_ID());
		if(!pos.get_ValueAsBoolean(COLUMNNAME_IsConfirmCompleteShipment)) {
			return;
		}
		//	Validate
		StringBuffer errors = new StringBuffer();
		MOrder order = (MOrder) shipment.getC_Order();
		Arrays.asList(order.getLines())
		.stream()
		.filter(orderLine -> orderLine.getM_Product_ID() > 0)
		.forEach(orderLine -> {
			MProduct product = MProduct.get(orderLine.getCtx(), orderLine.getM_Product_ID());
			if(orderLine.getQtyOrdered().subtract(orderLine.getQtyDelivered()).compareTo(Env.ZERO) > 0) {
				errors.append(Env.NL);
				//	
				errors.append(Msg.getMsg(order.getCtx(), MESSAGE_MissingProductsOnShipmentLine, new Object[]{
						orderLine.getLine(), 
						product.getValue() + "-" + product.getName(), 
						orderLine.getQtyOrdered().subtract(orderLine.getQtyDelivered())
						}
				));
			}
		});
		if(errors.length() > 0) {
			throw new AdempiereException(Msg.parseTranslation(shipment.getCtx(), "@" + MESSAGE_MissingProductsOnShipment + "@") + errors.toString());
		}
	}
}
