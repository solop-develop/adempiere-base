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
import java.util.Optional;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MOrderLine;
import org.compiere.model.MTax;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * Added for handle custom values for ADempiere core
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class PointOfSalesImprovementsChanges {
	/**	Used for change tax for Order	*/
	public static final String COLUMNNAME_IsTaxExempt = "IsTaxExempt";
	
	/**
	 * Change Order Line for tax exempt
	 * @param orderLine
	 * @return
	 */
	public static void changeTax(MOrderLine orderLine, boolean isSOTaxExempt) {
		if(!orderLine.isProcessed()) {
			if(isSOTaxExempt) {
				MTax exemptTax = getDefaultExemptTax(orderLine.getAD_Org_ID());
				if(exemptTax == null) {
					throw new AdempiereException("@C_Tax_ID@ @IsTaxExempt@ @NotFound@");
				}
				//	Set
				if(orderLine.getC_Tax_ID() == exemptTax.getC_Tax_ID()) {
					return;
				}
				orderLine.setC_Tax_ID(exemptTax.getC_Tax_ID());
				orderLine.setLineNetAmt();
			} else {
				orderLine.setTax();
				orderLine.setLineNetAmt();
			}
		}
	}
	
	/**
	 * Get Tax Rate
	 * @param taxCategoryId
	 * @return
	 */
	public static MTax getTax(int taxCategoryId) {
		Optional<MTax> optionalTax = Arrays.asList(MTax.getAll(Env.getCtx()))
			.stream()
			.filter(tax -> tax.getC_TaxCategory_ID() == taxCategoryId 
					&& isSalesTax(tax))
			.findFirst();
			//	Validate
			if(optionalTax.isPresent()) {
				return optionalTax.get();
			}
		return null;
	}
	
	/**
	 * Get Default Tax Rate for Exempt Tax
	 * @param organizationId
	 * @return
	 */
	private static MTax getDefaultExemptTax(int organizationId) {
		Optional<MTax> optionalTax = Arrays.asList(MTax.getAll(Env.getCtx()))
			.stream()
			.filter(tax -> tax.isZeroTax()
					&& (tax.getAD_Org_ID() == organizationId || tax.getAD_Org_ID() == 0) 
					&& isSalesTax(tax))
			.findFirst();
			//	Validate
			if(optionalTax.isPresent()) {
				return optionalTax.get();
			}
		return null;
	}
	
	/**
	 * Validate if is sales Tax
	 * @param tax
	 * @return
	 * @return boolean
	 */
	private static boolean isSalesTax(MTax tax) {
		return (tax.isSalesTax() 
						|| (!Util.isEmpty(tax.getSOPOType()) 
								&& (tax.getSOPOType().equals(MTax.SOPOTYPE_Both) 
										|| tax.getSOPOType().equals(MTax.SOPOTYPE_SalesTax))));
	}
}
