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
 *****************************************************************************/
package org.spin.hr.util;

import org.compiere.util.CLogger;
import org.compiere.util.Util;
import org.eevolution.hr.model.MHRAttribute;

import java.util.Map;
import java.util.logging.Level;

/**
 * Payroll Calculation Engine — evaluates embedded expressions in HR_Attribute.
 *
 * Only handles CalculationTypeValue='E' (Expression).
 * Scripts (CalculationTypeValue='S' or null) are handled by the AD_Rule engine
 * in MHRProcess.executeScript().
 *
 * @author Solop SP099
 */
public class PayrollCalculationEngine {

    private static final CLogger logger = CLogger.getCLogger(PayrollCalculationEngine.class);

    /**
     * Evaluates the expression stored in the attribute.
     * Called only when CalculationTypeValue = 'E'.
     *
     * @param attribute attribute record with Expression set
     * @param scriptCtx payroll script context variables
     * @return calculation result
     */
    public static Object calculate(MHRAttribute attribute, Map<String, Object> scriptCtx) {
        String expression = attribute.getExpression();
        if (Util.isEmpty(expression)) {
            logger.log(Level.WARNING, "HR_Attribute #" + attribute.getHR_Attribute_ID() + " has no Expression defined");
            return null;
        }
        logger.info("Evaluating expression: " + expression);
        return PayrollExpressionEvaluator.evaluate(expression, scriptCtx);
    }
}
