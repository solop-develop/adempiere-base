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

package org.solop.process;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.MDocType;
import org.compiere.model.MOrder;
import org.compiere.model.MProject;
import org.compiere.model.MProjectLine;
import org.compiere.util.TimeUtil;
import org.compiere.util.DisplayType;
import java.util.List;

/** Generated Process for (Quote Convert and Approve Project)
 *  @author ADempiere (generated) 
 *  @version Release 3.9.4
 */
public class QuoteCopyAndApprove extends QuoteCopyAndApproveAbstract
{
	@Override
	protected void prepare()
	{
		super.prepare();
	}

	@Override
	protected String doIt() throws Exception
	{
		if (getOrderId() == 0) {
			throw new AdempiereException("@C_Order_ID@ @NotFound@");
		}
		MDocType dt = MDocType.get(getCtx(), getDocTypeId());
		if (dt.get_ID() == 0) {
			throw new AdempiereException("@C_Order_ID@ @NotFound@");
		}
		if (getDateDoc() == null) {
			setDateDoc(TimeUtil.getDay(System.currentTimeMillis()));
		}
		//
		MOrder from = new MOrder (getCtx(), getOrderId(), get_TrxName());
		MOrder newOrder = MOrder.copyFrom (from, getDateDoc(),
				dt.getC_DocType_ID(), dt.isSOTrx(), false, true, get_TrxName());		//	copy ASI
		newOrder.setC_DocTypeTarget_ID(getDocTypeId());
		newOrder.set_ValueOfColumn("SourceQuote_ID", from.getC_Order_ID());
		if(from.getC_Project_ID() != 0) {
			newOrder.setC_Project_ID(from.getC_Project_ID());
			MProject project = new MProject(getCtx(), from.getC_Project_ID(), get_TrxName());
			if(project.get_ValueAsBoolean("ManageCustomerApproval")) {
				project.set_ValueOfColumn("IsCustomerApproved", true);
				project.saveEx();
				List<MProjectLine> projectLines = project.getLines("C_ProjectLine.IsSummary = 'Y'");
				if(!projectLines.isEmpty()) {
					for(MProjectLine line : projectLines) {
						line.set_ValueOfColumn("IsCustomerApproved", true);
						line.saveEx();
					}
				}
			}
		}
		newOrder.saveEx();
		if (isCloseDocument())  {
			MOrder original = new MOrder (getCtx(), getOrderId(), get_TrxName());
			original.setDocAction(MOrder.DOCACTION_Complete);
			original.processIt(MOrder.DOCACTION_Complete);
			original.saveEx();
			original.setDocAction(MOrder.DOCACTION_Close);
			original.processIt(MOrder.DOCACTION_Close);
			original.saveEx();
		}
		return newOrder.getDocumentNo() + " - " + DisplayType.getNumberFormat(DisplayType.Amount).format(newOrder.getGrandTotal());
	}

}