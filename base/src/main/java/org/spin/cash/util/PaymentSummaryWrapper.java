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
package org.spin.cash.util;

import java.math.BigDecimal;
import java.util.Optional;

import org.compiere.util.Env;

/**
 * A wrapper with the need for find match with bank withdrawal
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class PaymentSummaryWrapper {
	
	/**
	 * Static constructor
	 * @return
	 */
	public static PaymentSummaryWrapper newInstance() {
		return new PaymentSummaryWrapper();
	}
	
	private int currencyId;
	private int conversionTypeId;
	private BigDecimal amount;
	
	public final BigDecimal getAmount() {
		return amount;
	}

	public final PaymentSummaryWrapper addAmount(BigDecimal amount) {
		this.amount = Optional.ofNullable(this.amount).orElse(Env.ZERO).add(Optional.ofNullable(amount).orElse(Env.ZERO));
		return this;
	}

	public final int getConversionTypeId() {
		return conversionTypeId;
	}

	public final PaymentSummaryWrapper withConversionTypeId(int conversionTypeId) {
		this.conversionTypeId = conversionTypeId;
		return this;
	}
	
	public final int getCurrencyId() {
		return currencyId;
	}
	
	public final PaymentSummaryWrapper withCurrencyId(int currencyId) {
		this.currencyId = currencyId;
		return this;
	}

	@Override
	public String toString() {
		return "PaymentSummaryWrapper [currencyId=" + currencyId + ", conversionTypeId=" + conversionTypeId
				+ ", amount=" + amount + "]";
	}
}
