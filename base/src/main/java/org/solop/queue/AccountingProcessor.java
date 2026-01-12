package org.solop.queue;

import org.compiere.process.DocumentEngine;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.spin.queue.model.MADQueue;
import org.spin.queue.util.QueueManager;


/**
 * Accounting Processor (PAC)
 * @author Gabriel Escalona
 */
public class AccountingProcessor extends QueueManager {
    public static String QueueType_Accounting = "PAC";
    /**	Logger						*/
    protected CLogger log = CLogger.getCLogger(getClass());
    @Override
    public void add(int queueId) {
        log.fine("Accounting Queue Added: " + queueId);
    }

    @Override
    public void process(int queueId) {
        MADQueue queue = new MADQueue(getContext(), queueId, getTransactionName());
        //  Ignore without record
        if(queue.getAD_Table_ID() <= 0 || queue.getRecord_ID() <= 0) {
            return;
        }
        new DocumentEngine()
            .withContext(getContext())
            .withAD_Client_ID(Env.getAD_Client_ID(getContext()))
            .withAD_Table_ID(queue.get_Table_ID())
            .withRecord_ID(queue.getRecord_ID())
            .withTrxName(getTransactionName())
            .postImmediate(true); // Force

    }
}
