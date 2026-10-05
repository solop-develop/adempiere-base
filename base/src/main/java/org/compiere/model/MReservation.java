/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2016 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net or http://www.adempiere.net/license.html         *
 *****************************************************************************/
package org.compiere.model;

import org.adempiere.core.domains.models.X_M_Reservation;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.Optional;
import java.util.Properties;

public class MReservation extends X_M_Reservation {

    /** Expected sign for a shipment/receipt reservation, calculated once (null = not calculated) */
    private Integer expectedInOutSign = null;

    public MReservation(Properties ctx, int M_Reservation_ID, String trxName) {
        super(ctx, M_Reservation_ID, trxName);
    }

    public MReservation(Properties ctx, ResultSet rs, String trxName) {
        super(ctx, rs, trxName);
    }

    /**
     * Expected sign of the reservation change caused by a shipment/receipt line.
     * Same rule as MInOut.completeIt for QtySO / QtyPO (the change of C_OrderLine.QtyReserved):
     * a shipment (C-) or a vendor receipt (V+) decreases the pending quantity, a customer return (C+)
     * or a vendor return (V-) increases it, and a negative MovementQty (reversal) inverts the sign.
     * @param inOutLine shipment/receipt line
     * @return -1, 1, or 0 when no reservation applies (no order line, zero quantity or return order)
     */
    public static int getExpectedInOutSign(MInOutLine inOutLine) {
        if (inOutLine == null || inOutLine.getC_OrderLine_ID() <= 0) {
            return 0;
        }
        BigDecimal movementQuantity = Optional.ofNullable(inOutLine.getMovementQty()).orElse(Env.ZERO);
        if (movementQuantity.signum() == 0) {
            return 0;
        }
        MOrderLine orderLine = new MOrderLine(inOutLine.getCtx(), inOutLine.getC_OrderLine_ID(), inOutLine.get_TrxName());
        if (orderLine.getParent().isReturnOrder()) {
            return 0;
        }
        String movementType = inOutLine.getParent().getMovementType();
        boolean isDecreasePending = MInOut.MOVEMENTTYPE_CustomerShipment.equals(movementType)
                || MInOut.MOVEMENTTYPE_VendorReceipts.equals(movementType);
        if (isDecreasePending) {
            return -movementQuantity.signum();
        }
        return movementQuantity.signum();
    }

    /**
     * Expected sign of this reservation when it comes from a shipment/receipt line
     * @return -1, 1, or 0 when it does not come from a shipment/receipt line or no reservation applies
     */
    public int getExpectedInOutSign() {
        if (expectedInOutSign == null) {
            if (getM_InOutLine_ID() > 0) {
                MInOutLine inOutLine = new MInOutLine(getCtx(), getM_InOutLine_ID(), get_TrxName());
                expectedInOutSign = getExpectedInOutSign(inOutLine);
            } else {
                expectedInOutSign = 0;
            }
        }
        return expectedInOutSign;
    }

    @Override
    public void setM_InOutLine_ID(int M_InOutLine_ID) {
        super.setM_InOutLine_ID(M_InOutLine_ID);
        expectedInOutSign = null;
    }

    @Override
    protected boolean beforeSave(boolean newRecord) {
        //	The sign of a shipment/receipt reservation is defined by the movement, not by the caller.
        //	A wrong sign is corrected (not rejected) so the user operation is never interrupted.
        if (newRecord && getM_InOutLine_ID() > 0) {
            int expectedSign = getExpectedInOutSign();
            BigDecimal quantity = Optional.ofNullable(getQty()).orElse(Env.ZERO);
            if (expectedSign != 0 && quantity.signum() != 0 && quantity.signum() != expectedSign) {
                BigDecimal correctedQuantity = quantity.abs().multiply(BigDecimal.valueOf(expectedSign));
                MInOutLine inOutLine = new MInOutLine(getCtx(), getM_InOutLine_ID(), get_TrxName());
                log.warning(
                    "Reservation quantity sign corrected - M_InOutLine_ID=" + getM_InOutLine_ID()
                    + ", MovementType=" + inOutLine.getParent().getMovementType()
                    + ", MovementQty=" + inOutLine.getMovementQty()
                    + ", Received=" + quantity
                    + ", Used=" + correctedQuantity
                );
                setQty(correctedQuantity);
            }
        }
        return true;
    }

}
