CREATE OR REPLACE VIEW RV_KPI_Project
(RV_KPI_Project_ID,
 C_Project_ID, AD_Client_ID, AD_Org_ID, IsActive, Created, Updated, Value, Name, Description, Note, IsSummary,
 AD_User_ID, C_BPartner_ID, C_BPartner_Location_ID, POReference, C_PaymentTerm_ID, C_Currency_ID,
 M_PriceList_Version_ID, C_Campaign_ID, IsCommitment, PlannedAmt, PlannedQty, PlannedMarginAmt, CommittedAmt,
 DateContract, DateFinish, Processed, SalesRep_ID, C_ProjectType_ID, CommittedQty, InvoicedAmt, InvoicedQty,
 ProjectBalanceAmt, C_Phase_ID, IsCommitCeiling, M_Warehouse_ID, ProjectCategory, C_BPartnerSR_ID, ProjInvoiceRule,
 ProjectLineLevel, C_ProjectGroup_ID, C_ProjectClass_ID, C_ProjectCategory_ID, C_ProjectStatus_ID, DateStart,
 DateFinishSchedule, DateStartSchedule, DateDeadline, IsIndefinite, C_Activity_ID, AD_OrgTrx_ID, C_SalesRegion_ID,
 User1_ID, User2_ID, User3_ID, User4_ID, ProjectManager_ID, DurationUnit, PriorityRule, AD_Color_ID, DateLastAction,
 DateLastAlert, DueType, AlertMessage, IsCustomerApproved, M_PriceList_ID, S_Contract_ID, ParentProject_ID,
 ProjectBased, M_Product_ID, SP027_BrandProduct_ID, SP027_Brand_ID, SP027_Project_Budget_ID, IsCostBased,
 ManageCustomerApproval, SP009_IsExpedientProject, CostInvoicedQty, Margin, CostReceivedAmt, CostConsumedAmt,
 ProfitPlannedAmt, ProfitRealizedAmt, QtyOrdered, QtyDelivered, CostOrderedQty, CostReceivedQty, CostConsumedQty,
 OrderedAmt, DeliveredAmt, CostPlannedAmt, CostOrderedAmt, CostInvoicedAmt,
 StatusIsClosed, StatusIsFinalClose, IsOpenProject, NumProjects, HasDeadline, IsOverdue, DaysOverdue, DaysToDeadline,
 AgeDays, PlannedDays, ActualDays, ProjectLineCount, PhaseCount, PhaseDoneCount, MilestoneCount, MilestoneDoneCount,
 MilestoneOverdueCount, TaskCount, TaskDoneCount, TaskOverdueCount, PercentComplete, CommittedPct, InvoicedPct,
 ToInvoiceAmt, MarginPlannedPct, RequestCount, RequestOpenCount, RequestOverdueCount, RequestHours)
AS
SELECT x.C_Project_ID,
    x.C_Project_ID, x.AD_Client_ID, x.AD_Org_ID, x.IsActive, x.Created, x.Updated, x.Value, x.Name, x.Description,
    x.Note, x.IsSummary, x.AD_User_ID, x.C_BPartner_ID, x.C_BPartner_Location_ID, x.POReference, x.C_PaymentTerm_ID,
    x.C_Currency_ID, x.M_PriceList_Version_ID, x.C_Campaign_ID, x.IsCommitment, x.PlannedAmt, x.PlannedQty,
    x.PlannedMarginAmt, x.CommittedAmt, x.DateContract, x.DateFinish, x.Processed, x.SalesRep_ID, x.C_ProjectType_ID,
    x.CommittedQty, x.InvoicedAmt, x.InvoicedQty, x.ProjectBalanceAmt, x.C_Phase_ID, x.IsCommitCeiling,
    x.M_Warehouse_ID, x.ProjectCategory, x.C_BPartnerSR_ID, x.ProjInvoiceRule, x.ProjectLineLevel,
    x.C_ProjectGroup_ID, x.C_ProjectClass_ID, x.C_ProjectCategory_ID, x.C_ProjectStatus_ID, x.DateStart,
    x.DateFinishSchedule, x.DateStartSchedule, x.DateDeadline, x.IsIndefinite, x.C_Activity_ID, x.AD_OrgTrx_ID,
    x.C_SalesRegion_ID, x.User1_ID, x.User2_ID, x.User3_ID, x.User4_ID, x.ProjectManager_ID, x.DurationUnit,
    x.PriorityRule, x.AD_Color_ID, x.DateLastAction, x.DateLastAlert, x.DueType, x.AlertMessage,
    x.IsCustomerApproved, x.M_PriceList_ID, x.S_Contract_ID, x.ParentProject_ID, x.ProjectBased, x.M_Product_ID,
    x.SP027_BrandProduct_ID, x.SP027_Brand_ID, x.SP027_Project_Budget_ID, x.IsCostBased, x.ManageCustomerApproval,
    x.SP009_IsExpedientProject, x.CostInvoicedQty, x.Margin, x.CostReceivedAmt, x.CostConsumedAmt,
    x.ProfitPlannedAmt, x.ProfitRealizedAmt, x.QtyOrdered, x.QtyDelivered, x.CostOrderedQty, x.CostReceivedQty,
    x.CostConsumedQty, x.OrderedAmt, x.DeliveredAmt, x.CostPlannedAmt, x.CostOrderedAmt, x.CostInvoicedAmt,
    x.StatusIsClosed,
    x.StatusIsFinalClose,
    CAST(CASE WHEN x.IsActive = 'Y' AND COALESCE(x.StatusIsClosed, 'N') = 'N' THEN 1 ELSE 0 END AS INTEGER),
    CAST(1 AS INTEGER),
    CASE WHEN x.DateDeadline IS NOT NULL THEN 1 ELSE 0 END,
    CAST(CASE WHEN x.IsActive = 'Y' AND COALESCE(x.StatusIsClosed, 'N') = 'N' AND x.DateDeadline IS NOT NULL AND CAST(x.DateDeadline AS DATE) < CURRENT_DATE THEN 1 ELSE 0 END AS INTEGER),
    CASE WHEN CASE WHEN x.IsActive = 'Y' AND COALESCE(x.StatusIsClosed, 'N') = 'N' AND x.DateDeadline IS NOT NULL AND CAST(x.DateDeadline AS DATE) < CURRENT_DATE THEN 1 ELSE 0 END = 1 THEN CURRENT_DATE - CAST(x.DateDeadline AS DATE) ELSE 0 END,
    CASE WHEN x.DateDeadline IS NULL THEN NULL ELSE CAST(x.DateDeadline AS DATE) - CURRENT_DATE END,
    CASE WHEN CASE WHEN x.IsActive = 'Y' AND COALESCE(x.StatusIsClosed, 'N') = 'N' THEN 1 ELSE 0 END = 1 THEN CURRENT_DATE - CAST(x.Created AS DATE) ELSE NULL END,
    CASE WHEN x.DateStartSchedule IS NOT NULL AND x.DateFinishSchedule IS NOT NULL THEN CAST(x.DateFinishSchedule AS DATE) - CAST(x.DateStartSchedule AS DATE) ELSE NULL END,
    CASE WHEN x.DateStart IS NOT NULL AND x.DateFinish IS NOT NULL THEN CAST(x.DateFinish AS DATE) - CAST(x.DateStart AS DATE) ELSE NULL END,
    CAST(x.LineCnt AS INTEGER),
    CAST(x.PhaseCnt AS INTEGER),
    CAST(x.PhaseDone AS INTEGER),
    CAST(x.MsCnt AS INTEGER),
    CAST(x.MsDone AS INTEGER),
    CAST(x.MsOver AS INTEGER),
    CAST(x.TaskCnt AS INTEGER),
    CAST(x.TaskDone AS INTEGER),
    CAST(x.TaskOver AS INTEGER),
    CAST(ROUND(x.PctW, 2) AS NUMERIC),
    CASE WHEN COALESCE(x.PlannedAmt, 0) <> 0 THEN ROUND(COALESCE(x.CommittedAmt, 0) * 100 / x.PlannedAmt, 2) ELSE NULL END,
    CASE WHEN COALESCE(x.PlannedAmt, 0) <> 0 THEN ROUND(COALESCE(x.InvoicedAmt, 0) * 100 / x.PlannedAmt, 2) ELSE NULL END,
    COALESCE(x.PlannedAmt, 0) - COALESCE(x.InvoicedAmt, 0),
    CASE WHEN COALESCE(x.PlannedAmt, 0) <> 0 THEN ROUND(COALESCE(x.PlannedMarginAmt, 0) * 100 / x.PlannedAmt, 2) ELSE NULL END,
    CAST(x.RqCnt AS INTEGER),
    CAST(x.RqOpen AS INTEGER),
    CAST(x.RqOver AS INTEGER),
    x.RqHours
FROM (
    SELECT p.C_Project_ID,
        p.AD_Client_ID, p.AD_Org_ID, p.IsActive, p.Created, p.Updated, p.Value, p.Name, p.Description, p.Note,
        p.IsSummary, p.AD_User_ID, p.C_BPartner_ID, p.C_BPartner_Location_ID, p.POReference, p.C_PaymentTerm_ID,
        p.C_Currency_ID, p.M_PriceList_Version_ID, p.C_Campaign_ID, p.IsCommitment, p.PlannedAmt, p.PlannedQty,
        p.PlannedMarginAmt, p.CommittedAmt, p.DateContract, p.DateFinish, p.Processed, p.SalesRep_ID,
        p.C_ProjectType_ID, p.CommittedQty, p.InvoicedAmt, p.InvoicedQty, p.ProjectBalanceAmt, p.C_Phase_ID,
        p.IsCommitCeiling, p.M_Warehouse_ID, p.ProjectCategory, p.C_BPartnerSR_ID, p.ProjInvoiceRule,
        p.ProjectLineLevel, p.C_ProjectGroup_ID, p.C_ProjectClass_ID, p.C_ProjectCategory_ID, p.C_ProjectStatus_ID,
        p.DateStart, p.DateFinishSchedule, p.DateStartSchedule, p.DateDeadline, p.IsIndefinite, p.C_Activity_ID,
        p.AD_OrgTrx_ID, p.C_SalesRegion_ID, p.User1_ID, p.User2_ID, p.User3_ID, p.User4_ID, p.ProjectManager_ID,
        p.DurationUnit, p.PriorityRule, p.AD_Color_ID, p.DateLastAction, p.DateLastAlert, p.DueType, p.AlertMessage,
        p.IsCustomerApproved, p.M_PriceList_ID, p.S_Contract_ID, p.ParentProject_ID, p.ProjectBased, p.M_Product_ID,
        p.SP027_BrandProduct_ID, p.SP027_Brand_ID, p.SP027_Project_Budget_ID, p.IsCostBased,
        p.ManageCustomerApproval, p.SP009_IsExpedientProject, p.CostInvoicedQty, p.Margin, p.CostReceivedAmt,
        p.CostConsumedAmt, p.ProfitPlannedAmt, p.ProfitRealizedAmt, p.QtyOrdered, p.QtyDelivered, p.CostOrderedQty,
        p.CostReceivedQty, p.CostConsumedQty, p.OrderedAmt, p.DeliveredAmt, p.CostPlannedAmt, p.CostOrderedAmt,
        p.CostInvoicedAmt, ps.IsClosed AS StatusIsClosed, ps.IsFinalClose AS StatusIsFinalClose, ln.LineCnt,
        ln.PhaseCnt, ln.PhaseDone, ln.MsCnt, ln.MsDone, ln.MsOver, ln.TaskCnt, ln.TaskDone, ln.TaskOver, ln.PctW,
        rq.RqCnt, rq.RqOpen, rq.RqOver, rq.RqHours
    FROM C_Project p
        LEFT JOIN C_ProjectStatus ps ON (ps.C_ProjectStatus_ID = p.C_ProjectStatus_ID)
        LEFT JOIN LATERAL (
            SELECT COUNT(*) AS LineCnt,
                COUNT(*) FILTER (WHERE k.Kind = 'F') AS PhaseCnt,
                COUNT(*) FILTER (WHERE k.Kind = 'F' AND k.Done = 1) AS PhaseDone,
                COUNT(*) FILTER (WHERE k.Kind = 'H') AS MsCnt,
                COUNT(*) FILTER (WHERE k.Kind = 'H' AND k.Done = 1) AS MsDone,
                COUNT(*) FILTER (WHERE k.Kind = 'H' AND k.Done = 0 AND k.Over = 1) AS MsOver,
                COUNT(*) FILTER (WHERE k.Kind IN ('R', 'L')) AS TaskCnt,
                COUNT(*) FILTER (WHERE k.Kind IN ('R', 'L') AND k.Done = 1) AS TaskDone,
                COUNT(*) FILTER (WHERE k.Kind IN ('R', 'L') AND k.Done = 0 AND k.Over = 1) AS TaskOver,
                CASE WHEN COUNT(*) FILTER (WHERE k.Kind IN ('R', 'L')) = 0 THEN NULL
                     WHEN COALESCE(SUM(k.Qty) FILTER (WHERE k.Kind IN ('R', 'L')), 0) > 0
                         THEN SUM(k.Pct * k.Qty) FILTER (WHERE k.Kind IN ('R', 'L')) / SUM(k.Qty) FILTER (WHERE k.Kind IN ('R', 'L'))
                     WHEN COALESCE(SUM(k.Amt) FILTER (WHERE k.Kind IN ('R', 'L')), 0) > 0
                         THEN SUM(k.Pct * k.Amt) FILTER (WHERE k.Kind IN ('R', 'L')) / SUM(k.Amt) FILTER (WHERE k.Kind IN ('R', 'L'))
                     ELSE AVG(k.Pct) FILTER (WHERE k.Kind IN ('R', 'L'))
                END AS PctW
            FROM (
                SELECT CASE WHEN l.ProjectLineType = 'P' OR l.IsSummary = 'Y' THEN 'F' WHEN l.ProjectLineType = 'M' OR l.IsMilestone = 'Y' THEN 'H' WHEN l.ProjectLineType = 'R' THEN 'R' ELSE 'L' END AS Kind,
                    CASE WHEN l.IsComplete = 'Y' OR COALESCE(l.PercentageCompleted, 0) >= 100 THEN 1 ELSE 0 END AS Done,
                    CASE WHEN l.DateDeadline IS NOT NULL AND CAST(l.DateDeadline AS DATE) < CURRENT_DATE THEN 1 ELSE 0 END AS Over,
                    CASE WHEN l.IsComplete = 'Y' OR COALESCE(l.PercentageCompleted, 0) >= 100 THEN 100 ELSE COALESCE(l.PercentageCompleted, 0) END AS Pct,
                    COALESCE(l.PlannedQty, 0) AS Qty, COALESCE(l.PlannedAmt, 0) AS Amt
                FROM C_ProjectLine l
                WHERE l.C_Project_ID = p.C_Project_ID AND l.IsActive = 'Y'
            ) k
        ) ln ON TRUE
        LEFT JOIN LATERAL (
            SELECT COUNT(*) AS RqCnt,
                COUNT(*) FILTER (WHERE COALESCE(rs.IsClosed, 'N') = 'N') AS RqOpen,
                COUNT(*) FILTER (WHERE COALESCE(rs.IsClosed, 'N') = 'N' AND r.DateCompletePlan IS NOT NULL AND CAST(r.DateCompletePlan AS DATE) < CURRENT_DATE) AS RqOver,
                COALESCE(SUM(r.TimeSpent), 0) AS RqHours
            FROM R_Request r
                LEFT JOIN R_Status rs ON (rs.R_Status_ID = r.R_Status_ID)
            WHERE r.C_Project_ID = p.C_Project_ID AND r.IsActive = 'Y'
        ) rq ON TRUE
) x;
