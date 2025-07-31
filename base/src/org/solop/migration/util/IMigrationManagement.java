/**
 * 
 */
package org.solop.migration.util;

import java.io.File;
import java.util.List;
import java.util.Properties;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 */
public interface IMigrationManagement {
	void exportMigration(Properties context, int migrationId, String fileName, String transactionName);
	String getExtension();
	List<Integer> importMigration(List<File> files, MigrationFinder finder, String transactionName);
}
