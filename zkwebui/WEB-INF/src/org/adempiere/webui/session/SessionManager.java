/******************************************************************************
 * Product: Posterita Ajax UI 												  *
 * Copyright (C) 2007 Posterita Ltd.  All Rights Reserved.                    *
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
 * Posterita Ltd., 3, Draper Avenue, Quatre Bornes, Mauritius                 *
 * or via info@posterita.org or http://www.posterita.org/                     *
 *****************************************************************************/

package org.adempiere.webui.session;

import org.adempiere.exceptions.AdempiereException;
import org.adempiere.webui.IWebClient;
import org.adempiere.webui.apps.AEnv;
import org.adempiere.webui.desktop.IDesktop;
import org.adempiere.webui.util.UserPreference;
import org.compiere.model.MSession;
import org.compiere.model.MUser;
import org.compiere.util.CLogger;
import org.compiere.util.Env;
import org.compiere.util.SecureEngine;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.impl.ExecutionCarryOver;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.logging.Level;

import static org.adempiere.webui.session.SessionContextListener.SERVLET_SESSION_ID;

/**
 * @author <a href="mailto:agramdass@gmail.com">Ashley G Ramdass</a>
 * @version $Revision: 0.10 $
 * @date Feb 25, 2007
 */
public class SessionManager {

    private static final CLogger log = CLogger.getCLogger(SessionManager.class);

    private static final Map<String, IWebClient> applicationCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, HttpSession> sessionCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, Properties> sessionContextCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, IDesktop> desktopCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, ExecutionCarryOver> executionCarryOverCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, Desktop> executionDesktopCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, UserPreference> sessionUserPreferenceCache = Collections.synchronizedMap(new Hashtable<>());
    private static final Map<String, String> userAuthenticationCache = Collections.synchronizedMap(new Hashtable<>());

    public static boolean isUserLoggedIn(Properties ctx) {
        String adUserId = Env.getContext(ctx, "#AD_User_ID");
        String adRoleId = Env.getContext(ctx, "#AD_Role_ID");
        String adClientId = Env.getContext(ctx, "#AD_Client_ID");
        String adOrgId = Env.getContext(ctx, "#AD_Org_ID");

        return (!"".equals(adUserId) && !"".equals(adRoleId)
                && !"".equals(adClientId) && !"".equals(adOrgId));
    }

    public static void setApplication(String sessionId, IWebClient application) {
        applicationCache.put(sessionId, application);
    }

    public static IWebClient getApplication(String sessionId) {
        return applicationCache.get(sessionId);
    }

    public static void removeApplication(String sessionId) {
        applicationCache.remove(sessionId);
    }

    public static IDesktop getAppDesktop() {
        return getApplication().getApplicationDesktop();
    }

    public static IWebClient getApplication() {
        String sessionId = Env.getContext(Env.getCtx() , SERVLET_SESSION_ID);
        return applicationCache.get(sessionId);
    }

    public static void changeRole(MUser user) {
		Optional.ofNullable(getApplication()).ifPresent(application -> application.changeRole(user));
    }

    public static boolean activateDesktop(Desktop desktop) {
        if (Events.inEventListener()) {
            return true;
        }
        if (desktop == null) {
            log.severe("Attempted to activate NULL desktop.");
            return false;
        }

        try {
            if (Executions.activate((org.zkoss.zk.ui.Desktop) desktop, 500)) {
                return true;
            } else {
                log.fine("Unable to grab control of desktop.");
                if (!desktop.isServerPushEnabled()) {
                    log.severe("Unexpected. Server Push not enabled");
                }

            }

        } catch (Exception e) {
            log.severe(e.getMessage());
        } finally {
        }
        return false;
    }

    public static void releaseDesktop(Desktop desktop) {
        if (desktop == null) {
            return;
        }
        Executions.deactivate((org.zkoss.zk.ui.Desktop) desktop);
    }

    public static Map<String, HttpSession> getSessionCache() {
        return sessionCache;
    }

    public static Map<String, Properties> getSessionContextCache() {
        return sessionContextCache;
    }

    public static void removeSession(String sessionId) {
        if (sessionCache.containsKey(sessionId)) {
            sessionCache.remove(sessionId);
            removeSessionCache(sessionId);
        }
        else throw new AdempiereException("Application not exist with this Id :" + sessionId);
    }

    public static void addSession(HttpSession httpSession) {
        if (!sessionCache.containsKey(httpSession.getId())) {
            sessionCache.put(httpSession.getId(), httpSession);
            createSessionContext(httpSession.getId());
        }
    }

    public static HttpSession getSession(String sessionId) {
        if (sessionCache.containsKey(sessionId)) {
            return sessionCache.get(sessionId);
        } else {
            throw new AdempiereException("Session not exist");
        }
    }

    public static boolean existsSession(String sessionId) {
        return sessionCache.containsKey(sessionId);
    }

    public static void loadUserPreference(Integer authenticatedUserId) {
        HttpSession httpSession = (HttpSession) AEnv.getDesktop().getSession().getNativeSession();
        UserPreference userPreference = new UserPreference();
        userPreference.loadPreference(authenticatedUserId);
        sessionUserPreferenceCache.put(httpSession.getId(), userPreference);
    }

    public static void loadUserPreference(String httpSessionId, Integer authenticatedUserId) {
        UserPreference userPreference = new UserPreference();
        userPreference.loadPreference(authenticatedUserId);
        sessionUserPreferenceCache.put(httpSessionId, userPreference);
    }

    public static UserPreference getUserPreference() {
        HttpSession httpSession = (HttpSession) AEnv.getDesktop().getSession().getNativeSession();
        return getUserPreference(httpSession.getId());
    }

    public static UserPreference getUserPreference(String sessionId) {
        return sessionUserPreferenceCache.get(sessionId);
    }

    public static void clearSessions() {
        sessionCache.clear();
    }

    public static void clearSession(String sessionId) {
        HttpSession session = getSession(sessionId);
		Optional.ofNullable(getApplication()).ifPresent(application -> {
            int adempiereSessionId = Env.getContextAsInt(Env.getCtx(), "#AD_Session_ID");
            if (adempiereSessionId > 0) {
                MSession adempiereSession = new MSession(Env.getCtx(), adempiereSessionId, null);
                adempiereSession.logout();
                log.info("ADempiere Session " +sessionId + " Logout ...");
            }
            application.logoutDestroyed();
            session.removeAttribute("Check_AD_User_ID");
            session.removeAttribute(Attributes.PREFERRED_LOCALE);
            SessionManager.removeApplication(sessionId);
        });
        log.info("Session " + sessionId + " Invalidate ...");
    }



    public static Properties getSessionContext(String sessionId) {
        return sessionContextCache.get(sessionId);
    }

    public static void removeSessionCache(String sessionId) {
        sessionContextCache.remove(sessionId);
        sessionUserPreferenceCache.remove(sessionId);
        desktopCache.remove(sessionId);
        executionCarryOverCache.remove(sessionId);
        executionDesktopCache.remove(sessionId);
        userAuthenticationCache.remove(sessionId);
    }

    public static void createSessionContext(String sessionId) {
        Properties context = new Properties();
        context.put(SERVLET_SESSION_ID, sessionId);
        sessionContextCache.put(sessionId, context);
    }

    public static boolean containsKeySessionContext(String sessionId) {
        return sessionContextCache.containsKey(sessionId);
    }


    public static void setApplicationDesktop(String sessionId, IDesktop desktop) {
         desktopCache.put(sessionId,desktop);
    }

    public static IDesktop getApplicationDesktop(String sessionId) {
        return desktopCache.get(sessionId);
    }

    public static void setExecutionCarryOverCache(String sessionId, ExecutionCarryOver executionCarryOver) {
        executionCarryOverCache.put(sessionId,executionCarryOver);
    }

    public static ExecutionCarryOver getExecutionCarryOver(String sessionId){
        return executionCarryOverCache.get(sessionId);
    }

    public static void setDesktop(String sessionId, Desktop desktop) {
        executionDesktopCache.put(sessionId,desktop);
    }

    public static Desktop getDesktop(String sessionId){
        return executionDesktopCache.get(sessionId);
    }


    public static void setUserAuthentication(String sessionId, String authentication) {
        userAuthenticationCache.put(sessionId,  SecureEngine.encrypt(authentication));
    }

    public static String getUserAuthentication(String sessionId){
        return  SecureEngine.decrypt(userAuthenticationCache.get(sessionId));
    }
}
