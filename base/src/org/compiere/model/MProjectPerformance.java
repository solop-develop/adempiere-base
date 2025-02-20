package org.compiere.model;

import org.adempiere.core.domains.models.X_C_Project_Performance;

import java.sql.ResultSet;
import java.util.Properties;

public class MProjectPerformance extends X_C_Project_Performance {
    public MProjectPerformance(Properties ctx, int C_Project_Performance_ID, String trxName) {
        super(ctx, C_Project_Performance_ID, trxName);
    }

    public MProjectPerformance(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

}
