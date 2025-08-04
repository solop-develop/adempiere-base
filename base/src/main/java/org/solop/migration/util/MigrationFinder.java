/**
 * 
 */
package org.solop.migration.util;


import org.adempiere.core.domains.models.I_AD_Migration;
import org.compiere.model.MMigration;
import org.compiere.model.Query;
import org.compiere.util.Env;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 */
public class MigrationFinder {
    private final Map<String, Boolean> currentMigrations = new HashMap<>();

    public MigrationFinder() {
        loadCurrentMigrations();
    }

    public static MigrationFinder newInstance() {
        return new MigrationFinder();
    }

    private String getMigrationKey(String entityType, String releaseNo, String name, int sequence) {
        return entityType + "|" + Optional.ofNullable(releaseNo).orElse("") + "|" + name + "|" + sequence;
    }

    public boolean existsMigration(String entityType, String releaseNo, String name, int sequence) {
        return currentMigrations.getOrDefault(getMigrationKey(entityType, releaseNo, name, sequence), false);
    }

    private void loadCurrentMigrations() {
        new Query(Env.getCtx(), I_AD_Migration.Table_Name, null, null)
                .setOnlyActiveRecords(true)
                .getIDsAsList()
                .parallelStream()
                .forEach(migrationId -> {
                    MMigration migration = new MMigration(Env.getCtx(), migrationId, null);
                    String name = migration.getName().trim();
                    currentMigrations.put(getMigrationKey(migration.getEntityType(), migration.getReleaseNo(), name, migration.getSeqNo()), true);
                });
    }
}