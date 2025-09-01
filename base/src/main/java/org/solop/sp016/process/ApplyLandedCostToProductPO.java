/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.										*
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/

package org.solop.sp016.process;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.compiere.model.MCurrency;
import org.compiere.model.MProductPO;
import org.compiere.util.Env;
import org.solop.sp016.util.ConsignedMaterialUtil;

/** Generated Process for (Apply Landed Cost to Product POS)
 *  @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 *  @version Release 3.9.3
 */
public class ApplyLandedCostToProductPO extends ApplyLandedCostToProductPOAbstract {

	/**	Counter for created	*/
	private AtomicInteger updated = new AtomicInteger();
	
	@Override
	protected String doIt() throws Exception {
		if(Optional.ofNullable(getPercentage()).orElse(Env.ZERO).compareTo(Env.ZERO) > 0) {
			getSelectionKeys().forEach(productId -> {
				Arrays.asList(MProductPO.getOfProduct(getCtx(), productId, get_TrxName())).forEach(purchaseProduct -> {
					BigDecimal landedCostPrice = Optional.ofNullable(purchaseProduct.getPricePO()).orElse(Env.ZERO);
					landedCostPrice = landedCostPrice.multiply(getPercentage()).divide(Env.ONEHUNDRED, MathContext.DECIMAL128).setScale(MCurrency.getStdPrecision(getCtx(), purchaseProduct.getC_Currency_ID()), RoundingMode.HALF_UP);
					purchaseProduct.set_ValueOfColumn(ConsignedMaterialUtil.COLUMNNAME_PriceLastLanded, landedCostPrice);
					purchaseProduct.saveEx();
				});
				//	Increment updates
				updated.getAndIncrement();
			});
		}
		return "@Updated@ " + updated;
	}
}