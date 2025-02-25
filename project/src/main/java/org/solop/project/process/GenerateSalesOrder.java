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

package org.solop.project.process;

import org.adempiere.core.domains.models.I_C_Order;
import org.adempiere.core.domains.models.I_C_ProjectLine;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 * 	Generated Process for (Generate Sales Order (From Line))
 *  @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *  @version Release 3.9.4
 */
public class GenerateSalesOrder extends GenerateSalesOrderAbstract {
	@Override
	protected void prepare() {
		super.prepare();
		if(!MProjectLine.Table_Name.equals(getTableName())) {
			throw new AdempiereException("@C_ProjectLine_ID@ @NotFound@");
		}
		if (getRecord_ID() == 0) {
			throw new AdempiereException("@C_ProjectLine_ID@ @NotFound@");
		}
	}

	@Override
	protected String doIt() throws Exception {
		String description = null;
		BigDecimal quantityToOrder = null;
		BigDecimal priceActual = null;
		BigDecimal quantityEntered = null;
		int projectUomId = 0;
		MProjectLine mainLine = new MProjectLine(getCtx(), getRecord_ID(), get_TrxName());
		log.info("doIt - C_ProjectPhase_ID=" + getRecord_ID());
		description = mainLine.getDescription();
		quantityToOrder = mainLine.getPlannedQty();
		priceActual = mainLine.getPlannedAmt();
		//	Add UOM
		quantityEntered = mainLine.getPlannedQty();
		projectUomId = mainLine.get_ValueAsInt("C_UOM_ID");

		MProject fromProject = getProject (getCtx(), mainLine.getC_Project_ID(), get_TrxName());
		if (fromProject.getC_PaymentTerm_ID() <= 0) {
			throw new AdempiereException("@C_PaymentTerm_ID@ @NotFound@");
		}
		MOrder order = new MOrder (fromProject, true, MDocType.COLUMNNAME_DocSubTypeSO);
		//	Add Document Type Target
		if(getDocTypeTargetId() > 0) {
			order.setC_DocTypeTarget_ID(getDocTypeTargetId());
		}
		order.setC_Project_ID(mainLine.getC_Project_ID());
		order.set_ValueOfColumn(I_C_ProjectLine.COLUMNNAME_C_ProjectLine_ID, mainLine.getC_ProjectLine_ID());
		//	Phase
		if(mainLine.get_ValueAsBoolean(I_C_Order.COLUMNNAME_IsDropShip)) {
			int dropShipBPartnerId = mainLine.get_ValueAsInt(I_C_Order.COLUMNNAME_DropShip_BPartner_ID);
			int dropShipBPartnerLocationId = mainLine.get_ValueAsInt(I_C_Order.COLUMNNAME_DropShip_Location_ID);
			if(dropShipBPartnerId > 0
					&& dropShipBPartnerLocationId > 0) {
				order.setIsDropShip(mainLine.get_ValueAsBoolean(I_C_Order.COLUMNNAME_IsDropShip));
				order.setDropShip_BPartner_ID(dropShipBPartnerId);
				order.setDropShip_Location_ID(dropShipBPartnerLocationId);
			}
		}
		order.setDateOrdered(getDateOrdered());
		//	Get Lines
		List<MProjectLine> projectLines = mainLine.getChildren();
		for (MProjectLine pLine : projectLines) {
			Timestamp datePromisedLine = (Timestamp) pLine.getDatePromised();
			Timestamp dateOrder = TimeUtil.addDays(order.getDateOrdered(), -1);
			if (datePromisedLine != null && datePromisedLine.compareTo(dateOrder) <= 0) {
				throw new AdempiereException("@DatePromised@ < @DateOrdered@");
			}
		}

		order.setDescription(order.getDescription() + " - " + mainLine.getName());
		if(mainLine.getDatePromised() != null) {
			order.setDatePromised(mainLine.getDatePromised());
		}
		order.saveEx();

		//	Create an order on Phase Level
		if (mainLine.getM_Product_ID() != 0) {
			MProduct product = MProduct.get(getCtx(), mainLine.getM_Product_ID());
			MOrderLine orderLine = new MOrderLine(order);
			orderLine.setLine(mainLine.getLine());
			StringBuilder stringBuilder = new StringBuilder(mainLine.getName());
			if (!Util.isEmpty(description)) {
				stringBuilder.append(" - ").append(description);
			}
			orderLine.setDescription(stringBuilder.toString());
			//
			orderLine.setProduct(product);
			setQuantityToOrder(orderLine, product, projectUomId, quantityEntered, quantityToOrder);
			orderLine.setPrice();
			orderLine.setC_Project_ID(fromProject.getC_Project_ID());
			if (mainLine.getPlannedAmt()!= null && mainLine.getPlannedAmt().compareTo(Env.ZERO) != 0) {
				orderLine.setPrice(mainLine.getPlannedAmt());
			}
			orderLine.setTax();
			orderLine.saveEx();
			return "@C_Order_ID@ " + order.getDocumentNo() + " (1)";
		}

		//	Project Phase Lines
		AtomicInteger count = new AtomicInteger(0);
		projectLines
				.forEach(projectLine -> {
					MOrderLine orderLine = new MOrderLine(order);
					orderLine.setLine(projectLine.getLine());
					orderLine.setDescription(projectLine.getDescription());
					//
					orderLine.setM_Product_ID(projectLine.getM_Product_ID(), true);
					MProduct product = new MProduct(getCtx(),projectLine.getM_Product_ID(),get_TrxName());
					BigDecimal toOrder = projectLine.getPlannedQty().subtract(projectLine.getInvoicedQty());
					setQuantityToOrder(orderLine, product, projectLine.get_ValueAsInt("C_UOM_ID"), projectLine.getPlannedQty(), toOrder);
					orderLine.setPrice();
					if (projectLine.getPlannedPrice() != null && projectLine.getPlannedPrice().compareTo(Env.ZERO) != 0){
						orderLine.setPrice(projectLine.getPlannedPrice());
						orderLine.setPriceList(projectLine.getPlannedPrice());
					}
					orderLine.setDiscount();
					orderLine.setTax();
					orderLine.setC_Project_ID(fromProject.getC_Project_ID());
					orderLine.setC_ProjectPhase_ID(projectLine.getC_ProjectPhase_ID());
					if(projectLine.getDatePromised() != null) {
						orderLine.setDatePromised(projectLine.getDatePromised());
					}
					orderLine.set_ValueOfColumn(I_C_ProjectLine.COLUMNNAME_StartDate, projectLine.getStartDate());
					orderLine.set_ValueOfColumn(I_C_ProjectLine.COLUMNNAME_EndDate, projectLine.getEndDate());
					orderLine.set_ValueOfColumn(I_C_ProjectLine.COLUMNNAME_C_ProjectLine_ID, projectLine.getC_ProjectLine_ID());
					orderLine.saveEx();
					count.getAndUpdate(no -> no + 1);
				});    //	for all lines
		if (projectLines.size() != count.get())
			log.log(Level.SEVERE, "Lines difference - ProjectLines=" + projectLines.size() + " <> Saved=" + count.get());
		return "@C_Order_ID@ " + order.getDocumentNo() + " (" + count + ")";
	}	//	doIt

	/**
	 * Set line Quantity
	 * @param orderLine
	 * @param product
	 * @param uomToId
	 * @param quantityEntered
	 * @param quantityOrdered
	 */
	private void setQuantityToOrder(MOrderLine orderLine, MProduct product, int uomToId, BigDecimal quantityEntered, BigDecimal quantityOrdered) {
		int uomId = product.getC_UOM_ID();
		if(uomToId > 0
				&& quantityEntered != null
				&& quantityEntered != Env.ZERO) {
			uomId = uomToId;
			if(uomId != product.getC_UOM_ID()) {
				BigDecimal convertedQuantity = MUOMConversion.convertProductFrom (getCtx(), product.getM_Product_ID(), uomToId, quantityEntered);
				if (convertedQuantity == null) {
					quantityEntered = quantityOrdered;
				} else {
					quantityOrdered = convertedQuantity;
				}
				orderLine.setQty(quantityEntered);
				orderLine.setQtyOrdered(quantityOrdered);
			} else {
				orderLine.setQty(quantityOrdered);
			}
		} else {
			orderLine.setQty(quantityOrdered);
		}
		orderLine.setC_UOM_ID(uomId);
	}

	private MProject getProject (Properties ctx, int projectId, String trxName) {
		MProject fromProject = new MProject (ctx, projectId, trxName);
		if (fromProject.getC_Project_ID() == 0)
			throw new AdempiereException("@C_Project_ID@ @NotFound@" + projectId);
		if (fromProject.getM_PriceList_Version_ID() == 0)
			throw new AdempiereException("@M_PriceList_ID@ @NotFound @@To@ @C_Project_ID@");
		if (fromProject.getM_Warehouse_ID() == 0)
			throw new AdempiereException("@M_Warehouse_ID@ @NotFound@ @To@ @C_Project_ID@");
		if (fromProject.getC_BPartner_ID() == 0)
			throw new AdempiereException("@C_BPartner_ID@ @NotFound@");
		if (fromProject.getC_BPartner_Location_ID() == 0)
			throw new AdempiereException("@C_BPartner_Location_ID@ @NotFound@");
		return fromProject;
	}
}