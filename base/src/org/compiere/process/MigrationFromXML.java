package org.compiere.process;

import org.adempiere.exceptions.AdempiereException;
import org.apache.commons.io.FileUtils;
import org.compiere.model.MMigration;
import org.compiere.model.MSysConfig;
import org.compiere.util.Ini;
import org.compiere.util.Msg;
import org.compiere.util.TimeUtil;
import org.compiere.util.Trx;
import org.eevolution.services.dsl.ProcessBuilder;
import org.solop.migration.util.IMigrationManagement;
import org.solop.migration.util.MigrationFactory;
import org.solop.migration.util.MigrationFinder;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.*;
import java.util.logging.Level;

// This process is called from the Application Dictionary Menu to load Migrations from XML files.
// It is also called from the MigrationLoader which is called by RUN_MigrateXML.
public class MigrationFromXML extends MigrationFromXMLAbstract {
	private final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	private final Map<String, Integer> migrationsToApply = new HashMap<>();
	private final MigrationFactory migrationFactory = MigrationFactory.newInstance();
	private MigrationFinder finder;
	private IMigrationManagement importer;

	@Override
	protected String doIt() throws Exception {
		if (Ini.isPropertyBool(Ini.P_LOGMIGRATIONSCRIPT)) {
			addLog( Msg.getMsg(getCtx(), "LogMigrationScriptFlagIsSetMessage"));
			return "@Error@" + Msg.getMsg(getCtx(), "LogMigrationScriptFlagIsSet");
		}
		String format = MSysConfig.getValue("MIGRATION_DEFAULT_FORMAT", MigrationFactory.FORMAT_XML);
		finder = MigrationFinder.newInstance();
		importer = migrationFactory.getMigrationManagerByFormat(format);
		if(importer == null) {
			throw new AdempiereException("Unsupported format: " + format);
		}
		long startTime = System.currentTimeMillis();
		List<File> migrations = loadFiles();
		long loadElapsed = System.currentTimeMillis() - startTime;
		log.info("Load Files -> Time elapsed: " + TimeUtil.formatElapsed(loadElapsed));
		long importStartTime = System.currentTimeMillis();
		List<Integer> migrationsIds = importMigration(migrations);
		long importElapsed = System.currentTimeMillis() - importStartTime;
		log.info("Saving Migrations -> Time elapsed: " + TimeUtil.formatElapsed(importElapsed));
		long applyStartTime = System.currentTimeMillis();
		applyMigrations(migrationsIds);
		long applyElapsed = System.currentTimeMillis() - applyStartTime;
		log.info("Apply Migrations -> Time elapsed: " + TimeUtil.formatElapsed(applyElapsed));
		long elapsed = System.currentTimeMillis() - startTime;
		log.info("Total Time elapsed: " + TimeUtil.formatElapsed(elapsed));
		return "Import complete";
	}

	public List<Integer> importMigration(List<File> migrations) {
		return importer.importMigration(migrations, finder, get_TrxName());
	}

	private void applyMigrations(List<Integer> migrationsIds) {
		if(!isApply()) {
			return;
		}
		migrationsIds.forEach(migrationId -> {
			Trx.run(transactionName -> {
				try {
					applyMigration(getCtx(), migrationId, transactionName);
				} catch (Exception e) {
					if (!isForce()) {
						throw new AdempiereException(e);
					} else {
						log.log(Level.SEVERE, e.getLocalizedMessage());
					}
				}
			});
		});
	}

	public Comparator<File> fileComparator = new Comparator<File>() {
		// Note - Not locale sensitive.
	    public int compare(File f1, File f2) {
	        return f1.getName().compareToIgnoreCase(f2.getName());
	    }
	};
		
	/**
	 * Load the XML migration file or files.  
	 */
	private List<File> loadFiles() {
		List<File> migrationFiles = new ArrayList<File>();
		File file = new File(getFilePathOrName());
		try {
			if(!file.exists()) {
				log.log(Level.WARNING, "No file or directory found");
				return migrationFiles;
			} else if(file.isDirectory()) {
				log.log(Level.CONFIG, "Processing migration files in directory: " + file.getAbsolutePath() );
				// Recursively find files
				migrationFiles = (List<File>) FileUtils.listFiles(file, new String[]{importer.getExtension(), importer.getExtension().toLowerCase()}, true);
				migrationFiles.sort(fileComparator);
			} else {
				if(!file.getAbsolutePath().toLowerCase().endsWith(importer.getExtension())) {
					throw new AdempiereException("Invalid Extension, only is allowed " + importer.getExtension());
				}
				log.log(Level.CONFIG, "Processing migration file: " + file.getAbsolutePath());
				migrationFiles.add(file);
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getLocalizedMessage());
			if (!isForce() || migrationFiles.isEmpty()) {
				throw new AdempiereException("Loading Migration from XML failed.", e);
			}
		}
		return migrationFiles;
	}

	private void applyMigration(Properties ctx  , int migrationId, String trxName) throws AdempiereException {
		ProcessInfo processInfo = ProcessBuilder.create(ctx)
				.process(MigrationApply.getProcessId())
				.withTitle("Apply migration")
				.withRecordId(MMigration.Table_ID , migrationId)
				.withParameter("FailOnError",true)
				.withParameter(ISFORCE, isForce())
				.execute(trxName);

		log.log(Level.INFO, "Process=" + processInfo.getTitle() + " Error="+processInfo.isError() + " Summary=" + processInfo.getSummary());
		if (processInfo.isError()) {
			throw new AdempiereException(processInfo.getSummary());
		}
	}
}