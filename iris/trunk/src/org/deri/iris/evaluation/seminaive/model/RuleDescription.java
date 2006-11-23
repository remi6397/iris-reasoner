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
package org.deri.iris.evaluation.seminaive.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.evaluation.seminaive.model.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.terms.Variable;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 * @date $Date$
 * @id $Id$ 
 */
public class RuleDescription extends Leaf implements IRule{
	private String name;
	private int arity;
	private List<String> variables = new LinkedList<String>();
	
	RuleDescription(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}
	
	public String getName() {
		return name;
	}
	
	public int getArity() {
		return arity;
	}
	
	public void addVariable(String v){
		variables.add(v);
	}
	public void addVariable(IVariable v)
	{
		addVariable(((Variable)v).getName());
	}
	
	public void addVariables(List<String> lv){
		for (String v: lv)
			addVariable(v);		
	}
	
	public List<String> getVariables(){
		return variables;
	}
	
	public void addAllVariables(List<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());
		
	}
	
	public boolean hasVariable(String v){
		return variables.contains(v);
	}
	
	public void addAllVariables(Set<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());				
	}
	public boolean equals(final Object o) {
		if (!(o instanceof RuleDescription)) 
			return false;
		RuleDescription rd = (RuleDescription)o;

		if (this.getName() != rd.getName())
			return false;
		if (this.getArity() != rd.getArity())
			return false;
		if (this.getVariables().size() != rd.getVariables().size())
			return false;
		for (int i = 0; i < this.getVariables().size(); i++)
			if(!(this.getVariables().get(i)).equals(rd.getVariables().get(i)))
				return false;
		
		return super.equals(rd);
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("RELATION['");
		buffer.append(name);
		buffer.append("', ");
		buffer.append(arity);
		buffer.append("]");
		buffer.append("{");
		for (int i = 0; i < this.variables.size(); i++) {
			buffer.append(this.variables.get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		return buffer.toString();
	}
}
