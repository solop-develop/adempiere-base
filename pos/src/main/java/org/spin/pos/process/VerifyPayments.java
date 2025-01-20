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

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

import org.compiere.model.MPayment;
import org.compiere.util.Util;
import org.spin.pos.util.PaymentApprovalUtil;

/** Generated Process for (Verify Payments to Approve)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.3
 */
public class VerifyPayments extends VerifyPaymentsAbstract {
	@Override
	protected void prepare() {
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception {
		AtomicInteger verifiedcounter = new AtomicInteger();
		if(Optional.ofNullable(getIsPaymentVerified()).orElse("N").equals("Y")) {
			getSelectionKeys()
			.stream()
			.map(paymentId -> new MPayment(getCtx(), paymentId, get_TrxName()))
			.forEach(payment -> {
				String documentNo = getSelectionAsString(payment.getC_Payment_ID(), "P_DocumentNo");
				String description = getSelectionAsString(payment.getC_Payment_ID(), "P_Description");
				if(!Util.isEmpty(documentNo)) {
					payment.setDocumentNo(documentNo);
				}
				if(!Util.isEmpty(description)) {
					payment.setDescription(description);
				}
				payment.set_ValueOfColumn(PaymentApprovalUtil.COLUMNNAME_IsPaymentVerified, true);
				if(!Util.isEmpty(getDescription())) {
					payment.addDescription(getDescription());
				}
				payment.saveEx();
				verifiedcounter.incrementAndGet();
			});
		}
		return "@Updated@: " + verifiedcounter.get();
	}
}