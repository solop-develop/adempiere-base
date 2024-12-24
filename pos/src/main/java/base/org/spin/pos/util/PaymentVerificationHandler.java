/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2015 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.pos.util;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MPayment;
import org.compiere.util.Util;
import org.spin.queue.model.MADQueue;
import org.spin.queue.notification.model.MADNotificationQueue;
import org.spin.queue.notification.model.MADNotificationUpdates;
import org.spin.queue.notification.support.IUpdateHandler;

/**
 * @author Yamel Senih, ySenih@erpya.com, ERPCyA http://www.erpya.com
 * Contract for notification response handler, this is called from updates of notification
 */
public class PaymentVerificationHandler implements IUpdateHandler {

	@Override
	public void run(MADNotificationUpdates update) {
		if(Util.isEmpty(update.getNotificationResponseCode())) {
			return;
		}
		MADNotificationQueue notification = (MADNotificationQueue) update.getAD_NotificationQueue();
		MADQueue queue = (MADQueue) notification.getAD_Queue();
		if(queue.getAD_Table_ID() == MPayment.Table_ID
				&& queue.getRecord_ID() > 0) {
			MPayment paymentToVerify = new MPayment(update.getCtx(), queue.getRecord_ID(), update.get_TrxName());
			if(paymentToVerify.get_ValueAsBoolean(PaymentApprovalUtil.COLUMNNAME_IsPaymentVerificationRequired)
					&& !paymentToVerify.get_ValueAsBoolean(PaymentApprovalUtil.COLUMNNAME_IsPaymentVerified)) {
				paymentToVerify.set_ValueOfColumn(PaymentApprovalUtil.COLUMNNAME_IsPaymentVerified, update.getNotificationResponseCode().equals("Y"));
				if(!Util.isEmpty(update.getText())) {
					paymentToVerify.addDescription(update.getText());
				}
				paymentToVerify.saveEx();
			} else {
				throw new AdempiereException("@PaymentAlreadyVerified@");
			}
		}
	}
}
