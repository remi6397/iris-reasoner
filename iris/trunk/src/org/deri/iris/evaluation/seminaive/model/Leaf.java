package org.deri.iris.evaluation.seminaive.model;

import java.util.*;

import org.deri.iris.api.evaluation.seminaive.model.Component;

public abstract class Leaf implements Component{
	
	public ArrayList<Component> getChildren() { return null; }
	
	public boolean addComponent(Component c) { return false; }
	public boolean removeComponent(Component c) { return false; }
}
