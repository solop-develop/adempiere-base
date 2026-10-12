package org.compiere.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.util.Properties;

import org.compiere.util.CCache;
import org.compiere.util.Env;
import org.compiere.util.TimeUtil;

/**
 * Currency conversion helper for print-format items configured with a target currency.
 * <p>
 * Rates are cached by (conversion type, from, to, day). A missing rate is returned as
 * {@code null} and is NOT cached, so a later rate insert is reflected without waiting for
 * the cache to expire. Amounts are rounded to the target currency's standard precision
 * with {@link RoundingMode#HALF_UP}.
 */
public class ConversionUtil {

	private ConversionUtil() {
		//	Nothing
	}

	private static ConversionUtil conversionEngine = null;
	private static final CCache<String, BigDecimal> conversionValues =
			new CCache<String, BigDecimal>("ConversionRate", 100, 30);

	public synchronized static ConversionUtil get() {
		if (conversionEngine == null) {
			conversionEngine = new ConversionUtil();
		}
		return conversionEngine;
	}

	/**
	 * Get the conversion rate, or {@code null} when no rate is defined.
	 * A missing rate is not cached.
	 */
	public BigDecimal getConversionRate(int conversionTypeId, int fromCurrencyId, int toCurrencyId,
			Timestamp conversionDate, int clientId, int organizationId) {
		if (conversionDate == null) {
			return null;
		}
		if (fromCurrencyId == toCurrencyId) {
			return Env.ONE;
		}
		String key = conversionTypeId + "|" + fromCurrencyId + "|" + toCurrencyId + "|"
				+ TimeUtil.getDay(conversionDate).getTime();
		BigDecimal conversionRate = conversionValues.get(key);
		if (conversionRate != null) {
			return conversionRate;
		}
		conversionRate = MConversionRate.getRate(fromCurrencyId, toCurrencyId, conversionDate,
				conversionTypeId, clientId, organizationId);
		if (conversionRate == null) {
			//	Do NOT cache a missing rate: a later rate insert must be reflected immediately.
			return null;
		}
		conversionValues.put(key, conversionRate);
		return conversionRate;
	}

	/**
	 * Get the amount converted to the target currency, or {@code null} when the amount is null
	 * or no rate is available (so the report cell stays empty instead of printing 0.00).
	 */
	public BigDecimal getConvertedAmount(Properties context, int conversionTypeId, int fromCurrencyId,
			int toCurrencyId, Timestamp conversionDate, int clientId, int organizationId, BigDecimal amount) {
		if (amount == null) {
			return null;
		}
		BigDecimal conversionRate = getConversionRate(conversionTypeId, fromCurrencyId, toCurrencyId,
				conversionDate, clientId, organizationId);
		if (conversionRate == null) {
			return null;
		}
		MCurrency currencyTo = MCurrency.get(context, toCurrencyId);
		return amount.multiply(conversionRate)
				.setScale(currencyTo.getStdPrecision(), RoundingMode.HALF_UP);
	}
}
