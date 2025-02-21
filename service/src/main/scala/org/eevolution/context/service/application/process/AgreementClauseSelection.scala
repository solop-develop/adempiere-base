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
package application
package process

import org.eevolution.context.service.infrastructure.domain.entities.{MSAgreementClause, MSContractTermCondition, MSContractTermConditionLine}

/** Adding Agreement Clause to contract
  */
class AgreementClauseSelection extends AgreementClauseSelectionAbstract {

  override def prepare(): Unit = super.prepare()

  override def doIt(): String = {
    if getRecord_ID > 0 then {
      val contractTermCondition = new MSContractTermCondition(getCtx, getRecord_ID, get_TrxName())
      getSelectionKeys.forEach(agreementClauseId => {
        val agreementClause = new MSAgreementClause(getCtx, agreementClauseId, get_TrxName())
        val contractTermConditionLine = new MSContractTermConditionLine(getCtx, 0, get_TrxName())
        contractTermConditionLine.setS_ContractTermCondition_ID(contractTermCondition.get_ID())
        contractTermConditionLine.setS_AgreementClause_ID(agreementClause.getS_AgreementClause_ID)
        contractTermConditionLine.setDescription(agreementClause.getDescription)
        contractTermConditionLine.setSeqNo(agreementClause.getSeqNo)
        contractTermConditionLine.setIsActive(agreementClause.isActive)
        contractTermConditionLine.saveEx()
      })
      contractTermCondition.setTermsAndConditions("")
      contractTermCondition.saveEx()
    }

    "@Ok@"
  }

}
