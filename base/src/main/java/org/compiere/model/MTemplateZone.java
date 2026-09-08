package org.compiere.model;

import org.adempiere.core.domains.models.I_AD_DashboardTemplate;
import org.adempiere.core.domains.models.X_AD_TemplateZone;
import org.adempiere.exceptions.AdempiereException;

import java.sql.ResultSet;
import java.util.Properties;

public class MTemplateZone extends X_AD_TemplateZone {

    public MTemplateZone(Properties ctx, int AD_TemplateZone_ID, String trxName) {
        super(ctx, AD_TemplateZone_ID, trxName);
    }

    public MTemplateZone(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        if (getAD_DashboardTemplate_ID() > 0) {
            I_AD_DashboardTemplate template = getAD_DashboardTemplate();
            int gridColumns = template != null ? template.getGridColumns() : 0;
            if (gridColumns > 0 && (getPosX() + getWidth()) > gridColumns) {
                throw new AdempiereException("@PosX@ + @Width@ <= @GridColumns@");
            }
        }
        return super.beforeSave(newRecord);
    }

}
