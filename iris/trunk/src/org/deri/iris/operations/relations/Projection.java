package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.storage.IRelation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   30.05.2006 10:44:38
 */
public class Projection implements IProjection{

	private IRelation relation = null;
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.operations.relation.IProjection#project(org.deri.iris.api.storage.IRelation, int[])
	 */
	public IRelation project(IRelation relation, int[] indexes) {
		this.relation = relation;
		
		return null;
	}
	
	private ITuple getTupleProjected(ITuple t){
		BASIC.createTuple();
		return null;
	}
}
