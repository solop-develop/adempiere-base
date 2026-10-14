/************************************************************************************
 * Copyright (C) 2012-present E.R.P. Consultores y Asociados, C.A.                  *
 * Contributor(s): Yamel Senih ysenih@erpya.com                                     *
 * This program is free software: you can redistribute it and/or modify             *
 * it under the terms of the GNU General Public License as published by             *
 * the Free Software Foundation, either version 2 of the License, or                *
 * (at your option) any later version.                                              *
 * This program is distributed in the hope that it will be useful,                  *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of                   *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the                     *
 * GNU General Public License for more details.                                     *
 * You should have received a copy of the GNU General Public License                *
 * along with this program. If not, see <https://www.gnu.org/licenses/>.            *
 ************************************************************************************/
package org.spin.service.grpc.authentication;

import static io.grpc.Metadata.ASCII_STRING_MARSHALLER;

import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.compiere.model.MLanguage;
import org.compiere.util.Env;
import org.compiere.util.Util;

import io.grpc.Metadata;

/**
 * Resolves the AD_Language of a request from the `Accept-Language` header,
 * which the frontends send with the language the user picked in the UI. When
 * present and recognized it overrides the AD_Language frozen into the JWT /
 * session at login, the same rule sabana's LanguageResolver applies.
 */
public final class LanguageResolver {

	public static final Metadata.Key<String> ACCEPT_LANGUAGE_METADATA_KEY = Metadata.Key.of(
		"accept-language",
		ASCII_STRING_MARSHALLER
	);

	/**
	 * Primary language tag -> AD_Language with translation coverage. Fixed on
	 * purpose: a LanguageISO lookup could return any system language of that
	 * ISO code (es_ES, es_VE...) instead of the one actually translated.
	 */
	private static final Map<String, String> PRIMARY_TAG_TO_AD_LANGUAGE = Map.of(
		"es", "es_MX",
		"en", "en_US"
	);

	private LanguageResolver() {
	}

	/**
	 * @param context session context, used to validate full AD_Language tags
	 * @param acceptLanguageHeader raw header, e.g. `en_US`, `es-MX` or `es-US,es-419;q=0.9,en;q=0.8`
	 * @return AD_Language of the first recognized tag, or null to keep the session language
	 */
	public static String resolve(Properties context, String acceptLanguageHeader) {
		if (Util.isEmpty(acceptLanguageHeader, true)) {
			return null;
		}
		for (String rawTag : acceptLanguageHeader.split(",")) {
			String tag = rawTag.split(";")[0].trim().replace('-', '_');
			if (tag.isEmpty()) {
				continue;
			}
			String[] parts = tag.split("_");
			if (parts.length > 1) {
				String fullLanguage = parts[0].toLowerCase(Locale.ROOT) + "_" + parts[1].toUpperCase(Locale.ROOT);
				if (isActiveSystemLanguage(context, fullLanguage)) {
					return fullLanguage;
				}
			}
			String mapped = PRIMARY_TAG_TO_AD_LANGUAGE.get(parts[0].toLowerCase(Locale.ROOT));
			if (mapped != null) {
				return mapped;
			}
		}
		return null;
	}

	/**
	 * Copy of the session context with the request language applied, so the
	 * cached session context shared by other requests is never mutated.
	 * Returns the same context when the header does not resolve to a language.
	 */
	public static Properties applyRequestLanguage(Properties sessionContext, Metadata metadata) {
		if (sessionContext == null || metadata == null) {
			return sessionContext;
		}
		String language = resolve(sessionContext, metadata.get(ACCEPT_LANGUAGE_METADATA_KEY));
		if (language == null || language.equals(Env.getAD_Language(sessionContext))) {
			return sessionContext;
		}
		Properties requestContext = (Properties) sessionContext.clone();
		Env.setContext(requestContext, Env.LANGUAGE, language);
		return requestContext;
	}

	private static boolean isActiveSystemLanguage(Properties context, String adLanguage) {
		try {
			MLanguage language = MLanguage.get(context, adLanguage);
			return language != null
				&& language.get_ID() > 0
				&& language.isActive()
				&& (language.isSystemLanguage() || language.isBaseLanguage())
			;
		} catch (Exception e) {
			return false;
		}
	}
}
