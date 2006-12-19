/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.evaluation.algebra;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.terms.IVariable;

/**
 * 
 * 
 */
public abstract class Component implements IComponent{
	
	private ComponentType type = null;
	
	private List<IVariable> variables = new ArrayList<IVariable>();
	
	
	public Component(final ComponentType t){
		this.type = t;
	}
	
	public Component(final ComponentType t, final IVariable v){
		if (v == null) {
			throw new IllegalArgumentException("Constructor " +
				"parameters must not be specified (not null values");
		}
		this.type = t;
		this.variables = new ArrayList<IVariable>(1);
		this.variables.add(v);
	}
	
	public Component(final ComponentType t, final List<IVariable> vars){
		if (vars == null) {
			throw new IllegalArgumentException("Constructor " +
				"parameters must not be specified (not null values");
		}
		this.type = t;
		this.variables = vars;
	}
	
	private List<IComponent> components = 
		Collections.synchronizedList(new ArrayList<IComponent>());
	
	public ComponentType getType() {
		return this.type;
	}
	
	public List<IComponent> getChildren() {
		return this.components;
	}
	
	public void addVariables(final Collection<IVariable> vars){
		this.variables.addAll(vars);
	}	
	
	public boolean addChild(IComponent c){
		return this.components.add(c); 
	} 
	
	public boolean removeChild(IComponent c){ 
		return this.components.remove(c); 
	}
	
	public List<IVariable> getVariables(){
		return this.variables;
	}
	
	public boolean equals(final Object o) {
		if (!(o instanceof IComponent)) 
			return false;
		IComponent co = (IComponent)o;
		
		if (co == null)
			return false;
		if(this.components.size() != co.getChildren().size())
			return false;

		for (int i = 0; i < components.size(); i++)
			if(!((IComponent)this.components.get(i)).equals((IComponent)co.getChildren().get(i)))
				return false;
		
		return true;
	}
}
