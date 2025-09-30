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

import org.adempiere.core.domains.models.I_DD_Freight;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.eevolution.distribution.model.MDDFreight;
import org.eevolution.distribution.model.MDDOrderLine;
import org.eevolution.wms.model.MWMInOutBound;
import org.solop.freight.util.FreightOrder;
import org.solop.freight.util.FreightOrderLine;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/** Generated Process for (Create Freight Order From Outbound Order)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public class CreateFreightOrderFromInOutBound extends CreateFreightOrderFromInOutBoundAbstract {
	
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
		if(getFreightAmt() == null) {
			setFreightAmt(Env.ZERO);
		}
	}
	
	@Override
	protected String doIt() throws Exception {
		FreightOrder freightBuilder = FreightOrder.newInstance(getCtx(), getRecord_ID(), get_TrxName())
			.withDriverId(getDriverId())
			.withVehicleId(getVehicleId())
			.withDocumentDate(getDateDoc())
			.withOrderedDate(getDateOrdered())
			.withShipperId(getShipperId())
			.withFreightCategoryId(getFreightCategoryId())
			.withDocumentTypeId(getDocTypeId())
			.withDocumentAction(getDocAction())
			.withFreightAmount(getFreightAmt());
		if(isOverwriteFreightCostRule()) {
			freightBuilder.withFreightCostRule(getFreightCostRule());
		}
		freightBuilder.save();
		//	Add Lines
		AtomicInteger lineNo = new AtomicInteger();
		AtomicInteger lines = new AtomicInteger();
		AtomicReference<BigDecimal> freightAmount = new AtomicReference<BigDecimal>(Env.ZERO);
		getSelectionKeys().forEach(key -> {
			MWMInOutBound outboundOrder = new MWMInOutBound(getCtx(), key, get_TrxName());
			outboundOrder.getLines(true, null).forEach(outboundOrderLine -> {
				BigDecimal weight = Env.ZERO;
				BigDecimal volume = Env.ZERO;
				int productId = 0;
				int businessPartnerId = 0;
				int locationFromId = 0;
				int locationToId = 0;
				int orderId = 0;
				int distributionOrderId = 0;
				if(outboundOrderLine.getC_OrderLine_ID() > 0) {
					MOrderLine orderLine = (MOrderLine) outboundOrderLine.getC_OrderLine();
					orderId = orderLine.getC_Order_ID();
					productId = orderLine.getM_Product_ID();
					businessPartnerId = orderLine.getC_BPartner_ID();
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
					distributionOrderId = orderLine.getDD_Order_ID();
					productId = orderLine.getM_Product_ID();
					MLocator locatorFrom = MLocator.get(getCtx(), orderLine.getM_Locator_ID());
					MLocator locatorTo = MLocator.get(getCtx(), orderLine.getM_LocatorTo_ID());
					locationFromId = MWarehouse.get(getCtx(), locatorFrom.getM_Warehouse_ID()).getC_Location_ID();
					locationToId = MWarehouse.get(getCtx(), locatorTo.getM_Warehouse_ID()).getC_Location_ID();
					businessPartnerId = orderLine.getParent().getC_BPartner_ID();
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
						.withInOutboundOrderReferenceId(outboundOrder.getWM_InOutBound_ID())
						.withInOutboundOrderLineReferenceId(outboundOrderLine.getWM_InOutBoundLine_ID())
						.withBusinessPartnerId(businessPartnerId)
						.withLocationFromId(locationFromId)
						.withLocationToId(locationToId)
						.withFreightCategoryId(getFreightCategoryId())
						.withOrderLineReferenceId(outboundOrderLine.getC_OrderLine_ID())
						.withDistributionOrderLineReferenceId(outboundOrderLine.getDD_OrderLine_ID())
						;
						if (orderId > 0)
							freightLineBuilder.withOrderReferenceId(orderId);
						
						if (distributionOrderId > 0)
							freightLineBuilder.withDistributionOrderReferenceId(distributionOrderId);
						
						if(isOverwriteFreightCostRule()) {
							freightLineBuilder.withFreightCatehoryId(getFreightCategoryId());
						} else {
							freightLineBuilder.withFreightCatehoryId(outboundOrder.getM_FreightCategory_ID());
						}
						freightLineBuilder.save();
						freightAmount.getAndUpdate(amount -> amount.add(outboundOrder.getFreightAmt()));
			});
			lines.addAndGet(1);
			addLog(freightBuilder.getFreightOrderId(), getDateDoc(), null, "@Created@");
		});
		if(!isOverwriteFreightCostRule()) {
			freightBuilder.withFreightAmount(freightAmount.get()).save();
		}
		//	Process It
		if(getRecord_ID() <= 0) {
			freightBuilder.process(getDocAction());
			openResult(I_DD_Freight.Table_Name);
		}
		return "@DD_Freight_ID@ @Created@: " + freightBuilder.getFreightOrder().getDocumentInfo() + " @Lines@: " + lines.get();
	}
}