CREATE OR REPLACE VIEW RV_KPI_Replenish
(RV_KPI_Replenish_ID, AD_Client_ID, AD_Org_ID, IsActive,
 M_Product_ID, M_Product_Category_ID, M_Product_Group_ID, M_Product_Class_ID, M_Product_Classification_ID, C_UOM_ID,
 M_Warehouse_ID, M_WarehouseSource_ID, M_Locator_ID, C_BPartner_ID, ReplenishType,
 Level_Min, Level_Max, QtyAvailable, QtyOnHand, QtyReserved, QtyOrdered, QtyToOrder, Order_Min, Order_Pack)
AS
SELECT CAST(ROW_NUMBER() OVER (ORDER BY r.M_Warehouse_ID, r.M_Product_ID, r.M_Locator_ID) AS NUMERIC(10,0)),
    r.AD_Client_ID, r.AD_Org_ID, CAST('Y' AS CHAR(1)),
    r.M_Product_ID, r.M_Product_Category_ID, r.M_Product_Group_ID, r.M_Product_Class_ID, r.M_Product_Classification_ID, r.C_UOM_ID,
    r.M_Warehouse_ID, r.M_WarehouseSource_ID, r.M_Locator_ID, r.C_BPartner_ID, r.ReplenishType,
    r.Level_Min, r.Level_Max, r.QtyAvailable, r.QtyOnHand, r.QtyReserved, r.QtyOrdered, r.QtyToOrder, r.Order_Min, r.Order_Pack
FROM RV_M_Replenish r;
