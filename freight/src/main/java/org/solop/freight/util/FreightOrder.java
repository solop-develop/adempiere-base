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
import org.compiere.model.MDocType;
import org.compiere.model.MShipper;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.eevolution.distribution.model.MDDFreight;
import org.eevolution.distribution.model.MDDFreightLine;
import org.eevolution.distribution.model.MDDVehicle;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

/**
 * Freight Document Util using builder pattern
 * Please rename this class and package
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class FreightOrder {

	private MDDFreight freightOrder;
	
	private FreightOrder(Properties context, int freightOrderId, String transactionName) {
		freightOrder = new MDDFreight(context, freightOrderId, transactionName);
	}
	
	public static FreightOrder newInstance(Properties context, int freightOrderId, String transactionName) {
		return new FreightOrder(context, freightOrderId, transactionName);
	}
	
	public FreightOrder withDocumentDate(Timestamp documentDate) {
		if(documentDate != null) {
			freightOrder.setDateDoc(documentDate);
		}
		return this;
	}
	
	public FreightOrder withOrderedDate(Timestamp documentDate) {
		if(documentDate != null) {
			freightOrder.setDateOrdered(documentDate);
		} else if(freightOrder.getDateDoc() != null) {
			freightOrder.setDateOrdered(freightOrder.getDateDoc());
		}
		return this;
	}
	
	public FreightOrder withShipperId(int shipperId) {
		if(shipperId > 0) {
			freightOrder.setM_Shipper_ID(shipperId);
			MShipper shipper = new MShipper(freightOrder.getCtx(), shipperId, null);
			if(shipper.getC_BPartner_ID() > 0) {
				freightOrder.setC_BPartner_ID(shipper.getC_BPartner_ID());
			}
		}
		return this;
	}
	
	public FreightOrder withDriverId(int driverId) {
		if(driverId > 0) {
			freightOrder.setDD_Driver_ID(driverId);
		}
		return this;
	}
	
	public FreightOrder withVehicleId(int vehicleId) {
		if(vehicleId > 0) {
			freightOrder.setDD_Vehicle_ID(vehicleId);
		}
		return this;
	}
	
	public int getFreightOrderId() {
		if(freightOrder == null) {
			throw new AdempiereException("@DD_Freight_ID@ @NotFound@");
		}
		return freightOrder.getDD_Freight_ID();
	}
	
	public MDDFreight getFreightOrder() {
		return freightOrder;
	}
	
	public FreightOrder withDocumentTypeId(int documentTypeId) {
		if(documentTypeId > 0) {
			freightOrder.setC_DocType_ID(documentTypeId);
		} else {
			freightOrder.setC_DocType_ID(MDocType.getDocType(MDocType.DOCBASETYPE_FreightOrder, Env.getAD_Org_ID(freightOrder.getCtx())));
		}
		return this;
	}
	
	public FreightOrder withFreightCategoryId(int freightCategoryId) {
		if(freightCategoryId > 0) {
			freightOrder.setM_FreightCategory_ID(freightCategoryId);
		}
		return this;
	}
	
	public FreightOrder withDocumentAction(String documentAction) {
		if(!Util.isEmpty(documentAction)) {
			freightOrder.setDocAction(documentAction);
		}
		return this;
	}
	
	public FreightOrder withFreightCostRule(String freightCostRule) {
		if(!Util.isEmpty(freightCostRule)) {
			freightOrder.setFreightCostRule(freightCostRule);
		}
		return this;
	}
	
	public boolean process(String documentAction) throws Exception {
		//	Validate Capacity
		validateCapacity();
		boolean isOk = freightOrder.processIt(documentAction);
		freightOrder.saveEx();
		return isOk;
	}
	
	private void validateCapacity() {
		if(freightOrder.getDD_Driver_ID() > 0) {
			MDDVehicle vehicle = (MDDVehicle) freightOrder.getDD_Vehicle();
			List<MDDFreightLine> freightLines = freightOrder.getLines();
			BigDecimal weight = freightLines.stream().map(freightOrderLine -> Optional.ofNullable(freightOrderLine.getWeight()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
			BigDecimal volume = freightLines.stream().map(freightOrderLine -> Optional.ofNullable(freightOrderLine.getVolume()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
			//	
			if(weight.compareTo(Env.ZERO) <= 0 && volume.compareTo(Env.ZERO) <= 0) {
				return;
			}
			BigDecimal weightCapacity = Optional.ofNullable(vehicle.getMaximumWeight()).orElse(Env.ZERO);
			BigDecimal volumeCapacity = Optional.ofNullable(vehicle.getMaximumVolume()).orElse(Env.ZERO);
			if(weightCapacity.compareTo(Env.ZERO) <= 0 && volumeCapacity.compareTo(Env.ZERO) <= 0) {
				return;
			}
			StringBuffer errorMessage = new StringBuffer();
			if(weightCapacity.compareTo(weight) < 0) {
				errorMessage.append("[@Weight@ (" + getFormattedAmount(weight) + ") > @MaximumWeight@ (" + getFormattedAmount(weightCapacity) + ") => (" + getFormattedAmount(weightCapacity.subtract(weight)) + ")]");
			}
			if(volumeCapacity.compareTo(volume) < 0) {
				if(errorMessage.length() > 0) {
					errorMessage.append("|");
				}
				errorMessage.append("[@Volume@ (" + getFormattedAmount(volume) + ") > @MaximumVolume@ (" + getFormattedAmount(volumeCapacity) + ") => (" + getFormattedAmount(volumeCapacity.subtract(volume)) + ")]");
			}
			if(errorMessage.length() > 0) {
				throw new AdempiereException(errorMessage.toString());
			}
		}
	}
	
	private String getFormattedAmount(BigDecimal anmount) {
		return DisplayType.getNumberFormat(DisplayType.Number).format(anmount);
	}
	
	public FreightOrder withFreightAmount(BigDecimal freightAmount) {
		if(freightAmount != null) {
			freightOrder.setFreightAmt(freightAmount);
		}
		return this;
	}
	
	public void save() {
		if(freightOrder != null) {
			freightOrder.saveEx();
		}
	}
}
