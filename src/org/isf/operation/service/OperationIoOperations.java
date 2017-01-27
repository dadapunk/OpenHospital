package org.isf.operation.service;

/*----------------------------------------------------------
 * modification history
 * ====================
 * 13/02/09 - Alex - modified query for ordering resultset
 *                   by description only
 * 13/02/09 - Alex - added Major/Minor control
 -----------------------------------------------------------*/

import java.util.ArrayList;
import java.util.List;

import org.isf.operation.model.Operation;
import org.isf.opetype.model.OperationType;
import org.isf.utils.db.DbJpaUtil;
import org.isf.utils.exception.OHException;
import org.springframework.stereotype.Component;

/**
 * This class offers the io operations for recovering and managing
 * operations records from the database
 * 
 * @author Rick, Vero, pupo
 */
@Component
public class OperationIoOperations {
	
	/**
	 * return the {@link Operation}s whose type matches specified string
	 * 
	 * @param typeDescription - a type description
	 * @return the list of {@link Operation}s. It could be <code>empty</code> or <code>null</code>.
	 * @throws OHException 
	 */
    @SuppressWarnings("unchecked")
	public ArrayList<Operation> getOperation(
			String typeDescription) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		ArrayList<Operation> operations = null;
		String query = null;
		ArrayList<Object> params = new ArrayList<Object>();
				
		
		jpa.beginTransaction();
		if (typeDescription == null) 
		{
			query = "SELECT * FROM OPERATION JOIN OPERATIONTYPE ON OPE_OCL_ID_A = OCL_ID_A ORDER BY OPE_DESC";
		}
		else
		{
			query = "SELECT * FROM OPERATION JOIN OPERATIONTYPE ON OPE_OCL_ID_A = OCL_ID_A WHERE OCL_DESC LIKE CONCAT('%', ? , '%') ORDER BY OPE_DESC";
		}	
		jpa.createQuery(query, Operation.class, false);
		if (typeDescription != null) 
		{
			params.add(typeDescription);
			jpa.setParameters(params, false);
		}
		List<Operation> operationList = (List<Operation>)jpa.getList();
		operations = new ArrayList<Operation>(operationList);		
		
		jpa.commitTransaction();

		return operations;
	}
	
	/**
	 * insert an {@link Operation} in the DBs
	 * 
	 * @param operation - the {@link Operation} to insert
	 * @return <code>true</code> if the operation has been inserted, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean newOperation(
			Operation operation) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.persist(operation);
    	jpa.commitTransaction();
    	
		return result;
	}
	
	/**
	 * Checks if the specified {@link Operation} has been modified.
	 * @param operation - the {@link Operation} to check.
	 * @return <code>true</code> if has been modified, <code>false</code> otherwise.
	 * @throws OHException if an error occurs during the check.
	 */
	public boolean hasOperationModified(
			Operation operation) throws OHException 
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Operation foundOperation = (Operation)jpa.find(Operation.class, operation.getCode());
		boolean result = false;
		
		
		if (foundOperation.getLock() != operation.getLock())
		{
			result = true;
		}
		
		return result;
	}
	
	/** 
	 * updates an {@link Operation} in the DB
	 * 
	 * @param operation - the {@link Operation} to update
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean updateOperation(
			Operation operation) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		operation.setLock(operation.getLock() + 1);
		jpa.merge(operation);
    	jpa.commitTransaction();
    	
		return result;
	}
	
	/** 
	 * Delete a {@link Operation} in the DB
	 * @param operation - the {@link Operation} to delete
	 * @return <code>true</code> if the item has been updated, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean deleteOperation(
			Operation operation) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		boolean result = true;
		
		
		jpa.beginTransaction();	
		jpa.remove(operation);
    	jpa.commitTransaction();
    	
		return result;
	}
	
	/**
	 * checks if an {@link Operation} code has already been used
	 * @param code - the code
	 * @return <code>true</code> if the code is already in use, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean isCodePresent(
			String code) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Operation foundOperation = (Operation)jpa.find(Operation.class, code);
		boolean present = false;

		
		if (foundOperation != null)
		{
			present = true;
		}
		
		return present;
	}
	
	/**
	 * checks if an {@link Operation} description has already been used within the specified {@link OperationType} 
	 * 
	 * @param description - the {@link Operation} description
	 * @param typeCode - the {@link OperationType} code
	 * @return <code>true</code> if the description is already in use, <code>false</code> otherwise.
	 * @throws OHException 
	 */
	public boolean isDescriptionPresent(
			String description, 
			String typeCode) throws OHException
	{
		DbJpaUtil jpa = new DbJpaUtil(); 
		Operation foundOperation = (Operation)jpa.find(Operation.class, typeCode);
		boolean present = false;

		
		if (foundOperation.getDescription().compareTo(description) == 0)
		{
			present = true;
		}
		
		return present;
	}
}

