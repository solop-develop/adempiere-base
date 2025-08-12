package org.compiere.interfaces;

import java.sql.Timestamp;

public interface PaymentProcessorClosing {
    boolean closeBatch(int paymentMethod, Timestamp date);
}
