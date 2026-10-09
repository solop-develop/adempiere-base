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

import org.adempiere.core.domains.models.I_AD_Attachment;
import org.adempiere.core.domains.models.I_AD_AttachmentReference;
import org.adempiere.core.domains.models.I_AD_PrintFormat;
import org.adempiere.core.domains.models.I_C_OrderLine;
import org.adempiere.core.domains.models.I_M_ProductDescription;
import org.adempiere.core.domains.models.X_M_ProductDescription;
import org.adempiere.exceptions.AdempiereException;
import org.adempiere.pdf.IText7Document;
import org.compiere.model.MClientInfo;
import org.compiere.model.MOrder;
import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.model.MQuery;
import org.compiere.model.PrintInfo;
import org.compiere.model.Query;
import org.compiere.print.MPrintFormat;
import org.compiere.print.MPrintFormatItem;
import org.compiere.print.ReportEngine;
import org.compiere.process.ProcessInfo;
import org.compiere.util.Env;
import org.eevolution.services.dsl.ProcessBuilder;
import org.spin.util.AttachmentUtil;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/** Generated Process for (Print Order With Product Descriptions)
 *  Merge in a single PDF: the order document, the customer quote descriptions
 *  of the order products and the PDF attachments of the order products.
 *  @author Gabriel Escalona
 *  @version Release 3.9.4
 */
public class PrintOrderWithProductDescriptions extends PrintOrderWithProductDescriptionsAbstract
{
	/**	Standard Order Print process (Rpt C_Order)	*/
	private static final int ORDER_PRINT_PROCESS_ID = 110;
	/**	Printed columns for a new Product Description print format	*/
	private static final List<String> DESCRIPTION_PRINTED_COLUMNS = List.of(
		I_M_ProductDescription.COLUMNNAME_M_Product_ID,
		I_M_ProductDescription.COLUMNNAME_Description
	);

	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		if (getRecord_ID() <= 0) {
			throw new AdempiereException("@C_Order_ID@ @NotFound@");
		}
		MOrder order = new MOrder(getCtx(), getRecord_ID(), get_TrxName());
		List<Integer> productIds = getOrderProductIds(order.getC_Order_ID());

		List<File> pdfList = new ArrayList<>();
		File orderPdf = printOrder(order.getC_Order_ID());
		if (orderPdf == null) {
			throw new AdempiereException("@NoDocPrintFormat@ @C_Order_ID@ " + order.getDocumentNo());
		}
		pdfList.add(orderPdf);
		File descriptionsPdf = printProductDescriptions(order, productIds);
		if (descriptionsPdf != null) {
			pdfList.add(descriptionsPdf);
		}
		pdfList.addAll(getProductPdfAttachments(productIds));

		File resultPdf;
		try {
			resultPdf = File.createTempFile("Order_" + order.getDocumentNo() + "_", ".pdf");
			IText7Document.mergePdf(pdfList, resultPdf);
		} catch (Exception e) {
			throw new AdempiereException(e.getLocalizedMessage());
		}
		getProcessInfo().setPDFReport(resultPdf);
		getProcessInfo().setReportAsFile(resultPdf);
		return "";
	}

	/**
	 * Distinct products of the order lines, keeping the line order
	 * @param orderId
	 * @return product ids
	 */
	private List<Integer> getOrderProductIds(int orderId) {
		List<MOrderLine> lines = new Query(getCtx(), I_C_OrderLine.Table_Name,
				I_C_OrderLine.COLUMNNAME_C_Order_ID + " = ? AND " + I_C_OrderLine.COLUMNNAME_M_Product_ID + " IS NOT NULL",
				get_TrxName())
			.setParameters(orderId)
			.setOnlyActiveRecords(true)
			.setOrderBy(I_C_OrderLine.COLUMNNAME_Line)
			.list();
		Set<Integer> productIds = new LinkedHashSet<>();
		lines.forEach(line -> productIds.add(line.getM_Product_ID()));
		return new ArrayList<>(productIds);
	}

	/**
	 * Print the order with the document type print format (same as Rpt C_Order)
	 * @param orderId
	 * @return pdf file or null
	 */
	private File printOrder(int orderId) {
		ProcessInfo info = ProcessBuilder.create(getCtx())
			.process(ORDER_PRINT_PROCESS_ID)
			.withRecordId(MOrder.Table_ID, orderId)
			.withoutPrintPreview()
			.withoutBatchMode()
			.withWindowNo(0)
			.withoutTransactionClose()
			.withReportExportFormat("pdf")
			.execute(get_TrxName());
		if (info.isError()) {
			throw new AdempiereException(info.getSummary());
		}
		return Optional.ofNullable(info.getReportAsFile()).orElse(info.getPDFReport());
	}

	/**
	 * Print the customer quote descriptions of the order products
	 * @param order
	 * @param productIds
	 * @return pdf file or null when there are no descriptions
	 */
	private File printProductDescriptions(MOrder order, List<Integer> productIds) {
		if (productIds.isEmpty()) {
			return null;
		}
		String productIdsClause = productIds.stream().map(String::valueOf).collect(Collectors.joining(","));
		String whereClause = I_M_ProductDescription.COLUMNNAME_ProductDescriptionType + " = ?"
			+ " AND " + I_M_ProductDescription.COLUMNNAME_M_Product_ID + " IN (" + productIdsClause + ")";
		boolean hasDescriptions = new Query(getCtx(), I_M_ProductDescription.Table_Name, whereClause, get_TrxName())
			.setParameters(X_M_ProductDescription.PRODUCTDESCRIPTIONTYPE_CustomerQuote)
			.setOnlyActiveRecords(true)
			.setClient_ID()
			.match();
		if (!hasDescriptions) {
			return null;
		}
		MPrintFormat printFormat = getProductDescriptionPrintFormat();
		if (printFormat == null) {
			throw new AdempiereException("@AD_PrintFormat_ID@ @NotFound@ " + I_M_ProductDescription.Table_Name);
		}
		MQuery query = new MQuery(I_M_ProductDescription.Table_Name);
		query.addRestriction(I_M_ProductDescription.COLUMNNAME_ProductDescriptionType, MQuery.EQUAL,
			X_M_ProductDescription.PRODUCTDESCRIPTIONTYPE_CustomerQuote);
		query.addRestriction(I_M_ProductDescription.COLUMNNAME_IsActive, MQuery.EQUAL, "Y");
		query.addRestriction(I_M_ProductDescription.COLUMNNAME_M_Product_ID + " IN (" + productIdsClause + ")");
		PrintInfo printInfo = new PrintInfo(order.getDocumentNo(), MOrder.Table_ID, order.getC_Order_ID(), order.getC_BPartner_ID());
		ReportEngine reportEngine = new ReportEngine(getCtx(), printFormat, query, printInfo, get_TrxName());
		return reportEngine.getPDF();
	}

	/**
	 * Get the Product Description print format of the client (or system), create it when it does not exist
	 * @return print format
	 */
	private MPrintFormat getProductDescriptionPrintFormat() {
		int tableId = I_M_ProductDescription.Table_ID;
		int printFormatId = new Query(getCtx(), I_AD_PrintFormat.Table_Name,
				I_AD_PrintFormat.COLUMNNAME_AD_Table_ID + " = ? AND " + I_AD_PrintFormat.COLUMNNAME_AD_Client_ID + " IN (0, ?)",
				get_TrxName())
			.setParameters(tableId, Env.getAD_Client_ID(getCtx()))
			.setOnlyActiveRecords(true)
			.setOrderBy(I_AD_PrintFormat.COLUMNNAME_AD_Client_ID + " DESC, " + I_AD_PrintFormat.COLUMNNAME_IsDefault + " DESC")
			.firstId();
		if (printFormatId > 0) {
			return MPrintFormat.get(getCtx(), printFormatId, false);
		}
		MPrintFormat printFormat = MPrintFormat.createFromTable(getCtx(), tableId, 0, get_TrxName());
		if (printFormat == null) {
			return null;
		}
		for (MPrintFormatItem item : printFormat.getItems()) {
			boolean isPrinted = DESCRIPTION_PRINTED_COLUMNS.contains(item.getColumnName());
			item.setIsPrinted(isPrinted);
			if (I_M_ProductDescription.COLUMNNAME_M_Product_ID.equals(item.getColumnName())) {
				item.setIsOrderBy(true);
				item.setSortNo(10);
			}
			item.saveEx(get_TrxName());
		}
		return MPrintFormat.get(getCtx(), printFormat.getAD_PrintFormat_ID(), true);
	}

	/**
	 * Download from the file handler (S3) the PDF attachments of the products
	 * @param productIds
	 * @return pdf files
	 */
	private List<File> getProductPdfAttachments(List<Integer> productIds) {
		List<File> files = new ArrayList<>();
		int clientId = Env.getAD_Client_ID(getCtx());
		if (productIds.isEmpty() || !AttachmentUtil.getInstance().isValidForClient(clientId)) {
			return files;
		}
		int fileHandlerId = MClientInfo.get(getCtx(), clientId).getFileHandler_ID();
		String whereClause = I_AD_AttachmentReference.COLUMNNAME_FileHandler_ID + " = ?"
			+ " AND LOWER(" + I_AD_AttachmentReference.COLUMNNAME_FileName + ") LIKE '%.pdf'"
			+ " AND EXISTS (SELECT 1 FROM " + I_AD_Attachment.Table_Name + " a"
			+ " WHERE a." + I_AD_Attachment.COLUMNNAME_AD_Attachment_ID + " = " + I_AD_AttachmentReference.Table_Name + "." + I_AD_AttachmentReference.COLUMNNAME_AD_Attachment_ID
			+ " AND a." + I_AD_Attachment.COLUMNNAME_AD_Table_ID + " = ?"
			+ " AND a." + I_AD_Attachment.COLUMNNAME_Record_ID + " = ?)";
		productIds.forEach(productId -> {
			List<Integer> attachmentReferenceIds = new Query(getCtx(), I_AD_AttachmentReference.Table_Name, whereClause, get_TrxName())
				.setParameters(fileHandlerId, MProduct.Table_ID, productId)
				.setOnlyActiveRecords(true)
				.setClient_ID()
				.setOrderBy(I_AD_AttachmentReference.COLUMNNAME_FileName)
				.getIDsAsList();
			attachmentReferenceIds.forEach(attachmentReferenceId -> {
				File file = downloadPdf(clientId, attachmentReferenceId);
				if (file != null) {
					files.add(file);
				}
			});
		});
		return files;
	}

	/**
	 * Download an attachment reference and validate it is a PDF
	 * @param clientId
	 * @param attachmentReferenceId
	 * @return pdf file or null
	 */
	private File downloadPdf(int clientId, int attachmentReferenceId) {
		try {
			byte[] data = AttachmentUtil.getInstance()
				.clear()
				.withAttachmentReferenceId(attachmentReferenceId)
				.withClientId(clientId)
				.getAttachment();
			if (data == null || data.length < 4
					|| data[0] != '%' || data[1] != 'P' || data[2] != 'D' || data[3] != 'F') {
				log.warning("Attachment is not a PDF: " + attachmentReferenceId);
				return null;
			}
			File file = File.createTempFile("ProductAttachment_" + attachmentReferenceId + "_", ".pdf");
			Files.write(file.toPath(), data);
			return file;
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getLocalizedMessage(), e);
			return null;
		}
	}
}
