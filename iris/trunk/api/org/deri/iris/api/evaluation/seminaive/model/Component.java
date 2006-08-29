package org.deri.iris.api.evaluation.seminaive.model;

import java.util.*;

public interface Component {

	public ArrayList<Component> getChildren();
	public boolean addComponent(Component c);
	public boolean removeComponent(Component c);
}


