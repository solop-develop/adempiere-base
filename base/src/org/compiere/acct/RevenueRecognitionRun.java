package org.compiere.acct;

import org.compiere.model.*;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RevenueRecognitionRun extends Doc {

    public RevenueRecognitionRun (MAcctSchema[] ass, ResultSet rs, String trxName) {
        super (ass, MRevenueRecognitionRun.class, rs, null, trxName);
    }   //  RevenueRecognitionRun

    private MRevenueRecognitionPlan recognitionPlan;
    private MRevenueRecognitionRun recognitionRun;
    private DocLine line;
    private boolean isCredit;

    @Override
    public String loadDocumentDetails() {
        recognitionRun = (MRevenueRecognitionRun) getPO();
        recognitionPlan = recognitionRun.getRevenueRecognitionPlan();
        line = new DocLine(recognitionRun, this);
        setDateAcct(recognitionRun.getDateDoc());
        setDateDoc(recognitionRun.getDateDoc());
        setC_Currency_ID(recognitionRun.getC_Currency_ID());
        if(recognitionPlan.getC_Invoice_ID() > 0) {
            MInvoice invoice = new MInvoice(getCtx(), recognitionPlan.getC_Invoice_ID(), recognitionRun.get_TrxName());
            isCredit = invoice.isCreditMemo();
        } else {
            if(recognitionPlan.getC_Order_ID() > 0) {
                MOrder order = new MOrder(getCtx(), recognitionPlan.getC_Order_ID(), recognitionRun.get_TrxName());
                isCredit = order.isReturnOrder();
            }
        }
        return null;
    }

    @Override
    public BigDecimal getBalance() {
        return Env.ZERO;
    }

    @Override
    public ArrayList<Fact> createFacts(MAcctSchema mAcctSchema) {
        Fact fact = new Fact(this, mAcctSchema, Fact.POST_Actual);
        MAccount revenue = (MAccount) recognitionPlan.getP_Revenue_A();
        MAccount unEarnedRevenue = (MAccount) recognitionPlan.getUnEarnedRevenue_A();
        if(recognitionPlan.isSOTrx()) {
            if(!isCredit) {
                fact.createLine(line, unEarnedRevenue, getC_Currency_ID(), recognitionRun.getRecognizedAmt(), null);
                fact.createLine(line, revenue, getC_Currency_ID(), null, recognitionRun.getRecognizedAmt());
            } else {
                fact.createLine(line, unEarnedRevenue, getC_Currency_ID(), null, recognitionRun.getRecognizedAmt());
                fact.createLine(line, revenue, getC_Currency_ID(), recognitionRun.getRecognizedAmt(), null);
            }
        } else {
            if(!isCredit) {
                fact.createLine(line, unEarnedRevenue, getC_Currency_ID(), null, recognitionRun.getRecognizedAmt());
                fact.createLine(line, revenue, getC_Currency_ID(), recognitionRun.getRecognizedAmt(), null);
            } else {
                fact.createLine(line, unEarnedRevenue, getC_Currency_ID(), recognitionRun.getRecognizedAmt(), null);
                fact.createLine(line, revenue, getC_Currency_ID(), null, recognitionRun.getRecognizedAmt());
            }
        }
        ArrayList<Fact> facts = new ArrayList<Fact>();
        facts.add(fact);
        return facts;
    }
}
