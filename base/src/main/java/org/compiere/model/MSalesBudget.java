package org.compiere.model;
import org.adempiere.core.domains.models.X_C_SalesBudget;
import org.adempiere.exceptions.AdempiereException;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Optional;
import java.util.Properties;

/**
 *    @author Gabriel Escalona
 */
public class MSalesBudget extends X_C_SalesBudget {


    public MSalesBudget(Properties ctx, int C_SalesBudget_ID, String trxName) {
        super(ctx, C_SalesBudget_ID, trxName);
    }
    public MSalesBudget(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        if (getC_DocType_ID() <= 0) {
            Optional<MDocType> doctypeOptional = Arrays.stream(MDocType.getOfDocBaseType(getCtx(), MDocType.DOCBASETYPE_SalesBudget)).min((docType1, docType2) -> Boolean.compare(docType2.isDefault(), docType1.isDefault()));
            doctypeOptional.ifPresent(docType -> setC_DocType_ID(docType.getC_DocType_ID()));
            if (getC_DocType_ID() <= 0)
                throw new AdempiereException("@C_DocType_ID@ @FillMandatory@");

        }
        return super.beforeSave(newRecord);
    }

}