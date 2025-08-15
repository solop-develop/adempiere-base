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

/**
 * A wrapper with the need for find match with bank withdrawal
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class PaymentWrapper {
	
	/**
	 * Static constructor
	 * @return
	 */
	public static PaymentWrapper newInstance() {
		return new PaymentWrapper();
	}
	
	private int paymentId;
	private int bankAccountId;
	private String tenderType;
	private int currencyId;
	private int documentTypeId;
	private int bankId;
	private int businessPartnerId;
	private int conversionTypeId;
	private BigDecimal amount;
	private String documentNo;
	private int paymentMethodId;
	
	public final BigDecimal getAmount() {
		return amount;
	}

	public final PaymentWrapper withAmount(BigDecimal amount) {
		this.amount = amount;
		return this;
	}

	public final int getConversionTypeId() {
		return conversionTypeId;
	}

	public final PaymentWrapper withConversionTypeId(int conversionTypeId) {
		this.conversionTypeId = conversionTypeId;
		return this;
	}

	public final int getBankAccountId() {
		return bankAccountId;
	}
	
	public final PaymentWrapper withBankAccountId(int bankAccountId) {
		this.bankAccountId = bankAccountId;
		return this;
	}
	
	public final String getTenderType() {
		return tenderType;
	}
	
	public final PaymentWrapper withTenderType(String tenderType) {
		this.tenderType = tenderType;
		return this;
	}
	
	public final int getCurrencyId() {
		return currencyId;
	}
	
	public final PaymentWrapper withCurrencyId(int currencyId) {
		this.currencyId = currencyId;
		return this;
	}
	
	public final int getDocumentTypeId() {
		return documentTypeId;
	}
	
	public final PaymentWrapper withDocumentTypeId(int documentTypeId) {
		this.documentTypeId = documentTypeId;
		return this;
	}
	
	public final int getBankId() {
		return bankId;
	}
	
	public final PaymentWrapper withBankId(int bankId) {
		this.bankId = bankId;
		return this;
	}
	
	public final int getBusinessPartnerId() {
		return businessPartnerId;
	}
	
	public final PaymentWrapper withBusinessPartnerId(int businessPartnerId) {
		this.businessPartnerId = businessPartnerId;
		return this;
	}

	public final int getPaymentId() {
		return paymentId;
	}

	public final PaymentWrapper withPaymentId(int paymentId) {
		this.paymentId = paymentId;
		return this;
	}

	public final String getDocumentNo() {
		return documentNo;
	}

	public final PaymentWrapper withDocumentNo(String documentNo) {
		this.documentNo = documentNo;
		return this;
	}

	public int getPaymentMethodId() {
		return paymentMethodId;
	}

	public PaymentWrapper withPaymentMethodId(int paymentMethodId) {
		this.paymentMethodId = paymentMethodId;
		return this;
	}

	@Override
	public String toString() {
		return "PaymentWrapper [paymentId=" + paymentId + ", bankAccountId=" + bankAccountId + ", tenderType="
				+ tenderType + ", currencyId=" + currencyId + ", documentTypeId=" + documentTypeId + ", bankId="
				+ bankId + ", businessPartnerId=" + businessPartnerId + ", conversionTypeId=" + conversionTypeId
				+ ", amount=" + amount + "]";
	}
}
