package org.compiere.acct;

import org.compiere.model.MAccount;
import org.compiere.model.MAcctSchema;
import org.compiere.model.MRevenueRecognitionPlan;
import org.compiere.model.MRevenueRecognitionRun;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.ArrayList;

public class RevenueRecognitionRun extends Doc {

    public RevenueRecognitionRun (MAcctSchema[] ass, ResultSet rs, String trxName) {
        super (ass, MRevenueRecognitionRun.class, rs, null, trxName);
    }   //  RevenueRecognitionRun

    private MRevenueRecognitionPlan recognitionPlan;
    private MRevenueRecognitionRun recognition;
    private DocLine line;

    @Override
    public String loadDocumentDetails() {
        recognition = (MRevenueRecognitionRun) getPO();
        recognitionPlan = recognition.getRevenueRecognitionPlan();
        line = new DocLine(recognition, this);
        setDateAcct(recognition.getDateDoc());
        setDateDoc(recognition.getDateDoc());
        setC_Currency_ID(recognition.getC_Currency_ID());
        return null;
    }

    @Override
    public BigDecimal getBalance() {
        return Env.ZERO;
    }

    @Override
    public ArrayList<Fact> createFacts(MAcctSchema mAcctSchema) {
        Fact fact = new Fact(this, mAcctSchema, Fact.POST_Actual);
        BigDecimal totalDebit = Env.ZERO;
        BigDecimal totalCredit = Env.ZERO;
        MAccount revenue = (MAccount) recognitionPlan.getP_Revenue_A();
        MAccount unEarnedRevenue = (MAccount) recognitionPlan.getUnEarnedRevenue_A();
        if(!recognition.isReversal()) {
            fact.createLine(line, unEarnedRevenue, getC_Currency_ID(), recognition.getRecognizedAmt(), null);
            fact.createLine(line, revenue, getC_Currency_ID(), null, recognition.getRecognizedAmt());
        } else {
            fact.createLine(line, unEarnedRevenue, getC_Currency_ID(), null, recognition.getRecognizedAmt().negate());
            fact.createLine(line, revenue, getC_Currency_ID(), recognition.getRecognizedAmt().negate(), null);
        }
        ArrayList<Fact> facts = new ArrayList<Fact>();
        facts.add(fact);
        return facts;
    }
}
