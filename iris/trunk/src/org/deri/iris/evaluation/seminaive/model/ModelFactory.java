package org.deri.iris.evaluation.seminaive.model;


import org.deri.iris.api.evaluation.seminaive.model.*;
import org.deri.iris.api.factory.IModelFactory;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.api.basics.ITuple;

public class ModelFactory implements IModelFactory {
	public static final IModelFactory FACTORY = new ModelFactory();
	
	private ModelFactory() {
		// This is a singleton
	}
	
	public ISelection createSelection(ITuple pattern) {
		return new Selection(pattern);
	}
	
	public IProjection createProjection(int[] indexes) {
		return new Projection(indexes);
	}
	
	public IJoin createJoin(int[] indexes, JoinCondition condition) {
		return new Join(indexes, condition);
	}

	public IDifference createDifference() {
		return new Difference();
	}
	
	public IUnion createUnion() {
		return new Union();
	}
	
	public IRule createRule(String relationName, int relationArity) {
		return new Rule(relationName, relationArity);
	}

	
}
