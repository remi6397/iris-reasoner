package org.deri.iris.evaluation.seminaive.model;

import org.deri.iris.api.evaluation.seminaive.model.Component;
import java.util.*;

public abstract class Composite implements Component{

	private ArrayList<Component> components = new ArrayList<Component>();
	
	public ArrayList<Component> getChildren() {
		return components;
	}
	
	public boolean addComponent(Component c) { return components.add(c); } 
	
	public boolean removeComponent(Component c) { return components.remove(c); }
}
