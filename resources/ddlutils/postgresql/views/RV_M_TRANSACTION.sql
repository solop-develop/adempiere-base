CREATE OR REPLACE VIEW RV_M_TRANSACTION
(AD_CLIENT_ID, AD_ORG_ID, MOVEMENTDATE, MOVEMENTQTY, M_PRODUCT_ID, 
 M_LOCATOR_ID, M_ATTRIBUTESETINSTANCE_ID, M_PRODUCT_CATEGORY_ID, VALUE, C_BPARTNER_ID, 
 PRICEPO, PRICELASTPO, PRICELIST, M_Product_Class_ID, M_Product_Classification_ID, M_Product_Group_ID)
AS 
SELECT t.AD_Client_ID,t.AD_Org_ID, t.MovementDate, t.MovementQty, 
	t.M_Product_ID, t.M_Locator_ID, t.M_AttributeSetInstance_ID,
	p.M_Product_Category_ID, p.Value, 
	po.C_BPartner_ID, po.PricePO, po.PriceLastPO, po.PriceList, 
	p.M_Product_Class_ID, p.M_Product_Classification_ID, p.M_Product_Group_ID
FROM M_Transaction t
  INNER JOIN M_Product p ON (t.M_Product_ID = p.M_Product_ID)
  LEFT JOIN LATERAL (
    SELECT po.C_BPartner_ID, po.PricePO, po.PriceLastPO, po.PriceList
    FROM M_Product_PO po
    WHERE po.M_Product_ID = t.M_Product_ID
      AND po.IsCurrentVendor = 'Y'
      AND po.AD_Org_ID IN (0, t.AD_Org_ID)
    ORDER BY po.AD_Org_ID DESC
    LIMIT 1
  ) po ON true;