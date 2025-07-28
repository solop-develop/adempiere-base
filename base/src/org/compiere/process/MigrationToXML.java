/*******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution * Copyright (C)
 * 1999-2009 Adempiere, Inc. All Rights Reserved. * This program is free
 * software; you can redistribute it and/or modify it * under the terms version
 * 2 of the GNU General Public License as published * by the Free Software
 * Foundation. This program is distributed in the hope * that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied * warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. * See the GNU General
 * Public License for more details. * You should have received a copy of the GNU
 * General Public License along * with this program; if not, write to the Free
 * Software Foundation, Inc., * 59 Temple Place, Suite 330, Boston, MA
 * 02111-1307 USA. *
 * 
 ******************************************************************************/

package org.compiere.process;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MMigration;
import org.compiere.model.MSysConfig;
import org.solop.migration.util.IMigrationManagement;
import org.solop.migration.util.MigrationFactory;

import java.io.File;
import java.util.logging.Level;

/**
 * 
 * Process to export an AD migration script as xml
 * 
 * @author Paul Bowden, Adaxa Pty Ltd
 *
 * @author Michael McKay, michael.mckay@mckayerp.com, 
 *	 <li>Bug [ <a href="https://github.com/adempiere/adempiere/issues/1926">#1926</a> ] ZK Exports migration XML files to 
 *       different location than what is selected in the dialogs.
 * @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 */
public class MigrationToXML extends MigrationToXMLAbstract {

    private final String MIGRATION_DEFAULT_FORMAT = "MIGRATION_DEFAULT_FORMAT";
	private final MigrationFactory migrationFactory = MigrationFactory.newInstance();

	@Override
	protected void prepare() {
		log.log(Level.FINE, "AD_Migration_ID = " + getRecord_ID());
	}

	private String getValidString(String value) {
		if(value == null) {
			return "";
		}
		return value.trim().replaceAll("[^a-zA-Z0-9]", "_");
	}

	private String getFileName() {
		MMigration migration = new MMigration(getCtx(), getRecord_ID(), get_TrxName());
		//  Come up with a temporary filename.
		String fileName = String.format("%08d", migration.getSeqNo()) + "_" + migration.getEntityType() +
				"_" + getValidString(migration.getReleaseNo()) + "_" + getValidString(migration.getName())
				+ getValidateExtension();
		String folderName = MSysConfig.getValue("MIGRATION_DEFAULT_FOLDER", "/tmp");
		return folderName + File.separator + fileName;
	}

	private String getValidateExtension() {
		String format = MSysConfig.getValue(MIGRATION_DEFAULT_FORMAT, MigrationFactory.FORMAT_XML);
		if(format == null) {
			throw new AdempiereException("Extension is Mandatory");
		}
		IMigrationManagement manager = migrationFactory.getMigrationManagerByFormat(format);
		if(manager == null) {
			throw new AdempiereException("Unsupported format: " + format);
		}
		return "." + manager.getExtension();
	}

	public String exportMigration() {
		String format = MSysConfig.getValue(MIGRATION_DEFAULT_FORMAT, MigrationFactory.FORMAT_XML);
		IMigrationManagement exporter = migrationFactory.getMigrationManagerByFormat(format);
		if(exporter == null) {
			throw new AdempiereException("Unsupported format: " + format);
		}
		String fileName = getFileName();
		exporter.exportMigration(getCtx(), getRecord_ID(), fileName, get_TrxName());
		return fileName;
	}

	@Override
	protected String doIt() throws Exception {
		if (getRecord_ID() <= 0) {
			return "@NoMigrationFound@";  // No migration found
		}
		// "Exported migration to: " + fileName
		return "@ExportedMigrationXMLTo@: " + exportMigration();
	}
}
