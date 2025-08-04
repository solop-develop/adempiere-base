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
 * Contributor(s): Carlos Parada www.erpya.com                                *
 *****************************************************************************/
package org.solop.sp032.model;

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicReference;

import org.compiere.model.MOrder;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.solop.sp032.util.CurrencyConvertDocumentsUtil;

/**
 *	@author Carlos Parada, cparada@erpya.com, ERPCyA http://www.erpya.com
 *	Conversion Document
 */
public class MSP032Conversion extends X_SP032_Conversion {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static CCache<String, MSP032Conversion>	doc_cache	= new CCache<String, MSP032Conversion>(MSP032Conversion.Table_Name, 20, 2);	//	2 minutes
	private PO currentDocument = null;
	
	/**
	 * Constructor
	 * @param ctx
	 * @param rs
	 * @param trxName
	 */
	public MSP032Conversion(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	/**
	 * Constructor
	 * @param ctx
	 * @param SP032_Conversion_ID
	 * @param trxName
	 */
	public MSP032Conversion(Properties ctx, int SP032_Conversion_ID, String trxName) {
		super(ctx, SP032_Conversion_ID, trxName);
	}

	/**
	 * Constructor
	 * @param document
	 */
	public MSP032Conversion(PO document) {
		super(document.getCtx(), 0, document.get_TrxName());
		set_ValueOfColumn(document.get_TableName().concat("_ID"), document.get_ID());
		setC_Currency_ID(document.get_ValueAsInt(MSP032Conversion.COLUMNNAME_C_Currency_ID));
		setC_ConversionType_ID(document.get_ValueAsInt(MSP032Conversion.COLUMNNAME_C_ConversionType_ID));
		setSP032_IsGeneratedCurrencyType(true);
		setSP032_NegotiatedRate(Env.ZERO);
		setDateDoc(Optional.ofNullable((Timestamp)document.get_Value(CurrencyConvertDocumentsUtil.COLUMNNAME_DateAcct))
						   .orElse((Timestamp)document.get_Value(COLUMNNAME_DateDoc)));
		setSP032_NegotiatedDate(getDateDoc());
		currentDocument = document;
		save();
	}
	
	/**
	 * Set PO Document 
	 * @param document
	 */
	public void setDocument (PO document) {
		currentDocument = document;
	}
	
	/**
	 * Get PO Document
	 * @return
	 */
	private PO getDocument() {
		if (currentDocument==null) {
			if (getC_Payment_ID() > 0)
				currentDocument = (PO) getC_Payment();
			if (getC_Invoice_ID() > 0)
				currentDocument = (PO) getC_Invoice();
			if (getC_Order_ID() > 0)
				currentDocument = (PO) getC_Order();
		}
		return currentDocument;
	}
	
	/**
	 * Get PO Document
	 * @param document
	 * @return
	 */
	public static MSP032Conversion get(PO document) {
		AtomicReference<MSP032Conversion> result = new AtomicReference<>();
		Optional<PO> maybeDocument = Optional.ofNullable(document);
		maybeDocument.ifPresent(doc ->{
			String key = getKey(doc);
			result.set(doc_cache.get(key));
			
			if (result.get()==null) {
				String whereClause = doc.get_TableName() + "_ID = ?";
				Optional<MSP032Conversion> maybeConversion = Optional.ofNullable(new Query(doc.getCtx(),
																							  MSP032Conversion.Table_Name ,
																							  whereClause, 
																							  doc.get_TrxName())
																						.setParameters(doc.get_ID())
																						.first());
				maybeConversion.ifPresent(conversion ->{
					conversion.setDocument(document);
				});
				
				result.set(maybeConversion.orElseGet(() -> new MSP032Conversion(doc)));
			}
		});
		return result.get();
	}
	
	/**
	 * Delete PO Document
	 * @param document
	 */
	public static void delete(PO document) {
		Optional<PO> maybeDocument = Optional.ofNullable(document);
		maybeDocument.ifPresent(doc ->{
			String key = getKey(doc);
			MSP032Conversion conversionDocument = MSP032Conversion.get(document);
			conversionDocument.delete(true);
			doc_cache.remove(key);
		});
	}
	
	/**
	 * Get key for cache
	 * @param document
	 * @return
	 */
	public static String getKey(PO document) {
		return	document.get_TableName().concat("_").concat(String.valueOf(document.get_ID()));
	}
	
	public String getValueOfDocumentAsString(String columnName) {
		Optional<Object> maybeValue = Optional.ofNullable(getValueOfDocument(columnName));
		if (maybeValue.isPresent())
			return maybeValue.get().toString();
		return "";
	}
	
	/**
	 * Get Integer Value of Document
	 * @param columnName
	 * @return
	 */
	public Integer getValueOfDocumentAsInt(String columnName) {
		Optional<Object> maybeValue = Optional.ofNullable(getValueOfDocument(columnName));
		if (maybeValue.isPresent())
			return (Integer) maybeValue.get();
		return 0;
	}
	
	/**
	 * Get Object Value of Document
	 * @param columnName
	 * @return
	 */
	public Object getValueOfDocument(String columnName) {
		Optional<PO> maybePO = Optional.ofNullable(getDocument());
		AtomicReference<Object> result = new AtomicReference<>(null);
		maybePO.ifPresent(po ->{
			result.set(po.get_Value(columnName));
		});
		return result.get();
	}
	
	/**
	 * Get String Value of Document
	 * @return
	 */
	public String setC_ConversionTypeDocument() {
		Optional<PO> maybePO = Optional.ofNullable(getDocument());
		AtomicReference<String> result = new AtomicReference<>(null);
		maybePO.ifPresent(po ->{
			po.set_ValueOfColumn(MSP032Conversion.COLUMNNAME_C_ConversionType_ID, getC_ConversionType_ID());
			if (!po.save())
				result.set("@SaveError@ @DocumentNo@ " + po.get_ValueAsString(MOrder.COLUMNNAME_DocumentNo));
		});
		
		return result.get();
	}
}//MConversion_Document
