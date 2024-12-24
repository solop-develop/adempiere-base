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

package org.spin.pos.process;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.MTable;
import org.compiere.model.MUser;
import org.compiere.model.PO;
import org.compiere.util.DisplayType;
import org.compiere.util.Env;
import org.compiere.util.Msg;

/** 
 * 	Generate Refund (From Invoice Customer)
 *  @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 *  @version Release 3.9.3
 */
public class GenerateRefundFromPOS extends GenerateRefundFromPOSAbstract {

	/**	Created	*/
	private AtomicInteger created = new AtomicInteger();
	private StringBuffer generatedDocuments = new StringBuffer();
	private List<MPayment> payments = new ArrayList<MPayment>();
	
	@Override
	protected String doIt() throws Exception {
		MTable table = MTable.get(getCtx(), "C_POSPaymentReference");
		if(table == null) {
			throw new AdempiereException("@C_POSPaymentReference_ID@ @AD_Table_ID@ @NotFound@");
		}
		for(int orderRefundId : getSelectionKeys()) {
			PO orderBankAccount = table.getPO(orderRefundId, get_TrxName());
			createPayment(orderBankAccount, getSelectionAsBigDecimal(orderRefundId, "OPB_ConvertedAmt"));
		}
		processingPayments();
		//	Default Ok
		return "@Created@ " + created + (generatedDocuments.length() > 0? " [" + generatedDocuments + "]": "");
	}
	
	
	/**
	 * Add Document Info for message to return
	 * @param documentInfo
	 */
	private void addToMessage(String documentInfo) {
		if(generatedDocuments.length() > 0) {
			generatedDocuments.append(", ");
		}
		//	
		generatedDocuments.append(documentInfo);
	}
	
	/**
	 * Process Invoices
	 */
	private void processingPayments() {
		payments.forEach(payment -> {
			if(!payment.processIt(MPayment.DOCACTION_Complete)) {
				throw new AdempiereException("@Error@ " + payment.getProcessMsg());
			}
			//	
			payment.saveEx();
			created.incrementAndGet();
			addToMessage(payment.getDocumentNo());
		});
	}
	
	/**
	 * Create Payment to Open refund
	 * @param paymentReference
	 * @param convertedAmount
	 * @return
	 * @return MPayment
	 */
	private MPayment createPayment(PO paymentReference, BigDecimal convertedAmount) {
		int orderId = paymentReference.get_ValueAsInt("C_Order_ID");
		MOrder salesOrder = new MOrder(getCtx(), orderId, get_TrxName());
		List<MInvoice> invoices = Arrays.asList(salesOrder.getInvoices());
		MInvoice invoice = null;
		if(!invoices.isEmpty()) {
			invoice = invoices.stream().findFirst().get();
		}
		MPayment payment = new MPayment(getCtx(), 0, get_TrxName());
		int currencyId = paymentReference.get_ValueAsInt("C_Currency_ID");
		int conversionTypeId = paymentReference.get_ValueAsInt("C_ConversionType_ID");
		int paymentMethodId = paymentReference.get_ValueAsInt("C_PaymentMethod_ID");
		int posId = paymentReference.get_ValueAsInt("C_POS_ID");
		String description = paymentReference.get_ValueAsString("Description");
		String tenderType = paymentReference.get_ValueAsString("TenderType");
		//	
		payment.setC_BPartner_ID(salesOrder.getC_BPartner_ID());
		payment.setC_BankAccount_ID(getBankAccountId());
		payment.setC_ConversionType_ID(conversionTypeId);
		payment.setC_Currency_ID(currencyId);
		payment.setDateTrx(getPayDate());
		payment.setDateAcct(getPayDate());
		if(getDocTypeTargetId() > 0) {
			payment.setC_DocType_ID(getDocTypeTargetId());
		}
		payment.setIsReceipt(false);
		payment.setC_POS_ID(posId);
		if(invoice != null) {
			payment.setC_Invoice_ID(invoice.getC_Invoice_ID());
		} else {
			payment.setC_Order_ID(salesOrder.getC_Order_ID());
		}
		//	Add data for payment amount
		payment.setPayAmt(convertedAmount);
		MUser user = MUser.get(getCtx(), getAD_User_ID());
		String userName = "";
		if(user != null) {
			userName = user.getName();
		}
		//	Set description
		payment.addDescription(description);
		payment.addDescription(Msg.parseTranslation(Env.getCtx(), "@Created@ @from@")
				+ " - " + userName
				+ " - " + DisplayType.getDateFormat(DisplayType.Date).format(getPayDate()));
		//	Tender Type
		payment.set_ValueOfColumn("C_PaymentMethod_ID", paymentMethodId);
		payment.setTenderType(tenderType);
		payment.setDocumentNo(getDocumentNo());
		payment.setCheckNo(getDocumentNo());
		//	Save
		payment.saveEx();
		paymentReference.set_ValueOfColumn("IsPaid", true);
		paymentReference.set_ValueOfColumn("Processed", true);
		paymentReference.saveEx();
		payments.add(payment);
		return payment;
	}
}