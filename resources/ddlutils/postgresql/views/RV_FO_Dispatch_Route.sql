CREATE OR REPLACE VIEW RV_FO_Dispatch_Route
        AS SELECT io.AD_Client_ID,
        io.AD_Org_ID,
        io.IsActive,
        o.C_BPartner_ID,
        io.WM_InOutbound_ID,
        p.UnitsPerPallet,
        FLOOR(
        CASE
        WHEN COALESCE(p.UnitsPerPallet, 0::NUMERIC) > 0::NUMERIC THEN SUM(iol.MovementQty) / p.UnitsPerPallet
        ELSE 0::NUMERIC
        END) AS QtyBatchs,
        SUM(iol.MovementQty) - FLOOR(
        CASE
        WHEN COALESCE(p.UnitsPerPallet, 0::NUMERIC) > 0::NUMERIC THEN SUM(iol.MovementQty) / p.UnitsPerPallet
        ELSE 0::NUMERIC
        END) * COALESCE(p.UnitsPerPallet, 0::NUMERIC) AS PickedQty,
        SUM(iol.MovementQty) AS Qty,
        SUM(iol.MovementQty * p.Weight) AS Weight,
        SUM(iol.MovementQty * p.Volume) AS Volume,
        iol.C_UOM_ID,
        o.C_Order_ID,
        COALESCE(loc.Address1, '') || COALESCE(', ' || loc.Address2, '') || COALESCE(', ' || loc.Address3, '') || COALESCE(', ' || loc.Address4, '') DirectionName,
        loc.City,
        i.DocumentNo,
        iol.C_Invoice_ID,
        i.PaymentRule,
        i.GrandTotal,
        NULL::TEXT AS Comments
        FROM WM_InOutbound io
        JOIN WM_InOutboundLine iol ON iol.WM_InOutbound_ID = io.WM_InOutbound_ID
        JOIN c_order o ON o.c_order_id = iol.c_order_id
        JOIN c_bpartner bp ON bp.C_BPartner_ID = o.C_BPartner_ID
        JOIN m_product p ON p.M_Product_ID = iol.M_Product_ID
        JOIN C_BPartner_Location bpl ON (bpl.C_BPartner_Location_ID = o.C_BPartner_Location_ID)
        JOIN C_Location loc ON (loc.C_Location_ID = bpl.C_Location_ID)
        LEFT JOIN C_Invoice i ON (i.C_Invoice_ID = iol.C_Invoice_ID)
        GROUP BY io.AD_Client_ID, io.AD_Org_ID, io.IsActive, o.C_BPartner_ID, iol.C_UOM_ID, io.WM_InOutbound_ID, p.UnitsPerPallet,o.C_Order_ID, loc.Address1,
        loc.Address2, loc.Address3, loc.Address4, loc.City, i.DocumentNo, iol.C_Invoice_ID, i.PaymentRule, i.Grandtotal
        ORDER BY o.C_BPartner_ID
        ;