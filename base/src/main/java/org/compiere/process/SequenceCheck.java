/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.process;

import org.adempiere.core.domains.models.I_AD_Sequence;
import org.compiere.Adempiere;
import org.compiere.db.CConnection;
import org.compiere.model.MClient;
import org.compiere.model.MSequence;
import org.compiere.model.MSysConfig;
import org.compiere.model.Query;
import org.compiere.util.*;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

/**
 *	System + Document Sequence Check
 *	
 *  @author Jorg Janke
 *  @version $Id: SequenceCheck.java,v 1.3 2006/07/30 00:54:44 jjanke Exp $
 *  
 *  @author mckayERP www.mckayERP.com
 *  			<li>#284 The SequenceCheck fails if the the AD_Sequence Table ID sequence is out of sequence.
 */
public class SequenceCheck extends SvrProcess
{
	/**	Static Logger	*/
	private static CLogger	s_log	= CLogger.getCLogger (SequenceCheck.class);
	
	/**
	 *  Prepare - e.g., get Parameters.
	 */
	protected void prepare()
	{
	}	//	prepare
	
	/**
	 *  Perform process.
	 *  (see also MSequenve.validate)
	 *  @return Message to be translated
	 *  @throws Exception
	 */
	protected String doIt() throws Exception
	{
		log.info("");
		//
		// #284
		checkTableID (Env.getCtx(), this, true);
		this.commitEx();
		checkTableSequences (Env.getCtx(), this);
		checkTableID (Env.getCtx(), this, false);
		checkClientSequences (Env.getCtx(), this);
		return "Sequence Check";
	}	//	doIt
	
	/**
	 *	Validate Sequences
	 *	@param ctx context
	 */
	public static void validate(Properties ctx)
	{
		try
		{
			// #284
			checkTableID (ctx, null, true); // Only AD_Sequence
			checkTableSequences (ctx, null); // Requires AD_Sequence to be valid
			checkTableID (ctx, null, false); // Check others
			checkClientSequences (ctx, null);
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, "validate", e);
		}
	}	//	validate
	
	
	
	/**************************************************************************
	 * 	Check existence of Table Sequences.
	 *	@param ctx context
	 *	@param sp server process or null
	 */
	private static void checkTableSequences (Properties ctx, SvrProcess sp)
	{
		String trxName = null;
		if (sp != null)
			trxName = sp.get_TrxName();
		String sql = "SELECT TableName "
			+ "FROM AD_Table t "
			+ "WHERE IsActive='Y' AND IsView='N'"
			+ " AND NOT EXISTS (SELECT * FROM AD_Sequence s "
			+ "WHERE UPPER(s.Name)=UPPER(t.TableName) AND s.IsTableID='Y')";
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try
		{
			pstmt = DB.prepareStatement(sql, trxName);
			rs = pstmt.executeQuery();
			while (rs.next())
			{
				String tableName = rs.getString(1);
				if (MSequence.createTableSequence (ctx, tableName, trxName))
				{
					if (sp != null)
						sp.addLog(0, null, null, tableName);
					else
						s_log.fine(tableName);
				}
				else
				{
					rs.close();
					throw new Exception ("Error creating Table Sequence for " + tableName);
				}
			}
		}
		catch (Exception e)
		{
			s_log.log(Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
		
		//	Sync Table Name case
		sql = "UPDATE AD_Sequence s "
			+ "SET Name = (SELECT TableName FROM AD_Table t "
				+ "WHERE t.IsView='N' AND UPPER(s.Name)=UPPER(t.TableName)) "
			+ "WHERE s.IsTableID='Y'"
			+ " AND EXISTS (SELECT * FROM AD_Table t "
				+ "WHERE t.IsActive='Y' AND t.IsView='N'"
				+ " AND UPPER(s.Name)=UPPER(t.TableName) AND s.Name<>t.TableName)";
		int no = DB.executeUpdate(sql, trxName);
		if (no > 0)
		{
			if (sp != null)
				sp.addLog(0, null, null, "SyncName #" + no);
			else
				s_log.fine("Sync #" + no);
		}
		if (no >= 0)
			return;
		
		/** Find Duplicates 		 */
		sql = "SELECT TableName, s.Name "
			+ "FROM AD_Table t, AD_Sequence s "
			+ "WHERE t.IsActive='Y' AND t.IsView='N'"
			+ " AND UPPER(s.Name)=UPPER(t.TableName) AND s.Name<>t.TableName";
		//
		try
		{
			pstmt = DB.prepareStatement (sql, null);
			rs = pstmt.executeQuery ();
			while (rs.next ())
			{
				String TableName = rs.getString(1);
				String SeqName = rs.getString(2);
				sp.addLog(0, null, null, "ERROR: TableName=" + TableName + " - Sequence=" + SeqName);
			}
		}
		catch (Exception e)
		{
			s_log.log (Level.SEVERE, sql, e);
		}
		finally
		{
			DB.close(rs, pstmt);
			rs = null; pstmt = null;
		}
	}	//	checkTableSequences
	

	/**
	 * 	Check Table Sequence ID values
	 *	@param ctx context
	 *	@param sp server process or null
	 */
	private static void checkTableID (Properties ctx, SvrProcess sp, 
			boolean onlyADSequence) // #284
	{
		int IDRangeEnd = DB.getSQLValue(null,
			"SELECT IDRangeEnd FROM AD_System");
		if (IDRangeEnd <= 0)
			IDRangeEnd = DB.getSQLValue(null,
				"SELECT MIN(IDRangeStart)-1 FROM AD_Replication");
		s_log.info("IDRangeEnd = " + IDRangeEnd);
		//
		String whereClause = "IsTableID = 'Y'";
		if (onlyADSequence) {
			whereClause += " AND AD_Sequence_ID = 16"; // HARDCODED: AD_Sequence  #284
		}
		String trxName = (sp != null) ? sp.get_TrxName() : null;
		List<Integer> sequenceIds = new Query(ctx, I_AD_Sequence.Table_Name, whereClause, trxName)
				.setOrderBy(I_AD_Sequence.COLUMNNAME_Name)
				.setOnlyActiveRecords(true)
				.getIDsAsList();
		AtomicInteger counterValue = new AtomicInteger(0);
		sequenceIds.stream().parallel().forEach(sequenceId -> {
			Trx.run(transactionName -> {
				try {
					MSequence seq = new MSequence (ctx, sequenceId, transactionName);
					// The id list is fetched in the main trx, but each record is loaded in a
					// separate parallel trx that may not see it (MSequence.load: NO Data found),
					// leaving an empty PO. Skip it instead of validating a blank sequence.
					if (seq == null || seq.get_ID() != sequenceId.intValue()) {
						s_log.warning("AD_Sequence_ID=" + sequenceId + " could not be loaded, skipping");
						return;
					}
					// If the name is invalid (e.g. spaces) but its canonical form (spaces -> '_')
					// matches a real table, it was a typo: fix the stored name so it becomes a
					// valid table-ID sequence and gets validated/created below.
					String fixedName = fixTableSequenceName(seq);
					if (fixedName != null) {
						seq.saveEx();
						if (sp != null) {
							sp.addLog(0, null, null, "Sequence name fixed => " + fixedName);
						}
					}
					int old = seq.getCurrentNext();
					int oldSys = seq.getCurrentNextSys();
					// Created may be null for some sequences; guard against NPE so the
					// sequence is still validated below.
					boolean isNewSequence = seq.getCreated() != null && seq.getCreated().equals(seq.getUpdated());
					if (seq.validateTableIDValue()) {
						if (seq.getCurrentNext() != old) {
							String msg = seq.getName() + " ID  "
									+ old + " -> " + seq.getCurrentNext();
							if (sp != null) {
								sp.addLog(0, null, null, msg);
							}
						}
						if (seq.getCurrentNextSys() != oldSys) {
							String msg = seq.getName() + " Sys "
									+ oldSys + " -> " + seq.getCurrentNextSys();
							if (sp != null) {
								sp.addLog(0, null, null, msg);
							}
						}
						seq.saveEx();
						if(isNewSequence) {
							if(sp != null && createMissingNativeSequence(seq, transactionName)) {
								sp.addLog("Native Sequence Created => " + seq.getName());
							}
						}
						counterValue.incrementAndGet();
					} else if(isNewSequence) {
						String originalDescription = seq.getDescription();
						seq.setDescription(originalDescription + "-");
						seq.saveEx();
						seq.setDescription(originalDescription);
						seq.saveEx();
						if(sp != null && createMissingNativeSequence(seq, transactionName)) {
							sp.addLog("Native Sequence Created => " + seq.getName());
						}
					}
				} catch (Exception e) {
					s_log.severe(e.getLocalizedMessage());
				}
			});
		});
		s_log.fine("#" + counterValue);
	}	//	checkTableID

	/**
	 * Fix the name of a table-ID sequence stored with an invalid identifier (e.g. spaces).
	 * If the canonical form (whitespace collapsed to '_') matches a real table that has a
	 * &lt;canonical&gt;_ID column, the name was a typo: it is corrected on the PO (not saved
	 * here) and the new name is returned. Otherwise (name already valid, or no matching
	 * table) returns null and the caller leaves it for the skip+warning path.
	 * @param sequence sequence to inspect/fix
	 * @return the corrected name, or null if no correction applies
	 */
	private static String fixTableSequenceName(MSequence sequence) {
		if (!sequence.isTableID()) {
			return null;
		}
		String name = sequence.getName();
		if (name == null || name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
			return null; // null or already a valid identifier
		}
		String canonical = name.trim().replaceAll("\\s+", "_");
		if (!canonical.matches("[A-Za-z_][A-Za-z0-9_]*")) {
			return null; // still invalid after normalization (other special chars)
		}
		// Match the table case-insensitively but fetch the REAL TableName so the stored name
		// matches AD_Table exactly: validateTableIDValue() and the native sequence rely on the
		// exact (case-sensitive) name.
		String realTableName = DB.getSQLValueString(
			sequence.get_TrxName(),
			"SELECT t.TableName FROM AD_Table t"
			+ " INNER JOIN AD_Column c ON (t.AD_Table_ID=c.AD_Table_ID)"
			+ " WHERE UPPER(t.TableName)=UPPER(?) AND UPPER(c.ColumnName)=UPPER(?)",
			canonical, canonical + "_ID"
		);
		if (realTableName == null || realTableName.isEmpty()) {
			return null; // not a real table -> leave for skip+warn
		}
		sequence.setName(realTableName);
		return realTableName;
	}

	/**
	 * Create Native sequence if not exists
	 */
	private static boolean createMissingNativeSequence(MSequence sequence, String transactionName) {
		if(!sequence.isTableID()) {
			return false;
		}
		String name = sequence.getName();
		// A table-ID sequence name must be a valid SQL identifier (a real table name), because
		// the native sequence is created as <name>_SEQ. Names with spaces or special chars
		// (typically a document sequence mis-flagged as IsTableID='Y') break "CREATE SEQUENCE"
		// and would never be consumed, so skip them and surface the misconfiguration.
		if(name == null || !name.matches("[A-Za-z_][A-Za-z0-9_]*")) {
			s_log.warning(
				"Native sequence skipped, invalid name for IsTableID='Y': '"
				+ name + "' (AD_Sequence_ID=" + sequence.getAD_Sequence_ID() + ")"
			);
			return false;
		}
		boolean SYSTEM_NATIVE_SEQUENCE = MSysConfig.getBooleanValue("SYSTEM_NATIVE_SEQUENCE",false);
		if(SYSTEM_NATIVE_SEQUENCE) {
			CConnection.get().getDatabase().createSequence(name+"_SEQ", 1, 0 , 99999999,  sequence.getNextID(), transactionName);
		}
		return true;
	}
	
	/**
	 *	Check/Initialize DocumentNo/Value Sequences for all Clients 	
	 *	@param ctx context
	 *	@param sp server process or null
	 */
	private static void checkClientSequences (Properties ctx, SvrProcess sp)
	{
		String trxName = null;
		if (sp != null)
			trxName = sp.get_TrxName();

		// CarlosRuiz - globalqss - [ 1887608 ] SequenceCheck deadlock 
		// Commit previous work on AD_Sequence
		// previously could update a sequence record needed now that is going to create new ones
		Trx trx = Trx.get(trxName, false);
		trx.commit();
		
		
		//	Sequence for DocumentNo/Value
		MClient[] clients = MClient.getAll(ctx);
		for (int i = 0; i < clients.length; i++)
		{
			MClient client = clients[i];
			if (!client.isActive())
				continue;
			MSequence.checkClientSequences (ctx, client.getAD_Client_ID(), trxName);
		}	//	for all clients
		
	}	//	checkClientSequences
	
	//add main method, preparing for nightly build
	public static void main(String[] args) 
	{
		Adempiere.startupEnvironment(false);
		CLogMgt.setLevel(Level.FINE);
		s_log.info("Sequence Check");
		s_log.info("--------------");
		ProcessInfo pi = new ProcessInfo("Sequence Check", 258);
		pi.setAD_Client_ID(0);
		pi.setAD_User_ID(100);
		
		SequenceCheck sc = new SequenceCheck();
		sc.startProcess(Env.getCtx(), pi, null);
		
		System.out.println("Process=" + pi.getTitle() + " Error="+pi.isError() + " Summary=" + pi.getSummary());
	}

}	//	SequenceCheck
