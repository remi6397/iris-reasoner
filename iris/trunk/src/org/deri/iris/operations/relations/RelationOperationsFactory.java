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
import org.deri.iris.api.factory.IRelationOperationsFactory;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IIntersection;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IRelation;

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
	
	public IDifference createDifferenceOperator(IRelation arg0, 
			IRelation arg1) {
		
		return new Difference(arg0, arg1);
	}

	public IIntersection createIntersectionOperator(IRelation arg0, 
			IRelation arg1) {
		
		return null;
	}

	public IJoin createJoinOperator(IRelation arg0, IRelation arg1, 
			int[] indexes) {
		return new Join(arg0, arg1, indexes);
	}
	public IJoin createJoinOperator(IRelation arg0, IRelation arg1,
			int[] indexes, JoinCondition condition) {
		return new Join(arg0, arg1, indexes, condition);
	}
	public IJoin createJoinOperator(IRelation arg0, 
			IRelation arg1, int[] indexes, JoinCondition condition, 
			int[] projectIndexes) {
		return new Join(arg0, arg1, indexes, condition, projectIndexes);
	}
	public IJoin createJoinSimpleOperator(IRelation arg0, IRelation arg1, 
			int[] indexes) {
		return new JoinSimple(arg0, arg1, indexes);
	}
	public IJoin createJoinSimpleOperator(IRelation arg0, IRelation arg1,
			int[] indexes, JoinCondition condition) {
		return new JoinSimple(arg0, arg1, indexes, condition);
	}
	public IJoin createJoinSimpleOperator(IRelation arg0, 
			IRelation arg1, int[] i, JoinCondition c, int[] pi) {
		return new JoinSimple(arg0, arg1, i, c, pi);
	}
	public IJoin createJoinSimpleExtendedOperator(IRelation arg0, IRelation arg1, int[] indexes) {
		return new JoinSimpleExtended(arg0, arg1, indexes);
	}
	public IJoin createJoinSimpleExtendedOperator(IRelation arg0, IRelation arg1, int[] indexes, JoinCondition condition) {
		return new JoinSimpleExtended(arg0, arg1, indexes, condition);
	}
	public IJoin createJoinSimpleExtendedOperator(IRelation arg0, IRelation arg1, int[] indexes, JoinCondition condition, int[] projectIndexes) {
		return new JoinSimpleExtended(arg0, arg1, indexes, condition, projectIndexes);
	}
	public IJoin createJoinNewSimpleOperator(IRelation arg0, IRelation arg1, int[] indexes, JoinCondition condition) {
		return new NewJoin(arg0, arg1, indexes, condition);
	}
	
	public IProjection createProjectionOperator(IRelation relation, 
			int[] pattern) {
		return new Projection(relation, pattern);
	}

	public ISelection createSelectionOperator(IRelation relation, 
			ITuple pattern) {
		return new Selection(relation, pattern);
	}

	public ISelection createSelectionOperator(IRelation relation, int[] indexes) {
		return new Selection(relation, indexes);
	}
	
	public ISelection createSelectionOperator(IRelation relation, ITuple pattern, int[] indexes) {
		return new Selection(relation, pattern, indexes);
	}

	public IUnion createUnionOperator(IRelation... args) {
		return new Union(args);
	}
	
	public IUnion createUnionOperator(final List<IRelation> arg){
		return new Union(arg);
	}
}
