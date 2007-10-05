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

import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.operations.relation.IBuiltinEvaluator;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IIntersection;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.operations.relations.JoinCondition;

/**
 * <p>
 * An interface that can be used to obtain references to set of 
 * relational algebra operations.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 26.06.2006 14:26:39
 */
public interface IRelationOperationsFactory {

	public IDifference createDifferenceOperator(IMixedDatatypeRelation arg0, 
			IMixedDatatypeRelation arg1);
	
	public IIntersection createIntersectionOperator(IRelation arg0,
			IRelation arg1);
	
	/**
	 * <p>
	 * Creates an equijoin (default) operator which joins two relations where 
	 * the second relation is the complement of its original relation.
	 * Basically the joinComplement operator do the following:
	 * <p>
	 * arg0 - (arg0 semijoin arg1)
	 * </p>
	 * <p>
	 * Remark: The set difference operation is denoted with "-".
	 * </p>
	 * @param arg0
	 *            the first relation to be joined
	 * @param arg1
	 *            the second relation to be joined
	 * @param inds
	 *            join indexes
	 * @return
	 */
	public IJoin createJoinComplementOperator(IMixedDatatypeRelation arg0, IMixedDatatypeRelation arg1, int[] inds);

	/**
	 * <p>
	 *  Creates a general join operator, implemented using the SortMerge 
	 *  algorithm, where one of the following conditions: =, !=, <, >, <=, 
	 *  >= must hold. The join operator performs join operation on two 
	 *  IMixedDatatypeRelation relations.
	 *  </p>
	 * @param arg0		IMixedDatatypeRelation relation to be joined.
	 * @param arg1		IMixedDatatypeRelation relation to be joined.
	 * @param indexes	Join indexes.
	 * @param condition	Join condition.
	 * @return			Joined relation.
	 */
	public IJoin createSortMergeJoinOperator(final IMixedDatatypeRelation arg0, final IMixedDatatypeRelation arg1,
			final int[] indexes, final JoinCondition condition);
	
	public IJoin createSortMergeJoinOperator(final IMixedDatatypeRelation arg0, final IMixedDatatypeRelation arg1, 
			final int[] indexes, final JoinCondition condition, final int semiJoin);
	
	public IProjection createProjectionOperator(IMixedDatatypeRelation relation, 
			int[] pattern);

	/**
	 * Create a selection operator that does selection with the following rule:
	 * For provided pattern p = createTuple("d", "a", null), the operator will
	 * select those tuples from relation r, which have term "d" at the 0th
	 * position and term "a" at the 1st position.
	 * 
	 * @param RelationDescriptor
	 *            to be selected
	 * @param Pattern
	 *            Pattern that defines the selection condition
	 * @return Relataion containing selected elements
	 */
	public ISelection createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern);

	/**
	 * Create a selection operator that does selection with the following rule:
	 * For provided indexes i = int[]{1, 1, -1, 2, 2}, the operator will select
	 * those tuples from relation r, which have equal terms at the 0th and 1st
	 * position and equal terms at the 3rd and 4th position.
	 * 
	 * @param RelationDescriptor
	 *            which selection will be performed on
	 * @param Indexes
	 *            Indexes that define the selection condition
	 * @return Relataion containing selected elements
	 */
	public ISelection createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes);

	/**
	 * Create a selection operator that does selection with the following rule:
	 * For provided pattern eq = createTuple("d", "a", null, null, null, null)
	 * and indexes i = int[]{-1, -1, 1, 1, 2, 2}, the operator will select those
	 * tuples from relation r, which have term "d" at the 0th position and term
	 * "a" at the 1st position, as well as equal terms at the 3th and 4th
	 * position and equal terms at the 4th and 5th position. Providing a pattern
	 * neq = createTuple("d", "a", null, null, null, null), the operator will
	 * select those tuples from relation r, which does not have term "d" at the
	 * 0th position and term "a" at the 1st position,
	 * 
	 * @param r
	 *            RelationDescriptor which selection will be performed on
	 * @param p
	 *            Pattern that defines the selection condition
	 * @param neq
	 *            Pattern that defines the selection unequality condition
	 * @param inds
	 *            Indexes Indexes that define the selection variables condition
	 * @return Relataion containing selected elements
	 */
	public ISelection createSelectionOperator(IMixedDatatypeRelation r, ITuple p, int[] inds);
	
	public IUnion createUnionOperator(final IMixedDatatypeRelation... args);

	public IUnion createUnionOperator(final List<IMixedDatatypeRelation> arg);
	
	public IBuiltinEvaluator createBuiltinEvaluatorOperator(boolean positive, IBuiltInAtom builtin, 
			List<IVariable> relVars, IMixedDatatypeRelation rel);
}
