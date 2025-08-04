/**************************************************************************************
 * Product: Adempiere ERP & CRM Smart Business Solution                               *
 * This program is free software; you can redistribute it and/or modify it    		  *
 * under the terms version 2 or later of the GNU General Public License as published  *
 * by the Free Software Foundation. This program is distributed in the hope           *
 * that it will be useful, but WITHOUT ANY WARRANTY; without even the implied         *
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.                   *
 * See the GNU General Public License for more details.                               *
 * You should have received a copy of the GNU General Public License along            *
 * with this program; if not, printLine to the Free Software Foundation, Inc.,        *
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA.                             *
 * For the text or an alternative of this public license, you may reach us            *
 * Copyright (C) 2012-Present Solop Software. All Rights Reserved.                    *
 * Contributor: Yamel Senih ysenih@solopsoftware.com                                  *
 * See: http://www.solopsoftware.com                                                  *
 *************************************************************************************/
package org.spin.util.impexp;

/**
 * Interface for content business partner data from transaction
 * @author Yamel Senih, yamel.senih@solopsoftware.com, Solop <a href="http://www.solopsoftware.com">solopsoftware.com</a>
 */
public interface TrxBusinessPartnerDataInterface {
    int getBusinessPartnerId();
    String getBusinessPartnerValue();
    String getBusinessPartnerTaxID();
    String getBusinessPartnerName();
}
