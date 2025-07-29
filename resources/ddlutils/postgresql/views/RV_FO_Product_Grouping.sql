CREATE OR REPLACE VIEW RV_FO_Product_Grouping
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
        io.WM_InOutbound_ID,
        SUM(iol.MovementQty) - FLOOR(
        CASE
        WHEN COALESCE(p.UnitsPerPallet, 0::NUMERIC) > 0::NUMERIC THEN SUM(iol.MovementQty) / p.UnitsPerPallet
        ELSE 0::NUMERIC
        END) * COALESCE(p.UnitsPerPallet, 0::NUMERIC) AS PickedQty,
        SUM(iol.MovementQty) AS Qty,
        SUM(iol.MovementQty * p.Weight) AS Weight,
        SUM(iol.MovementQty * p.Volume) AS Volume,
        iol.C_UOM_ID
        FROM WM_InOutbound io
        JOIN WM_InOutboundLine iol ON iol.WM_InOutbound_ID = io.WM_InOutbound_ID
        JOIN C_Order o ON o.C_Order_ID = iol.C_Order_ID
        JOIN C_BPartner bp ON bp.C_BPartner_ID = o.C_BPartner_ID
        JOIN M_Product p ON p.M_Product_ID = iol.M_Product_ID
        JOIN M_Product_Category pc ON pc.M_Product_Category_ID = p.M_Product_Category_ID
        LEFT JOIN m_product_group pg ON pg.m_product_group_id = p.m_product_group_id
        LEFT JOIN m_product_class mpc ON mpc.m_product_class_id = p.m_product_class_id
        LEFT JOIN m_product_classification pcl ON pcl.m_product_classification_id = p.m_product_classification_id
        LEFT JOIN m_industry_sector isp ON isp.m_industry_sector_id = p.m_industry_sector_id
        LEFT JOIN m_material_group mg ON mg.m_material_group_id = p.m_material_group_id
        LEFT JOIN m_material_type mt ON mt.m_material_type_id = p.m_material_type_id
        LEFT JOIN m_purchase_group mpg ON mpg.m_purchase_group_id = p.m_purchase_group_id
        LEFT JOIN m_sales_group sg ON sg.m_sales_group_id = p.m_sales_group_id
        GROUP BY io.AD_Client_ID, io.AD_Org_ID, io.IsActive,pc.M_Product_Category_ID, pg.M_Product_Group_ID, mpc.M_Product_Class_ID, pcl.M_Product_Classification_ID, isp.M_Industry_Sector_ID,
        mg.M_Material_Group_ID, mt.M_Material_Type_ID, mpg.M_Purchase_Group_ID, sg.M_Sales_Group_ID, iol.C_UOM_ID, io.WM_InOutbound_ID, p.UnitsPerPallet, o.C_Order_ID
        ;