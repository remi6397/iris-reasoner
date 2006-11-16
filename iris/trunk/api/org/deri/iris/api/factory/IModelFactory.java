package org.deri.iris.api.factory;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.seminaive.model.IDifference;
import org.deri.iris.api.evaluation.seminaive.model.IJoin;
import org.deri.iris.api.evaluation.seminaive.model.INaturalJoin;
import org.deri.iris.api.evaluation.seminaive.model.IProjection;
import org.deri.iris.api.evaluation.seminaive.model.IRule;
import org.deri.iris.api.evaluation.seminaive.model.ISelection;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.evaluation.seminaive.model.IUnion;
import org.deri.iris.operations.relations.JoinCondition;

public interface IModelFactory {

	public IProjection createProjection(int[] indexes);
	public ISelection createSelection(ITuple pattern);
	public ISelection createSelection(ITuple pattern, int[] indexes);
	public ISelection createSelection(int[] indexes);
	public IJoin createJoin(int[] indexes, JoinCondition condition);
	public IJoin createJoin(int[] i, JoinCondition c, int[] pi);
	public IJoin createJoin(IJoin j, int[] pi);
	public IDifference createDifference();
	public IUnion createUnion();
	public IRule createRule(String relationName, int relationArity);
	public IRule createUnaryRule(String value);
	public ITree createTree(String relationName);	
}
