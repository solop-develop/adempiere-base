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
package org.solop.freight.util;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClientInfo;
import org.compiere.model.MFreightCategory;
import org.eevolution.distribution.model.MDDFreightLine;

import java.math.BigDecimal;
import java.util.Properties;

/**
 * Freight Line Document Util using builder pattern
 * Please rename this class and package
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class FreightOrderLine {

	private MDDFreightLine freightOrderLine;
	
	private FreightOrderLine(Properties context, String transactionName) {
		freightOrderLine = new MDDFreightLine(context, 0, transactionName);
	}
	
	public static FreightOrderLine newInstance(Properties context, int freightOrderId, String transactionName) {
		return new FreightOrderLine(context, transactionName).withFreightOrderId(freightOrderId);
	}
	
	private FreightOrderLine withFreightOrderId(int freightOrderId) {
		if(freightOrderId > 0) {
			freightOrderLine.setDD_Freight_ID(freightOrderId);
		}
		return this;
	}
	
	public FreightOrderLine withLineNo(int lineNo) {
		freightOrderLine.setLine(lineNo);
		return this;
	}
	
	public FreightOrderLine withWeight(BigDecimal weight) {
		freightOrderLine.setWeight(weight);
		return this;
	}
	
	public FreightOrderLine withVolume(BigDecimal volume) {
		freightOrderLine.setVolume(volume);
		return this;
	}
	
	public FreightOrderLine withFreightAmount(BigDecimal freightAmount) {
		freightOrderLine.setFreightAmt(freightAmount);
		return this;
	}
	
	public FreightOrderLine withFreightRate(BigDecimal freightrate) {
		freightOrderLine.setFreightRate(freightrate);
		return this;
	}
	
	public FreightOrderLine withFreightId(int freightId) {
		if(freightId > 0) {
			freightOrderLine.setM_Freight_ID(freightId);
		}
		return this;
	}
	
	public FreightOrderLine withFreightCategoryId(int freightCategoryId) {
		if(freightCategoryId > 0) {
			freightOrderLine.setM_FreightCategory_ID(freightCategoryId);
		}
		return this;
	}
	
	public FreightOrderLine withLocationFromId(int locationFromId) {
		if(locationFromId > 0) {
			freightOrderLine.setC_LocFrom_ID(locationFromId);
		}
		return this;
	}
	
	public FreightOrderLine withLocationToId(int locationToId) {
		if(locationToId > 0) {
			freightOrderLine.setC_LocTo_ID(locationToId);
		}
		return this;
	}
	
	public FreightOrderLine withBusinessPartnerId(int businessPartnerId) {
		if(businessPartnerId > 0) {
			freightOrderLine.setC_BPartner_ID(businessPartnerId);
		}
		return this;
	}
	
	public FreightOrderLine withFreightCatehoryId(int freightCategoryId) {
		if(freightCategoryId > 0) {
			freightOrderLine.setM_FreightCategory_ID(freightCategoryId);
			MFreightCategory freightCategory = MFreightCategory.getById(freightOrderLine.getCtx(), freightCategoryId, freightOrderLine.get_TrxName());
			if(freightCategory.isInvoiced()) {
				if(freightCategory.getM_Product_ID() <= 0
						&& freightCategory.getC_Charge_ID() <= 0) {
					throw new AdempiereException(freightCategory.getName() + ": @IsInvoiced@ @M_Product_ID@ / @C_Charge_ID@ @NotFound@");
				}
			}
			freightOrderLine.setIsInvoiced(freightCategory.isInvoiced());
			//	Set Product and Charge
			if(freightCategory.getM_Product_ID() > 0) {
				freightOrderLine.setM_Product_ID(freightCategory.getM_Product_ID());
			}
			
			if(freightCategory.getC_Charge_ID() > 0) {
				freightOrderLine.setC_Charge_ID(freightCategory.getC_Charge_ID());
			}
		}
		return this;
	}
	
	public FreightOrderLine withInOutboundOrderReferenceId(int inOutboundOrderId) {
		if(inOutboundOrderId > 0) {
			freightOrderLine.set_ValueOfColumn("WM_InOutBound_ID", inOutboundOrderId);
		}
		return this;
	}
	
	public FreightOrderLine withDistributionOrderReferenceId(int distributionOrderId) {
		if(distributionOrderId > 0) {
			freightOrderLine.set_ValueOfColumn("DD_Order_ID", distributionOrderId);
		}
		return this;
	}
	
	public FreightOrderLine withOrderReferenceId(int orderId) {
		if(orderId > 0) {
			freightOrderLine.set_ValueOfColumn("C_Order_ID", orderId);
		}
		return this;
	}
	
	public FreightOrderLine withOrderLineReferenceId(int orderLineId) {
		if(orderLineId > 0) {
			freightOrderLine.set_ValueOfColumn("C_OrderLine_ID", orderLineId);
		}
		return this;
	}
	
	public FreightOrderLine withDistributionOrderLineReferenceId(int distributionOrderLineId) {
		if(distributionOrderLineId > 0) {
			freightOrderLine.set_ValueOfColumn("DD_OrderLine_ID", distributionOrderLineId);
		}
		return this;
	}
	
	public FreightOrderLine withInOutboundOrderLineReferenceId(int inOutboundOrderLineId) {
		if(inOutboundOrderLineId > 0) {
			freightOrderLine.set_ValueOfColumn("WM_InOutBoundLine_ID", inOutboundOrderLineId);
		}
		return this;
	}
	
	public void save() {
		if(freightOrderLine != null) {
			//	Set from client
			MClientInfo clientInfo = MClientInfo.get(freightOrderLine.getCtx());
			//	Weight
			if(clientInfo.getC_UOM_Weight_ID() <= 0) {
				throw new AdempiereException("@C_UOM_Weight_ID@ @NotFound@ @SeeClientInfoConfig@");
			}
			//	Volume
			if(clientInfo.getC_UOM_Volume_ID() <= 0) {
				throw new AdempiereException("@C_UOM_Volume_ID@ @NotFound@ @SeeClientInfoConfig@");
			}
			//	Set values
			freightOrderLine.setWeight_UOM_ID(clientInfo.getC_UOM_Weight_ID());
			freightOrderLine.setVolume_UOM_ID(clientInfo.getC_UOM_Volume_ID());
			freightOrderLine.saveEx();
		}
	}
}
