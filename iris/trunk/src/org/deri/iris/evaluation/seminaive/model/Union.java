package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.IUnion;

public class Union extends Composite implements IUnion{
	Union() {}
	
	public String toString() {
		return "UNION (" + this.getChildren().get(0).toString() + ") , (" + this.getChildren().get(1).toString() + ")";
	}
}
