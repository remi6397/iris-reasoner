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

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.factory.IRelationFactory;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.operations.relation.IIntersection;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.06.2006 14:47:03
 */
public class RelationFactory implements IRelationFactory{
	private static final IRelationFactory FACTORY = new RelationFactory();
	
	private RelationFactory() {
		// this is a singelton
	}
	
	public IDifference createDifferenceOperator(IRelation arg0, 
			IRelation arg1) {
		
		return null;
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
	public IJoin createJoinSimpleOperator(IRelation arg0, IRelation arg1, 
			int[] indexes) {
		return new Join(arg0, arg1, indexes);
	}
	public IJoin createJoinSimpleOperator(IRelation arg0, IRelation arg1,
			int[] indexes, JoinCondition condition) {
		return new Join(arg0, arg1, indexes, condition);
	}
	
	public IProjection createProjectionOperator(IRelation relation, 
			int[] indexes) {
		
		return null;
	}

	public ISelection createSelectionOperator(IRelation relation, 
			ITuple pattern) {
		return new Selection(relation, pattern);
	}

	public static IRelationFactory getInstance() {
		return FACTORY;
	}
}
