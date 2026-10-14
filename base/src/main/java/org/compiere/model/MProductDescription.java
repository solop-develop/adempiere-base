/******************************************************************************
 * Product: ADempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 2006-2017 ADempiere Foundation, All Rights Reserved.         *
 * This program is free software, you can redistribute it and/or modify it    *
 * under the terms version 2 of the GNU General Public License as published   *
 * or (at your option) any later version.                                     *
 * by the Free Software Foundation. This program is distributed in the hope   *
 * that it will be useful, but WITHOUT ANY WARRANTY, without even the implied *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.           *
 * See the GNU General Public License for more details.                       *
 * You should have received a copy of the GNU General Public License along    *
 * with this program, if not, write to the Free Software Foundation, Inc.,    *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                     *
 * For the text or an alternative of this public license, you may reach us    *
 * or via info@adempiere.net                                                  *
 * or https://github.com/adempiere/adempiere/blob/develop/license.html        *
 *****************************************************************************/
package org.compiere.model;

import java.sql.ResultSet;
import java.util.Properties;

import org.adempiere.core.domains.models.X_M_ProductDescription;
import org.compiere.util.Msg;
import org.compiere.util.Util;

/**
 * 	Product Description Model.
 * 	The description is mandatory and only one description per product and description type is allowed.
 */
public class MProductDescription extends X_M_ProductDescription
{

	private static final long serialVersionUID = 20261008L;

	public MProductDescription(Properties ctx, int M_ProductDescription_ID, String trxName)
	{
		super(ctx, M_ProductDescription_ID, trxName);
	}

	public MProductDescription(Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}

	@Override
	protected boolean beforeSave(boolean newRecord)
	{
		if (Util.isEmpty(getDescription(), true))
		{
			log.saveError("FillMandatory", Msg.parseTranslation(getCtx(), "@Description@"));
			return false;
		}
		if (newRecord
				|| is_ValueChanged(COLUMNNAME_M_Product_ID)
				|| is_ValueChanged(COLUMNNAME_ProductDescriptionType))
		{
			boolean exists = new Query(getCtx(), Table_Name,
					COLUMNNAME_M_Product_ID + "=? AND "
					+ COLUMNNAME_ProductDescriptionType + "=? AND "
					+ COLUMNNAME_M_ProductDescription_ID + "<>?",
					get_TrxName())
				.setParameters(getM_Product_ID(), getProductDescriptionType(), get_ID())
				.match();
			if (exists)
			{
				log.saveError("SaveErrorNotUnique", Msg.parseTranslation(getCtx(),
						"@M_Product_ID@ - @ProductDescriptionType@"));
				return false;
			}
		}
		return true;
	}
}
