CREATE OR REPLACE VIEW RV_C_INVOICE_CUSTOMERVENDQTR
(AD_CLIENT_ID, AD_ORG_ID, C_BPARTNER_ID, VENDOR_ID, DATEINVOICED,
 LINENETAMT, LINELISTAMT, LINELIMITAMT, LINEDISCOUNTAMT, LINEDISCOUNT,
 LINEOVERLIMITAMT, LINEOVERLIMIT, QTYINVOICED,
 ISSOTRX, C_BP_GROUP_ID, C_DOCTYPETARGET_ID, DOCSTATUS,
 C_BP_ACCOUNTTYPE_ID, C_BP_SALESGROUP_ID, C_BP_SEGMENT_ID, C_BP_INDUSTRYTYPE_ID,
 C_SALESREGION_ID, WEIGHT, VOLUME)
AS
SELECT il.AD_Client_ID, il.AD_Org_ID, il.C_BPartner_ID, po.Vendor_ID,
    firstOf(il.DateInvoiced, 'Q') AS DateInvoiced,
    SUM(LineNetAmt) AS LineNetAmt,
    SUM(LineListAmt) AS LineListAmt,
    SUM(LineLimitAmt) AS LineLimitAmt,
    SUM(LineDiscountAmt) AS LineDiscountAmt,
    CASE WHEN SUM(LineListAmt)=0 THEN 0 ELSE
      ROUND((SUM(LineListAmt)-SUM(LineNetAmt))/SUM(LineListAmt)*100,2) END AS LineDiscount,
    SUM(LineOverLimitAmt) AS LineOverLimitAmt,
    CASE WHEN SUM(LineNetAmt)=0 THEN 0 ELSE
      100-ROUND((SUM(LineNetAmt)-SUM(LineOverLimitAmt))/SUM(LineNetAmt)*100,2) END AS LineOverLimit,
    SUM(QtyInvoiced) AS QtyInvoiced,
    IsSOTrx, C_BP_Group_ID, C_DocTypeTarget_ID, DocStatus,
    C_BP_AccountType_ID, C_BP_SalesGroup_ID, C_BP_Segment_ID, C_BP_IndustryType_ID,
    C_SalesRegion_ID, SUM(Weight) AS Weight, SUM(Volume) AS Volume
FROM RV_C_InvoiceLine il
  LEFT JOIN LATERAL (
      SELECT ppo.C_BPartner_ID AS Vendor_ID
      FROM M_Product_PO ppo
      WHERE ppo.M_Product_ID = il.M_Product_ID
        AND ppo.AD_Org_ID IN (0, il.AD_Org_ID)
      ORDER BY ppo.AD_Org_ID DESC LIMIT 1
  ) po ON true
GROUP BY il.AD_Client_ID, il.AD_Org_ID, il.C_BPartner_ID, po.Vendor_ID,
    firstOf(il.DateInvoiced, 'Q'),
    IsSOTrx, C_BP_Group_ID, C_DocTypeTarget_ID, DocStatus,
    C_BP_AccountType_ID, C_BP_SalesGroup_ID, C_BP_Segment_ID, C_BP_IndustryType_ID, C_SalesRegion_ID;