/** Copyright (C) 2023, e-Evolution , http://e-evolution.com This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by
  * the Free Software Foundation, either version 3 of the License, or (at your option) any later version. This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
  * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details. You should have received a copy of the GNU General
  * Public License along with this program. If not, see <http://www.gnu.org/licenses/>. Email: victor.perez@e-evolution.com , http://www.e-evolution.com , http://github.com/e-Evolution Created by
  * victor.perez@e-evolution.com , www.e-evolution.com
  */

package org
package eevolution
package context
package service
package domain
package callouts

import java.util.Properties

import org.adempiere.model.GridTabWrapper
import org.compiere.model.*
import org.adempiere.core.domains.models.*

class CalloutAssignment extends CalloutEngine {

  def product(
    context:   Properties,
    windowNo:  Int,
    gridTab:   GridTab,
    gridField: GridField,
    value:     Object
  ): String = {
    val contractLine = GridTabWrapper.create(gridTab, classOf[I_S_ContractLine])
    if isCalloutActive || value == null then return ""
    val resourceAssignmentId = value.asInstanceOf[Integer]
    if resourceAssignmentId == 0 then return ""
    val resourceAssignment = new MResourceAssignment(context, resourceAssignmentId, null)
    val product = MProduct.forS_Resource_ID(context, resourceAssignment.getS_Resource_ID, null)
    log.fine(s"S_ResourceAssignment_ID $resourceAssignmentId - M_Product_ID= ${product.getM_Product_ID}");
    if product.getM_Product_ID > 0 then {
      contractLine.setM_Product_ID(product.getM_Product_ID)
      val description = Option(product.getDescription).map(description => product.getName + " (" + description + ")")
      contractLine.setDescription(description.getOrElse(product.getName))
      if gridTab.getTableName.startsWith("S_ContractLine") then {
        contractLine.setQtyEntered(resourceAssignment.getQty)
      }
    }
    ""
  }

}
