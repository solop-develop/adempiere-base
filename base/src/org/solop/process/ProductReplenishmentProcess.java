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

package org.solop.process;

import org.adempiere.core.domains.models.*;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.AdempiereUserError;
import org.compiere.util.Env;
import org.compiere.util.Msg;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

/**
 * 	Generated Process for (Product Replenishment Process)
 *  @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *  @version Release 5.1.2
 */
public class ProductReplenishmentProcess extends ProductReplenishmentProcessAbstract {

	/** Return Info				*/
	private final List<String> generated = new ArrayList<>();

	@Override
	protected void prepare() {
		super.prepare();
		if(getDocTypeId() <= 0) {
			throw new AdempiereException("@C_DocType_ID@ @NotFound@");
		}
		if(getReplenishmentCreate() == null) {
			throw new AdempiereException("@ReplenishmentCreate@ @NotFound@");
		}
		MDocType documentType = MDocType.get(getCtx(), getDocTypeId());
		if (!Optional.ofNullable(documentType.getDocBaseType()).orElse("").equals(getReplenishmentCreate())) {
			throw new AdempiereException("@C_DocType_ID@=" + documentType.getName() + " <> " + getReplenishmentCreate());
		}
	}

	@Override
	protected String doIt() throws Exception {
		if(getReplenishmentCreate().equals(MDocType.DOCBASETYPE_PurchaseOrder)) {
			createPurchaseOrder();
		} else if(getReplenishmentCreate().equals(MDocType.DOCBASETYPE_PurchaseRequisition)) {
			createRequisition();
		} else if(getReplenishmentCreate().equals(MDocType.DOCBASETYPE_MaterialMovement)) {
			createMovements();
		} else if(getReplenishmentCreate().equals(MDocType.DOCBASETYPE_DistributionOrder)) {
			createDistributionOrder();
		}
		return "@Created@ " + generated.toString();
	}

	/**
	 * Get from Smart Browser Selection
	 * @return
	 */
	private List<X_T_Replenish> getReplenish(boolean isMandatoryBusinessPartner) {
		List<X_T_Replenish> replenishList = new ArrayList<X_T_Replenish>();
		for(Integer key : getSelectionKeys()) {
			BigDecimal qtyToOrdered = getSelectionAsBigDecimal(key, "R_QtyToOrder");
			int bPartnerId = getSelectionAsInt(key, "R_C_BPartner_ID");
			if(qtyToOrdered == null
					|| qtyToOrdered.compareTo(Env.ZERO) <= 0) {
				continue;
			}
			//	Validate Distribution Orders
			if(isMandatoryBusinessPartner) {
				if(bPartnerId <= 0) {
					continue;
				}
			}
			//	
			X_T_Replenish replenish = new X_T_Replenish(getCtx(), 0, get_TrxName());
			replenish.setAD_PInstance_ID(getAD_PInstance_ID());
			replenish.setM_Warehouse_ID(getSelectionAsInt(key, "R_M_Warehouse_ID"));
			replenish.setM_Product_ID(getSelectionAsInt(key, "R_M_Product_ID"));
			replenish.setAD_Org_ID(getSelectionAsInt(key, "R_AD_Org_ID"));
			replenish.setReplenishType(getSelectionAsString(key, "R_ReplenishType"));
			replenish.setLevel_Min(getSelectionAsBigDecimal(key, "R_Level_Min"));
			replenish.setLevel_Max(getSelectionAsBigDecimal(key, "R_Level_Max"));
			replenish.setC_BPartner_ID(bPartnerId);
			replenish.setOrder_Min(getSelectionAsBigDecimal(key, "R_Order_Min"));
			replenish.setOrder_Pack(getSelectionAsBigDecimal(key, "R_Order_Pack"));
			replenish.setQtyToOrder(qtyToOrdered);
			replenish.setReplenishmentCreate(getReplenishmentCreate());
			replenish.setM_WarehouseSource_ID(getSelectionAsInt(key, "R_M_WarehouseSource_ID"));
			replenish.setC_DocType_ID(getDocTypeId());
			replenishList.add(replenish);
		}
		//	Default return
		return replenishList;
	}

	/**
	 * 	Create PO's
	 */
	private void createPurchaseOrder() {
		Map<String, MOrder> orders = new HashMap<>();
		List<X_T_Replenish> replenishList = getReplenish(true);
		for (X_T_Replenish replenish : replenishList) {
			String key = replenish.getC_BPartner_ID() + "|" + replenish.getM_Warehouse_ID();
			MOrder order = null;
			if(orders.containsKey(key)) {
				order = orders.get(key);
			} else {
				MWarehouse warehouse = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
				order = new MOrder(getCtx(), 0, get_TrxName());
				order.setIsSOTrx(false);
				order.setC_DocTypeTarget_ID(getDocTypeId());
				MBPartner businessPartner = new MBPartner(getCtx(), replenish.getC_BPartner_ID(), get_TrxName());
				order.setBPartner(businessPartner);
				order.setSalesRep_ID(getAD_User_ID());
				order.setDescription(Msg.getMsg(getCtx(), "Replenishment"));
				//	Set Org/WH
				order.setAD_Org_ID(warehouse.getAD_Org_ID());
				order.setM_Warehouse_ID(warehouse.getM_Warehouse_ID());
				order.saveEx();
				orders.put(key, order);
				log.fine(order.toString());
				generated.add(order.getDocumentNo());
			}
			MOrderLine line = new MOrderLine (order);
			line.setM_Product_ID(replenish.getM_Product_ID());
			line.setQty(replenish.getQtyToOrder());
			line.setPrice();
			line.saveEx();
			setLastMovementDate(replenish.getM_Product_ID(), replenish.getM_Warehouse_ID());
		}
	}	//	createPO

	/**
	 * 	Create Requisition
	 */
	private void createRequisition() {
		Map<Integer, MRequisition> requisitions = new HashMap<>();
		List<X_T_Replenish> replenishList = getReplenish(false);
		for (X_T_Replenish replenish : replenishList) {
			MRequisition requisition = null;
			if(requisitions.containsKey(replenish.getM_Warehouse_ID())) {
				requisition = requisitions.get(replenish.getM_Warehouse_ID());
			} else {
				MWarehouse warehouse = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
				requisition = new MRequisition (getCtx(), 0, get_TrxName());
				requisition.setAD_User_ID (getAD_User_ID());
				requisition.setC_DocType_ID(getDocTypeId());
				requisition.setDescription(Msg.getMsg(getCtx(), "Replenishment"));
				//	Set Org/WH
				requisition.setAD_Org_ID(warehouse.getAD_Org_ID());
				requisition.setM_Warehouse_ID(warehouse.getM_Warehouse_ID());
				requisition.saveEx();
				requisitions.put(replenish.getM_Warehouse_ID(), requisition);
				log.fine(requisition.toString());
				generated.add(requisition.getDocumentNo());
			}
			//
			MRequisitionLine line = new MRequisitionLine(requisition);
			line.setM_Product_ID(replenish.getM_Product_ID());
			line.setC_BPartner_ID(replenish.getC_BPartner_ID());
			line.setQty(replenish.getQtyToOrder());
			line.setPrice();
			line.saveEx();
			setLastMovementDate(replenish.getM_Product_ID(), replenish.getM_Warehouse_ID());
		}
	}	//	createRequisition

	/**
	 * 	Create Inventory Movements
	 */
	private void createMovements() {
		Map<Integer, MMovement> movements = new HashMap<>();
		List<X_T_Replenish> replenishList = getReplenish(false);
		for (X_T_Replenish replenish : replenishList) {
			MMovement move = null;
			MWarehouse sourceWarehouse = MWarehouse.get(getCtx(), replenish.getM_WarehouseSource_ID());
			MWarehouse warehouse = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
			if(movements.containsKey(replenish.getM_WarehouseSource_ID())) {
				move = movements.get(replenish.getM_WarehouseSource_ID());
			} else {
				move = new MMovement (getCtx(), 0, get_TrxName());
				move.setC_DocType_ID(getDocTypeId());
				move.setDescription(Msg.getMsg(getCtx(), "Replenishment")
						+ ": " + sourceWarehouse.getName() + "->" + warehouse.getName());
				//	Set Org
				move.setAD_Org_ID(sourceWarehouse.getAD_Org_ID());
				move.saveEx();
				movements.put(replenish.getM_WarehouseSource_ID(), move);
				log.fine(move.toString());
				generated.add(move.getDocumentNo());
			}
			//	To
			int M_LocatorTo_ID = warehouse.getDefaultLocator().getM_Locator_ID();
			//	From: Look-up Storage
			MProduct product = MProduct.get(getCtx(), replenish.getM_Product_ID());
			String MMPolicy = product.getMMPolicy();
			MStorage[] storages = MStorage.getWarehouse(getCtx(), sourceWarehouse.getM_Warehouse_ID(), replenish.getM_Product_ID(),
					0, null, MClient.MMPOLICY_FiFo.equals(MMPolicy), false, 0, get_TrxName());
			//
			BigDecimal target = replenish.getQtyToOrder();
            for (MStorage storage : storages) {
                if (storage.getQtyOnHand().signum() <= 0)
                    continue;
                BigDecimal moveQty = target;
                if (storage.getQtyOnHand().compareTo(moveQty) < 0)
                    moveQty = storage.getQtyOnHand();
                //
                MMovementLine line = new MMovementLine(move);
                line.setM_Product_ID(replenish.getM_Product_ID());
                line.setMovementQty(moveQty);
                if (replenish.getQtyToOrder().compareTo(moveQty) != 0) {
					line.setDescription("Total: " + replenish.getQtyToOrder());
				}
                line.setM_Locator_ID(storage.getM_Locator_ID());        //	from
                line.setM_AttributeSetInstance_ID(storage.getM_AttributeSetInstance_ID());
                line.setM_LocatorTo_ID(M_LocatorTo_ID);                    //	to
                line.setM_AttributeSetInstanceTo_ID(storage.getM_AttributeSetInstance_ID());
                line.saveEx();
                //
                target = target.subtract(moveQty);
                if (target.signum() == 0)
                    break;
            }
			setLastMovementDate(replenish.getM_Product_ID(), replenish.getM_Warehouse_ID());
		}
		if (replenishList.isEmpty()) {
			throw new AdempiereException("@Error@ @M_WarehouseSource_ID@ @NotFound@");
		}
	}	//	Create Inventory Movements

	/**
	 * 	Create Distribution Order
	 */
	private void createDistributionOrder() throws Exception {
		Map<Integer, X_DD_Order> distributions = new HashMap<>();
		List<X_T_Replenish> replenishList = getReplenish(false);
		for (X_T_Replenish replenish : replenishList) {
			MWarehouse sourceWarehouse = MWarehouse.get(getCtx(), replenish.getM_WarehouseSource_ID());
			MWarehouse warehouse = MWarehouse.get(getCtx(), replenish.getM_Warehouse_ID());
			X_DD_Order order = null;
			if(distributions.containsKey(replenish.getM_WarehouseSource_ID())) {
				order = distributions.get(replenish.getM_WarehouseSource_ID());
			} else {
				order = new X_DD_Order(getCtx(), 0, get_TrxName());
				order.setC_DocType_ID(getDocTypeId());
				order.setDescription(Msg.getMsg(getCtx(), "Replenishment")
						+ ": " + sourceWarehouse.getName() + "->" + warehouse.getName());
				//	Set Org
				order.setAD_Org_ID(sourceWarehouse.getAD_Org_ID());
				// Set Org Trx
				MOrg orgTrx = MOrg.get(getCtx(), warehouse.getAD_Org_ID());
				order.setAD_OrgTrx_ID(orgTrx.getAD_Org_ID());
				int bPartnerId = orgTrx.getLinkedC_BPartner_ID(get_TrxName());
				if (bPartnerId == 0)
					throw new AdempiereUserError("@C_BPartner_ID@ @AD_Org_ID@ @FillMandatory@ ");
				MBPartner bp = new MBPartner(getCtx(),bPartnerId,get_TrxName());
				// Set BPartner Link to Org
				setBusinessPartner(order, bp);
				order.setDateOrdered(new Timestamp(System.currentTimeMillis()));
				order.setDeliveryRule(MOrder.DELIVERYRULE_Availability);
				order.setDeliveryViaRule(MOrder.DELIVERYVIARULE_Delivery);
				order.setPriorityRule(MOrder.PRIORITYRULE_Medium);
				order.setIsInDispute(false);
				order.setIsApproved(false);
				order.setIsDropShip(false);
				order.setIsDelivered(false);
				order.setIsInTransit(false);
				order.setIsPrinted(false);
				order.setIsSelected(false);
				order.setIsSOTrx(false);
				// Warehouse in Transit
				MWarehouse[] whsInTransit  = MWarehouse.getForOrg(getCtx(), sourceWarehouse.getAD_Org_ID());
				for (MWarehouse whInTransit:whsInTransit) {
					if(whInTransit.isInTransit())
						order.setM_Warehouse_ID(whInTransit.getM_Warehouse_ID());
				}
				if (order.get_ValueAsInt("M_Warehouse_ID") == 0) {
					throw new AdempiereUserError("@M_Warehouse_ID@ @InTransit@ @FillMandatory@ ");
				}
				order.saveEx();
				distributions.put(replenish.getM_WarehouseSource_ID(), order);
				log.fine(order.toString());
				generated.add(order.getDocumentNo());
			}
			int M_LocatorTo_ID = warehouse.getDefaultLocator().getM_Locator_ID();
			int M_Locator_ID = sourceWarehouse.getDefaultLocator().getM_Locator_ID();
			if(M_LocatorTo_ID == 0 || M_Locator_ID==0)
				throw new AdempiereUserError(Msg.translate(getCtx(), "M_Locator_ID")+" @FillMandatory@ ");
			//
			X_DD_OrderLine line = getDistributionOrderLineInstanceFromParent(order);
			line.setM_Product_ID(replenish.getM_Product_ID());
			line.setQtyEntered(replenish.getQtyToOrder());
			line.setQtyOrdered(replenish.getQtyToOrder());
			if (replenish.getQtyToOrder().compareTo(replenish.getQtyToOrder()) != 0) {
				line.setDescription("Total: " + replenish.getQtyToOrder());
			}
			line.setM_Locator_ID(M_Locator_ID);		//	from
			line.setM_AttributeSetInstance_ID(0);
			line.setM_LocatorTo_ID(M_LocatorTo_ID);					//	to
			line.setM_AttributeSetInstanceTo_ID(0);
			line.setIsInvoiced(false);
			line.saveEx();
			setLastMovementDate(replenish.getM_Product_ID(), replenish.getM_Warehouse_ID());
		}
		if (replenishList.isEmpty()) {
			throw new AdempiereException("@Error@ @M_WarehouseSource_ID@ @NotFound@");
		}
	}	//	create Distribution Order

	private void setLastMovementDate(int productId, int warehouseId) {
		PO replenish = new Query(getCtx(), I_M_Replenish.Table_Name, "M_Product_ID = ? AND M_Warehouse_ID = ?", get_TrxName())
				.setParameters(productId, warehouseId)
				.setOnlyActiveRecords(true)
				.first();
		if(replenish != null) {
			Timestamp lastReplenishmentDate = new Timestamp(System.currentTimeMillis());
			replenish.set_ValueOfColumn("LastReplenishmentDate", lastReplenishmentDate);
			replenish.saveEx();
		}
	}

	/**
	 * Set Business Partner Reference
	 * @param referenceToSet
	 * @param bp
	 */
	private void setBusinessPartner(X_DD_Order referenceToSet, MBPartner bp) {
		if (bp == null) {
			return;
		}
		referenceToSet.setC_BPartner_ID(bp.getC_BPartner_ID());
		//	Defaults Payment Term
		int ii = 0;
		if (referenceToSet.isSOTrx())
			ii = bp.getC_PaymentTerm_ID();
		else
			ii = bp.getPO_PaymentTerm_ID();

		//	Default Price List
		if (referenceToSet.isSOTrx())
			ii = bp.getM_PriceList_ID();
		else
			ii = bp.getPO_PriceList_ID();
		//	Default Delivery/Via Rule
		String ss = bp.getDeliveryRule();
		if (ss != null)
			referenceToSet.setDeliveryRule(ss);
		ss = bp.getDeliveryViaRule();
		if (ss != null)
			referenceToSet.setDeliveryViaRule(ss);
		//	Default Invoice/Payment Rule
		ss = bp.getInvoiceRule();

		if (referenceToSet.getSalesRep_ID() == 0) {
			ii = Env.getAD_User_ID(referenceToSet.getCtx());
			if (ii != 0) {
				referenceToSet.setSalesRep_ID(ii);
			}
		}

		List<MBPartnerLocation> partnerLocations = Arrays.asList(bp.getLocations(false));
		// search the Ship To Location
		MBPartnerLocation partnerLocation = partnerLocations.stream() 			// create steam
				.filter(X_C_BPartner_Location::isShipTo).reduce((first , last ) -> last) 	// get of last Ship to location
				.orElseGet(() -> partnerLocations.stream() 								// if not exist Ship to location else get first partner location
						.findFirst()										// if not exist partner location then throw an exception
						.orElseThrow(() -> new AdempiereException("@IsShipTo@ @NotFound@"))
				);

		referenceToSet.setC_BPartner_Location_ID(partnerLocation.getC_BPartner_Location_ID());
		//
		Arrays.stream(bp.getContacts(false))
				.findFirst()
				.ifPresent(user -> referenceToSet.setAD_User_ID(user.getAD_User_ID()));
	}

	/**
	 * Get Instance of Distribution Order Line from Distribution Order
	 * @param distributionOrder
	 * @return
	 */
	private X_DD_OrderLine getDistributionOrderLineInstanceFromParent(X_DD_Order distributionOrder) {
		X_DD_OrderLine distributionOrderLine = new X_DD_OrderLine(distributionOrder.getCtx(), 0, distributionOrder.get_TrxName());
		distributionOrderLine.setDD_Order_ID(distributionOrder.get_ID());
		distributionOrderLine.setAD_Org_ID(distributionOrder.getAD_Org_ID());
		distributionOrderLine.setDateOrdered(distributionOrder.getDateOrdered());
		distributionOrderLine.setDatePromised(distributionOrder.getDatePromised());
		return distributionOrderLine;
	}
}