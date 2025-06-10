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
package org.solop.sp032.model.validator;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import org.adempiere.model.ImportValidator;
import org.adempiere.process.ImportProcess;
// import org.compiere.asset.process.ImportFixedAsset;
import org.adempiere.core.domains.models.I_A_Asset_Addition;
import org.adempiere.core.domains.models.I_I_FixedAsset;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MClient;
import org.compiere.model.MColumn;
import org.compiere.model.MConversionRate;
import org.compiere.model.MConversionType;
import org.compiere.model.MDocType;
import org.compiere.model.MInvoice;
import org.compiere.model.MOrder;
import org.compiere.model.MPaySelectionLine;
import org.compiere.model.MPayment;
import org.compiere.model.ModelValidationEngine;
import org.compiere.model.ModelValidator;
import org.compiere.model.PO;
import org.compiere.model.Query;
// import org.adempiere.core.domains.models.X_A_Asset_Addition;
// import org.adempiere.core.domains.models.X_I_FixedAsset;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.solop.sp032.model.MSP032Conversion;
import org.solop.sp032.util.CurrencyConvertDocumentsUtil;

/**
 * 	Add default model validator for Generated Automatic Currency Types
 * 	@author Carlos Parada, cparada@erpya.com, ERPCyA http://www.erpya.com
 */
public class CurrencyConvertDocuments implements ModelValidator, ImportValidator {

	/**
	 * Constructor
	 */
	public CurrencyConvertDocuments() {
		super();
	}

	/** Logger */
	private static CLogger log = CLogger
			.getCLogger(CurrencyConvertDocuments.class);
	/** Client */
	private int clientId = -1;

	@Override
	public void initialize(ModelValidationEngine engine, MClient client) {
		// client = null for global validator
		if (client != null) {
			clientId = client.getAD_Client_ID();
			log.info(client.toString());
		} else {
			log.info("Initializing global validator: " + this.toString());
		}
		engine.addDocValidate(MOrder.Table_Name, this);
		engine.addDocValidate(MInvoice.Table_Name, this);
		engine.addDocValidate(MPayment.Table_Name, this);
		engine.addDocValidate(I_A_Asset_Addition.Table_Name, this);
		engine.addDocValidate(CurrencyConvertDocumentsUtil.Expedient_TableName, this);
		
		engine.addModelChange(MOrder.Table_Name, this);
		engine.addModelChange(MInvoice.Table_Name, this);
		engine.addModelChange(MPayment.Table_Name, this);
		engine.addModelChange(I_A_Asset_Addition.Table_Name, this);
		engine.addModelChange(CurrencyConvertDocumentsUtil.Expedient_TableName, this);
		engine.addModelChange(MPaySelectionLine.Table_Name, this);
		
		engine.addImportValidate(I_I_FixedAsset.Table_Name, this);
	}

	@Override
	public int getAD_Client_ID() {
		return clientId;
	}

	@Override
	public String login(int AD_Org_ID, int AD_Role_ID, int AD_User_ID) {
		log.info("AD_User_ID=" + AD_User_ID);
		return null;
	}

	@Override
	public String docValidate(PO po1, int timing) {
		AtomicReference<String> result = new AtomicReference<String>(null);
		Optional<PO> maybeDocument = Optional.ofNullable(po1);
		maybeDocument.ifPresent(document -> {
			if (timing == TIMING_BEFORE_COMPLETE) {
				if ((document.get_TableName().equals(MOrder.Table_Name)
						|| document.get_TableName().equals(MInvoice.Table_Name)
							|| document.get_TableName().equals(MPayment.Table_Name)
								|| document.get_TableName().equals(I_A_Asset_Addition.Table_Name)
									|| document.get_TableName().equals(CurrencyConvertDocumentsUtil.Expedient_TableName))
									&& isSP032_GeneratedCurrencyType(document)) {
					MSP032Conversion conversionDocument = MSP032Conversion.get(document);
					Arrays.stream(MAcctSchema.getClientAcctSchema(document.getCtx(),document.getAD_Client_ID()))
						  .filter(schema -> schema.getC_Currency_ID() != conversionDocument.getC_Currency_ID())
						  .forEach(schema ->{
							  MConversionType conversionType =  Optional.ofNullable(new Query(document.getCtx(), 
									  														MConversionType.Table_Name, 
									  														"C_ConversionType_ID = ? ", 
									  														document.get_TrxName())
									  														.setParameters(conversionDocument.getC_ConversionType_ID())
									  														.<MConversionType>first()
									  												)
									  									.orElse(new MConversionType(document.getCtx(), MConversionType.getDefault(document.getAD_Client_ID()), document.get_TrxName()));
							  if (conversionType.get_ValueAsInt(CurrencyConvertDocumentsUtil.COLUMNNAME_SP032_ParentCType_ID) == 0)
								  result.set(createConversionType(conversionType, conversionDocument, schema.getC_Currency_ID()));		  
						  });
				}
			}
		});
		
		return result.get();
	}

	@Override
	public String modelChange(PO po, int type) throws Exception {
		AtomicReference<String> result = new AtomicReference<String>(null);
		Optional<PO> maybeDocument = Optional.ofNullable(po);
		maybeDocument.ifPresent(document -> {
			if (document.get_TableName().equals(MOrder.Table_Name)
					|| document.get_TableName().equals(MInvoice.Table_Name)
						|| document.get_TableName().equals(MPayment.Table_Name)
							|| document.get_TableName().equals(I_A_Asset_Addition.Table_Name)
								|| document.get_TableName().equals(CurrencyConvertDocumentsUtil.Expedient_TableName)){
				if (type == TYPE_BEFORE_CHANGE
						|| type == TYPE_AFTER_NEW) {				
					if (isSP032_GeneratedCurrencyType(document)) {
						MSP032Conversion conversionDocument = MSP032Conversion.get(document);
						if (document.is_ValueChanged(CurrencyConvertDocumentsUtil.getDocumentDateColumnName(document.get_TableName()))) {
							Timestamp newDate = (Timestamp) document.get_Value(CurrencyConvertDocumentsUtil.getDocumentDateColumnName(document.get_TableName()));
							if (conversionDocument.getDateDoc().equals(conversionDocument.getSP032_NegotiatedDate()))
								conversionDocument.setSP032_NegotiatedDate(newDate);
							
							conversionDocument.setDateDoc(newDate);
						}
						
						if (document.is_ValueChanged(MSP032Conversion.COLUMNNAME_C_ConversionType_ID))
							conversionDocument.setC_ConversionType_ID(document.get_ValueAsInt(MSP032Conversion.COLUMNNAME_C_ConversionType_ID));
						
						if (document.is_ValueChanged(MSP032Conversion.COLUMNNAME_C_Currency_ID))
							conversionDocument.setC_Currency_ID(document.get_ValueAsInt(MSP032Conversion.COLUMNNAME_C_Currency_ID));
						
						if (document.is_ValueChanged(MSP032Conversion.COLUMNNAME_AD_Org_ID))
							conversionDocument.setAD_Org_ID(document.getAD_Org_ID());
						
						if (document.is_ValueChanged(MSP032Conversion.COLUMNNAME_Processed))
							conversionDocument.setProcessed(document.get_ValueAsBoolean(MSP032Conversion.COLUMNNAME_Processed));
						
						if (conversionDocument.is_Changed())
							conversionDocument.save();
					}
				}else if (type == TYPE_BEFORE_DELETE) 
					MSP032Conversion.delete(document);
			}
			
			if (document.get_TableName().equals(MPayment.Table_Name)) {
				if (type == TYPE_BEFORE_CHANGE
						|| type == TYPE_BEFORE_NEW) {
					MPayment payment = (MPayment) document;
					if (payment.getC_ConversionType_ID() == 0) {
						if (payment.getC_Order_ID() > 0)
							payment.setC_ConversionType_ID(payment.getC_Order().getC_ConversionType_ID());
						if (payment.getC_Invoice_ID() > 0)
							payment.setC_ConversionType_ID(payment.getC_Invoice().getC_ConversionType_ID());
					}
				}
			}
			
			if (document.get_TableName().equals(MPaySelectionLine.Table_Name)) {
				if (type == TYPE_BEFORE_CHANGE
						|| type == TYPE_BEFORE_NEW) {
					MPaySelectionLine paySelectLine = (MPaySelectionLine) document;
						if ((type == TYPE_BEFORE_CHANGE 
								&& paySelectLine.is_ValueChanged(MPaySelectionLine.COLUMNNAME_C_ConversionType_ID))
									|| type == TYPE_BEFORE_NEW) {
							if (paySelectLine.getC_Invoice_ID() > 0 )
								paySelectLine.setC_ConversionType_ID(paySelectLine.getInvoice().getC_ConversionType_ID());
							if (paySelectLine.getC_Order_ID() > 0 )
								paySelectLine.setC_ConversionType_ID(paySelectLine.getOrder().getC_ConversionType_ID());
						}
					if (type == TYPE_BEFORE_CHANGE 
							&& paySelectLine.is_ValueChanged(MPaySelectionLine.COLUMNNAME_DifferenceAmt)){
						if(paySelectLine.getC_Invoice_ID() != 0
								|| paySelectLine.getC_Order_ID() != 0
								|| paySelectLine.getHR_Movement_ID() != 0) {
							BigDecimal difference = paySelectLine.getOpenAmt().subtract(paySelectLine.getPayAmt()).subtract(paySelectLine.getDiscountAmt());
							paySelectLine.setDifferenceAmt(difference);
						}
					}
				}
			}
		});
		
		return result.get();
	}
	
	/**
	 * Create Conversion Type Automatically
	 * @param p_ConversionTypeBase
	 * @param conversionDocument
	 * @param p_SchemaCurrency_ID
	 * @return
	 */
	private String createConversionType(MConversionType p_ConversionTypeBase, MSP032Conversion conversionDocument, int p_SchemaCurrency_ID) {
		AtomicReference<String> result = new AtomicReference<String>(null);
		Optional.ofNullable(p_ConversionTypeBase).ifPresent(conversionTypeBase ->{
			Optional.ofNullable(conversionDocument).ifPresent(document ->{
				Trx conversionTypeTrx = null;
				try {
					conversionTypeTrx = Trx.get("#"+ conversionDocument.get_ID(), true); 
					final String trxName = conversionTypeTrx.getTrxName();
					MConversionType conversionType = new MConversionType(document.getCtx(), 0, trxName);
					PO.copyValues(conversionTypeBase, conversionType);
					conversionType.setAD_Org_ID(0);
					setDocumentToConversionType(conversionType, conversionTypeBase, document);
					conversionType.setIsDefault(false);
					if (!conversionType.save())
						result.set("@SaveError@ @C_ConversionType_ID@ " + conversionType.getName());
					BigDecimal negotiatedRate = Optional.ofNullable(conversionDocument.getSP032_NegotiatedRate()).orElse(Env.ZERO);
					if (negotiatedRate.compareTo(Env.ZERO) == 0) { 
						new Query(document.getCtx(), 
									MConversionRate.Table_Name, 
									" (? BETWEEN ValidFrom AND ValidTo) AND C_ConversionType_ID = ? ", 
									document.get_TrxName())
								.setParameters(conversionDocument.getSP032_NegotiatedDate(), conversionTypeBase.getC_ConversionType_ID())
								.<MConversionRate>list()
								.forEach(conversionRateBase ->{
									MConversionRate conversionRate = new MConversionRate(document.getCtx(), 0, trxName);
									PO.copyValues(conversionRateBase, conversionRate);
									conversionRate.setC_ConversionType_ID(conversionType.get_ID());
									conversionRate.setAD_Org_ID(0);
									conversionRate.setValidFrom(conversionDocument.getDateDoc());
									conversionRate.setValidTo(TimeUtil.addYears(conversionDocument.getDateDoc(),CurrencyConvertDocumentsUtil.TIME_Interval));
									if (!conversionRate.save())
										result.set("@SaveError@ @C_ConversionRate_ID@ ");
								});	
					}else {
						MConversionRate conversionRateFrom = new MConversionRate(conversionType, 
																					conversionType.get_ID(), 
																					conversionDocument.getC_Currency_ID(), 
																					p_SchemaCurrency_ID, 
																					negotiatedRate, 
																					conversionDocument.getDateDoc());
						conversionRateFrom.setValidTo(TimeUtil.addYears(conversionDocument.getDateDoc(),CurrencyConvertDocumentsUtil.TIME_Interval));
						conversionRateFrom.setAD_Org_ID(0);
						if (!conversionRateFrom.save())
							result.set("@SaveError@ @C_ConversionRate_ID@ ");
						
						MConversionRate conversionRateTo = new MConversionRate(conversionDocument.getCtx(), 0, trxName);
						MConversionRate.copyValues(conversionRateFrom, conversionRateTo);
						conversionRateTo.setAD_Org_ID(0);
						conversionRateTo.setC_ConversionType_ID(conversionType.get_ID());
						conversionRateTo.setC_Currency_ID(p_SchemaCurrency_ID);
						conversionRateTo.setC_Currency_ID_To(conversionDocument.getC_Currency_ID());
						conversionRateTo.setDivideRate(negotiatedRate);
						
						if (!conversionRateTo.save())
							result.set("@SaveError@ @C_ConversionRate_ID@ ");
					}
					
					document.setC_ConversionType_ID(conversionType.get_ID());
					document.save();
					conversionTypeTrx.commit(true);
					
					result.set(document.setC_ConversionTypeDocument());
					
				} catch (SQLException e) {
					result.set(e.getMessage());
				}finally{
					Optional.ofNullable(conversionTypeTrx).ifPresent(connection ->{
						connection.close();
						connection= null;
					});
					
				}
				
			
			});
		});
		return result.get();
	}
	
	/**
	 * Set Document Info on Conversion Type Generated
	 * @param conversionType
	 * @param parentConversionType
	 * @param document
	 */
	private void setDocumentToConversionType(MConversionType conversionType, MConversionType parentConversionType, MSP032Conversion document) {
		if (document.getC_Order_ID() > 0)
			conversionType.set_ValueOfColumn(MOrder.COLUMNNAME_C_Order_ID, document.getC_Order_ID());
		if (document.getC_Invoice_ID() > 0)
			conversionType.set_ValueOfColumn(MInvoice.COLUMNNAME_C_Invoice_ID, document.getC_Invoice_ID());
		if (document.getC_Payment_ID() > 0)
			conversionType.set_ValueOfColumn(MPayment.COLUMNNAME_C_Payment_ID, document.getC_Payment_ID());
		if (document.getA_Asset_Addition_ID() > 0)
			conversionType.set_ValueOfColumn(I_A_Asset_Addition.COLUMNNAME_A_Asset_Addition_ID, document.getA_Asset_Addition_ID());
		if (document.get_ValueAsInt(CurrencyConvertDocumentsUtil.ColumnName_SP009_Expedient_ID) > 0)
			conversionType.set_ValueOfColumn(CurrencyConvertDocumentsUtil.ColumnName_SP009_Expedient_ID, document.get_ValueAsInt(CurrencyConvertDocumentsUtil.ColumnName_SP009_Expedient_ID));
		
		
		MColumn columnValue = MColumn.get(document.getCtx(),MColumn.getColumn_ID(MConversionType.Table_Name, MConversionType.COLUMNNAME_Value));
		MColumn columnName = MColumn.get(document.getCtx(),MColumn.getColumn_ID(MConversionType.Table_Name, MConversionType.COLUMNNAME_Name));
		
		if (document.getValueOfDocumentAsInt(MOrder.COLUMNNAME_C_BPartner_ID) > 0)
			conversionType.set_ValueOfColumn(MOrder.COLUMNNAME_C_BPartner_ID, document.getValueOfDocumentAsInt(MOrder.COLUMNNAME_C_BPartner_ID));
		conversionType.setValue(conversionType.getValue()
				 .concat(" - ")
				 .concat(document.getValueOfDocumentAsString(MOrder.COLUMNNAME_DocumentNo))
				 );
		Integer fieldLength = columnValue.getFieldLength();
		conversionType.setValue(String.format("%-".concat(fieldLength.toString()).concat(".")
												  .concat(fieldLength.toString()).concat("s"),conversionType.getValue())
								.trim());
		conversionType.setName(conversionType.getName()
											 .concat(" - ")
											 .concat(document.getValueOfDocumentAsString(MOrder.COLUMNNAME_DocumentNo))
											 );
		fieldLength = columnName.getFieldLength();
		conversionType.setName(String.format("%-".concat(fieldLength.toString()).concat(".")
				  								 .concat(fieldLength.toString()).concat("s"),conversionType.getName())
								.trim());

		conversionType.set_ValueOfColumn(CurrencyConvertDocumentsUtil.COLUMNNAME_SP032_ParentCType_ID, parentConversionType.get_ID());
	}
	
	/**
	 * Is Generated Currency Type Automatically
	 * @param document
	 * @return
	 */
	private boolean isSP032_GeneratedCurrencyType(PO document) {
		int C_DocType_ID = 0;
		if (document instanceof MOrder
				|| document instanceof MInvoice)
			C_DocType_ID = document.get_ValueAsInt(MOrder.COLUMNNAME_C_DocTypeTarget_ID);
		else 
			C_DocType_ID = document.get_ValueAsInt(MPayment.COLUMNNAME_C_DocType_ID);
		
		return Optional.ofNullable(MDocType.get(document.getCtx(), C_DocType_ID).get_ValueAsBoolean(CurrencyConvertDocumentsUtil.COLUMNNAME_SP032_GeneratedCurrencyType)).orElse(false);
	}

	@Override
	public void validate(ImportProcess process, Object importModel, Object targetModel, int timing) {
		Optional<ImportProcess> maybeImportProcess = Optional.ofNullable(process);
		maybeImportProcess.ifPresent(importProcess -> {
			/*
			if (importProcess instanceof ImportFixedAsset) {
				Optional<X_I_FixedAsset> maybeImportFixesAsset = Optional.ofNullable((X_I_FixedAsset) importModel);
				maybeImportFixesAsset.ifPresent(importFixesAsset -> {
					Optional<X_A_Asset_Addition> maybeAssetAddition = Optional.ofNullable((X_A_Asset_Addition) targetModel);
					maybeAssetAddition.ifPresent(assetAddition -> {
						if (timing == TIMING_BEFORE_IMPORT) {
							String whereClause = I_A_Asset_Addition.COLUMNNAME_A_Asset_Addition_ID.concat("=?");
							Optional<MSP032Conversion> maybeConversionDocument = Optional.ofNullable(new Query(importFixesAsset.getCtx(), MSP032Conversion.Table_Name, whereClause, importFixesAsset.get_TrxName())
																											.setParameters(assetAddition.get_ID())
																											.first()); 
							maybeConversionDocument.ifPresent(conversionDocument -> {
								Optional<BigDecimal> maybeNegotiateRate = Optional.ofNullable((BigDecimal)importFixesAsset.get_Value(CurrencyConvertDocumentsUtil.COLUMNNAME_SP032_NegotiatedRate));
								maybeNegotiateRate.ifPresent(negotiatedRate -> {
									conversionDocument.setSP032_NegotiatedRate(negotiatedRate);
									conversionDocument.saveEx();
								});
							});
						}
					});
				});
			}
			*/
		});
	}
	
}
