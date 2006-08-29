package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.ISelection;
import org.deri.iris.api.basics.ITuple;

public class Selection extends Composite implements ISelection{
	private ITuple pattern = null;
	
	Selection(ITuple pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		this.pattern = pattern;
	}
	
	public ITuple getPattern() {
		return pattern;
	}
	
	public String toString()
	{
		return "SELECTION[" + pattern.toString() + "] (" + this.getChildren().get(0).toString() + ")";
	}
}
