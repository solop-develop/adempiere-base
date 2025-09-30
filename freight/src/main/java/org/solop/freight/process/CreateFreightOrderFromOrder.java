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
import org.compiere.model.MOrder;
import org.compiere.model.MWarehouse;
import org.compiere.util.Env;
import org.eevolution.distribution.model.MDDFreight;
import org.solop.freight.util.FreightOrder;
import org.solop.freight.util.FreightOrderLine;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

/** Generated Process for (Create Freight Order From Order)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public class CreateFreightOrderFromOrder extends CreateFreightOrderFromOrderAbstract {

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
			MOrder order = new MOrder(getCtx(), key, get_TrxName());
			MWarehouse warehouse = MWarehouse.get(getCtx(), order.getM_Warehouse_ID());
			FreightOrderLine freightLineBuilder = FreightOrderLine.newInstance(getCtx(), freightBuilder.getFreightOrderId(), get_TrxName())
			.withLineNo(lineNo.addAndGet(10))
			.withWeight(order.getWeight())
			.withVolume(order.getVolume())
			.withFreightAmount(order.getFreightAmt())
			.withFreightRate(order.getFreightRate())
			.withFreightId(order.getM_Freight_ID())
			.withBusinessPartnerId(order.getC_BPartner_ID())
			.withFreightCategoryId(getFreightCategoryId())
			.withLocationFromId(warehouse.getC_Location_ID())
			.withLocationToId(order.getC_BPartner_Location().getC_Location_ID())
			.withOrderReferenceId(order.getC_Order_ID())
			;
			if(isOverwriteFreightCostRule()) {
				freightLineBuilder.withFreightCatehoryId(getFreightCategoryId());
			} else {
				freightLineBuilder.withFreightCatehoryId(order.getM_FreightCategory_ID());
			}
			freightLineBuilder.save();
			freightAmount.getAndUpdate(amount -> amount.add(order.getFreightAmt()));
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