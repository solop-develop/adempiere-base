/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import org.adempiere.core.domains.models.I_C_RecognitionSetup;
import org.adempiere.core.domains.models.X_C_RecognitionSetup;
import org.compiere.util.TimeUtil;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;


/**
 *	Revenue Recognition Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MRevenueRecognition.java,v 1.2 2006/07/30 00:51:03 jjanke Exp $
 */
public class MRecognitionSetup extends X_C_RecognitionSetup
{

	/**
	 *
	 */
	private static final long serialVersionUID = -8528224265258285903L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param C_RecognitionSetup_ID id
	 */
	public MRecognitionSetup(Properties ctx, int C_RecognitionSetup_ID, String trxName)
	{
		super (ctx, C_RecognitionSetup_ID, trxName);
	}	//	MRevenueRecognition

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 */
	public MRecognitionSetup(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MRevenueRecognition

	@Override
	protected boolean beforeSave(boolean newRecord) {
		if(getNoMonths() <= 0 && getEndDate() == null) {
			//	Default
			setNoMonths(6);
		}
		if(getStartDate() == null) {
			if(getC_Order_ID() > 0) {
				MOrder order = new MOrder(getCtx(), getC_Order_ID(), get_TrxName());
				setStartDate(order.getDateOrdered());
			} else if(getC_Invoice_ID() > 0) {
				MInvoice invoice = new MInvoice(getCtx(), getC_Invoice_ID(), get_TrxName());
				setStartDate(invoice.getDateInvoiced());
			}
		}
		if(getEndDate() == null) {
			setEndDate(TimeUtil.addMonths(getStartDate(), getNoMonths()));
		} else if(getNoMonths() <= 0) {
			setNoMonths(TimeUtil.getMonthsBetween(getStartDate(), getEndDate()));
		}
		return super.beforeSave(newRecord);
	}

	public static List<MRecognitionSetup> getSetupsFromInvoice(MInvoice invoice) {
		return new Query(invoice.getCtx(), I_C_RecognitionSetup.Table_Name, "C_Invoice_ID = ?", invoice.get_TrxName())
				.setParameters(invoice.getC_Invoice_ID())
				.<MRecognitionSetup>list();
	}

	public static List<MRecognitionSetup> getSetupsFromOrder(MOrder order) {
		return new Query(order.getCtx(), I_C_RecognitionSetup.Table_Name, "C_Order_ID = ?", order.get_TrxName())
				.setParameters(order.getC_Order_ID())
				.<MRecognitionSetup>list();
	}
}	//	MRevenueRecognition
