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
package org.spin.pos.setup;

import org.adempiere.core.domains.models.X_AD_ModelValidator;
import org.compiere.model.Query;
import org.spin.pos.model.validator.GiftCardModelValidator;
import org.spin.util.ISetupDefinition;

import java.util.Properties;

/**
 * Setup for GiftCard validator
 * @author Gabriel Escalona
 */
public class DeployGiftCard implements ISetupDefinition {

	private static final String SETUP_DESCRIPTION = "(*Created from Setup Automatically*)";
	private static final String SETUP_UUID = "(*AutomaticSetup*)";
	
	@Override
	public String doIt(Properties context, String transactionName) {
		//	Add Model Validator
		createModelValidator(context, transactionName);
		return "@AD_SetupDefinition_ID@ @Ok@";
	}
	
	/**
	 * Create Model Validator
	 * @param context
	 * @param transactionName
	 * @return
	 */
	private X_AD_ModelValidator createModelValidator(Properties context, String transactionName) {
		X_AD_ModelValidator modelValidator = new Query(context, X_AD_ModelValidator.Table_Name, X_AD_ModelValidator.COLUMNNAME_ModelValidationClass + " = ?", transactionName)
				.setParameters(GiftCardModelValidator.class.getName())
				.setClient_ID()
				.<X_AD_ModelValidator>first();
		//	Validate
		if(modelValidator != null
				&& modelValidator.getAD_ModelValidator_ID() > 0) {
			return modelValidator;
		}
		//	
		modelValidator = new X_AD_ModelValidator(context, 0, transactionName);
		modelValidator.setName("Gift Card Model Validator");
		modelValidator.setEntityType("ECA14");
		modelValidator.setDescription(SETUP_DESCRIPTION);
		modelValidator.setSeqNo(200);
		modelValidator.setModelValidationClass(GiftCardModelValidator.class.getName());
		modelValidator.setUUID(SETUP_UUID);
		modelValidator.setIsDirectLoad(true);
		modelValidator.saveEx();
		return modelValidator;
	}
}
