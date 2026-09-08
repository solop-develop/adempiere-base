package org.compiere.model;

import org.adempiere.core.domains.models.X_AD_DashboardInstance;
import org.adempiere.exceptions.AdempiereException;

import java.sql.ResultSet;
import java.util.Properties;

public class MDashboardInstance extends X_AD_DashboardInstance {

    public MDashboardInstance(Properties ctx, int AD_DashboardInstance_ID, String trxName) {
        super(ctx, AD_DashboardInstance_ID, trxName);
    }

    public MDashboardInstance(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        String ownerType = getOwnerType();
        if (OWNERTYPE_User.equals(ownerType)) {
            if (getAD_User_ID() <= 0 || getAD_Role_ID() > 0) {
                throw new AdempiereException("@OwnerType@ = @AD_User_ID@");
            }
        } else if (OWNERTYPE_Role.equals(ownerType)) {
            if (getAD_Role_ID() <= 0 || getAD_User_ID() > 0) {
                throw new AdempiereException("@OwnerType@ = @AD_Role_ID@");
            }
        }
        return super.beforeSave(newRecord);
    }

}
