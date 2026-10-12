package org.compiere.print;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.model.ConversionUtil;

/**
 * A {@link PrintDataElement} whose value is a numeric amount converted to another currency.
 * <p>
 * The conversion is computed once and handed to the superclass constructor, so every inherited
 * behavior works with no overrides:
 * <ul>
 *   <li>a missing rate yields a {@code null} value, so {@link PrintDataElement#isNull()} is correct
 *       (suppress-null, empty XML nodes and display logic all behave), instead of the legacy code
 *       that passed a non-null placeholder and always reported not-null;</li>
 *   <li>the real display type of the column is used (Amount/Number/Quantity/CostPrice), so detail
 *       rows and subtotal/total rows format with the same precision.</li>
 * </ul>
 */
public class ConversionElement extends PrintDataElement {
	private static final long serialVersionUID = 1L;

	public ConversionElement(Properties context, String columnName, BigDecimal amount,
			int conversionTypeId, int fromCurrencyId, int toCurrencyId, Timestamp conversionDate,
			int clientId, int organizationId, int displayType, String pattern) {
		super(columnName,
				ConversionUtil.get().getConvertedAmount(context, conversionTypeId, fromCurrencyId,
						toCurrencyId, conversionDate, clientId, organizationId, amount),
				displayType, pattern);
	}
}
