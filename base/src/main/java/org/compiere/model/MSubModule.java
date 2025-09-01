package org.compiere.model;
import org.adempiere.core.domains.models.X_AD_SubModule;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *	<a href="https://github.com/solop-develop/adempiere-base/issues/333">https://github.com/solop-develop/adempiere-base/issues/333</a>
 */
public class MSubModule extends X_AD_SubModule {

    public MSubModule(Properties ctx, int AD_SubModule_ID, String trxName) {
        super(ctx, AD_SubModule_ID, trxName);
    }

    public MSubModule(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static MCard getFromValue(String value) {
        return new Query(Env.getCtx(), Table_Name, "Value = ?", null)
                .setParameters(value)
                .first();
    }
}