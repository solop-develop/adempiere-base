package org.adempiere.process.util;

import org.compiere.model.MOrderLine;
import org.compiere.model.MProduct;
import org.compiere.util.Env;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Keeps, for a single invoice generation run, the quantity still pending to invoice
 * of each sales order line ({@code QtyOrdered - QtyInvoiced}), so that the generated
 * invoice lines never exceed the ordered quantity, even when the same order line has
 * several shipment lines (e.g. a shipment that was voided and generated again).
 * <p>
 * Bulk products ({@code M_Product.IsBulk}) and return orders are not controlled:
 * bulk quantities may legitimately vary (e.g. weighbridge gains/losses).
 */
public class InvoiceQuantityControl {

	/**	Order Line ID -> Quantity still available to invoice in this run	*/
	private final Map<Integer, BigDecimal> pendingQuantityByOrderLine = new HashMap<>();

	public static InvoiceQuantityControl newInstance() {
		return new InvoiceQuantityControl();
	}

	/**
	 * Is the quantity of this order line controlled against the ordered quantity
	 * @param orderLine sales order line
	 * @return false for bulk products and return orders
	 */
	public boolean isControlled(MOrderLine orderLine) {
		if (orderLine == null || orderLine.getC_OrderLine_ID() <= 0) {
			return false;
		}
		if (orderLine.getParent().isReturnOrder()) {
			return false;
		}
		MProduct product = orderLine.getProduct();
		return product == null || !product.isBulk();
	}

	/**
	 * Get quantity still pending to invoice for the order line in this run
	 * @param orderLine sales order line
	 * @return QtyOrdered - QtyInvoiced - quantity already reserved in this run
	 */
	public BigDecimal getPendingQuantity(MOrderLine orderLine) {
		return pendingQuantityByOrderLine.computeIfAbsent(orderLine.getC_OrderLine_ID(),
				key -> orderLine.getQtyOrdered().subtract(orderLine.getQtyInvoiced()).max(Env.ZERO));
	}

	/**
	 * Get the quantity that can be invoiced for the requested quantity, capped by the pending quantity
	 * of the order line, and reserve it for this run.
	 * @param orderLine sales order line
	 * @param requestedQuantity quantity requested to invoice (e.g. shipment movement quantity)
	 * @return quantity to invoice (zero when the order line is already fully invoiced)
	 */
	public BigDecimal reserveQuantity(MOrderLine orderLine, BigDecimal requestedQuantity) {
		if (requestedQuantity == null || requestedQuantity.signum() <= 0 || !isControlled(orderLine)) {
			return requestedQuantity;
		}
		BigDecimal pendingQuantity = getPendingQuantity(orderLine);
		BigDecimal quantityToInvoice = requestedQuantity.min(pendingQuantity);
		pendingQuantityByOrderLine.put(orderLine.getC_OrderLine_ID(), pendingQuantity.subtract(quantityToInvoice));
		return quantityToInvoice;
	}
}
