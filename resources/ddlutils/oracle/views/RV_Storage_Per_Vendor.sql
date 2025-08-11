CREATE OR REPLACE VIEW RV_Storage_Per_Vendor
(AD_CLIENT_ID, AD_ORG_ID, M_PRODUCT_ID, VALUE, NAME, 
 DESCRIPTION, UPC, SKU, C_UOM_ID, M_PRODUCT_CATEGORY_ID, 
 CLASSIFICATION, WEIGHT, VOLUME, VERSIONNO, GUARANTEEDAYS, 
 GUARANTEEDAYSMIN, M_LOCATOR_ID, M_WAREHOUSE_ID, X, Y, 
 Z, QTYONHAND, QTYRESERVED, QTYAVAILABLE, QTYORDERED, 
 M_Product_Class_ID, M_Product_Classification_ID, M_Product_Group_ID,
 PriceList, PriceEffective, 
 PricePO, RoyaltyAmt, C_Currency_ID, IsCurrentVendor, PriceLastPO, CostPerOrder, Order_Min, Order_Pack,
 C_BP_Group_ID, C_BP_AccountType_ID, C_BP_Segment_ID, C_BP_IndustryType_ID, C_BPartner_ID)
AS 
SELECT s.AD_Client_ID, s.AD_Org_ID,
    -- Product
    s.M_Product_ID, p.Value,p.Name, p.Description, p.UPC, p.SKU,
    p.C_UOM_ID, p.M_Product_Category_ID, p.Classification, p.Weight, p.Volume, p.VersionNo,
    p.GuaranteeDays, p.GuaranteeDaysMin,
    --  Locator
    s.M_Locator_ID, l.M_Warehouse_ID, l.X, l.Y, l.Z,
    -- Storage
    SUM(s.QtyOnHand) QtyOnHand, SUM(s.QtyReserved) QtyReserved, SUM(s.QtyOnHand-s.QtyReserved) AS QtyAvailable, 
    SUM(s.QtyOrdered) QtyOrdered,
    p.M_Product_Class_ID, p.M_Product_Classification_ID, p.M_Product_Group_ID, po.PriceList, po.PriceEffective, 
    po.PricePO, po.RoyaltyAmt, po.C_Currency_ID, po.IsCurrentVendor, po.PriceLastPO, po.CostPerOrder, po.Order_Min, po.Order_Pack,
    bp.C_BP_Group_ID, bp.C_BP_AccountType_ID, bp.C_BP_Segment_ID, bp.C_BP_IndustryType_ID, bp.C_BPartner_ID
FROM M_Storage s
  INNER JOIN M_Locator l ON (s.M_Locator_ID=l.M_Locator_ID)
  INNER JOIN M_Product p ON (s.M_Product_ID=p.M_Product_ID)
  LEFT JOIN M_Product_PO po ON(po.M_Product_ID = p.M_Product_ID)
  LEFT JOIN C_BPartner bp ON(bp.C_BPartner_ID = po.C_BPartner_ID)
  GROUP BY s.AD_Client_ID, s.AD_Org_ID,
    -- Product
    s.M_Product_ID, p.Value,p.Name, p.Description, p.UPC, p.SKU,
    p.C_UOM_ID, p.M_Product_Category_ID, p.Classification, p.Weight, p.Volume, p.VersionNo,
    p.GuaranteeDays, p.GuaranteeDaysMin,
    --  Locator
    s.M_Locator_ID, l.M_Warehouse_ID, l.X, l.Y, l.Z,
    p.M_Product_Class_ID, p.M_Product_Classification_ID, p.M_Product_Group_ID,
    po.PriceList, po.PriceEffective, 
    po.PricePO, po.RoyaltyAmt, po.C_Currency_ID, po.IsCurrentVendor, po.PriceLastPO, po.CostPerOrder, po.Order_Min, po.Order_Pack,
    bp.C_BP_Group_ID, bp.C_BP_AccountType_ID, bp.C_BP_Segment_ID, bp.C_BP_IndustryType_ID, bp.C_BPartner_ID
;