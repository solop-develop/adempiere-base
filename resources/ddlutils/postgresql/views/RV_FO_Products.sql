CREATE OR REPLACE VIEW adempiere.RV_FO_Products
AS SELECT io.AD_Client_ID,
io.AD_Org_ID,
io.IsActive,
pc.M_Product_Category_ID,
pg.M_Product_Group_ID,
mpc.M_Product_Class_ID,
pcl.M_Product_Classification_ID,
isp.M_Industry_Sector_ID,
mg.M_Material_Group_ID,
mt.M_Material_Type_ID,
mpg.M_Purchase_Group_ID,
sg.M_Sales_Group_ID,
iol.M_Product_ID,
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
iol.M_Locator_ID,
iol.M_LocatorTo_ID
FROM WM_InOutbound io
JOIN WM_InOutboundLine iol ON iol.WM_InOutbound_ID = io.WM_InOutbound_ID
JOIN m_product p ON p.M_Product_ID = iol.M_Product_ID
LEFT JOIN m_product_category pc ON pc.m_product_category_id = p.m_product_category_id
LEFT JOIN M_Product_Group pg ON pg.M_Product_Group_ID = p.M_Product_Group_ID
LEFT JOIN M_Product_Class mpc ON mpc.M_Product_Class_ID = p.M_Product_Class_ID
LEFT JOIN M_Product_Classification pcl ON pcl.M_Product_Classification_ID = p.M_Product_Classification_ID
LEFT JOIN M_Industry_Sector isp ON isp.M_Industry_Sector_ID = p.M_Industry_Sector_ID
LEFT JOIN M_Material_Group mg ON mg.M_Material_Group_ID = p.M_Material_Group_ID
LEFT JOIN M_Material_Type mt ON mt.M_Material_Type_ID = p.M_Material_Type_ID
LEFT JOIN M_Purchase_Group mpg ON mpg.M_Purchase_Group_ID = p.M_Purchase_Group_ID
LEFT JOIN M_Sales_Group sg ON sg.M_Sales_Group_ID = p.M_Sales_Group_ID
GROUP BY pc.M_Product_Category_ID, pg.M_Product_Group_ID, mpc.M_Product_Class_ID, pcl.M_Product_Classification_ID, isp.M_Industry_Sector_ID,
mg.M_Material_Group_ID, mt.M_Material_Type_ID, mpg.M_Purchase_Group_ID, sg.M_Sales_Group_ID,iol.M_Product_ID, io.AD_Client_ID, io.AD_Org_ID,
io.IsActive, iol.C_UOM_ID, io.WM_InOutbound_ID, p.UnitsPerPallet, iol.M_Locator_ID, iol.M_LocatorTo_ID
ORDER BY iol.M_Product_ID
;