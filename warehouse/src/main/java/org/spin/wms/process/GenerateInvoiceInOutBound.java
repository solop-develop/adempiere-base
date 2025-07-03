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

package org.spin.wms.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MInvoiceLine;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MUOMConversion;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Trx;
import org.eevolution.wms.model.MWMInOutBound;
import org.eevolution.wms.model.MWMInOutBoundLine;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Class for generate invoices from outbound orders
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 * @version Release 3.9.3
 * See: https://github.com/adempiere/adempiere/issues/2730
 */
public class GenerateInvoiceInOutBound extends GenerateInvoiceInOutBoundAbstract {
	private Hashtable<String, MInvoice> invoices;
	private HashMap<Integer, Integer> numberOfLines;
	private HashMap<Integer, Integer> numberOfInvoices;
	private HashMap<String, List<MWMInOutBoundLine>> groupedOutBoundLines;
	List<PO> invoicesToPrint;
	private int created = 0;
	private int withError = 0;
	private StringBuffer generatedDocuments = new StringBuffer();
	private int maxLines = 0;

	@Override
	protected String doIt() throws Exception {
		invoices  = new Hashtable<String, MInvoice>();
		numberOfLines = new HashMap<>();
		numberOfInvoices = new HashMap<>();
		groupedOutBoundLines = new HashMap<>();
		invoicesToPrint = new ArrayList<PO>();
		List<MWMInOutBoundLine> outBoundLines = null;
		//	Get from record
		if(getRecord_ID() > 0) {
			outBoundLines = new Query(getCtx(), MWMInOutBoundLine.Table_Name, MWMInOutBound.COLUMNNAME_WM_InOutBound_ID + "=?", get_TrxName())
					.setParameters(getRecord_ID())
					.setOrderBy(MWMInOutBoundLine.COLUMNNAME_C_Order_ID + ", " + MWMInOutBoundLine.COLUMNNAME_DD_Order_ID)
					.list();
		} else if(isSelection()) {
			// Overwrite table RV_WM_InOutBoundLine by WM_InOutBoundLine
			getProcessInfo().setTableSelectionId(MWMInOutBoundLine.Table_ID);
			outBoundLines = (List<MWMInOutBoundLine>) getInstancesForSelection(get_TrxName());
		}
		//	Create
		if(outBoundLines != null) {
			if (getDocTypeTargetId() > 0) {
				MDocType docType = MDocType.get(getCtx(), getDocTypeTargetId());
				maxLines = docType.get_ValueAsInt("MaxLinesPerDocument");
			}
			outBoundLines.stream()
					.filter(outBoundLine -> outBoundLine.getC_Invoice_ID() <= 0)
					.forEach(outBoundLine -> groupOutBoundLine(outBoundLine));
			//.forEach(outBoundLine -> createInvoice(outBoundLine));
			createAndProcessInvoices();
			printDocument(invoicesToPrint, true);
		}

		//
		//processingInvoices();
		//
		return "@Created@ " + created + (generatedDocuments.length() > 0? " [" + generatedDocuments + "]": "") +  (withError > 0 ? " | @Error@ " + withError : "");
	}

	private void createAndProcessInvoices() {
		groupedOutBoundLines.entrySet().stream().filter(entry -> entry != null).forEach(entry -> {
			try {
				Trx.run(transactionName -> {
					List<MWMInOutBoundLine> lines = entry.getValue();
					AtomicReference<MInvoice> maybeInvoice = new AtomicReference<>();
					lines.forEach(outboundLine -> {
						MOrderLine orderLine = outboundLine.getOrderLine();
						MInvoice invoice = maybeInvoice.get();
						if (invoice == null) {
							MOrder order = orderLine.getParent();
							invoice = new MInvoice(order, 0, getDateInvoiced());
							if(getDocTypeTargetId() > 0) {
								invoice.setC_DocType_ID(getDocTypeTargetId());
							}
							invoice.setIsSOTrx(true);
							invoice.saveEx(transactionName);
							maybeInvoice.set(invoice);
						}
						BigDecimal qtyInvoiced = outboundLine.getPickedQty();
						MInvoiceLine invoiceLine = new MInvoiceLine(outboundLine.getCtx(), 0 , transactionName);
						invoiceLine.setOrderLine(orderLine);
						// Set Shipment Line
						if (outboundLine.getM_InOutLine_ID() > 0) {
							invoiceLine.setM_InOutLine_ID(outboundLine.getM_InOutLine_ID());
						}
						invoiceLine.setC_Invoice_ID(invoice.get_ID());
						invoiceLine.setC_UOM_ID(outboundLine.getC_UOM_ID());
						invoiceLine.setPrice(MUOMConversion.convertProductTo(getCtx(), outboundLine.getM_Product_ID(), outboundLine.getC_UOM_ID(), orderLine.getPriceActual()));
						invoiceLine.setQtyEntered(qtyInvoiced);
						invoiceLine.setQtyInvoiced(qtyInvoiced);
						invoiceLine.setWM_InOutBoundLine_ID(outboundLine.get_ID());
						invoiceLine.saveEx();
					});
					MInvoice invoice = maybeInvoice.get();
					invoice.setDocAction(getDocAction());
					if (!invoice.processIt(getDocAction())) {
						addLog("@ProcessFailed@ : " + invoice.getDocumentInfo());
						log.warning("@ProcessFailed@ :" + invoice.getDocumentInfo());
					}
					invoice.saveEx();
					created++;
					addToMessage(invoice.getDocumentNo());
					invoicesToPrint.add(invoice);

				});
			} catch (Exception e) {
				withError += entry.getValue().size();
			}
		});

	}

	private void groupOutBoundLine (MWMInOutBoundLine line) {
		if (line.getC_OrderLine_ID() <= 0) {
			return;
		}

		MOrderLine orderLine = line.getOrderLine();
		if (orderLine.getQtyOrdered().subtract(orderLine.getQtyInvoiced()).subtract(line.getPickedQty()).signum() < 0 && !getParameterAsBoolean("IsIncludeNotAvailable")) {
			return;
		}
		int key = orderLine.getC_Order_ID();
		if(isConsolidateDocument()) {
			key = orderLine.getC_BPartner_ID();
		}
		int keyNumber = numberOfInvoices.getOrDefault(key, 0);
		String keyString = String.valueOf(key) + keyNumber;
		List<MWMInOutBoundLine> lines = groupedOutBoundLines.getOrDefault(keyString, new ArrayList<>());


		if (getDocTypeTargetId() <= 0) {
			MDocType orderDocType = MDocType.get(getCtx(), orderLine.getParent().getC_DocType_ID());
			int invoiceDocTypeId = 0;
			if (orderDocType != null) {
				invoiceDocTypeId = orderDocType.getC_DocTypeInvoice_ID();
				if (invoiceDocTypeId <= 0) {
					throw new AdempiereException("@NotFound@ @C_DocTypeInvoice_ID@ - @C_DocType_ID@:" + orderDocType.get_Translation("Name"));
				}
			}
			MDocType invoiceDocType = MDocType.get(getCtx(),invoiceDocTypeId);
			maxLines = invoiceDocType.get_ValueAsInt("MaxLinesPerDocument");
		}
		if (lines.isEmpty()) {
			keyNumber++;
			keyString = String.valueOf(key) + keyNumber;
			groupedOutBoundLines.put(keyString, lines);
			numberOfInvoices.put(key, keyNumber);
			lines.add(line);
			return;
		}
		if (maxLines > 0 && lines.size() >= maxLines) {
			keyNumber++;
			keyString = String.valueOf(key) + keyNumber;
			lines = new ArrayList<>();
			groupedOutBoundLines.put(keyString, lines);
			numberOfInvoices.put(key, keyNumber);

		}
		lines.add(line);
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
}