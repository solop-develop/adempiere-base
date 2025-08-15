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
/** Generated Model - DO NOT CHANGE */
package org.adempiere.core.domains.models;

import org.compiere.model.I_Persistent;
import org.compiere.model.MTable;
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.util.KeyNamePair;

import java.sql.ResultSet;
import java.util.Properties;

/** Generated Model for C_BankAccountWithdrawal
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_C_BankAccountWithdrawal extends PO implements I_C_BankAccountWithdrawal, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20250814L;

    /** Standard Constructor */
    public X_C_BankAccountWithdrawal (Properties ctx, int C_BankAccountWithdrawal_ID, String trxName)
    {
      super (ctx, C_BankAccountWithdrawal_ID, trxName);
      /** if (C_BankAccountWithdrawal_ID == 0)
        {
			setC_BankAccount_ID (0);
			setC_BankAccountWithdrawal_ID (0);
			setName (null);
        } */
    }

    /** Load Constructor */
    public X_C_BankAccountWithdrawal (Properties ctx, ResultSet rs, String trxName)
    {
      super (ctx, rs, trxName);
    }

    /** AccessLevel
      * @return 3 - Client - Org 
      */
    protected int get_AccessLevel()
    {
      return accessLevel.intValue();
    }

    /** Load Meta Data */
    protected POInfo initPO (Properties ctx)
    {
      POInfo poi = POInfo.getPOInfo (ctx, Table_ID, get_TrxName());
      return poi;
    }

    public String toString()
    {
      StringBuffer sb = new StringBuffer ("X_C_BankAccountWithdrawal[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	public I_C_BankAccount getC_BankAccount() throws RuntimeException
    {
		return (I_C_BankAccount)MTable.get(getCtx(), I_C_BankAccount.Table_Name)
			.getPO(getC_BankAccount_ID(), get_TrxName());	}

	/** Set Bank Account.
		@param C_BankAccount_ID 
		Account at the Bank
	  */
	public void setC_BankAccount_ID (int C_BankAccount_ID)
	{
		if (C_BankAccount_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankAccount_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankAccount_ID, Integer.valueOf(C_BankAccount_ID));
	}

	/** Get Bank Account.
		@return Account at the Bank
	  */
	public int getC_BankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Bank Account Withdrawal.
		@param C_BankAccountWithdrawal_ID Bank Account Withdrawal	  */
	public void setC_BankAccountWithdrawal_ID (int C_BankAccountWithdrawal_ID)
	{
		if (C_BankAccountWithdrawal_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_C_BankAccountWithdrawal_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_C_BankAccountWithdrawal_ID, Integer.valueOf(C_BankAccountWithdrawal_ID));
	}

	/** Get Bank Account Withdrawal.
		@return Bank Account Withdrawal	  */
	public int getC_BankAccountWithdrawal_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BankAccountWithdrawal_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Bank getC_Bank() throws RuntimeException
    {
		return (I_C_Bank)MTable.get(getCtx(), I_C_Bank.Table_Name)
			.getPO(getC_Bank_ID(), get_TrxName());	}

	/** Set Bank.
		@param C_Bank_ID 
		Bank
	  */
	public void setC_Bank_ID (int C_Bank_ID)
	{
		if (C_Bank_ID < 1) 
			set_Value (COLUMNNAME_C_Bank_ID, null);
		else 
			set_Value (COLUMNNAME_C_Bank_ID, Integer.valueOf(C_Bank_ID));
	}

	/** Get Bank.
		@return Bank
	  */
	public int getC_Bank_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Bank_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BPartner getC_BPartner() throws RuntimeException
    {
		return (I_C_BPartner)MTable.get(getCtx(), I_C_BPartner.Table_Name)
			.getPO(getC_BPartner_ID(), get_TrxName());	}

	/** Set Business Partner .
		@param C_BPartner_ID 
		Identifies a Business Partner
	  */
	public void setC_BPartner_ID (int C_BPartner_ID)
	{
		if (C_BPartner_ID < 1) 
			set_Value (COLUMNNAME_C_BPartner_ID, null);
		else 
			set_Value (COLUMNNAME_C_BPartner_ID, Integer.valueOf(C_BPartner_ID));
	}

	/** Get Business Partner .
		@return Identifies a Business Partner
	  */
	public int getC_BPartner_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_BPartner_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_Currency getC_Currency() throws RuntimeException
    {
		return (I_C_Currency)MTable.get(getCtx(), I_C_Currency.Table_Name)
			.getPO(getC_Currency_ID(), get_TrxName());	}

	/** Set Currency.
		@param C_Currency_ID 
		The Currency for this record
	  */
	public void setC_Currency_ID (int C_Currency_ID)
	{
		if (C_Currency_ID < 1) 
			set_Value (COLUMNNAME_C_Currency_ID, null);
		else 
			set_Value (COLUMNNAME_C_Currency_ID, Integer.valueOf(C_Currency_ID));
	}

	/** Get Currency.
		@return The Currency for this record
	  */
	public int getC_Currency_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_Currency_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_DocType getC_DocType() throws RuntimeException
    {
		return (I_C_DocType)MTable.get(getCtx(), I_C_DocType.Table_Name)
			.getPO(getC_DocType_ID(), get_TrxName());	}

	/** Set Document Type.
		@param C_DocType_ID 
		Document type or rules
	  */
	public void setC_DocType_ID (int C_DocType_ID)
	{
		if (C_DocType_ID < 0) 
			set_Value (COLUMNNAME_C_DocType_ID, null);
		else 
			set_Value (COLUMNNAME_C_DocType_ID, Integer.valueOf(C_DocType_ID));
	}

	/** Get Document Type.
		@return Document type or rules
	  */
	public int getC_DocType_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_DocType_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_PaymentMethod getC_PaymentMethod() throws RuntimeException
    {
		return (I_C_PaymentMethod)MTable.get(getCtx(), I_C_PaymentMethod.Table_Name)
			.getPO(getC_PaymentMethod_ID(), get_TrxName());	}

	/** Set Store Payment Method.
		@param C_PaymentMethod_ID 
		Payment Methods allowed for Store
	  */
	public void setC_PaymentMethod_ID (int C_PaymentMethod_ID)
	{
		if (C_PaymentMethod_ID < 1) 
			set_Value (COLUMNNAME_C_PaymentMethod_ID, null);
		else 
			set_Value (COLUMNNAME_C_PaymentMethod_ID, Integer.valueOf(C_PaymentMethod_ID));
	}

	/** Get Store Payment Method.
		@return Payment Methods allowed for Store
	  */
	public int getC_PaymentMethod_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_C_PaymentMethod_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	public I_C_BankAccount getDepositBankAccount() throws RuntimeException
    {
		return (I_C_BankAccount)MTable.get(getCtx(), I_C_BankAccount.Table_Name)
			.getPO(getDepositBankAccount_ID(), get_TrxName());	}

	/** Set Deposit Bank Account.
		@param DepositBankAccount_ID 
		Bank Account used for deposit from cash by default
	  */
	public void setDepositBankAccount_ID (int DepositBankAccount_ID)
	{
		if (DepositBankAccount_ID < 1) 
			set_Value (COLUMNNAME_DepositBankAccount_ID, null);
		else 
			set_Value (COLUMNNAME_DepositBankAccount_ID, Integer.valueOf(DepositBankAccount_ID));
	}

	/** Get Deposit Bank Account.
		@return Bank Account used for deposit from cash by default
	  */
	public int getDepositBankAccount_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_DepositBankAccount_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Reconcile Automatically.
		@param IsAutoReconciled 
		Reconcile a payment automatically
	  */
	public void setIsAutoReconciled (boolean IsAutoReconciled)
	{
		set_Value (COLUMNNAME_IsAutoReconciled, Boolean.valueOf(IsAutoReconciled));
	}

	/** Get Reconcile Automatically.
		@return Reconcile a payment automatically
	  */
	public boolean isAutoReconciled () 
	{
		Object oo = get_Value(COLUMNNAME_IsAutoReconciled);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Split Deposits.
		@param IsSplitDeposits Split Deposits	  */
	public void setIsSplitDeposits (boolean IsSplitDeposits)
	{
		set_Value (COLUMNNAME_IsSplitDeposits, Boolean.valueOf(IsSplitDeposits));
	}

	/** Get Split Deposits.
		@return Split Deposits	  */
	public boolean isSplitDeposits () 
	{
		Object oo = get_Value(COLUMNNAME_IsSplitDeposits);
		if (oo != null) 
		{
			 if (oo instanceof Boolean) 
				 return ((Boolean)oo).booleanValue(); 
			return "Y".equals(oo);
		}
		return false;
	}

	/** Set Name.
		@param Name 
		Alphanumeric identifier of the entity
	  */
	public void setName (String Name)
	{
		set_Value (COLUMNNAME_Name, Name);
	}

	/** Get Name.
		@return Alphanumeric identifier of the entity
	  */
	public String getName () 
	{
		return (String)get_Value(COLUMNNAME_Name);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getName());
    }

	/** TenderType AD_Reference_ID=214 */
	public static final int TENDERTYPE_AD_Reference_ID=214;
	/** Credit Card = C */
	public static final String TENDERTYPE_CreditCard = "C";
	/** Check = K */
	public static final String TENDERTYPE_Check = "K";
	/** Direct Deposit = A */
	public static final String TENDERTYPE_DirectDeposit = "A";
	/** Direct Debit = D */
	public static final String TENDERTYPE_DirectDebit = "D";
	/** Account = T */
	public static final String TENDERTYPE_Account = "T";
	/** Cash = X */
	public static final String TENDERTYPE_Cash = "X";
	/** Credit Memo = M */
	public static final String TENDERTYPE_CreditMemo = "M";
	/** Zelle = Z */
	public static final String TENDERTYPE_Zelle = "Z";
	/** Mobile Payment Interbank = P */
	public static final String TENDERTYPE_MobilePaymentInterbank = "P";
	/** Gift Card = G */
	public static final String TENDERTYPE_GiftCard = "G";
	/** Set Tender type.
		@param TenderType 
		Method of Payment
	  */
	public void setTenderType (String TenderType)
	{

		set_Value (COLUMNNAME_TenderType, TenderType);
	}

	/** Get Tender type.
		@return Method of Payment
	  */
	public String getTenderType () 
	{
		return (String)get_Value(COLUMNNAME_TenderType);
	}

	/** Set Immutable Universally Unique Identifier.
		@param UUID 
		Immutable Universally Unique Identifier
	  */
	public void setUUID (String UUID)
	{
		set_Value (COLUMNNAME_UUID, UUID);
	}

	/** Get Immutable Universally Unique Identifier.
		@return Immutable Universally Unique Identifier
	  */
	public String getUUID () 
	{
		return (String)get_Value(COLUMNNAME_UUID);
	}
}