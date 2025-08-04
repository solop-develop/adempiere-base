/**
 * 
 */
package org.solop.migration.util;

import org.adempiere.core.domains.models.I_AD_MigrationData;
import org.adempiere.core.domains.models.I_AD_MigrationStep;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MMigration;
import org.compiere.model.MMigrationData;
import org.compiere.model.MMigrationStep;
import org.compiere.model.Query;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;
import org.solop.grpc.updates.Step;
import org.solop.grpc.updates.StepValue;
import org.solop.grpc.updates.Update;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 */
public class ProtoMigration implements IMigrationManagement {
	private static final int BATCH_SIZE = 500;
	private final CLogger log = CLogger.getCLogger(this.getClass());
	public void exportMigration(Properties context, int migrationId, String fileName, String transactionName) {
		MMigration migration = new MMigration(context, migrationId, transactionName);
		Update.Builder updateBuilder = Update.newBuilder()
				.setEntityType(migration.getEntityType())
				.setSequence(migration.getSeqNo())
				.setReleaseNo(Optional.ofNullable(migration.getReleaseNo()).orElse(""))
				.setName(migration.getName())
				.setComments(Optional.ofNullable(migration.getComments()).orElse(""))
				;
		new Query(context, I_AD_MigrationStep.Table_Name, "AD_Migration_ID = ?", transactionName)
				.setParameters(migrationId)
				.getIDsAsList()
				.parallelStream()
				.forEach(stepId -> {
			MMigrationStep step = new MMigrationStep(context, stepId, transactionName);
			Step.Builder stepBuilder = Step.newBuilder()
					.setSequence(step.getSeqNo())
					.setStepType(step.getStepType())
					.setComments(Optional.ofNullable(step.getComments()).orElse(""))
			;
			if(MMigrationStep.STEPTYPE_SQLStatement.equals(step.getStepType())) {
				stepBuilder
						.setDatabaseType(Optional.ofNullable(step.getDBType()).orElse(""))
						.setIsParsed(step.isParse())
						.setSqlStatement(Optional.ofNullable(step.getSQLStatement()).orElse(""))
						.setRollbackStatement(Optional.ofNullable(step.getRollbackStatement()).orElse(""))
				;
			} else if(MMigrationStep.STEPTYPE_ApplicationDictionary.equals(step.getStepType())) {
				stepBuilder
						.setAction(step.getAction())
						.setTableId(step.getAD_Table_ID())
						.setRecordId(step.getRecord_ID())
				;
				new Query(context, I_AD_MigrationData.Table_Name, "AD_MigrationStep_ID = ?", transactionName)
						.setParameters(stepId)
						.getIDsAsList()
						.forEach(dataId -> {
					MMigrationData data = new MMigrationData(context, dataId, transactionName);
					StepValue.Builder stepValueBuilder = StepValue.newBuilder()
							.setColumnId(data.getAD_Column_ID())
					;
					if(MMigrationStep.ACTION_Insert.equals(step.getAction())) {
						stepValueBuilder.setIsOldNull(data.isOldNull());
						if(!data.isOldNull()) {
							stepValueBuilder.setOldValue(Optional.ofNullable(data.getOldValue()).orElse(""));
						}
					}
					stepValueBuilder.setIsNewNull(data.isNewNull() || data.getNewValue() == null);
					if(data.getNewValue() != null) {
						stepValueBuilder.setNewValue(Optional.ofNullable(data.getNewValue()).orElse(""));
					}
					stepBuilder.addStepValues(stepValueBuilder);
				});
			}
			updateBuilder.addSteps(stepBuilder);
		});
		Update update = updateBuilder.build();
		try (FileOutputStream outputStream = new FileOutputStream(fileName)) {
			update.writeTo(outputStream);
			log.fine("File saved: " + fileName);
		} catch (IOException e) {
			throw new AdempiereException(e);
		}
	}

	@Override
	public String getExtension() {
		return "bin";
	}

	@Override
	public List<Integer> importMigration(List<File> files, MigrationFinder finder, String transactionName) {
		if(files == null || files.isEmpty()) {
			return List.of();
		}
		List<Integer> migrationsToApply = new ArrayList<>();
		files.parallelStream().forEach(file ->{
			try {
				int migrationId = loadFile(file, finder);
				if(migrationId > 0) {
					migrationsToApply.add(migrationId);
				}
			} catch (Exception e) {
				log.log(Level.SEVERE, e.getLocalizedMessage());
			}
		});
		return migrationsToApply;
	}

	private int loadFile(File file, MigrationFinder finder) {
		AtomicInteger migrationReferenceId = new AtomicInteger(-1);
		try {
			if (!file.exists() || !file.getName().endsWith(".bin")) {
				log.log(Level.CONFIG, "Wrong Format: " + file);
				return -1;
			}
			log.log(Level.CONFIG, "Loading file: " + file);
			try (FileInputStream inputStream = new FileInputStream(file)) {
				Update update = Update.parseFrom(inputStream);
				Properties ctx = Env.getCtx();
				MMigration migration = saveMigration(ctx, update, finder);
				if(migration != null) {
					migrationReferenceId.set(migration.getAD_Migration_ID());
				}
				log.log(Level.CONFIG, "File Loaded: " + file);
			} catch (IOException e) {
				log.log(Level.SEVERE, e.getLocalizedMessage());
			}
		} catch (Exception e) {
			log.log(Level.SEVERE, e.getLocalizedMessage());
		}
		return migrationReferenceId.get();
	}

	private MMigration saveMigration(Properties ctx, Update update, MigrationFinder finder) {
		String name = update.getName().trim();
		int sequence = update.getSequence();
		String entityType = update.getEntityType();
		String releaseNo = update.getReleaseNo();
		if(finder.existsMigration(entityType, releaseNo, name, sequence)) {
			log.log(Level.CONFIG, "Already exist migration: " + update.getName());
			return null;
		}
		MMigration migration = new MMigration(ctx, 0, null);
		migration.setName(name);
		migration.setSeqNo(sequence);
		migration.setEntityType(entityType);
		migration.setReleaseNo(releaseNo);
		migration.setComments(update.getComments());
		migration.saveEx();
		//	Save all steps
		saveSteps(ctx, migration, update.getStepsList());
		return migration;
	}

	private void saveSteps(Properties ctx, MMigration migration, List<Step> steps) {
		String sql = "INSERT INTO AD_MigrationStep (" +
				"AD_Client_ID, AD_Org_ID, Created, CreatedBy, Updated, UpdatedBy, IsActive, UUID, AD_MigrationStep_ID, " +
				"AD_Migration_ID, Action, AD_Table_ID, Comments, " +
				"DBType, Parse, Record_ID, RollbackStatement, " +
				"SeqNo, SQLStatement, StepType, StatusCode" +
				") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		AtomicReference<PreparedStatement> statementReference = new AtomicReference<>();;
		try {
			statementReference.set(DB.prepareStatement(sql, null));
			steps.forEach(step -> {
				int column = 1;
				try {
					PreparedStatement statement = statementReference.get();
					int stepId = DB.getNextID(ctx, "AD_MigrationStep", null);
					statement.setInt(column++, migration.getAD_Client_ID());
					statement.setInt(column++, migration.getAD_Org_ID());
					statement.setTimestamp(column++, migration.getCreated());
					statement.setInt(column++, migration.getCreatedBy());
					statement.setTimestamp(column++, migration.getUpdated());
					statement.setInt(column++, migration.getUpdatedBy());
					statement.setString(column++, "Y");
					statement.setString(column++, DB.getUUID(null));
					statement.setInt(column++, stepId);
					statement.setInt(column++, migration.getAD_Migration_ID());
					//	Fill Rest of Columns
					statement.setString(column++, getValidValue(step.getAction()));
					statement.setInt(column++, step.getTableId());
					statement.setString(column++, getValidValue(step.getComments()));
					statement.setString(column++, getValidValue(step.getDatabaseType()));
					statement.setString(column++, step.getIsParsed() ? "Y": "N");
					statement.setInt(column++, step.getRecordId());
					statement.setString(column++, getValidValue(step.getRollbackStatement()));
					statement.setInt(column++, step.getSequence());
					statement.setString(column++, getValidValue(step.getSqlStatement()));
					statement.setString(column++, getValidValue(step.getStepType()));
					statement.setString(column, "U");
					statement.execute();
					//	Save Values
					if(MMigrationStep.STEPTYPE_ApplicationDictionary.equals(step.getStepType())) {
						saveNodes(ctx, migration, stepId, step.getStepValuesList());
					}
				} catch (SQLException e) {
                    throw new RuntimeException(e);
                }
			});
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			throw new AdempiereException(e);
		} finally {
			DB.close(statementReference.get());
		}
	}

	private String getValidValue(String value) {
		return Util.isEmpty(value)? null: value;
	}

	private void saveNodes(Properties ctx, MMigration migration, int stepId, List<StepValue> values) {
		String sql = "INSERT INTO AD_MigrationData (" +
				"AD_Client_ID, AD_Org_ID, Created, CreatedBy, Updated, UpdatedBy, IsActive, UUID, AD_MigrationData_ID, " +
				"AD_MigrationStep_ID, AD_Column_ID, BackupValue, IsBackupNull, IsNewNull, IsOldNull, NewValue, OldValue" +
				") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		AtomicReference<PreparedStatement> statementReference = new AtomicReference<>();;
		try {
			statementReference.set(DB.prepareStatement(sql, null));
			AtomicInteger count = new AtomicInteger(0);
			values.forEach(value -> {
				int column = 1;
				try {
					PreparedStatement statement = statementReference.get();
					int stepValueId = DB.getNextID(ctx, "AD_MigrationData", null);
					statement.setInt(column++, migration.getAD_Client_ID());
					statement.setInt(column++, migration.getAD_Org_ID());
					statement.setTimestamp(column++, migration.getCreated());
					statement.setInt(column++, migration.getCreatedBy());
					statement.setTimestamp(column++, migration.getUpdated());
					statement.setInt(column++, migration.getUpdatedBy());
					statement.setString(column++, "Y");
					statement.setString(column++, DB.getUUID(null));
					statement.setInt(column++, stepValueId);
					statement.setInt(column++, stepId);
					//	Fill Rest of Columns
					statement.setInt(column++, value.getColumnId());
					statement.setString(column++, value.getBackupValue());
					statement.setString(column++, value.getIsBackupNull() ? "Y": "N");
					statement.setString(column++, value.getIsNewNull() ? "Y": "N");
					statement.setString(column++, value.getIsOldNull() ? "Y": "N");
					statement.setString(column++, value.getNewValue());
					statement.setString(column++, value.getOldValue());
					statement.addBatch();
					count.getAndIncrement();
					if (count.get() % BATCH_SIZE == 0) {
						statement.executeBatch();
						statement.clearBatch();
					}
				} catch (SQLException e) {
					throw new RuntimeException(e);
				}
			});
			statementReference.get().executeBatch();
		} catch (Exception e) {
			log.severe(e.getLocalizedMessage());
			throw new AdempiereException(e);
		} finally {
			DB.close(statementReference.get());
		}
	}

	@Override
	public String toString() {
		return "ProtoMigration: " + getExtension();
	}
}