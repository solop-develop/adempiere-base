/*************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                              *
 * This program is free software; you can redistribute it and/or modify it    		 *
 * under the terms version 2 or later of the GNU General Public License as published *
 * by the Free Software Foundation. This program is distributed in the hope   		 *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied 		 *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           		 *
 * See the GNU General Public License for more details.                       		 *
 * You should have received a copy of the GNU General Public License along    		 *
 * with this program; if not, write to the Free Software Foundation, Inc.,    		 *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     		 *
 * For the text or an alternative of this public license, you may reach us    		 *
 * Copyright (C) 2012-2018 E.R.P. Consultores y Asociados, S.A. All Rights Reserved. *
 * Contributor(s): Yamel Senih www.erpya.com				  		                 *
 *************************************************************************************/
package org.spin.cash.model;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MBankAccount;
import org.compiere.model.Query;
import org.spin.cash.util.CashManagementUtil;
import org.spin.cash.util.PaymentWrapper;

/**
 * Model class fcor apply match of payments
 * @author Yamel Senih, ysenih@erpya.com , http://www.erpya.com
 */
public class MCBankAccountWithdrawal extends X_C_BankAccountWithdrawal {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3542373877117442475L;
	
	public MCBankAccountWithdrawal(Properties ctx, ResultSet rs, String trxName) {
		super(ctx, rs, trxName);
	}

	public MCBankAccountWithdrawal(Properties ctx, int C_BankAccountWithdrawal_ID, String trxName) {
		super(ctx, C_BankAccountWithdrawal_ID, trxName);
	}
	
	@Override
	protected boolean beforeSave(boolean newRecord) {
		MBankAccount bankAccount = (MBankAccount) getC_BankAccount();
		//	Validate Business Partner
		if(bankAccount.getC_BPartner_ID() <= 0) {
			throw new AdempiereException("@C_BankAccount_ID@ @C_BPartner_ID@ @IsMandatory@");
		}
		//	Validate Charge
		if(bankAccount.get_ValueAsInt(CashManagementUtil.COLUMNNAME_DepositCharge_ID) <= 0) {
			throw new AdempiereException("@C_BankAccount_ID@ @" + CashManagementUtil.COLUMNNAME_DepositCharge_ID + "@ @IsMandatory@");
		}
		//	Validate Charge
		if(bankAccount.get_ValueAsInt(CashManagementUtil.COLUMNNAME_DepositBankAccount_ID) <= 0) {
			if(getDepositBankAccount_ID() <= 0) {
				throw new AdempiereException("@C_BankAccount_ID@ @" + CashManagementUtil.COLUMNNAME_DepositBankAccount_ID + "@ @IsMandatory@");
			}
		}
		return super.beforeSave(newRecord);
	}
	
	/**
	 * Get Match for withdrawal
	 * @param payment
	 * @return
	 */
	public static MCBankAccountWithdrawal findMatchFromPayment(Properties context, PaymentWrapper payment, String transactionName) {
		if(payment == null) {
			return null;
		}
		StringBuffer whereClause = new StringBuffer();
		List<Object> parameters = new ArrayList<>();
		whereClause.append(COLUMNNAME_C_BankAccount_ID + " = ?");
		parameters.add(payment.getBankAccountId());
		//	Tender Type
		whereClause.append(" AND (TenderType = ? OR TenderType IS NULL)");
		parameters.add(payment.getTenderType());
		//	Currency
		whereClause.append(" AND (C_Currency_ID = ? OR C_Currency_ID IS NULL)");
		parameters.add(payment.getCurrencyId());
		//	Document Type
		whereClause.append(" AND (C_DocType_ID = ? OR C_DocType_ID IS NULL)");
		parameters.add(payment.getDocumentTypeId());
		//	Bank
		if(payment.getBankId() > 0) {
			whereClause.append(" AND (C_Bank_ID = ? OR C_Bank_ID IS NULL)");
			parameters.add(payment.getBankId());
		}
		//	Business Partner
		if(payment.getBusinessPartnerId() > 0) {
			whereClause.append(" AND (C_BPartner_ID = ? OR C_BPartner_ID IS NULL)");
			parameters.add(payment.getBusinessPartnerId());
		}
		//	Get Match
		return new Query(context, Table_Name, whereClause.toString(), transactionName)
				.setParameters(parameters)
				.setOnlyActiveRecords(true)
				.setClient_ID()
				.setOrderBy("C_Currency_ID DESC NULLS LAST, TenderType DESC NULLS LAST, C_DocType_ID DESC NULLS LAST, C_Bank_ID DESC NULLS LAST, C_BPartner_ID DESC NULLS LAST")
				.first();
	}

	@Override
	public String toString() {
		return "MCBankAccountWithdrawal [getC_BankAccountWithdrawal_ID()=" + getC_BankAccountWithdrawal_ID()
				+ ", getName()=" + getName() + "]";
	}
}
