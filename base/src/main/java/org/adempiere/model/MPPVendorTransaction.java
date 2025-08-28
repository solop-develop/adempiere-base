package org.adempiere.model;
import org.adempiere.core.domains.models.X_C_PPVendorTransaction;

import java.sql.ResultSet;
import java.util.Properties;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *	<a href="https://github.com/solop-develop/adempiere-base/issues/338">https://github.com/solop-develop/adempiere-base/issues/338</a>
 */
public class MPPVendorTransaction extends X_C_PPVendorTransaction {

    public MPPVendorTransaction(Properties ctx, int C_PPVendorTransaction_ID, String trxName) {
        super(ctx, C_PPVendorTransaction_ID, trxName);
    }

    public MPPVendorTransaction(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }
}