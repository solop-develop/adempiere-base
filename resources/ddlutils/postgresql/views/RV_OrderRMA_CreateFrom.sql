CREATE OR REPLACE VIEW RV_OrderRMA_CreateFrom AS
--  From InOut for RMA
SELECT l.AD_Client_ID, l.AD_Org_ID, l.CreatedBy, l.Created, l.UpdatedBy, l.Updated, l.IsActive, l.M_InOutLine_ID AS RV_OrderRMA_CreateFrom_ID, l.Line,
((CASE WHEN l.MovementQty = 0 THEN 0 ELSE l.QtyEntered / l.MovementQty END) * (l.MovementQty - SUM(COALESCE(ol.QtyOrdered, 0)))) QtyEntered,
l.C_UOM_ID, (l.MovementQty - SUM(COALESCE(ol.QtyOrdered, 0))) MovementQty,
(CASE WHEN l.MovementQty = 0 THEN 0 ELSE l.QtyEntered / l.MovementQty END) Multiplier,
COALESCE(p.Name, c.Name) AS Name, l.M_Product_ID, l.M_AttributeSetInstance_ID, l.C_Charge_ID, l.Description, po.VendorProductNo,
-- Reference
io.C_Order_ID, 0 AS C_Invoice_ID, io.M_InOut_ID, 0 AS M_RMA_ID, io.MovementDate AS DateDoc, io.C_BPartner_ID, io.DocStatus
FROM M_InOut io
INNER JOIN M_InOutLine l ON (l.M_InOut_ID = io.M_InOut_ID)
LEFT JOIN M_Product p ON (l.M_Product_ID = p.M_Product_ID)
LEFT JOIN C_Charge c ON (l.C_Charge_ID = c.C_Charge_ID)
LEFT JOIN (SELECT ol_sub.Ref_InOutLine_ID, ol_sub.QtyOrdered, o_sub.C_Currency_ID
                FROM C_Order o_sub
                INNER JOIN C_OrderLine ol_sub ON(ol_sub.C_Order_ID = o_sub.C_Order_ID)
                INNER JOIN C_DocType dt_sub ON(dt_sub.C_DocType_ID = COALESCE(NULLIF(o_sub.C_DocType_ID, 0), o_sub.C_DocTypeTarget_ID))
                WHERE o_sub.DocStatus NOT IN('VO', 'CL')
                AND dt_sub.DocSubTypeSO = 'RM'
                AND ol_sub.Ref_InOutLine_ID IS NOT NULL) ol ON (ol.Ref_InOutLine_ID = l.M_InOutLine_ID)
LEFT JOIN LATERAL (
    SELECT ppo.VendorProductNo
    FROM M_Product_PO ppo
    WHERE ppo.M_Product_ID = l.M_Product_ID
      AND ppo.C_BPartner_ID = io.C_BPartner_ID
      AND ppo.C_Currency_ID = ol.C_Currency_ID
      AND ppo.IsActive = 'Y'
      AND ppo.AD_Org_ID IN (0, io.AD_Org_ID)
    ORDER BY ppo.AD_Org_ID DESC LIMIT 1
) po ON true
WHERE l.MovementQty <> 0
GROUP BY l.AD_Client_ID, l.AD_Org_ID, l.CreatedBy, l.Created, l.UpdatedBy, l.Updated, l.IsActive, l.M_InOutLine_ID, l.Line,
l.MovementQty, l.QtyEntered,
l.C_UOM_ID, p.Name, c.Name, l.M_Product_ID, l.M_AttributeSetInstance_ID, l.C_Charge_ID, l.Description, po.VendorProductNo,
io.C_Order_ID, io.M_InOut_ID, io.MovementDate, io.C_BPartner_ID, io.DocStatus
HAVING (l.MovementQty - SUM(COALESCE(ol.QtyOrdered, 0)) <> 0);