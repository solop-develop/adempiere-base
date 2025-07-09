package org.compiere.recognition.support;

import org.compiere.model.MRevenueRecognitionRun;

/**
 * Make a contract for define recognition revenue
 * @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">http://www.solopsoftware.com</a>
 */
public interface IRecognitionRevenue {
    String run(MRevenueRecognitionRun recognitionRun);
}
