/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.evaluation.seminaive;

import org.deri.iris.api.evaluation.IEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;
import org.deri.iris.api.IEDB;
import org.deri.iris.api.evaluation.seminaive.model.*;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.api.evaluation.seminaive.IEvaluationProcedure;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.basics.IPredicate;

import java.util.Map;
import java.util.Arrays;
import java.util.LinkedList;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 08-sep-2006
 */
public abstract class GeneralSeminaiveEvaluation implements IEvaluator{
	protected IEvaluationProcedure method;
	protected IEDB EDB;
	protected Map<ITree, ITree> IDB;
	
	GeneralSeminaiveEvaluation(IEvaluationProcedure e, IEDB EDB, Map<ITree, ITree> IDB) {
		this.method = e;
		this.EDB = EDB;
		this.IDB = IDB;
	}
	
	public abstract IRelation<ITuple>[] evaluate() throws DataModelException;
	
	/**
	 * 
	 * @param P set of original relations
	 * @param Q set of temporal backup relations
	 * @return True if there are no new tuples in any relation in P; false otherwise
	 */
	protected boolean compare(Map<ITree, IRelation<ITuple>> P, Map<ITree, IRelation<ITuple>> Q){		
		for (ITree head : P.keySet())
		{
			if (!Q.get(head).containsAll(Arrays.asList(P.get(head).toArray())))
				return false;
		}
		return true;
	}
	
	/**
	 * @param r set of relations
	 * @return True if all the relations are empty; false otherwise
	 */
	protected boolean isEmpty(Map<ITree, IRelation<ITuple>> r){		
		for (ITree head: r.keySet())
			if (!r.get(head).isEmpty())
				return false;
		return true;
	}
	
	/**
	 * Make a copy of the set of source relations to the target
	 * @param source Set of source relations
	 * @param target Set of target relations
	 */
	protected void copyRelations(Map<ITree, IRelation<ITuple>> source, Map<ITree, IRelation<ITuple>> target) {
		for (ITree head: source.keySet()){
			// 1st. Empty target
			target.get(head).clear();
			// 2nd. Copy all
			target.get(head).addAll(new LinkedList( Arrays.asList(source.get(head).toArray())));
		}
	}
	
	/**
	 * Add tuples from the source relations to the target relations
	 * @param source Set of source relations
	 * @param target Set of target relations
	 */
	protected void addRelations(Map<ITree, IRelation<ITuple>> source, Map<ITree, IRelation<ITuple>> target) {
		for (ITree head: source.keySet())
			target.get(head).addAll(new LinkedList(Arrays.asList(source.get(head).toArray())));
		
	}
	


}
