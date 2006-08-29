package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.IRule;

public class Rule extends Leaf implements IRule{
	private String name;
	private int arity;
	
	Rule(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}
	
	public String getName() {
		return name;
	}
	
	public int getArity() {
		return arity;
	}
	
	
	public String toString() {
		return "Relation['" + name + "', " + arity  + "]";
	}
}
