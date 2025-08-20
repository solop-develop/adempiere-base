package org.compiere.model;
import org.adempiere.core.domains.models.I_C_Card;
import org.adempiere.core.domains.models.X_C_Card;
import org.compiere.util.Env;

import java.sql.ResultSet;
import java.util.Properties;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *	<a href="https://github.com/solop-develop/adempiere-base/issues/328">https://github.com/solop-develop/adempiere-base/issues/328</a>
 */
public class MCard extends X_C_Card {

    public MCard(Properties ctx, int C_Card_ID, String trxName) {
        super(ctx, C_Card_ID, trxName);
    }

    public MCard(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static MCard getFromValue(String value) {
        return new Query(Env.getCtx(), I_C_Card.Table_Name, "Value = ?", null)
                .setParameters(value)
                .first();
    }
}