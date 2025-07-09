package org.compiere.recognition.support;

import org.compiere.model.*;
import org.compiere.util.DisplayType;

import java.math.BigDecimal;
import java.math.MathContext;
/**
 * Accrual Recognition
 * @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">http://www.solopsoftware.com</a>
 */
public class AccrualRecognition implements IRecognitionRevenue {
    @Override
    public String run(MRevenueRecognitionRun recognitionRun) {
        MRevenueRecognitionPlan recognitionPlan = recognitionRun.getRevenueRecognitionPlan();
        MRevenueRecognition recognition = recognitionRun.getRevenueRecognition();
        int months = recognition.getNoMonths();
        if(months <= 0) {
            months = 1;
        }
        BigDecimal recognitionAmount = recognitionPlan.getTotalAmt();
        BigDecimal amountToRecognized = recognitionAmount.divide(BigDecimal.valueOf(months), MathContext.DECIMAL128);
        recognitionRun.setRecognizedAmt(amountToRecognized);
        recognitionRun.setSourceRecognizedAmt(amountToRecognized);
        String documentNo = "";
        if(recognitionPlan.getC_Invoice_ID() > 0) {
            MInvoice invoice = (MInvoice) recognitionPlan.getC_Invoice();
            recognitionRun.setC_ConversionType_ID(invoice.getC_ConversionType_ID());
            documentNo = invoice.getDocumentNo();
        } else if(recognitionPlan.getC_Order_ID() > 0) {
            MOrder order = (MOrder) recognitionPlan.getC_Order();
            recognitionRun.setC_ConversionType_ID(order.getC_ConversionType_ID());
            documentNo = order.getDocumentNo();
        }
        recognitionRun.saveEx();
        return documentNo + " @RecognizedAmt@: " + DisplayType.getNumberFormat(DisplayType.Amount).format(amountToRecognized);
    }
}
