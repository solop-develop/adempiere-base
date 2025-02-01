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
package org.compiere.process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.util.TimeUtil;

/**
 *	Copy Order and optionally close
 *	
 *  @author Jorg Janke
 *  @version $Id: CopyOrder.java,v 1.2 2006/07/30 00:51:01 jjanke Exp $
 */
public class CopyOrder extends CopyOrderAbstract {

	/**
	 *  Perform process.
	 *  @return Message (clear text)
	 *  @throws Exception if not successful
	 */
	protected String doIt() throws Exception {
		log.info("C_Order_ID=" + getOrderId() 
			+ ", C_DocType_ID=" + getDocTypeId() 
			+ ", CloseDocument=" + isCloseDocument());
		if (getOrderId() == 0)
			throw new AdempiereException("@C_Order_ID@ @NotFound@");
		MDocType dt = MDocType.get(getCtx(), getDocTypeId());
		if (dt.get_ID() == 0) {
			throw new AdempiereException("@C_Order_ID@ @NotFound@");
		}
		if (getDateDoc() == null) {
			setDateDoc(TimeUtil.getDay(System.currentTimeMillis()));
		}
		//
		MOrder from = new MOrder (getCtx(), getOrderId(), get_TrxName());
		MOrder newOrder = MOrder.copyFrom (from, getDateDoc(), 
			dt.getC_DocType_ID(), dt.isSOTrx(), false, true, get_TrxName());		//	copy ASI
		newOrder.setC_DocTypeTarget_ID(getDocTypeId());
		newOrder.set_ValueOfColumn("SourceQuote_ID", from.getC_Order_ID());
		newOrder.saveEx();
		if (isCloseDocument())  {
			MOrder original = new MOrder (getCtx(), getOrderId(), get_TrxName());
			original.setDocAction(MOrder.DOCACTION_Complete);
			original.processIt(MOrder.DOCACTION_Complete);
			original.saveEx();
			original.setDocAction(MOrder.DOCACTION_Close);
			original.processIt(MOrder.DOCACTION_Close);
			original.saveEx();
		}
		//	Add Reference
		addLog(newOrder.getC_Order_ID(), newOrder.getDateOrdered(), newOrder.getGrandTotal(), null);
		return dt.getName() + ": " + newOrder.getDocumentNo();
	}	//	doIt
}	//	CopyOrder