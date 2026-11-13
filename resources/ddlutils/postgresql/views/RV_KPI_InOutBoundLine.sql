CREATE OR REPLACE VIEW RV_KPI_InOutBoundLine
(RV_KPI_InOutBoundLine_ID, AD_Client_ID, AD_Org_ID, IsActive,
 WM_InOutBoundLine_ID, WM_InOutBound_ID, DocumentNo, POReference, TrackingNo,
 C_BPartner_ID, C_Order_ID, C_OrderLine_ID, C_DocType_ID, DocStatus, IsSOTrx,
 SalesRep_ID, M_Warehouse_ID, M_Shipper_ID, C_Project_ID, C_Campaign_ID, C_Activity_ID, AD_OrgTrx_ID,
 DeliveryRule, DeliveryViaRule, PriorityRule, IsInTransit,
 M_Product_ID, M_Product_Category_ID, M_Product_Group_ID, C_UOM_ID,
 MovementQty, PickedQty, DateTrx, PickDate, ShipDate, LineCount)
AS
SELECT v.WM_InOutBoundLine_ID,
    v.AD_Client_ID, v.AD_Org_ID, v.IsActive,
    v.WM_InOutBoundLine_ID, v.WM_InOutBound_ID, v.DocumentNo, v.POReference, v.TrackingNo,
    v.C_BPartner_ID, v.C_Order_ID, v.C_OrderLine_ID, v.C_DocType_ID, v.DocStatus, v.IsSOTrx,
    v.SalesRep_ID, v.M_Warehouse_ID, v.M_Shipper_ID, v.C_Project_ID, v.C_Campaign_ID, v.C_Activity_ID, v.AD_OrgTrx_ID,
    v.DeliveryRule, v.DeliveryViaRule, v.PriorityRule, v.IsInTransit,
    v.M_Product_ID, p.M_Product_Category_ID, p.M_Product_Group_ID, v.C_UOM_ID,
    v.MovementQty, v.PickedQty, h.DateTrx, v.PickDate, v.ShipDate,
    1
FROM RV_WM_InOutBoundLine v
    INNER JOIN WM_InOutBound h ON (h.WM_InOutBound_ID = v.WM_InOutBound_ID)
    LEFT JOIN M_Product p ON (p.M_Product_ID = v.M_Product_ID)
WHERE v.IsDescription = 'N';
