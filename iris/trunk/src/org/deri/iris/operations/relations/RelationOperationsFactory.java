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

import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.factory.IRelationOperationsFactory;
import org.deri.iris.api.operations.relation.IBuiltinEvaluator;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IIntersection;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.api.storage_old.IRelation;
import org.deri.iris.api.terms.IVariable;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.06.2006 14:47:03
 */
public class RelationOperationsFactory implements IRelationOperationsFactory{
	private static final IRelationOperationsFactory FACTORY = new RelationOperationsFactory();

	private RelationOperationsFactory() {
		// this is a singelton
	}
	
	public static IRelationOperationsFactory getInstance() {
		return FACTORY;
	}
	
	public IDifference createDifferenceOperator(IMixedDatatypeRelation arg0, 
			IMixedDatatypeRelation arg1) {
		return new GeneralDifference(arg0, arg1);
	}
	
	public IIntersection createIntersectionOperator(IRelation arg0, 
			IRelation arg1) {
		
		return null;
	}

	public IJoin createJoinComplementOperator(IMixedDatatypeRelation arg0, IMixedDatatypeRelation arg1, 
			int[] inds) {
		return new JoinComplement(arg0, arg1, inds);
	}
	
	public IJoin createSortMergeJoinOperator(final IMixedDatatypeRelation arg0, final IMixedDatatypeRelation arg1, 
			final int[] indexes, final JoinCondition condition) {
		// FIXME: either the check whether something wron was cut off,
		// should be removed, or the index arrays should be constructed
		// from the start on...
		final int[] copy = new int[arg0.getArity()];
		System.arraycopy(indexes, 0, copy, 0, copy.length);
		// checking whether something wrong was cut off
		for (int i = arg0.getArity(), max = arg1.getArity(); i < max; i++) {
			if (indexes[i] >= 0) {
				throw new IllegalArgumentException("cut something wrong off: " + 
						java.util.Arrays.toString(indexes) + " from: " + arg0.getArity());
			}
		}
		
		return new SortMergeJoin(arg0, arg1, copy, condition);
	}
	public IJoin createSortMergeJoinOperator(IMixedDatatypeRelation r0, IMixedDatatypeRelation r1, 
			int[] idx, JoinCondition c, int semiJoin) {
		// FIXME: either the check whether something wron was cut off,
		// should be removed, or the index arrays should be constructed
		// from the start on...
		final int[] copy = new int[r0.getArity()];
		System.arraycopy(idx, 0, copy, 0, copy.length);
		// checking whether something wrong was cut off
		for (int i = r0.getArity(), max = r0.getArity(); i < max; i++) {
			if (idx[i] >= 0) {
				throw new IllegalArgumentException("cut something wrong off: " + 
						java.util.Arrays.toString(idx) + " from: " + r0.getArity());
			}
		}
		
		return new SortMergeJoin(r0, r1, copy, c, semiJoin);
	}
	
	public IProjection createProjectionOperator(IMixedDatatypeRelation relation, 
			int[] pattern) {
		return new GeneralProjection(relation, pattern);
	}

	public ISelection createSelectionOperator(IMixedDatatypeRelation relation, 
			ITuple pattern) {
		return new GeneralSelection(relation, pattern);
	}

	public ISelection createSelectionOperator(IMixedDatatypeRelation relation, int[] indexes) {
		return new GeneralSelection(relation, indexes);
	}
	
	public ISelection createSelectionOperator(IMixedDatatypeRelation relation, ITuple pattern, int[] indexes) {
		if (pattern == null && indexes != null) {
			return new GeneralSelection(relation, indexes);
		}
		if (pattern != null && indexes == null) {
			return new GeneralSelection(relation, pattern);
		}
		return new GeneralSelection(relation, pattern, indexes);
	}

	public IUnion createUnionOperator(IMixedDatatypeRelation... args) {
		return new GeneralUnion(args);
	}
	
	public IUnion createUnionOperator(final List<IMixedDatatypeRelation> arg){
		return new GeneralUnion(arg);
	}
	
	public IBuiltinEvaluator createBuiltinEvaluatorOperator(boolean positive, IBuiltInAtom builtin, List<IVariable> relVars, IMixedDatatypeRelation rel) {
		return new GeneralBuiltinEvaluator(positive, builtin, relVars, rel);
	}
}
