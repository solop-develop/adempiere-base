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
 * Copyright (C) 2003-2010 e-Evolution,SC. All Rights Reserved.               *
 * Contributor(s): victor.perez@e-evolution.com http://www.e-evolution.com    *
 *****************************************************************************/

package org.eevolution.manufacturing.process;

import org.adempiere.core.domains.models.I_M_Cost;
import org.adempiere.engine.CostEngineFactory;
import org.adempiere.engine.IDocumentLine;
import org.adempiere.exceptions.AdempiereException;
import org.compiere.model.*;
import org.compiere.util.DB;
import org.compiere.util.Env;
import org.compiere.util.Trx;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * Regenerate Cost Detail The Generate Cost Transaction process allows the
 * detailed cost calculation and cost generation beginning from a date. If you
 * have not yet set COGs Adjustment, you should execute this process before a
 * period's end in order to fix the cost layers.
 *
 * @author victor.perez@e-evolution.com, www.e-evolution.com
 * @author Yamel Senih, ysenih@erpcya.com, ERPCyA http://www.erpcya.com
 *			<li> FR [ 405 ] Wrong Syntax of Delete query in GenerateCostDetail.java
 *			@see https://github.com/adempiere/adempiere/issues/405
 */
public class GenerateCostDetail extends GenerateCostDetailAbstract {
    /**
     * Variables *
     */
    private ArrayList<Object> deleteParameters;
    private ArrayList<Object> resetCostParameters;
    private List<MAcctSchema> acctSchemas = new ArrayList<MAcctSchema>();
    private List<MCostType> costTypes = new ArrayList<MCostType>();
    private List<MCostElement> costElements = new ArrayList<MCostElement>();
    private StringBuffer deleteCostDetailWhereClause;
    private StringBuffer resetCostWhereClause;
    private AtomicInteger processed = new AtomicInteger();
    private List<TransactionsToProcess> transactionsToProcess = new ArrayList<>();
    private boolean isResetAccount = false;

    /**
     * Prepare - e.g., get Parameters.
     */
    protected void prepare() {
        super.prepare();
        if (getDateAcct() != null) {
        	setup();
        }
    } // prepare

    /**
     * execute the Generate Cost Detail
     */
    protected String doIt() throws Exception {
        generateCostDetail();
        return "@Processed@: " + processed;

    }

    /**
     * Delete Cost Detail
     *
     * @throws SQLException
     */
    private void deleteCostDetail(String trxName) throws SQLException {
        StringBuffer sqlDelete;
        //	BR [ 405 ]
        sqlDelete = new StringBuffer("DELETE FROM M_CostDetail WHERE ");
        sqlDelete.append(deleteCostDetailWhereClause);
        DB.executeUpdateEx(sqlDelete.toString(), deleteParameters.toArray(), trxName);
    }

    /**
     * Reset Cost Dimension
     * @param costingMethod
     * @param trxName
     * @throws SQLException
     */
    private void resetCostDimension(String costingMethod, String trxName) throws SQLException {
        StringBuffer sqlReset;
        sqlReset = new StringBuffer("UPDATE M_Cost SET ");

        // Delete M_Cost not for others than average
        if (MCostType.COSTINGMETHOD_AverageInvoice.equals(costingMethod)) {
            sqlReset.append(I_M_Cost.COLUMNNAME_CurrentCostPrice).append("=0.0,");
            sqlReset.append(I_M_Cost.COLUMNNAME_CurrentCostPriceLL).append("= 0.0,");
        }

        sqlReset.append(I_M_Cost.COLUMNNAME_CurrentQty).append("= 0.0,");
        sqlReset.append(I_M_Cost.COLUMNNAME_CumulatedAmt).append("= 0.0,");
        sqlReset.append(I_M_Cost.COLUMNNAME_CumulatedAmtLL).append("= 0.0,");
        sqlReset.append(I_M_Cost.COLUMNNAME_CumulatedQty).append("= 0.0 ");
        sqlReset.append(" WHERE ").append(resetCostWhereClause);
        DB.executeUpdateEx(sqlReset.toString(),
                resetCostParameters.toArray(), trxName);
    }


    /**
     * Setup the collections
     *
     * @throws SQLException
     */
    private void setup() {

        if (getAcctSchemaId() > 0)
            acctSchemas.add(MAcctSchema.get(getCtx(), getAcctSchemaId() , get_TrxName()));
        else
            acctSchemas = Arrays.asList(MAcctSchema
                    .getClientAcctSchema(getCtx(), getAD_Client_ID(),
                            get_TrxName()));

        if (getCostTypeId() > 0)
            costTypes.add(new MCostType(getCtx(), getCostTypeId(),
                    get_TrxName()));
        else
            costTypes = MCostType.get(getCtx(), get_TrxName());

        if (getCostElementId() > 0)
            costElements.add(MCostElement.get(getCtx(), getCostElementId()));
        else
            costElements = MCostElement.getCostElement(getCtx(), get_TrxName());
        
        isResetAccount = getParameterAsBoolean("IsResetAccount");
    }

    /**
     * Apply Criteria for where clause
     *
     * @param accountSchemaId
     * @param costTypeId
     * @param costElementId
     * @param productId
     * @param dateAccount
     */
    private void applyCriteria(int accountSchemaId, int costTypeId,
                               int costElementId, int productId, Timestamp dateAccount, Timestamp dateAccountTo) {
        deleteParameters = new ArrayList<Object>();
        resetCostParameters = new ArrayList<Object>();
        deleteCostDetailWhereClause = new StringBuffer("1=1");
        resetCostWhereClause = new StringBuffer("1=1");

        if (accountSchemaId > 0) {
            deleteCostDetailWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_C_AcctSchema_ID).append("=? ");
            deleteParameters.add(accountSchemaId);
            resetCostWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_C_AcctSchema_ID).append("=? ");
            resetCostParameters.add(accountSchemaId);
        }
        if (costTypeId > 0) {
            deleteCostDetailWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_M_CostType_ID).append("=? ");
            deleteParameters.add(costTypeId);
            resetCostWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_M_CostType_ID).append("=? ");
            resetCostParameters.add(costTypeId);//SHW
        }
        if (costElementId > 0) {
            deleteCostDetailWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_M_CostElement_ID).append("=? ");
            deleteParameters.add(costElementId);
            resetCostWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_M_CostElement_ID).append("=? ");
            resetCostParameters.add(costElementId);//SHW
        }
        if (productId > 0) {
            deleteCostDetailWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_M_Product_ID).append("=? ");
            deleteParameters.add(productId);
            resetCostWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_M_Product_ID).append("=? ");
            resetCostParameters.add(productId);
        }
        if (dateAccount != null) {
            deleteCostDetailWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_DateAcct).append(">=? ");
            deleteParameters.add(dateAccount);
        }
        if (dateAccountTo != null) {
            deleteCostDetailWhereClause.append(" AND ").append(MCostDetail.COLUMNNAME_DateAcct).append("<=? ");
            deleteParameters.add(dateAccountTo);
        }
        //avoid rest cost dimension if not exist transaction conserve of last cost calculated
        resetCostWhereClause.append(" AND EXISTS ( SELECT 1 FROM RV_Transaction WHERE M_Product_ID=? AND TRUNC(DateAcct)>=? AND TRUNC(DateAcct)<=?)");
        resetCostParameters.add(productId);
        resetCostParameters.add(dateAccount);
        resetCostParameters.add(dateAccountTo);
        return;
    }

    public void generateCostDetail() {
    	//	Generate Cost detail
    	transactionsToProcess = getTransactionIdsByDateAcct();
        Map<Integer, Timestamp> productMap = new HashMap<>();
        transactionsToProcess
        .stream()
        .map(productTransactions -> productTransactions.getProductId())
        .distinct()
        .forEach(productTransactionId -> {
        	int transactionProductId = productTransactionId;//new Integer(productTransaction.getName());
            //	
            Trx.run(transactionName -> {
            	acctSchemas.forEach(accountSchema -> {
            		costTypes.forEach(costType -> {
            			costElements.forEach(costElement -> {
            				applyCriteria(accountSchema.getC_AcctSchema_ID(), costType.getM_CostType_ID(), costElement.getM_CostElement_ID(), transactionProductId, getDateAcct(), getDateAcctTo());
            				try {
            					deleteCostDetail(transactionName);
                                resetCostDimension(costType.getCostingMethod(), transactionName);
            				} catch (Exception e) {
								throw new AdempiereException(e);
							}
            			});
            		});
            	});
        		MProduct product = MProduct.get(getCtx(), transactionProductId);
                addLog(transactionProductId, new Timestamp(System.currentTimeMillis()), null, "@M_Product_ID@: " + product .getValue() + " - " + product.getName() + " @Deleted@");
            });
        });
        List<TransactionsToProcess> TransactionsToProcessCopy = transactionsToProcess
        																.stream()
        																.filter(transaction -> transaction.getMovementType().equals(MTransaction.MOVEMENTTYPE_VendorReceipts) && transaction.getInOutLineId() > 0)
        																.collect(Collectors.toList());
        TransactionsToProcessCopy.forEach(transaction -> {
        	addLandedAndAdjustmentsCosts(transaction);
        });
        AtomicReference <Timestamp> beforeProcessFirst  = new AtomicReference<>(null);
        //	Generate Cost
        transactionsToProcess
        .stream()
        .sorted(Comparator.comparing(TransactionsToProcess::getProductId)
        				  .thenComparing(TransactionsToProcess::getDateAcct)
        		)
        .forEach(productTransaction -> {
            int transactionProductId = productTransaction.getProductId();
            //	
            Trx.run(transactionName -> {
            	//	
            	//Generate cost
//            	Timestamp beforeProcess = new Timestamp(System.currentTimeMillis());
//            	beforeProcessFirst.set(Optional.ofNullable(beforeProcessFirst.get()).orElse(new Timestamp(System.currentTimeMillis())));
            	acctSchemas.forEach(accountSchema -> {
            		costTypes.forEach(costType -> {
            			costElements.forEach(costElement -> {
            				try {
            					productTransaction.getTransaction().set_TrxName(transactionName);
//            					System.out.println( productTransaction.getDateAcct());
                                generateCostDetail(accountSchema, costType, costElement, productTransaction);
                                
            				} catch (Exception e) {
								throw new AdempiereException(e);
							}
            			});
            		});
            	});
//            	System.out.println(TimeUtil.formatElapsed(beforeProcess, null));
//            	System.out.println("Total -> " + TimeUtil.formatElapsed(beforeProcessFirst.get(), null));
            });
            
        	productMap.put(transactionProductId, new Timestamp(System.currentTimeMillis()));
        });
        productMap.entrySet().forEach(products -> {
        	//	Add to log
    		MProduct product = MProduct.get(getCtx(), products.getKey());
            addLog(products.getKey(), products.getValue(), null, "@M_Product_ID@: " + product .getValue() + " - " + product.getName() + " @Processed@");
        	//	Increment counter
        	processed.incrementAndGet();
        });

    }
    
    private void addLandedAndAdjustmentsCosts(TransactionsToProcess transaction) {
    	
    	MInOutLine line = MInOutLine.get(getCtx(), transaction.getInOutLineId());
    	
    	// Calculate adjustment cost by variances in
        // invoices
    	List<MMatchPO> orderMatches = MMatchPO.getInOutLine(line);
        orderMatches.stream().forEach(match -> {
            if (match.getM_Product_ID() == transaction.getProductId()
		            && match.getDateAcct().after(getDateAcct())
		            	&& match.getDateAcct().before(getDateAcctTo())) 
            	transactionsToProcess.add(new TransactionsToProcess(transaction.getTransaction(), match, get_TrxName()));
            
        });
        
        List<MMatchInv> invoiceMatches = MMatchInv.getInOutLine(line);
        invoiceMatches.forEach(match -> {
             if (match.getM_Product_ID() == transaction.getProductId()
            		 && match.getDateAcct().after(getDateAcct())
            		 	&& match.getDateAcct().before(getDateAcctTo())) 
            	 transactionsToProcess.add(new TransactionsToProcess(transaction.getTransaction(), match, get_TrxName()));
        });
        
    	//get landed allocation cost
        List<MLandedCostAllocation> landedCostAllocations;
        if (getCostElementId() > 0 )
        	landedCostAllocations = MLandedCostAllocation.getOfInOutline(line, getCostElementId());
        else
        	landedCostAllocations = MLandedCostAllocation.getOfInOutline(line);
        
        landedCostAllocations.stream().filter(allocation -> allocation.getM_InOutLine().isProcessed()).forEach(allocation -> {
            if (allocation.getDateAcct().after(getDateAcct()) && allocation.getDateAcct().before(getDateAcctTo())) 
            	transactionsToProcess.add(new TransactionsToProcess(transaction.getTransaction(), allocation, get_TrxName()));
        });
    	
    }
    
    public void generateCostDetail(MAcctSchema accountSchema, MCostType costType, MCostElement costElement, TransactionsToProcess transaction) {

    	MTransaction inventoryTransaction = transaction.getTransaction(); 
        //Create Cost Detail for this Transaction
    	if (transaction.isInventory()) 
    		CostEngineFactory.getCostEngine(accountSchema.getAD_Client_ID()).createCostDetail(accountSchema, costType, costElement, inventoryTransaction, inventoryTransaction.getDocumentLine(), true);
    	
    	if (isResetAccount)
    		CostEngineFactory.getCostEngine(accountSchema.getAD_Client_ID()).clearAccounting(accountSchema, inventoryTransaction);
    	
    	
        // Calculate adjustment cost by variances in
        // invoices
        if (MTransaction.MOVEMENTTYPE_VendorReceipts.equals(inventoryTransaction.getMovementType())) {
            if (MCostElement.COSTELEMENTTYPE_Material.equals(costElement.getCostElementType())
            		&& (transaction.getModel() instanceof MMatchInv || transaction.getModel() instanceof MMatchPO)) {
            	CostEngineFactory.getCostEngine(accountSchema.getAD_Client_ID()).createCostDetail(accountSchema, costType, costElement, inventoryTransaction, transaction.getModel(), true);
            }	
            //get landed allocation cost
            if (transaction.getModel() instanceof MLandedCostAllocation) {
            	MLandedCostAllocation landedCostAllocation = (MLandedCostAllocation) transaction.getModel();
            	if (landedCostAllocation.getM_CostElement_ID() == costElement.getM_CostElement_ID()) {
	                	CostEngineFactory.getCostEngine(accountSchema.getAD_Client_ID()).createCostDetail(accountSchema, costType, costElement, inventoryTransaction, landedCostAllocation, true);
            	}
            }
        }
    }

    private List<TransactionsToProcess> getTransactionIdsByDateAcct() {
        StringBuilder sql = new StringBuilder();
        StringBuilder whereClause = new StringBuilder("WHERE ");
       
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<TransactionsToProcess> list = new ArrayList<TransactionsToProcess>();
        try
        {
        	 whereClause.append(MCostDetail.COLUMNNAME_AD_Client_ID).append("=")
             .append(getAD_Client_ID()).append(" AND ");
		     if (getProductId() > 0) {
		         whereClause.append(MCostDetail.COLUMNNAME_M_Product_ID)
		                 .append("=?").append(" AND ");
		     }
		     
		     if (getProductCategoryId() > 0) {
		         whereClause.append("M_Product_Category_ID")
		                 .append("=?").append(" AND ");
		     }
		     whereClause.append("TRUNC(").append(MCostDetail.COLUMNNAME_DateAcct).append(")>=?");
		
		     if (getDateAcctTo() != null) {
		         whereClause.append(" AND TRUNC(").append(MCostDetail.COLUMNNAME_DateAcct).append(")<=?");
		     }
		
		     sql.append("SELECT M_Transaction_ID , M_Product_ID, DateAcct, MovementType, M_InOutLine_ID FROM RV_Transaction ")
		             .append(whereClause)
		             .append(" ORDER BY lowlevel desc, M_Product_ID ,  TRUNC( DateAcct ) , M_Transaction_ID , SUBSTR(MovementType,2,1) , IsReversed");
		     
		     
        	int index = 1;
            pstmt = DB.prepareStatement(sql.toString(), get_TrxName());
            if (getProductId() > 0)
            	pstmt.setInt(index++, getProductId());
            
            if (getProductCategoryId() > 0)
            	pstmt.setInt(index++, getProductCategoryId());
            
            pstmt.setTimestamp(index++, getDateAcct());
            
            if (getDateAcctTo() != null)
            	pstmt.setTimestamp(index++, getDateAcctTo());
            
            
            rs = pstmt.executeQuery();
            while (rs.next())
                list.add(new TransactionsToProcess(rs.getInt(MTransaction.COLUMNNAME_M_Transaction_ID),
                										rs.getInt(MTransaction.COLUMNNAME_M_Product_ID), 
                										rs.getTimestamp(MCostDetail.COLUMNNAME_DateAcct),
                										rs.getString(MTransaction.COLUMNNAME_MovementType),
                										Optional.ofNullable(rs.getInt(MTransaction.COLUMNNAME_M_InOutLine_ID)).orElse(0),
                										get_TrxName()
                										));
            
        }
        catch (Exception e)
        {
            log.severe(e.getMessage());
        }
        finally
        {
        	DB.close(rs, pstmt);
            rs= null;
            pstmt = null;
        }
        
        return list;
    }
}

class TransactionsToProcess{

	private MTransaction transaction;
	private IDocumentLine model;
	private Timestamp dateAcct;
	private int transactionId =0;
	private int productId = 0;
	private boolean process = false;
	private boolean isInventory = false;
	private String movementType = "";
	private int inOutLineId = 0;
	private String trxName = null;
	
	public TransactionsToProcess(MTransaction transaction , IDocumentLine model, String trxName) {
		this.transaction = transaction;
		this.model = model;
		this.dateAcct = model.getDateAcct();
		this.trxName = trxName;
		Optional.ofNullable(this.transaction).ifPresent(trx -> setProductId(trx.getM_Product_ID())); 
		isInventory = false;
		
	}
	
	public TransactionsToProcess(int transactionId, int productId, Timestamp dateAcct, String movementType, int inOutLineId, String trxName) {
		this.transactionId = transactionId;
		this.productId = productId;
		this.dateAcct = dateAcct;
		this.movementType = movementType;
		this.inOutLineId = inOutLineId; 
		this.trxName = trxName;
		isInventory =true;
	}
	
	public MTransaction getTransaction() {
		transaction = Optional.ofNullable(transaction).orElseGet(() -> new MTransaction(Env.getCtx(), transactionId, trxName));
		return transaction;
	}
	
	public void setTransaction(MTransaction transaction) {
		this.transaction = transaction;
		Optional.ofNullable(this.transaction).ifPresent(trx -> setTransactionId(trx.getM_Transaction_ID()));
	}
	
	public IDocumentLine getModel() {
		return model;
	}
	
	public void setModel(IDocumentLine model) {
		this.model = model;
	}
	
	public Timestamp getDateAcct() {
		return dateAcct;
	}
	
	public void setDateAcct(Timestamp dateAcct) {
		this.dateAcct = dateAcct;
	}
	
	public int getTransactionId() {
		return transactionId;
	}
	
	public void setTransactionId(int transactionId) {
		this.transactionId = transactionId;
	}
	
	public int getProductId() {
		return productId;
	}
	
	public void setProductId(int productId) {
		this.productId = productId;
	}
	
	public boolean isProcess() {
		return process;
	}
	
	public void setProcess(boolean process) {
		this.process = process;
	}
	
	public boolean isInventory() {
		return isInventory;
	}
	
	public String getMovementType() {
		return movementType;
	}
	
	public void setMovementType(String movementType) {
		this.movementType = movementType;
	}
	
	public int getInOutLineId() {
		return inOutLineId;
	}
	
	public void setInOutLineId(int inOutLineId) {
		this.inOutLineId = inOutLineId;
	}
	
	public String getTrxName() {
		return trxName;
	}
	
	public void setTrxName(String trxName) {
		this.trxName = trxName;
	}
	
	@Override
	public String toString() {
		return getDateAcct().toString().concat(" ").concat(Integer.toString(getTransactionId())).concat(" ").concat(this.model ==null ? "" : this.model.getClass().getCanonicalName());
	}
}
 