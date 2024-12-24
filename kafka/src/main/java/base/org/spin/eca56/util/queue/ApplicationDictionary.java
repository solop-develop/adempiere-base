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
 * Copyright (C) 2003-2013 E.R.P. Consultores y Asociados, C.A.               *
 * All Rights Reserved.                                                       *
 * Contributor(s): Yamel Senih www.erpcya.com                                 *
 *****************************************************************************/
package org.spin.eca56.util.queue;

import java.util.List;

import org.adempiere.core.domains.models.I_AD_Browse;
import org.adempiere.core.domains.models.I_AD_Form;
import org.adempiere.core.domains.models.I_AD_Language;
import org.adempiere.core.domains.models.I_AD_Menu;
import org.adempiere.core.domains.models.I_AD_Process;
import org.adempiere.core.domains.models.I_AD_Role;
import org.adempiere.core.domains.models.I_AD_Tree;
import org.adempiere.core.domains.models.I_AD_Window;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MClient;
import org.compiere.model.MClientInfo;
import org.compiere.model.MLanguage;
import org.compiere.model.PO;
import org.compiere.model.Query;
import org.compiere.util.Util;
import org.spin.eca56.util.support.IGenericDictionaryDocument;
import org.spin.eca56.util.support.IGenericSender;
import org.spin.eca56.util.support.documents.Browser;
import org.spin.eca56.util.support.documents.Form;
import org.spin.eca56.util.support.documents.MenuItem;
import org.spin.eca56.util.support.documents.MenuTree;
import org.spin.eca56.util.support.documents.Process;
import org.spin.eca56.util.support.documents.Role;
import org.spin.eca56.util.support.documents.Window;
import org.spin.queue.model.MADQueue;
import org.spin.queue.util.QueueManager;

/**
 * @author Yamel Senih, ysenih@erpya.com, ERPCyA http://www.erpya.com
 * Just a test for service  
 */
public class ApplicationDictionary extends QueueManager implements IEngineDictionaryManager {

	public static final String CODE = "ADM";
	public static final String ECA56_TemplateDictionary = "ECA56_TemplateDictionary";
	public static final String ECA56_DictionaryCode = "ECA56_DictionaryCode";
	
	@Override
	public void add(int queueId) {
		logger.fine("Queue Added: " + queueId);
		try {
			send(queueId);
			MADQueue queue = new MADQueue(getContext(), queueId, getTransactionName());
			queue.setProcessed(true);
			queue.saveEx();
		} catch (Throwable e) {
			logger.warning(e.getLocalizedMessage());
		}
	}

	@Override
	public void process(int queueId) {
		send(queueId);
	}
	
	private String getDictionaryCode() {
		// Only system
		MClientInfo clientInfo = MClientInfo.get(getContext(), 0);
		String code = clientInfo.get_ValueAsString(ECA56_DictionaryCode);
		if(Util.isEmpty(code, true)) {
			code = "";
		}
		return code.toLowerCase();
	}
	
	public void send(int queueId) {
		PO entity = getEntity();
		if(entity != null) {
			IGenericSender sender = DefaultEngineQueueUtil.getEngineManager();
			if(sender != null) {
				IGenericDictionaryDocument documentByLanguage = getDocumentManager(entity);
				if(documentByLanguage != null) {
					sender.send(documentByLanguage, documentByLanguage.getChannel());
				}
				getLanguages().forEach(languageId -> {
					MLanguage language = new MLanguage(getContext(), languageId, getTransactionName());
					IGenericDictionaryDocument aloneDocument = getDocumentManager(entity, language.getAD_Language());
					if(aloneDocument != null) {
						sender.send(aloneDocument, aloneDocument.getChannel());
					}
				});
			} else {
				throw new AdempiereException("@AD_AppRegistration_ID@ @NotFound@");
			}
			logger.fine("Queue Processed: " + queueId);
		}
	}
	
	private List<Integer> getLanguages() {
		return new Query(getContext(), I_AD_Language.Table_Name, "(IsBaseLanguage = 'Y' OR IsSystemLanguage = 'Y')", getTransactionName())
				.setOnlyActiveRecords(true)
				.getIDsAsList();
	}

	@Override
	public IGenericDictionaryDocument getDocumentManager(PO entity) {
		String tableName = entity.get_TableName();
		if(Util.isEmpty(tableName)) {
			return null;
		}
		if(MClientInfo.get(getContext()).get_ValueAsBoolean(ECA56_TemplateDictionary)) {
			if(tableName.equals(I_AD_Tree.Table_Name)) {
				return MenuTree.newInstance().withEntity(entity);
			}
		} else {
			if(tableName.equals(I_AD_Tree.Table_Name)) {
				return MenuTree.newInstance().withClientId(getDictionaryCode()).withEntity(entity);
			}
		}
		if(tableName.equals(I_AD_Role.Table_Name)) {
			String localClientId = MClient.get(getContext(), entity.getAD_Client_ID()).getUUID();
			return Role.newInstance().withClientId(localClientId).withEntity(entity);
		}
		return null;
	}

	@Override
	public IGenericDictionaryDocument getDocumentManager(PO entity, String language) {
		String tableName = entity.get_TableName();
		if(Util.isEmpty(tableName)) {
			return null;
		}
		if(MClientInfo.get(getContext()).get_ValueAsBoolean(ECA56_TemplateDictionary)) {
			if(tableName.equals(I_AD_Process.Table_Name)) {
				return Process.newInstance().withLanguage(language).withEntity(entity);
			} else if(tableName.equals(I_AD_Browse.Table_Name)) {
				return Browser.newInstance().withLanguage(language).withEntity(entity);
			} else if(tableName.equals(I_AD_Window.Table_Name)) {
				return Window.newInstance().withLanguage(language).withEntity(entity);
			} else if(tableName.equals(I_AD_Menu.Table_Name)) {
				return MenuItem.newInstance().withLanguage(language).withEntity(entity);
			} else if (tableName.equals(I_AD_Form.Table_Name)) {
				return Form.newInstance().withLanguage(language).withEntity(entity);
			}
		} else {
			if(tableName.equals(I_AD_Process.Table_Name)) {
				return Process.newInstance().withLanguage(language).withClientId(getDictionaryCode()).withEntity(entity);
			} else if(tableName.equals(I_AD_Browse.Table_Name)) {
				return Browser.newInstance().withLanguage(language).withClientId(getDictionaryCode()).withEntity(entity);
			} else if(tableName.equals(I_AD_Window.Table_Name)) {
				return Window.newInstance().withLanguage(language).withClientId(getDictionaryCode()).withEntity(entity);
			} else if(tableName.equals(I_AD_Menu.Table_Name)) {
				return MenuItem.newInstance().withLanguage(language).withClientId(getDictionaryCode()).withEntity(entity);
			} else if (tableName.equals(I_AD_Form.Table_Name)) {
				return Form.newInstance().withLanguage(language).withClientId(getDictionaryCode()).withEntity(entity);
			}
		}
		return null;
	}

	@Override
	public IGenericDictionaryDocument getDocumentManagerByRole(PO entity, String language, int roleId) {
		return null;
	}

	@Override
	public IGenericDictionaryDocument getDocumentManagerByUser(PO entity, String language, int userId) {
		return null;
	}
}
