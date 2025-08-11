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

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.core.domains.models.I_C_Invoice;
import org.compiere.model.MInvoice;
import com.solop.sp016.util.ConsignedMaterialUtil;

/**
 * Generated Process for (Create AP Invoice From Sales)
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class RecalculateRateForInvoiceLines extends RecalculateRateForInvoiceLinesAbstract {
	@Override
	protected void prepare() {
		super.prepare();
		//	Validate Record ID and processed
		if(getRecord_ID() <= 0) {
			throw new AdempiereException("@C_Invoice_ID@ @NotFound@");
		}
		if(getTable_ID() != I_C_Invoice.Table_ID) {
			throw new AdempiereException("@C_Invoice_ID@ @IsMandatory@");
		}
	}

	@Override
	protected String doIt() throws Exception {
		MInvoice invoice = new MInvoice(getCtx(), getRecord_ID(), get_TrxName());
		AtomicInteger counter = new AtomicInteger();
		Arrays.asList(invoice.getLines(true)).forEach(invoiceLine -> {
			ConsignedMaterialUtil.recalculateInvoiceLineRate(invoiceLine);
			invoiceLine.saveEx();
			counter.incrementAndGet();
		});
		return "@Processed@: " + counter;
	}
}