/**
 * 
 */
package org.solop.migration.util;

import java.util.HashMap;
import java.util.Map;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 */
public class MigrationFactory {
	public static final String FORMAT_XML = "xml";
	public static final String FORMAT_PROTOBUF = "protobuf";
	private final Map<String, IMigrationManagement> migrationsByFormat = new HashMap<>();
	private final Map<String, IMigrationManagement> migrationsByExtension = new HashMap<>();

	private MigrationFactory() {
		registerMigration(FORMAT_XML, new XMLMigration());
		registerMigration(FORMAT_PROTOBUF, new ProtoMigration());
	}

	public static MigrationFactory newInstance() {
		return new MigrationFactory();
	}

	private void registerMigration(String format, IMigrationManagement migrationManager) {
		migrationsByFormat.put(format, migrationManager);
		migrationsByExtension.put(migrationManager.getExtension(), migrationManager);
	}

	public IMigrationManagement getMigrationManagerByFormat(String format) {
		return migrationsByFormat.get(format);
	}

	public IMigrationManagement getMigrationManagerByExtension(String extension) {
		return migrationsByExtension.get(extension);
	}
}