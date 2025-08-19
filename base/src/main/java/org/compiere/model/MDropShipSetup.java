package org.compiere.model;

import org.adempiere.core.domains.models.I_C_DropShipSetup;
import org.adempiere.core.domains.models.X_C_DropShipSetup;

import java.sql.ResultSet;
import java.util.Properties;

/**
 *    @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 *	<a href="https://github.com/solop-develop/adempiere-base/issues/70">https://github.com/solop-develop/adempiere-base/issues/70</a>
 */
public class MDropShipSetup extends X_C_DropShipSetup {

    public MDropShipSetup(Properties ctx, int C_DropShipSetup_ID, String trxName) {
        super(ctx, C_DropShipSetup_ID, trxName);
    }

    public MDropShipSetup(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    public static void setDropShipFromSetup(MOrder salesOrder) {
        MDropShipSetup dropShipSetup = getSetupFromSalesOrder(salesOrder);
        if(dropShipSetup == null) {
            return;
        }
        salesOrder.setDropShip_BPartner_ID(dropShipSetup.getDropShip_BPartner_ID());
        salesOrder.setDropShip_Location_ID(dropShipSetup.getDropShip_Location_ID());
        if(dropShipSetup.getDropShip_User_ID() > 0) {
            salesOrder.setDropShip_User_ID(dropShipSetup.getDropShip_User_ID());
        }
    }

    public static MDropShipSetup getSetupFromSalesOrder(MOrder salesOrder) {
        return new Query(salesOrder.getCtx(), I_C_DropShipSetup.Table_Name, "AD_Org_ID = ? AND (C_DocType_ID = ? OR C_DocType_ID IS NULL)", null)
                .setParameters(salesOrder.getAD_Org_ID(), salesOrder.getC_DocTypeTarget_ID())
                .setOnlyActiveRecords(true)
                .setClient_ID()
                .first();
    }

}
