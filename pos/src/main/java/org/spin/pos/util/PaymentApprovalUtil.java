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

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MMailText;
import org.compiere.model.MOrder;
import org.compiere.model.MPayment;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.spin.queue.notification.DefaultNotifier;
import org.spin.queue.notification.model.MADNotificationRecipient;
import org.spin.queue.util.QueueLoader;

/**
 * Added for handle custom values for ADempiere core
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class PaymentApprovalUtil {
	/**	Payment Verified	*/
	public static final String COLUMNNAME_IsPaymentVerified = "IsPaymentVerified";
	/**	Require Payment Verification */
	public static final String COLUMNNAME_IsPaymentVerificationRequired = "IsPaymentVerificationRequired";
	/** Payment not verify, please take a time for verify it */
	public static final String MESSAGE_PaymentNotVerifyByOperator = "PaymentNotVerifyByOperator";
	/**	Payment Method	*/
	public static final String COLUMNNAME_C_PaymentMethod_ID = "C_PaymentMethod_ID";
	/**	Payment Verification Supervisor	*/
	public static final String COLUMNNAME_PayVerificationSupervisor_ID = "PayVerificationSupervisor_ID";
	/**	Payment Verification Mail Template	*/
	public static final String COLUMNNAME_PayVerificationMailText_ID = "PayVerificationMailText_ID";
	

	/**
	 * Set default verification from POS payment term
	 * @param payment
	 * @return void
	 */
	public static void setDefaultVerificationFromPointOfSales(MPayment payment) {
		if(payment.isProcessed()
				|| payment.getC_POS_ID() <= 0
				|| !payment.isReceipt()
				|| payment.getC_Charge_ID() > 0) {
			return;
		}
		//	get payment method allocation
		PO paymentTypeAllocation = new Query(payment.getCtx(), "C_POSPaymentTypeAllocation", "C_POS_ID = ? AND C_PaymentMethod_ID = ? AND IsPaymentVerificationRequired = ?", payment.get_TrxName())
			.setParameters(payment.getC_POS_ID(), payment.get_ValueAsInt(COLUMNNAME_C_PaymentMethod_ID), "Y")
			.setOnlyActiveRecords(true)
			.first();
		if(paymentTypeAllocation != null
				&& paymentTypeAllocation.get_ID() > 0) {
			payment.set_ValueOfColumn(COLUMNNAME_IsPaymentVerificationRequired, true);
		}
	}
	
	/**
	 * Set default verification from POS payment term
	 * @param payment
	 * @return void
	 */
	public static void sendPaymentVerificationNotification(MPayment payment) {
		if(payment.isProcessed()
				|| payment.getC_POS_ID() <= 0
				|| !payment.isReceipt()
				|| payment.getC_Charge_ID() > 0) {
			return;
		}
		//	get payment method allocation
		PO paymentTypeAllocation = new Query(payment.getCtx(), "C_POSPaymentTypeAllocation", "C_POS_ID = ? AND C_PaymentMethod_ID = ? AND IsPaymentVerificationRequired = ?", payment.get_TrxName())
			.setParameters(payment.getC_POS_ID(), payment.get_ValueAsInt(COLUMNNAME_C_PaymentMethod_ID), "Y")
			.setOnlyActiveRecords(true)
			.first();
		if(paymentTypeAllocation != null
				&& paymentTypeAllocation.get_ID() > 0) {
			//	Notify
			if(paymentTypeAllocation.get_ValueAsInt(COLUMNNAME_PayVerificationSupervisor_ID) > 0
					&& paymentTypeAllocation.get_ValueAsInt(COLUMNNAME_PayVerificationMailText_ID) > 0) {
				DefaultNotifier notifier = (DefaultNotifier) QueueLoader.getInstance().getQueueManager(DefaultNotifier.QUEUETYPE_DefaultNotifier)
						.withContext(Env.getCtx())
						.withTransactionName(payment.get_TrxName());
				//	
				MMailText template = new MMailText(payment.getCtx(), paymentTypeAllocation.get_ValueAsInt(COLUMNNAME_PayVerificationMailText_ID), payment.get_TrxName());
				template.setPO(payment);
				template.setPO(paymentTypeAllocation);
				String description = template.getMailHeader();
				String text = template.getMailText(true);
				notifier
					.clearMessage()
					.withUpdateHandler(PaymentVerificationHandler.class.getName())
					.withApplicationType(DefaultNotifier.DefaultNotificationType_UserDefined)
					.withUserId(payment.get_ValueAsInt("CollectingAgent_ID"))
					.withText(text)
					.addRecipient(paymentTypeAllocation.get_ValueAsInt(COLUMNNAME_PayVerificationSupervisor_ID), null, MADNotificationRecipient.MESSAGETYPE_Confirmation)
					.withEntity(payment)
					.withDescription(description)
					.addToQueue();
			}
		}
	}
	
	
	/**
	 * Validate that a order has approval from WF for all related payments
	 * @param order
	 * @return void
	 */
	public static void validateApprovalforPayment(MOrder order) {
		MPayment.getOfOrder(order).forEach(payment -> validateApprovalForPayment(payment));
	}
	
	/**
	 * Validate that a payment has approval from WF
	 * @param payment
	 * @return void
	 */
	public static void validateApprovalForPayment(MPayment payment) {
		if(payment.get_ValueAsBoolean(COLUMNNAME_IsPaymentVerificationRequired)
				&& !payment.get_ValueAsBoolean(COLUMNNAME_IsPaymentVerified)) {
			throw new AdempiereException(Msg.parseTranslation(payment.getCtx(), "@" + MESSAGE_PaymentNotVerifyByOperator + "@"));
		}
	}
}
