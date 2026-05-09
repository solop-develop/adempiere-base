/******************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                       *
 * Copyright (C) 1999-2006 ComPiere, Inc. All Rights Reserved.                *
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
 * ComPiere, Inc., 2620 Augustine Dr. #245, Santa Clara, CA 95054, USA        *
 * or via info@compiere.org or http://www.compiere.org/license.html           *
 *****************************************************************************/
package org.compiere.model;

import org.adempiere.core.domains.models.X_M_DistributionRun;

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

/**
 *	Distribution Run Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MDistributionRun.java,v 1.3 2006/07/30 00:51:03 jjanke Exp $
 */
public class MDistributionRun extends X_M_DistributionRun
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4355723603388382287L;

	/**
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param M_DistributionRun_ID id
	 *	@param trxName transaction
	 */
	public MDistributionRun (Properties ctx, int M_DistributionRun_ID, String trxName)
	{
		super (ctx, M_DistributionRun_ID, trxName);
	}	//	MDistributionRun

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs result set
	 *	@param trxName transaction
	 */
	public MDistributionRun (Properties ctx, ResultSet rs, String trxName)
	{
		super(ctx, rs, trxName);
	}	//	MDistributionRun
	
	/**	 Cached Lines					*/
	private List<MDistributionRunLine> 	m_lines = null;
	
	/**
	 * 	Get active, non zero lines
	 *	@param reload true if reload
	 *	@return lines
	 */
	public List<MDistributionRunLine> getLines (boolean reload)
	{
		if (!reload && m_lines != null) {
			set_TrxName(m_lines.toArray(new MDistributionRunLine[0]), get_TrxName());
			return m_lines;
		}
		m_lines = new Query(getCtx(), MDistributionRunLine.Table_Name,
			"M_DistributionRun_ID=? " +
					"AND IsActive='Y' " +
					"AND TotalQty IS NOT NULL " +
					"AND TotalQty<> 0", get_TrxName())
				.setParameters(getM_DistributionRun_ID())
			.setOrderBy("Line")
			.list();
		return m_lines;
	}	//	getLines
	
}	//	MDistributionRun
