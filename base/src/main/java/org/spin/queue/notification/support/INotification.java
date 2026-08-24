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
package org.spin.queue.notification.support;

import org.spin.queue.notification.model.MADNotificationQueue;
import org.spin.queue.notification.model.MADNotificationRecipient;
import org.spin.util.support.IAppSupport;

import java.util.List;

/**
 * @author Yamel Senih, ySenih@erpya.com, ERPCyA http://www.erpya.com
 * Contract for notification
 */
public interface INotification extends IAppSupport {

	/**
	 * Send notification method from queue
	 * @param queue
	 */
	public void sendNotification(MADNotificationQueue queue);

	/**
	 * Send the notification only to the given recipients (already selected for this channel
	 * by the caller). Each recipient successfully sent must be marked processed; failures are
	 * recorded per recipient. Used by the User Defined dispatch so a recipient with several
	 * channels does not clash. The default keeps backward compatibility by sending to every
	 * recipient of the queue.
	 * @param queue
	 * @param recipients recipients to notify through this channel
	 */
	default void sendNotification(MADNotificationQueue queue, List<MADNotificationRecipient> recipients) {
		sendNotification(queue);
	}
}
