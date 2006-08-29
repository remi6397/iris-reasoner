package org.deri.iris.api.evaluation.seminaive.model;
 
import org.deri.iris.operations.relations.JoinCondition;

public interface IJoin extends ITree{

	public int[] getIndexes();
	public JoinCondition getCondition();
}
