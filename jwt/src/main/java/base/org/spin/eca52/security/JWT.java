/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * This program is free software; you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program; if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * Copyright (C) 2003-2015 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpya.com                                  *
 *****************************************************************************/
package org.spin.eca52.security;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MSysConfig;
import org.compiere.model.Query;
import org.compiere.util.Env;
import org.compiere.util.SecureEngine;
import org.compiere.util.Util;
import org.spin.eca52.util.JWTUtil;
import org.spin.model.MADToken;
import org.spin.model.MADTokenDefinition;
import org.spin.util.IThirdPartyAccessGenerator;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * A simple token generator for third party access
 * Note: You should generate your own token generator
 * @author Yamel Senih, ySenih@erpya.com, ERPCyA http://www.erpya.com
 */
public class JWT implements IThirdPartyAccessGenerator {

	/**	Default Token	*/
	private MADToken token = null;
	/**	User Token value	*/
	private String userTokenValue = null;
	/**	Language	*/
	private String language;
	/**	Session ID	*/
	private int sessionId;

	public JWT() {
    	//	
    }
	
    @Override
	public String generateToken(String tokenType, int userId) {
    	throw new AdempiereException("@Unsupported@");
	}

	
	@Override
	public boolean validateToken(String token, int userId) {
		return false;
	}
	
	@Override
	public MADToken getToken() {
		return token;
	}
	
	@Override
	public String getTokenValue() {
		return userTokenValue;
	}

	/**
	 * Get JWT Secrect Key generates with HMAC-SHA algorithms
	 * @return
	 */
	private static String getJWT_SecretKeyAsString(int clientId) {
		// get by SysConfig client
		String secretKey = MSysConfig.getValue(
			JWTUtil.ECA52_JWT_SECRET_KEY,
			clientId
		);
		if(Util.isEmpty(secretKey, true)) {
			throw new AdempiereException(
				"@ECA52_JWT_SECRET_KEY@ @NotFound@"
			);
		}
		return secretKey;
	}
	private static SecretKey getJWT_SecretKey(int clientId) {
		byte[] keyBytes = Base64.getDecoder().decode(
			getJWT_SecretKeyAsString(clientId)
		);
		SecretKey secretKey = Keys.hmacShaKeyFor(keyBytes);
		return secretKey;
	}


	@Override
	public String generateToken(int userId, int roleId) {
		//	Validate user
		if(userId < 0) {
			throw new AdempiereException("@AD_User_ID@ @NotFound@");
		}
		//	Validate Role
		if(roleId < 0) {
			throw new AdempiereException("@AD_Role_ID@ @NotFound@");
		}
		MADToken token = new MADToken(Env.getCtx(), 0, null);
        token.setTokenType(MADTokenDefinition.TOKENTYPE_ThirdPartyAccess);
        if(token.getAD_TokenDefinition_ID() <= 0) {
			// TODO: Change message translation
        	throw new AdempiereException("@AD_TokenDefinition_ID@ @NotFound@");
        }

		MADTokenDefinition definition = MADTokenDefinition.getById(Env.getCtx(), token.getAD_TokenDefinition_ID(), null);
		if (definition == null || definition.getAD_TokenDefinition_ID() <= 0) {
			throw new AdempiereException("@AD_TokenDefinition_ID@ @NotFound@");
		}

		//	Create default session
        //	TODO: Create session with created from parameter
		int clientId = Env.getAD_Client_ID(Env.getCtx());
		SecretKey secretKey = getJWT_SecretKey(clientId);
		long currentTimeMillis = System.currentTimeMillis();
		JwtBuilder jwtBuilder = Jwts.builder()
			// .id(String.valueOf(session.getAD_Session_ID()))
			.claim("AD_Client_ID", clientId)
			.claim("AD_Org_ID", Env.getAD_Org_ID(Env.getCtx()))
			.claim("AD_Role_ID", roleId)
			.claim("AD_User_ID", userId)
			.claim("M_Warehouse_ID", Env.getContextAsInt(Env.getCtx(), "#M_Warehouse_ID"))
			.claim("AD_Language", Env.getAD_Language(Env.getCtx()))
			.issuedAt(
				new Date(currentTimeMillis)
			)
			.signWith(secretKey, Jwts.SIG.HS256)
		;

		//	Validate
		if(definition.isHasExpireDate()) {
			BigDecimal expirationTime = Optional.ofNullable(
					definition.getExpirationTime()
				).orElse(
					new BigDecimal(5 * 60 * 1000)
				)
			;
			token.setExpireDate(
				new Timestamp(currentTimeMillis + expirationTime.longValue())
			);
			jwtBuilder.expiration(
				new Date(currentTimeMillis + expirationTime.longValue())
			);
		}
		userTokenValue = jwtBuilder.compact();
		String tokenValue = null;
		try {
			// 
			byte[] saltValue = new byte[8];
			// Digest computation
			tokenValue = SecureEngine.getSHA512Hash(1000, userTokenValue, saltValue);
		} catch (NoSuchAlgorithmException e) {
			new AdempiereException(e);
		} catch (UnsupportedEncodingException e) {
			new AdempiereException(e);
		}
		if(Util.isEmpty(userTokenValue)) {
			throw new AdempiereException("@TokenValue@ @NotFound@");
		}
        token.setTokenValue(tokenValue);
        token.setAD_User_ID(userId);
        token.setAD_Role_ID(roleId);
        token.saveEx();
        return userTokenValue;
	}

	@Override
	public boolean validateToken(String tokenValue) {
		String encryptedValue = null;
		try {
			Base64.Decoder decoder = Base64.getUrlDecoder();
			String[] chunks = tokenValue.split("\\.");
			if(chunks == null || chunks.length < 3) {
				throw new AdempiereException("@TokenValue@ @NotFound@");
			}
			String payload = new String(decoder.decode(chunks[1]));
			ObjectMapper mapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			Map<String, Object> dataAsMap = mapper.readValue(payload, Map.class);
			Object clientAsObject = dataAsMap.get("AD_Client_ID");
			if(clientAsObject == null
					|| !clientAsObject.getClass().isAssignableFrom(Integer.class)) {
				throw new AdempiereException("@Invalid@ @AD_Client_ID@");
			}

			SecretKey secretKey = getJWT_SecretKey(
				(int) clientAsObject
			);
			//	Validate if is token based
			JwtParser parser = Jwts.parser()
				.verifyWith(secretKey)
				.build()
			;
			Jws<Claims> claims = parser.parseSignedClaims(tokenValue);
			if(!Util.isEmpty(claims.getPayload().getId(), true)) {
				sessionId = Integer.parseInt(
					claims.getPayload().getId()
				);
			} else {
				sessionId = 0;
			}

			byte[] saltValue = new byte[8];
			encryptedValue = SecureEngine.getSHA512Hash(1000, tokenValue, saltValue);
		} catch (Exception e) {
			new AdempiereException(e);
		}
		token = getToken(encryptedValue);
		if(token == null
				|| token.getAD_Token_ID() <= 0) {
			return false;
		}
		MADTokenDefinition definition = MADTokenDefinition.getById(Env.getCtx(), token.getAD_TokenDefinition_ID(), null);
		if(definition.isHasExpireDate()) {
			Timestamp current = new Timestamp(System.currentTimeMillis());
			if(token != null && token.getExpireDate().compareTo(current) > 0) {
				return true;
			} else {
				return false;
			}
		}
		//	Is Ok
		return true;
	}
	
	/**
	 * Get system token based on encrypted user token
	 * @param encryptedValue
	 * @return
	 */
	private MADToken getToken(String encryptedValue) {
		return new Query(Env.getCtx(), MADToken.Table_Name, "TokenValue = ? "
				+ "AND EXISTS(SELECT 1 FROM AD_User_Roles ur WHERE ur.AD_User_ID = AD_Token.AD_User_ID AND ur.AD_Role_ID = AD_Token.AD_Role_ID AND ur.IsActive = 'Y') "
				+ "AND EXISTS(SELECT 1 FROM AD_User u WHERE u.AD_User_ID = AD_Token.AD_User_ID AND u.IsActive = 'Y' AND u.IsLoginUser = 'Y') "
				+ "AND EXISTS(SELECT 1 FROM AD_Role r WHERE r.AD_Role_ID = AD_Token.AD_Role_ID AND r.IsActive = 'Y')", null)
				.setParameters(encryptedValue)
				.setOnlyActiveRecords(true)
				.first();
	}

	public String getLanguage() {
		return language;
	}
	
	public int getSessionId() {
		return sessionId;
	}
}
