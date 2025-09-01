/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.                                     *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net                                                  *
 * or https://github.com/adempiere/adempiere/blob/develop/license.html        *
 *****************************************************************************/

package org.solop.process;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.pdf.IText7Document;
import org.compiere.model.MInvoice;
import org.compiere.model.Query;
import org.compiere.print.ReportEngine;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

/** Generated Process for (Print OutBound Invoices)
 *  @author Gabriel Escalona
 *  @version Release 3.9.4
 */
public class Print_OutBoundInvoices extends Print_OutBoundInvoicesAbstract
{
	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		List<File> pdfList = new ArrayList<>();
		List<Integer> invoiceIds = new ArrayList<>();
		if (getRecord_ID() > 0) {
			String whereClause = "EXISTS (SELECT 1 FROM WM_InOutBoundLine l WHERE l.WM_InOutBound_ID = ? AND l.C_Invoice_ID = C_Invoice.C_Invoice_ID)";
			invoiceIds = new Query(getCtx(), MInvoice.Table_Name, whereClause, get_TrxName())
					.setParameters(getRecord_ID())
					.setOrderBy("DateInvoiced, DocumentNo")
					.getIDsAsList();
		} else if(isSelection()) {
			invoiceIds = getSelectionKeys();
		}
		if(invoiceIds != null) {
			invoiceIds.forEach(invoiceId -> {
				File pdf = printInvoice(invoiceId);
				if (pdf != null) {
					pdfList.add(pdf);
				}
			});
		}
		File resultPdf = null;
		try {
			resultPdf = File.createTempFile("Invoices", ".pdf");
			IText7Document.mergePdf(pdfList, resultPdf);
		} catch (Exception e) {
			throw new AdempiereException(e.getLocalizedMessage());
		}
		getProcessInfo().setPDFReport(resultPdf);
		getProcessInfo().setReportAsFile(resultPdf);
		return "";
	}

	private File printInvoice(int invoiceId) {
		File pdf = null;
		ReportEngine reportEngine = ReportEngine.get(
				getCtx(),
				ReportEngine.INVOICE,
				invoiceId,
				get_TrxName()
		);

		try {
			pdf = File.createTempFile("Invoice", ".pdf");
			reportEngine.createPDF(pdf);
		}
		catch (Exception e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
		}
		return pdf;
	}
}