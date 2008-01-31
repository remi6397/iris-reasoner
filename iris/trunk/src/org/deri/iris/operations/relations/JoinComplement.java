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

package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION_OPERATION;

import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;

/**
 * <p>
 * When a rule contains a negated literal/s (subgoal/s), we 
 * evaluate first positive literals and then negative ones. 
 * For negative literals we compute complement relations. 
 * The complement relation is defined with respect to some 
 * universe U, such that U includes at least the tuples we 
 * got from the evaluation of positive literals. In this 
 * way our universe U will be smaller than, for example, a 
 * universe U' which is the union of the symbols appearing 
 * in the EDB relations and in the rules (from the entire 
 * logic program) themselves. Theoretical approach assumes 
 * using the universe U' for computing a complement. For 
 * instance a complement R*(X,Y) of a relation R(X,Y) is:
 * </p>
 * <p>
 * R*(X,Y) = U' x U' - R(X,Y)
 * </p>
 * <p>
 * However, practically constraining U' to U is valid if 
 * negation is stratified. Smaller universe provides more 
 * effective evaluation of a rule which contains negated 
 * literals. The JoinComplment releases a join of relations: 
 * R1 and not R2, requiring that all variables from R2 are 
 * contained in list of variables from R1. The JoinComplment 
 * first computes the semijoin of R1 and R2 and then does the 
 * set difference operation between R1 and the result of the 
 * previous operation.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   16.03.2007 14:26:43
 */
public class JoinComplement implements IJoin{

	private IMixedDatatypeRelation relation0 = null;

	private IMixedDatatypeRelation relation1 = null;

	private int[] inds = null;

	/**
	 * @param arg0
	 * @param arg1
	 * @param inds
	 */
	JoinComplement(IMixedDatatypeRelation arg0, IMixedDatatypeRelation arg1, int[] inds) {
		if (arg0 == null || arg1 == null || inds == null) {
			throw new IllegalArgumentException("Constructor "
					+ "parameters are not specified correctly!");
		}
		this.relation0 = arg0;
		this.relation1 = arg1;
		this.inds = inds;
	}
	
	public IMixedDatatypeRelation join() {
		IJoin joinOperator = RELATION_OPERATION.createSortMergeJoinOperator(
				this.relation0, this.relation1, this.inds, JoinCondition.EQUALS, 0);
		
		IMixedDatatypeRelation tmpR = joinOperator.join();
		
		IDifference diffOperator = RELATION_OPERATION.createDifferenceOperator(
				this.relation0, tmpR);
		
		return diffOperator.difference();
	}
}
