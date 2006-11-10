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
package org.deri.iris.evaluation.seminaive.model;


import org.deri.iris.api.evaluation.seminaive.model.*;
import org.deri.iris.api.factory.IModelFactory;
import org.deri.iris.api.factory.IRelationOperationsFactory;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.api.basics.ITuple;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 */
public class ModelFactory implements IModelFactory {
	public static final IModelFactory FACTORY = new ModelFactory();
	
	private ModelFactory() {
		// This is a singleton
	}
	
	public static IModelFactory getInstance() {
		return FACTORY;
	}
	
	public ISelection createSelection(ITuple pattern) {
		return new SelectionDescription(pattern);
	}
	
	public ISelection createSelection(ITuple pattern, int[] indexes) {
		return new SelectionDescription(pattern, indexes);
	}
	
	public ISelection createSelection(int[] indexes) {
		return new SelectionDescription(indexes);		
	}
	
	public IProjection createProjection(int[] indexes) {
		return new ProjectionDescription(indexes);
	}
	
	public IJoin createJoin(int[] indexes, JoinCondition condition) {
		return new JoinDescription(indexes, condition);
	}
	public INaturalJoin createNaturalJoin() {
		return new NaturalJoinDescription();
	}
	public IDifference createDifference() {
		return new DifferenceDescription();
	}
	
	public IUnion createUnion() {
		return new UnionDescription();
	}
	
	public IRule createRule(String relationName, int relationArity) {
		return new RuleDescription(relationName, relationArity);
	}
	
	public IRule createUnaryRule(String value) {
		return new UnaryRuleDescription(value);
	}

	public ITree createTree(String relationName) {
		return new Tree(relationName);
	}

	
}
