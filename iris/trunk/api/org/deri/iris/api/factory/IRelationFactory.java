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
package org.deri.iris.api.factory;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IIntersection;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.operations.relations.JoinCondition;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.06.2006 14:26:39
 */
public interface IRelationFactory {
	
	public IDifference createDifferenceOperator(IRelation arg0, IRelation arg1);
	
	public IIntersection createIntersectionOperator(IRelation arg0, IRelation arg1);
	
	/**
	 * Creates an equijoin (default) operator.
	 * 
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @return
	 */
	public IJoin createJoinOperator(IRelation arg0, IRelation arg1, 
			int[] indexes);
	/**
	 * Creates a general join operator where one of the following 
	 * conditions: =, !=, <, >, <=, >= must hold.
	 * 
	 * @param relation0
	 * @param arg1
	 * @param indexes
	 * @param condition
	 * @return
	 */
	public IJoin createJoinOperator(IRelation arg0, 
			IRelation arg1, int[] indexes, JoinCondition condition);
	/**
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @param condition
	 * @param projectIndexes
	 * 						define indexes which the projection operation
	 * 						will be applied on. For example, if set of 
	 * 						tuples of arity 3 needs to be projected on 
	 * 						the first and last term, the projectIndexes 
	 * 						will look like: [1, -1, 1]. -1 means that terms
	 * 						with that index will be omitted. Note that an 
	 * 						equivalent array for the project indexes could 
	 * 						be also: [0, -1, 2], in which case the array 
	 * 						values represent the term indexes in a tuple.  
	 * 						If not specified join tuples will be simple 
	 * 						merged.
	 * @return
	 */
	public IJoin createJoinOperator(IRelation arg0, 
			IRelation arg1, int[] indexes, JoinCondition condition,
			int[] projectIndexes);
	/**
	 * No duplicates handled,
	 * 
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @return
	 */
	public IJoin createJoinSimpleOperator(IRelation arg0, IRelation arg1, 
			int[] indexes);
	/**
	 * No duplicates handled.
	 * 
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @param condition
	 * @return
	 */
	public IJoin createJoinSimpleOperator(IRelation arg0, 
			IRelation arg1, int[] indexes, JoinCondition condition);
	/**
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @param condition
	 * @param projectIndexes
	 * 						define indexes which the projection operation
	 * 						will be applied on.  If not specified join 
	 * 						tuples will be simple merged.
	 * @return
	 */
	public IJoin createJoinSimpleOperator(IRelation arg0, 
			IRelation arg1, int[] indexes, JoinCondition condition,
			int[] projectIndexes);
	
	public IProjection createProjectionOperator(IRelation relation, int[] indexes);
	
	public ISelection createSelectionOperator(IRelation relation, ITuple pattern);
	
}
