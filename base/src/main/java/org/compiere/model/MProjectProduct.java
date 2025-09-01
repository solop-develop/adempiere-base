package org.compiere.model;
import org.adempiere.core.domains.models.I_C_ProjectProduct;
import org.adempiere.core.domains.models.X_C_ProjectProduct;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *	<a href="https://github.com/solop-develop/adempiere-base/issues/70">https://github.com/solop-develop/adempiere-base/issues/70</a>
 */
public class MProjectProduct extends X_C_ProjectProduct {

    public MProjectProduct(Properties ctx, int C_ProjectProduct_ID, String trxName) {
        super(ctx, C_ProjectProduct_ID, trxName);
    }

    public MProjectProduct(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static List<Integer> getFromProject(MProject project) {
        return new Query(project.getCtx(), I_C_ProjectProduct.Table_Name, "C_Project_ID = ?", project.get_TrxName())
                .setParameters(project.getC_Project_ID())
                .getIDsAsList();
    }
}