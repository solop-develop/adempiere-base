package org.solop.util;

import org.compiere.model.MSequence;
import org.compiere.model.MSysConfig;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Util;

/**
 * Util class to get nextSequence SQL String depending on Database Type
 *
 * @author Gabriel Escalona
 */
public class SequenceUtil {

    public static String getNextSequenceSqlString(String tableName, boolean isSystem) {
        String result = "";
        boolean SYSTEM_NATIVE_SEQUENCE = MSysConfig.getBooleanValue("SYSTEM_NATIVE_SEQUENCE",false);
        if (SYSTEM_NATIVE_SEQUENCE && !isSystem) {
            if (DB.isPostgreSQL()) {
                result = getPostgreSQLNextValString(tableName);
            } else if (DB.isOracle()) {
                result = getOracleNextValString(tableName);
            } else if (DB.isMySQL()) {
                result = getMySQLNextValString(tableName);
            }
        }
        if (Util.isEmpty(result, true)) {
            result = getNextIDString(tableName, isSystem);
        }
        return result;
    }
    /**
     * Retrieves the next sequence value for PostgreSQL.
     */
    private static String getPostgreSQLNextValString(String tableName) {
        String result = null;
        String sequenceName = tableName + "_SEQ";
        String postgresSequenceSql = "SELECT 1 FROM information_schema.SEQUENCES WHERE lower(sequence_name) LIKE lower(?)";
        int pgValue = DB.getSQLValue(null, postgresSequenceSql, sequenceName);
        if (pgValue > 0) {
            result = " NEXTVAL('" + sequenceName + "') ";
        }
        return result;
    }

    /**
     * Retrieves the next sequence value for MySQL.
     * TODO: Has not been Tested
     */
    private static String getMySQLNextValString(String tableName) {
        String result = null;
        String mysqlSequenceSql = "SELECT AUTO_INCREMENT FROM information_schema.TABLES WHERE TABLE_NAME = ?";
        int myValue = DB.getSQLValue(null, mysqlSequenceSql, tableName);
        if (myValue > 0) {
            result = " AUTO_INCREMENT(" + tableName + ") ";
        }
        return result;
    }

    /**
     * Retrieves the next sequence value for Oracle.
     * TODO: Has not been Tested
     */
    private static String getOracleNextValString(String tableName) {
        String sequenceName = tableName + "_SEQ";
        String result = null;
        String oracleSequenceSql = "SELECT COUNT(*) FROM user_sequences WHERE UPPER(sequence_name) = ?";
        int oracleValue = DB.getSQLValue(null, oracleSequenceSql, sequenceName.toLowerCase());
        if (oracleValue > 0) {
            result = " (SELECT " + sequenceName.toUpperCase() + ".nextval FROM DUAL) ";
        }
        return result;
    }

    /**
     * Retrieves the next ID using the MSequence model.
     */
    private static String getNextIDString(String tableName, boolean isSystem) {
        MSequence sequence = MSequence.get(Env.getCtx(), tableName);
        return " nextID(" + sequence.getAD_Sequence_ID() + ", '" + (isSystem ? "Y": "N") + "') ";
    }
}
