package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.IProjection;

public class Projection extends Composite implements IProjection{
	private int[] indexes = null;
	
	Projection(int[] indexes) {
		if (indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		
		this.indexes = indexes;
	}

	public int[] getIndexes() {
		return indexes;
	}
	
	public String toString() {
		String result = "PROJECTION[";
		for(int i = 0; i < indexes.length; i++)
		{
			result += new Integer(indexes[i]).toString() + " ";			
		}
		result += "] (" + this.getChildren().get(0).toString() + ")";
		
		return result;
	}
}
