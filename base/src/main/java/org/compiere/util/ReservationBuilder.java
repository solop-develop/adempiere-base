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
package org.compiere.util;

import org.compiere.model.*;
import org.eevolution.distribution.model.MDDOrderLine;

import java.math.BigDecimal;
import java.util.Properties;

//  Builder for Reservation objects
public class ReservationBuilder {
    private final MReservation reservation;
    public static ReservationBuilder newInstance(Properties context, String transactionName) {
        return new ReservationBuilder(context, transactionName);
    }

    private ReservationBuilder(Properties context, String transactionName) {
        reservation = new MReservation(context, 0, transactionName);
    }

    public ReservationBuilder withProductId(int productId) {
        reservation.setM_Product_ID(productId);
        return this;
    }

    public ReservationBuilder withWarehouseId(int warehouseId) {
        reservation.setM_Warehouse_ID(warehouseId);
        return this;
    }

    public ReservationBuilder withLocatorId(int locatorId) {
        reservation.setM_Locator_ID(locatorId);
        return this;
    }

    public ReservationBuilder withOrderLine(MOrderLine orderLine) {
        reservation.setC_OrderLine_ID(orderLine.getC_OrderLine_ID());
        reservation.setC_Order_ID(orderLine.getC_Order_ID());
        reservation.setM_Product_ID(orderLine.getM_Product_ID());
        MOrder order = orderLine.getParent();
        if(order.isSOTrx()) {
            reservation.setReservationType(MReservation.RESERVATIONTYPE_SOReserveQuantity);
        } else {
            reservation.setReservationType(MReservation.RESERVATIONTYPE_POOrderQuantity);
        }
        reservation.setM_Warehouse_ID(orderLine.getM_Warehouse_ID());
        return this;
    }

    public ReservationBuilder withInOutLine(MInOutLine inOutLine) {
        if(inOutLine.getC_OrderLine_ID() > 0) {
            reservation.setC_OrderLine_ID(inOutLine.getC_OrderLine_ID());
            MOrderLine orderLine = new MOrderLine(inOutLine.getCtx(), inOutLine.getC_OrderLine_ID(), inOutLine.get_TrxName());
            reservation.setC_Order_ID(orderLine.getC_Order_ID());
            reservation.setM_InOutLine_ID(inOutLine.getM_InOutLine_ID());
            reservation.setM_InOut_ID(inOutLine.getM_InOut_ID());
            reservation.setM_Product_ID(orderLine.getM_Product_ID());
            MOrder order = orderLine.getParent();
            if(order.isSOTrx()) {
                reservation.setReservationType(MReservation.RESERVATIONTYPE_SODeliveryQuantity);
            } else {
                reservation.setReservationType(MReservation.RESERVATIONTYPE_POReceiptQuantity);
            }
            reservation.setM_Warehouse_ID(orderLine.getM_Warehouse_ID());
            reservation.setQty(inOutLine.getMovementQty().negate());
        }
        return this;
    }

    public ReservationBuilder withDistributionOrderLine(MDDOrderLine orderLine, BigDecimal currentReservation, boolean isToLocator) {
        reservation.setDD_OrderLine_ID(orderLine.getDD_OrderLine_ID());
        reservation.setDD_Order_ID(orderLine.getDD_Order_ID());
        reservation.setM_Product_ID(orderLine.getM_Product_ID());
        reservation.setReservationType(MReservation.RESERVATIONTYPE_DistributionOrderQuantity);
        return this;
    }

    public ReservationBuilder withMovementLine(MMovementLine movementLine) {
        if(movementLine.getDD_OrderLine_ID() > 0) {
            reservation.setDD_OrderLine_ID(movementLine.getDD_OrderLine_ID());
            MDDOrderLine orderLine = new MDDOrderLine(movementLine.getCtx(), movementLine.getDD_OrderLine_ID(), movementLine.get_TrxName());
            reservation.setDD_Order_ID(orderLine.getDD_Order_ID());
            reservation.setM_MovementLine_ID(movementLine.getM_MovementLine_ID());
            reservation.setM_Movement_ID(movementLine.getM_Movement_ID());
            reservation.setM_Product_ID(orderLine.getM_Product_ID());
            reservation.setReservationType(MReservation.RESERVATIONTYPE_DistributionMoveQuantity);
            reservation.setQty(movementLine.getMovementQty().negate());
        }
        return this;
    }

    public ReservationBuilder withProductionOrderLine(MProductionBatchLine orderLine) {
        reservation.setM_ProductionBatchLine_ID(orderLine.getM_ProductionBatchLine_ID());
        reservation.setM_ProductionBatch_ID(orderLine.getM_ProductionBatch_ID());
        reservation.setM_Product_ID(orderLine.getM_Product_ID());
        reservation.setReservationType(MReservation.RESERVATIONTYPE_ProductionOrderQuantity);
        return this;
    }

    public ReservationBuilder withProductionLine(MProductionLine productionLine) {
        if(productionLine.get_ValueAsInt("M_ProductionBatchLine_ID") > 0) {
            reservation.setM_ProductionBatchLine_ID(productionLine.get_ValueAsInt("M_ProductionBatchLine_ID"));
            MProductionBatchLine batchLine = new MProductionBatchLine(productionLine.getCtx(), productionLine.get_ValueAsInt("M_ProductionBatchLine_ID"), productionLine.get_TrxName());
            reservation.setM_ProductionBatch_ID(batchLine.getM_ProductionBatch_ID());
            reservation.setM_ProductionLine_ID(productionLine.getM_ProductionLine_ID());
            reservation.setM_Production_ID(productionLine.getM_Production_ID());
            reservation.setM_Product_ID(productionLine.getM_Product_ID());
            reservation.setReservationType(MReservation.RESERVATIONTYPE_ProductionUseQuantity);
            reservation.setQty(productionLine.getMovementQty().negate());
        }
        return this;
    }

    public ReservationBuilder withQuantity(BigDecimal quantity) {
        reservation.setQty(quantity);
        return this;
    }

    private boolean isValid() {
        return reservation.getReservationType() != null;
    }

    public MReservation build() {
        if(!isValid()) {
            return null;
        }
        reservation.saveEx();
        return reservation;
    }
}
