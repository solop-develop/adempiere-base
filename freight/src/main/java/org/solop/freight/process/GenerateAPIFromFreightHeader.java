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
import org.compiere.model.MFreightCategory;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.eevolution.distribution.model.MDDFreight;
import org.eevolution.distribution.model.MDDFreightLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/** Generated Process for (Generate AP Invoice from Freight Order Header)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public class GenerateAPIFromFreightHeader extends GenerateAPIFromFreightHeaderAbstract {
	private MInvoice invoice;
	private AtomicInteger created = new AtomicInteger();
	
	@SuppressWarnings("unchecked")
	@Override
	protected String doIt() throws Exception {
		invoice = new MInvoice(getCtx(), getRecord_ID(), get_TrxName());
		((List<MDDFreight>) getInstancesForSelection(get_TrxName()))
			.stream()
			.forEach(freightOrder -> createInvoiceLine(freightOrder));
		//	
		return "@Created@ " + created.get();
	}
	
	
	/**
	 * Create Invoice from Freight Line
	 * @param freightOrder
	 */
	private void createInvoiceLine(MDDFreight freightOrder) {
		BigDecimal qtyInvoiced = Env.ONE;
		MInvoiceLine invoiceLine = new MInvoiceLine(invoice);
		// Freight values
		invoiceLine.set_ValueOfColumn("DD_Freight_ID", freightOrder.getDD_Freight_ID());
		if(getFreightCategoryId() <= 0) {
			setFreightCategoryId(freightOrder.getM_FreightCategory_ID());
		}
		if(getFreightCategoryId() <= 0) {
			throw new AdempiereException("@M_FreightCategory_ID@ @IsMandatory@");
		}
		MFreightCategory freightCategory = MFreightCategory.getById(getCtx(), getFreightCategoryId(), null);
		if(freightCategory.getM_Product_ID() != 0) {
			invoiceLine.setM_Product_ID(freightCategory.getM_Product_ID());
			invoiceLine.setQty(qtyInvoiced);
			invoiceLine.setPrice(freightOrder.getFreightAmt());
			invoiceLine.setTax();
		} else if(freightCategory.getC_Charge_ID() != 0){
			invoiceLine.setC_Charge_ID(freightCategory.getC_Charge_ID());
			invoiceLine.setQty(qtyInvoiced);
			invoiceLine.setPrice(freightOrder.getFreightAmt());
			invoiceLine.setTax();
		} else {
			invoiceLine.setQty(qtyInvoiced);
			invoiceLine.setPrice(freightOrder.getFreightAmt());
		}
		List<MDDFreightLine> freightLines = freightOrder.getLines();
		BigDecimal weight = freightLines.stream().map(freightOrderLine -> Optional.ofNullable(freightOrderLine.getWeight()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
		BigDecimal volume = freightLines.stream().map(freightOrderLine -> Optional.ofNullable(freightOrderLine.getVolume()).orElse(BigDecimal.ZERO)).reduce(BigDecimal.ZERO, BigDecimal::add);
		//	Add description from freight line
		invoiceLine.addDescription(Msg.getMsg(getCtx(), "SOLOP_FreightDetailForAP", new Object[] {
				freightOrder.getDocumentNo(), 
				freightLines.size(), 
				weight, 
				volume, 
				freightOrder.getFreightAmt()
				}));
		invoiceLine.saveEx();
	}
}