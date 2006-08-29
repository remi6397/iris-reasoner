package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.IJoin;
import org.deri.iris.operations.relations.JoinCondition;

public class Join extends Composite implements IJoin{
	private int[] indexes = null;
	private JoinCondition condition = null;
	
	Join(int[] indexes, JoinCondition condition) {
		if (indexes == null || condition == null) {
			throw new IllegalArgumentException("All constructor " +
				"parameters must not be specified (non null values");
		}

		this.condition = condition;
		this.indexes = indexes;
	}

	public int[] getIndexes() {
		return indexes;
	}
	
	public JoinCondition getCondition() {
		return condition;
	}
	
	public String toString() {
		String result = " JOIN " + condition + "[";
		for(int i = 0; i < indexes.length; i++)
		{
			result += new Integer(indexes[i]).toString() + " ";			
		}
		result += "] (" + this.getChildren().get(0).toString() + ") , (" +
		this.getChildren().get(1).toString() + ")";
		
		return result;
	}
}
