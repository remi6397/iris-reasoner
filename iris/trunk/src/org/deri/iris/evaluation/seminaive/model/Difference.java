package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.IDifference;

public class Difference extends Composite implements IDifference{

	Difference() {}
	
	public String toString() {
		return "DIFFERENCE\n\t(" + this.getChildren().get(0).toString() + 
		") , (" + 
		this.getChildren().get(1).toString() + ")";
	}
}
