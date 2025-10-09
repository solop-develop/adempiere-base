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

package org.solop.freight.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.eevolution.distribution.model.MDDFreight;
import org.eevolution.distribution.model.MDDOrder;
import org.eevolution.distribution.model.MDDOrderLine;
import org.eevolution.wms.model.MWMInOutBoundLine;
import org.solop.freight.util.FreightOrder;
import org.solop.freight.util.FreightOrderLine;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/** Generated Process for (Create Freight Order From Outbound Order Lines)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public class CreateFreightOrderFromInOutBoundLine extends CreateFreightOrderFromInOutBoundLineAbstract {
	@Override
	protected void prepare() {
		super.prepare();
		if(getRecord_ID() > 0) {
			MDDFreight freightOrder = new MDDFreight(getCtx(), getRecord_ID(), get_TrxName());
			if(freightOrder.isProcessed()
					|| freightOrder.isProcessing()) {
				throw new AdempiereException("@DD_Freight_ID@ @Processed@");
			}
		}
	}
	
	@Override
	protected String doIt() throws Exception {
		FreightOrder freightBuilder = FreightOrder.newInstance(getCtx(), getRecord_ID(), get_TrxName());
		freightBuilder.save();
		//	Add Lines
		AtomicInteger lineNo = new AtomicInteger();
		AtomicInteger lines = new AtomicInteger();
		AtomicReference<BigDecimal> freightAmount = new AtomicReference<BigDecimal>(Env.ZERO);
		MDDFreight freightOrder = new MDDFreight(getCtx(), getRecord_ID(), get_TrxName());
		freightAmount.set(freightOrder.getFreightAmt());
		getSelectionKeys().forEach(key -> {
			MWMInOutBoundLine outboundOrderLine = new MWMInOutBoundLine(getCtx(), key, get_TrxName());
			BigDecimal weight = Env.ZERO;
			BigDecimal volume = Env.ZERO;
			int productId = 0;
			int businessPartnerId = 0;
			int locationFromId = 0;
			int locationToId = 0;
			int freightCategoryId = 0;
			int orderId = 0;
			int distributionOrderId = 0;
			AtomicReference<BigDecimal> orderFreightAmount = new AtomicReference<BigDecimal>(Env.ZERO);
			if(outboundOrderLine.getC_OrderLine_ID() > 0) {
				MOrderLine orderLine = (MOrderLine) outboundOrderLine.getC_OrderLine();
				MOrder order = orderLine.getParent();
				orderId = order.get_ID();
				freightCategoryId = order.getM_FreightCategory_ID();
				productId = orderLine.getM_Product_ID();
				businessPartnerId = orderLine.getC_BPartner_ID();
				orderFreightAmount.set(Optional.ofNullable(order.getFreightAmt()).orElse(Env.ZERO));
				int warehouseId = orderLine.getM_Warehouse_ID();
				if(warehouseId <= 0) {
					warehouseId = orderLine.getParent().getM_Warehouse_ID();
				}
				locationFromId = MWarehouse.get(Env.getCtx(), warehouseId).getC_Location_ID();
				if(orderLine.getC_BPartner_Location_ID() > 0) {
					MBPartnerLocation businessPartnerLocation = (MBPartnerLocation) orderLine.getC_BPartner_Location();
					locationToId = businessPartnerLocation.getC_Location_ID();
				} else {
					MBPartnerLocation businessPartnerLocation = (MBPartnerLocation) orderLine.getParent().getC_BPartner_Location();
					locationToId = businessPartnerLocation.getC_Location_ID();
				}
				
			} else if(outboundOrderLine.getDD_OrderLine_ID() > 0) {
				MDDOrderLine orderLine = (MDDOrderLine) outboundOrderLine.getDD_OrderLine();
				MDDOrder order = orderLine.getParent();
				distributionOrderId = order.get_ID();
				freightCategoryId = order.getM_FreightCategory_ID();
				productId = orderLine.getM_Product_ID();
				MLocator locatorFrom = MLocator.get(getCtx(), orderLine.getM_Locator_ID());
				MLocator locatorTo = MLocator.get(getCtx(), orderLine.getM_LocatorTo_ID());
				locationFromId = MWarehouse.get(getCtx(), locatorFrom.getM_Warehouse_ID()).getC_Location_ID();
				locationToId = MWarehouse.get(getCtx(), locatorTo.getM_Warehouse_ID()).getC_Location_ID();
				businessPartnerId = orderLine.getParent().getC_BPartner_ID();
				orderFreightAmount.set(Optional.ofNullable(order.getFreightAmt()).orElse(Env.ZERO));
			}
			if(productId > 0) {
				MProduct product = MProduct.get(getCtx(), productId);
				weight = Optional.ofNullable(product.getWeight()).orElse(Env.ZERO).multiply(Optional.ofNullable(outboundOrderLine.getPickedQty()).orElse(Env.ZERO));
				volume = Optional.ofNullable(product.getVolume()).orElse(Env.ZERO).multiply(Optional.ofNullable(outboundOrderLine.getPickedQty()).orElse(Env.ZERO));
			}
			FreightOrderLine freightLineBuilder = FreightOrderLine.newInstance(getCtx(), freightBuilder.getFreightOrderId(), get_TrxName())
					.withLineNo(lineNo.addAndGet(10))
					.withWeight(weight)
					.withVolume(volume)
					.withFreightAmount(outboundOrderLine.getFreightAmt())
					.withFreightRate(outboundOrderLine.getFreightRate())
					.withFreightId(outboundOrderLine.getM_Freight_ID())
					.withInOutboundOrderReferenceId(outboundOrderLine.getWM_InOutBound_ID())
					.withInOutboundOrderLineReferenceId(outboundOrderLine.getWM_InOutBoundLine_ID())
					.withBusinessPartnerId(businessPartnerId)
					.withLocationFromId(locationFromId)
					.withLocationToId(locationToId)
					.withFreightCategoryId(freightOrder.getM_FreightCategory_ID())
					.withFreightCategoryId(freightCategoryId)
					.withOrderLineReferenceId(outboundOrderLine.getC_OrderLine_ID())
					.withDistributionOrderLineReferenceId(outboundOrderLine.getDD_OrderLine_ID())
					;
			if (orderId > 0)
				freightLineBuilder.withOrderReferenceId(orderId);
			
			if (distributionOrderId > 0)
				freightLineBuilder.withDistributionOrderReferenceId(distributionOrderId);
			
			freightLineBuilder.save();
			freightAmount.getAndUpdate(amount -> amount.add(orderFreightAmount.get()));
			lines.addAndGet(1);
		});
		freightBuilder.withFreightAmount(freightAmount.get()).save();
		return "@DD_Freight_ID@ @Created@: " + freightBuilder.getFreightOrder().getDocumentInfo() + " @Lines@: " + lines.get();
	}
}