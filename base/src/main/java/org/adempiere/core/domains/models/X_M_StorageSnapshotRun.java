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
import org.compiere.model.PO;
import org.compiere.model.POInfo;
import org.compiere.util.Env;
import org.compiere.util.KeyNamePair;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.Properties;

/** Generated Model for M_StorageSnapshotRun
 *  @author Adempiere (generated) 
 *  @version Release 3.9.4 - $Id$ */
public class X_M_StorageSnapshotRun extends PO implements I_M_StorageSnapshotRun, I_Persistent 
{

	/**
	 *
	 */
	private static final long serialVersionUID = 20260121L;

    /** Standard Constructor */
    public X_M_StorageSnapshotRun (Properties ctx, int M_StorageSnapshotRun_ID, String trxName)
    {
      super (ctx, M_StorageSnapshotRun_ID, trxName);
      /** if (M_StorageSnapshotRun_ID == 0)
        {
			setDateLastRun (new Timestamp( System.currentTimeMillis() ));
			setDocumentNo (null);
			setM_StorageSnapshotRun_ID (0);
        } */
    }

    /** Load Constructor */
    public X_M_StorageSnapshotRun (Properties ctx, ResultSet rs, String trxName)
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
      StringBuffer sb = new StringBuffer ("X_M_StorageSnapshotRun[")
        .append(get_ID()).append("]");
      return sb.toString();
    }

	/** Set Date last run.
		@param DateLastRun 
		Date the process was last run.
	  */
	public void setDateLastRun (Timestamp DateLastRun)
	{
		set_Value (COLUMNNAME_DateLastRun, DateLastRun);
	}

	/** Get Date last run.
		@return Date the process was last run.
	  */
	public Timestamp getDateLastRun () 
	{
		return (Timestamp)get_Value(COLUMNNAME_DateLastRun);
	}

	/** Set Document No.
		@param DocumentNo 
		Document sequence number of the document
	  */
	public void setDocumentNo (String DocumentNo)
	{
		set_Value (COLUMNNAME_DocumentNo, DocumentNo);
	}

	/** Get Document No.
		@return Document sequence number of the document
	  */
	public String getDocumentNo () 
	{
		return (String)get_Value(COLUMNNAME_DocumentNo);
	}

    /** Get Record ID/ColumnName
        @return ID/ColumnName pair
      */
    public KeyNamePair getKeyNamePair() 
    {
        return new KeyNamePair(get_ID(), getDocumentNo());
    }

	/** Set Storage Snapshot Run.
		@param M_StorageSnapshotRun_ID Storage Snapshot Run	  */
	public void setM_StorageSnapshotRun_ID (int M_StorageSnapshotRun_ID)
	{
		if (M_StorageSnapshotRun_ID < 1) 
			set_ValueNoCheck (COLUMNNAME_M_StorageSnapshotRun_ID, null);
		else 
			set_ValueNoCheck (COLUMNNAME_M_StorageSnapshotRun_ID, Integer.valueOf(M_StorageSnapshotRun_ID));
	}

	/** Get Storage Snapshot Run.
		@return Storage Snapshot Run	  */
	public int getM_StorageSnapshotRun_ID () 
	{
		Integer ii = (Integer)get_Value(COLUMNNAME_M_StorageSnapshotRun_ID);
		if (ii == null)
			 return 0;
		return ii.intValue();
	}

	/** Set Product Processes.
		@param ProductProcesses Product Processes	  */
	public void setProductProcesses (BigDecimal ProductProcesses)
	{
		set_Value (COLUMNNAME_ProductProcesses, ProductProcesses);
	}

	/** Get Product Processes.
		@return Product Processes	  */
	public BigDecimal getProductProcesses () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_ProductProcesses);
		if (bd == null)
			 return Env.ZERO;
		return bd;
	}

	/** Set Transactions Processed.
		@param TransactionsProcessed Transactions Processed	  */
	public void setTransactionsProcessed (BigDecimal TransactionsProcessed)
	{
		set_Value (COLUMNNAME_TransactionsProcessed, TransactionsProcessed);
	}

	/** Get Transactions Processed.
		@return Transactions Processed	  */
	public BigDecimal getTransactionsProcessed () 
	{
		BigDecimal bd = (BigDecimal)get_Value(COLUMNNAME_TransactionsProcessed);
		if (bd == null)
			 return Env.ZERO;
		return bd;
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