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

import java.sql.ResultSet;
import java.util.List;
import java.util.Properties;

import org.adempiere.core.domains.models.I_CM_Media_Server;
import org.adempiere.core.domains.models.X_CM_Media_Server;
import org.compiere.util.CLogger;

/**
 * 	Media Server Model
 *	
 *  @author Jorg Janke
 *  @version $Id: MMediaServer.java,v 1.3 2006/07/30 00:51:05 jjanke Exp $
 *  @author red1 - FR: [ 2214883 ] Remove SQL code and Replace for Query
 */
public class MMediaServer extends X_CM_Media_Server
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1065424104545571149L;


	/**
	 * 	Get Media Server
	 *	@param project
	 *	@return server list
	 */
	public static MMediaServer[] getMediaServer (MWebProject project)
	{
		final String whereClause = I_CM_Media_Server.COLUMNNAME_CM_WebProject_ID+"=?";
		List<MMediaServer> list = new Query(project.getCtx(),MMediaServer.Table_Name,whereClause,project.get_TrxName())
		.setParameters(project.getCM_WebProject_ID())
		.setOrderBy(I_CM_Media_Server.COLUMNNAME_CM_Media_Server_ID)
		.list();
		MMediaServer[] retValue = new MMediaServer[list.size ()];
		list.toArray (retValue);
		return retValue;
	}	//	getMediaServer
	
	/**	Logger	*/
	private static CLogger s_log = CLogger.getCLogger (MMediaServer.class);
	
	
	/**************************************************************************
	 * 	Standard Constructor
	 *	@param ctx context
	 *	@param CM_Media_Server_ID id
	 *	@param trxName transaction
	 */
	public MMediaServer (Properties ctx, int CM_Media_Server_ID, String trxName)
	{
		super (ctx, CM_Media_Server_ID, trxName);
	}	//	MMediaServer

	/**
	 * 	Load Constructor
	 *	@param ctx context
	 *	@param rs request 
	 *	@param trxName transaction
	 */
	public MMediaServer (Properties ctx, ResultSet rs, String trxName)
	{
		super (ctx, rs, trxName);
	}	//	MMediaServer
	
}	//	MMediaServer
