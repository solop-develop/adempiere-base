/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
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
 * Copyright (C) 2003-2015 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.cash.setup;

import java.util.Properties;

import org.adempiere.core.domains.models.I_C_BP_Group;
import org.compiere.model.MBPartner;
import org.compiere.model.MBank;
import org.compiere.model.MBankAccount;
import org.compiere.model.MCharge;
import org.compiere.model.MCurrency;
import org.compiere.model.Query;
import org.adempiere.core.domains.models.X_AD_ModelValidator;
import org.compiere.util.Env;
import org.spin.cash.model.MCBankAccountWithdrawal;
import org.spin.cash.model.validator.CashManagement;
import org.spin.util.ISetupDefinition;

/**
 * Testing class for setup
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class Test implements ISetupDefinition {

	private static final String SETUP_DESCRIPTION = "(*Created from Setup Automatically*)";
	private static final String SETUP_UUID = "(*AutomaticSetup*)";
	
	@Override
	public String doIt(Properties context, String transactionName) {
		//	Bank
		String name = "Main Bank";
		String routingNo = "010203";
		MBank bank = new Query(context, MBank.Table_Name, MBank.COLUMNNAME_Name + " = ? AND " + MBank.COLUMNNAME_RoutingNo + " = ?", transactionName)
				.setParameters(name, routingNo)
				.setClient_ID()
				.<MBank>first();
		int accountId = 0;
		//	Validate
		if(bank == null
				|| bank.getC_Bank_ID() <= 0) {
			bank = new MBank(Env.getCtx(), 0, transactionName);
			bank.setName(name);
			bank.setRoutingNo(routingNo);
			bank.setBankType(MBank.BANKTYPE_Bank);
			bank.setDescription(SETUP_DESCRIPTION);
			bank.setUUID(SETUP_UUID);
			bank.setIsDirectLoad(true);
			bank.saveEx();
			//	Add Account
			MBankAccount account = new MBankAccount(context, 0, transactionName);
			account.setC_Bank_ID(bank.getC_Bank_ID());
			account.setAccountNo("Main Account");
			account.setC_Currency_ID(MCurrency.get(Env.getCtx(), "USD").getC_Currency_ID());
			account.setBankAccountType(MBankAccount.BANKACCOUNTTYPE_Checking);
			account.setDescription(SETUP_DESCRIPTION);
			account.setUUID(SETUP_UUID);
			account.setIsDirectLoad(true);
			account.saveEx();
			accountId = account.getC_BankAccount_ID();
		}
		//	Create cash
		name = "Main Cash";
		routingNo = "None";
		MBank cash = new Query(context, MBank.Table_Name, MBank.COLUMNNAME_Name + " = ? AND " + MBank.COLUMNNAME_RoutingNo + " = ?", transactionName)
				.setParameters(name, routingNo)
				.setClient_ID()
				.<MBank>first();
		if(cash == null
				|| cash.getC_Bank_ID() <= 0) {
			cash = new MBank(Env.getCtx(), 0, transactionName);
			cash.setName(name);
			cash.setRoutingNo(routingNo);
			cash.setBankType(MBank.BANKTYPE_CashJournal);
			cash.setDescription(SETUP_DESCRIPTION);
			cash.setUUID(SETUP_UUID);
			cash.setIsDirectLoad(true);
			cash.saveEx();
			//	Create charge
			MCharge charge = new MCharge(Env.getCtx(), 0, transactionName);
			charge.setName("Bank transfer (Test)");
			charge.setDescription(SETUP_DESCRIPTION);
			charge.setUUID(SETUP_UUID);
			charge.setIsDirectLoad(true);
			charge.saveEx();
			//	Create Business Partner
			MBPartner businessPartner = new MBPartner(Env.getCtx(), 0, transactionName);
			businessPartner.setValue("TestBP");
			businessPartner.setName("Test Business Partner");
			businessPartner.setDescription(SETUP_DESCRIPTION);
			businessPartner.setC_BP_Group_ID(new Query(Env.getCtx(), I_C_BP_Group.Table_Name, null, transactionName).setClient_ID().firstId());
			businessPartner.setSOCreditStatus(MBPartner.SOCREDITSTATUS_CreditOK);
			businessPartner.setIsCustomer(true);
			businessPartner.setIsVendor(true);
			businessPartner.setUUID(SETUP_UUID);
			businessPartner.setIsDirectLoad(true);
			businessPartner.saveEx();
			//	Add Account
			MBankAccount cashAccount = new MBankAccount(context, 0, transactionName);
			cashAccount.setC_Bank_ID(cash.getC_Bank_ID());
			cashAccount.setAccountNo("Main Account");
			cashAccount.setC_Currency_ID(MCurrency.get(Env.getCtx(), "USD").getC_Currency_ID());
			cashAccount.setBankAccountType(MBankAccount.BANKACCOUNTTYPE_Checking);
			cashAccount.setDescription(SETUP_DESCRIPTION);
			cashAccount.setUUID(SETUP_UUID);
			cashAccount.setDepositBankAccount_ID(accountId);
			cashAccount.setDepositCharge_ID(charge.getC_Charge_ID());
			cashAccount.setC_BPartner_ID(businessPartner.getC_BPartner_ID());
			cashAccount.setIsDirectLoad(true);
			cashAccount.saveEx();
			//	Add configuration
			MCBankAccountWithdrawal withdrawal = new MCBankAccountWithdrawal(context, 0, transactionName);
			withdrawal.setC_BankAccount_ID(cashAccount.getC_BankAccount_ID());
			withdrawal.setName("Test for Direct Debit");
			withdrawal.setTenderType(MCBankAccountWithdrawal.TENDERTYPE_DirectDebit);
			withdrawal.setDepositBankAccount_ID(accountId);
			withdrawal.setUUID(SETUP_UUID);
			withdrawal.setIsDirectLoad(true);
			withdrawal.saveEx();
		}
		//	Add Model Validator
		createModelValidator(context, transactionName);
		//	financial management
		return "@AD_SetupDefinition_ID@ @Ok@";
	}
	
	/**
	 * Create Model Vaidator
	 * @param context
	 * @param transactionName
	 * @return
	 */
	private X_AD_ModelValidator createModelValidator(Properties context, String transactionName) {
		X_AD_ModelValidator modelValidator = new Query(context, X_AD_ModelValidator.Table_Name, X_AD_ModelValidator.COLUMNNAME_ModelValidationClass + " = ?", transactionName)
				.setParameters(CashManagement.class.getName())
				.setClient_ID()
				.<X_AD_ModelValidator>first();
		//	Validate
		if(modelValidator != null
				&& modelValidator.getAD_ModelValidator_ID() > 0) {
			return modelValidator;
		}
		//	
		modelValidator = new X_AD_ModelValidator(context, 0, transactionName);
		modelValidator.setName("Cash Management");
		modelValidator.setEntityType("ECA16");
		modelValidator.setDescription(SETUP_DESCRIPTION);
		modelValidator.setSeqNo(200);
		modelValidator.setModelValidationClass(CashManagement.class.getName());
		modelValidator.setUUID(SETUP_UUID);
		modelValidator.setIsDirectLoad(true);
		modelValidator.saveEx();
		return modelValidator;
	}
}
