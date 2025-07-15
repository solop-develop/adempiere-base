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
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.cash.util;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.core.domains.models.I_C_Payment;
import org.compiere.model.MBank;
import org.compiere.model.MBankAccount;
import org.compiere.model.MBankStatement;
import org.compiere.model.MBankStatementLine;
import org.compiere.model.MOrder;
import org.compiere.model.MPOS;
import org.compiere.model.MPayment;
import org.compiere.model.Query;
import org.compiere.process.ProcessInfo;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Util;
import org.eevolution.process.BankTransfer;
import org.spin.cash.model.MCBankAccountWithdrawal;

/**
 * Added for handle custom values for ADempiere core
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class CashManagementUtil {
	/**	Withdrawal Document Type	*/
	public static final String COLUMNNAME_WithdrawalDocumentType_ID = "WithdrawalDocumentType_ID";
	/**	Deposit Bank Account	*/
	public static final String COLUMNNAME_DepositBankAccount_ID = "DepositBankAccount_ID";
	/**	Deposit Charge	*/
	public static final String COLUMNNAME_DepositCharge_ID = "DepositCharge_ID";
	/**	Deposit Document Type	*/
	public static final String COLUMNNAME_DepositDocumentType_ID = "DepositDocumentType_ID";
	/**	Deposit Tender Type	*/
	public static final String COLUMNNAME_DepositTenderType = "DepositTenderType";
	/**	Parameter Name for Reconcile Automatically	*/
	public static final String COLUMNNAME_IsAutoReconciled = "IsAutoReconciled";
	/**	Deposit Automatically After Close Cash	*/
	public static final String COLUMNNAME_IsAutoDepositAfterClose = "IsAutoDepositAfterClose";
	/**	Split Deposits	*/
	public static final String COLUMNNAME_IsSplitDeposits = "IsSplitDeposits";
	/**	Validate Cash Opening	*/
	public static final String COLUMNNAME_IsValidateCashOpening = "IsValidateCashOpening";
	/** Not exists a cash opening for current cash and day */
	public static final String MESSAGE_CashOpeningValidationError = "CashOpeningValidationError";
	
	/**
	 * Create withdrawal automatically after close cash
	 * @param bankStatement
	 */
	public static void createWithdrawalFromBankStatement(MBankStatement bankStatement) {
		MBankAccount cashAccount = MBankAccount.get(bankStatement.getCtx(), bankStatement.getC_BankAccount_ID());
		MBank cashJournal = MBank.get(bankStatement.getCtx(), cashAccount.getC_Bank_ID());
		if(!cashAccount.get_ValueAsBoolean(COLUMNNAME_IsAutoDepositAfterClose)) {
			return;
		}
		if(Optional.ofNullable(cashJournal.getBankType()).orElse(MBank.BANKTYPE_Bank).equals(MBank.BANKTYPE_CashJournal)) {
			Map<Integer, List<PaymentWrapper>> paymentsByMatchedCombination = new HashMap<Integer, List<PaymentWrapper>>();
			getColletsToDeposit(bankStatement)
			.forEach(paymentWrapper -> {
				AtomicInteger matchedCombinationId = new AtomicInteger(0);
				Optional.ofNullable(MCBankAccountWithdrawal.findMatchFromPayment(bankStatement.getCtx(), paymentWrapper, bankStatement.get_TrxName()))
					.ifPresent(matchedCombination -> matchedCombinationId.set(matchedCombination.getC_BankAccountWithdrawal_ID()));
				List<PaymentWrapper> payments = paymentsByMatchedCombination.get(matchedCombinationId.get());
				if(payments == null) {
					payments = new ArrayList<PaymentWrapper>();
				}
				payments.add(paymentWrapper);
				paymentsByMatchedCombination.put(matchedCombinationId.get(), payments);
			});
			//	Create deposits
			if(paymentsByMatchedCombination.size() > 0) {
				paymentsByMatchedCombination.keySet().forEach(combinationId -> {
					//	Get default values
					AtomicInteger depositBankAccountId = new AtomicInteger(cashAccount.get_ValueAsInt(CashManagementUtil.COLUMNNAME_DepositBankAccount_ID));
					AtomicBoolean reconcilePayments = new AtomicBoolean(true);
					AtomicBoolean splitDeposits = new AtomicBoolean(false);
					AtomicReference<String> defaultTenderType = new AtomicReference<String>(cashAccount.get_ValueAsString(CashManagementUtil.COLUMNNAME_DepositTenderType));
					if(combinationId > 0) {
						MCBankAccountWithdrawal withdrawalConfiguration = new MCBankAccountWithdrawal(bankStatement.getCtx(), combinationId, bankStatement.get_TrxName());
						if(withdrawalConfiguration.getDepositBankAccount_ID() > 0) {
							depositBankAccountId.set(withdrawalConfiguration.getDepositBankAccount_ID());
						}
						reconcilePayments.set(withdrawalConfiguration.isAutoReconciled());
						splitDeposits.set(withdrawalConfiguration.isSplitDeposits());
						if(!Util.isEmpty(withdrawalConfiguration.getTenderType())) {
							defaultTenderType.set(withdrawalConfiguration.getTenderType());
						}
					}
					//	Split all deposits
					if(splitDeposits.get()) {
						paymentsByMatchedCombination.get(combinationId).forEach(paymentWrapper -> {
							createWithdrawal(cashAccount, bankStatement, depositBankAccountId.get(), paymentWrapper.getCurrencyId(), paymentWrapper.getConversionTypeId(), paymentWrapper.getAmount(), reconcilePayments.get(), paymentWrapper.getDocumentNo(), paymentWrapper.getTenderType(), paymentWrapper.getBusinessPartnerId());
						});
					} else {
						Map<String, PaymentSummaryWrapper> paymentToWithdrawal = new HashMap<String, PaymentSummaryWrapper>();
						paymentsByMatchedCombination.get(combinationId).forEach(paymentWrapper -> {
							PaymentSummaryWrapper summary = paymentToWithdrawal.get(paymentWrapper.getCurrencyId() + "|" + paymentWrapper.getConversionTypeId());
							if(summary == null) {
								summary = PaymentSummaryWrapper.newInstance().withCurrencyId(paymentWrapper.getCurrencyId()).withConversionTypeId(paymentWrapper.getConversionTypeId());
							}
							summary.addAmount(paymentWrapper.getAmount());
							paymentToWithdrawal.put(paymentWrapper.getCurrencyId() + "|" + paymentWrapper.getConversionTypeId(), summary);
						});
						if(paymentToWithdrawal.size() > 0) {
							paymentToWithdrawal.values().forEach(summaryWrapper -> {
								createWithdrawal(cashAccount, bankStatement, depositBankAccountId.get(), summaryWrapper.getCurrencyId(), summaryWrapper.getConversionTypeId(), summaryWrapper.getAmount(), reconcilePayments.get(), bankStatement.getDocumentNo(), defaultTenderType.get(), cashAccount.getC_BPartner_ID());
							});
						}
					}
				});
			}
			//	Calculate balance
			calculateBankStatementBalance(bankStatement);
		}
	}
	
	private static void createWithdrawal(MBankAccount cashAccount, MBankStatement bankStatement, int depositBankAccountId, int currencyId, int conversionTypeId, BigDecimal amount, boolean isReconciled, String documentNo, String tenderType, int businessPartnerId) {
		ProcessInfo result = org.eevolution.services.dsl.ProcessBuilder
				.create(bankStatement.getCtx())
				.process(BankTransfer.getProcessId())
				.withoutTransactionClose()
				.withParameter(BankTransfer.WITHDRAWALDOCUMENTTYPE_ID, cashAccount.get_ValueAsInt(CashManagementUtil.COLUMNNAME_WithdrawalDocumentType_ID))
				.withParameter(BankTransfer.DEPOSITDOCUMENTTYPE_ID, cashAccount.get_ValueAsInt(CashManagementUtil.COLUMNNAME_DepositDocumentType_ID))
				.withParameter(BankTransfer.STATEMENTDATE, bankStatement.getStatementDate())
				.withParameter(BankTransfer.DATEACCT, bankStatement.getStatementDate())
				.withParameter(BankTransfer.C_BPARTNER_ID, businessPartnerId)
				.withParameter(BankTransfer.C_CHARGE_ID, cashAccount.get_ValueAsInt(CashManagementUtil.COLUMNNAME_DepositCharge_ID))
				.withParameter(BankTransfer.FROM_C_BANKACCOUNT_ID, bankStatement.getC_BankAccount_ID())
				.withParameter(BankTransfer.C_BANKACCOUNTTO_ID, depositBankAccountId)
				.withParameter(BankTransfer.TENDERTYPE, tenderType)
				.withParameter(BankTransfer.DOCUMENTNO, documentNo)
				.withParameter(BankTransfer.C_CURRENCY_ID, currencyId)
				.withParameter(BankTransfer.C_CONVERSIONTYPE_ID, conversionTypeId)
				.withParameter(BankTransfer.AMOUNT, amount)
				.withParameter(BankTransfer.ISAUTORECONCILED, isReconciled)
				.withParameter(BankTransfer.DESCRIPTION, bankStatement.getDescription())
				.execute(bankStatement.get_TrxName());
		//	
		if(result.isError()) {
			throw new AdempiereException(result.getSummary());
		}
	}
	
	/**
	 * Recalculate bank statement balance
	 * @param bankStatement
	 */
	private static void calculateBankStatementBalance(MBankStatement bankStatement) {
		List<MBankStatementLine> lines = Arrays.asList(bankStatement.getLines(true));
		//	Lines
		AtomicReference<BigDecimal> total = new AtomicReference<BigDecimal>(Env.ZERO);
		AtomicReference<Timestamp> minimumDate = new AtomicReference<Timestamp>(bankStatement.getStatementDate());
		AtomicReference<Timestamp> maximumDate = new AtomicReference<Timestamp>(bankStatement.getStatementDate());
		lines.forEach(statementLine -> {
			total.updateAndGet(totalAmount -> totalAmount.add(statementLine.getStmtAmt()));
			if (statementLine.getDateAcct().before(minimumDate.get())) {
				minimumDate.set(statementLine.getDateAcct()); 
			}
			if (statementLine.getDateAcct().after(maximumDate.get())) {
				maximumDate.set(statementLine.getDateAcct());
			}
		});
		bankStatement.setStatementDifference(total.get());
		bankStatement.setEndingBalance(bankStatement.getBeginningBalance().add(total.get()));
		bankStatement.saveEx();
	}
	
	/**
	 * Validate that exists a cash opening
	 * @param context
	 * @param pointOfSalesId
	 * @param transactionName
	 */
	public static void validateCashOpeningForPayment(MPayment payment) {
		if(payment.getC_POS_ID() <= 0) {
			return;
		}
		MBankAccount cashAccount = MBankAccount.get(payment.getCtx(), payment.getC_BankAccount_ID());
		if(cashAccount.get_ValueAsBoolean(COLUMNNAME_IsValidateCashOpening)) {
			int paymentId = new Query(payment.getCtx(), I_C_Payment.Table_Name, "DocStatus IN('CO', 'CL') "
					+ "AND IsReceipt = 'Y' "
					+ "AND C_Charge_ID IS NOT NULL "
					+ "AND DateTrx = ? "
					+ "AND EXISTS(SELECT 1 FROM C_BankStatementLine bsl WHERE bsl.C_Payment_ID = C_Payment.C_Payment_ID AND bsl.Processed = 'N')", payment.get_TrxName())
					.setParameters(TimeUtil.getDay(System.currentTimeMillis()))
					.firstId();
			if(paymentId <= 0) {
				throw new AdempiereException(Msg.parseTranslation(payment.getCtx(), "@CashOpeningValidationError@"));
			}
		}
	}
	
	/**
	 * Validate that exists a cash opening
	 * @param context
	 * @param pointOfSalesId
	 * @param transactionName
	 */
	public static void validateCashOpeningForOrder(MOrder order) {
		if(order.getC_POS_ID() <= 0) {
			return;
		}
		MPOS pointOfSales = MPOS.get(order.getCtx(), order.getC_POS_ID());
		MBankAccount cashAccount = MBankAccount.get(order.getCtx(), pointOfSales.getC_BankAccount_ID());
		if(cashAccount.get_ValueAsBoolean(COLUMNNAME_IsValidateCashOpening)) {
			int paymentId = new Query(order.getCtx(), I_C_Payment.Table_Name, "DocStatus IN('CO', 'CL') "
					+ "AND IsReceipt = 'Y' "
					+ "AND C_Charge_ID IS NOT NULL "
					+ "AND C_POS_ID = ? "
					+ "AND DateTrx = ? "
					+ "AND EXISTS(SELECT 1 FROM C_BankStatementLine bsl WHERE bsl.C_Payment_ID = C_Payment.C_Payment_ID AND bsl.Processed = 'N')", order.get_TrxName())
					.setParameters(order.getC_POS_ID(), TimeUtil.getDay(System.currentTimeMillis()))
					.firstId();
			if(paymentId <= 0) {
				throw new AdempiereException(Msg.parseTranslation(order.getCtx(), "@CashOpeningValidationError@"));
			}
		}
	}
	
	/**
	 * Get List of payments for a bank statement
	 * @param bankStatement
	 * @return
	 */
	private static List<PaymentWrapper> getColletsToDeposit(MBankStatement bankStatement) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<PaymentWrapper> wrapperList = new ArrayList<PaymentWrapper>();
		try {
			String sql = "SELECT p.C_Payment_ID, p.DocumentNo, p.C_DocType_ID, p.C_BPartner_ID, p.C_Bank_ID, p.C_BankAccount_ID, p.TenderType, p.C_Currency_ID, p.C_ConversionType_ID, (p.PayAmt * CASE WHEN p.IsReceipt = 'Y' THEN 1 ELSE -1 END) AS PaymentAmount "
					+ "FROM C_Payment p "
					+ "WHERE p.DocStatus IN('CO', 'CL') "
					+ "AND EXISTS(SELECT 1 FROM C_BankStatementLine bsl WHERE bsl.C_Payment_ID = p.C_Payment_ID AND bsl.C_BankStatement_ID = ?)";
			pstmt = DB.prepareStatement(sql, bankStatement.get_TrxName());
			pstmt.setInt(1, bankStatement.getC_BankStatement_ID());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				wrapperList.add(PaymentWrapper.newInstance()
						.withPaymentId(rs.getInt("C_Payment_ID"))
						.withDocumentNo(rs.getString("DocumentNo"))
						.withDocumentTypeId(rs.getInt("C_DocType_ID"))
						.withBusinessPartnerId(rs.getInt("C_BPartner_ID"))
						.withBankId(rs.getInt("C_Bank_ID"))
						.withBankAccountId(rs.getInt("C_BankAccount_ID"))
						.withTenderType(rs.getString("TenderType"))
						.withCurrencyId(rs.getInt("C_Currency_ID"))
						.withConversionTypeId(rs.getInt("C_ConversionType_ID"))
						.withAmount(rs.getBigDecimal("PaymentAmount")));
			}
		} catch (Exception e) {
			throw new AdempiereException(e);
		} finally {
			DB.close(rs, pstmt);
		}
		return wrapperList;
	}
}
