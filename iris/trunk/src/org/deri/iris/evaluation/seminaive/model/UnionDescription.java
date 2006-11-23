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

import org.deri.iris.api.evaluation.seminaive.model.Component;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.evaluation.seminaive.model.IUnion;
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
public class UnionDescription extends Composite implements IUnion{

	private List<String> variables = new LinkedList<String>();
	
	UnionDescription() {
	}

	public String getName() {
		return "union";
	}

	public int getArity() {
		return variables.size();
	}
	public void addVariable(String v){
		variables.add(v);
	}
	
	public void addVariable(IVariable v)
	{
		addVariable(((Variable)v).getName());
	}
	
	public void addAllVariables(List<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());
		
	}
	
	public void addVariables(List<String> lv){
		for (String v: lv)
			addVariable(v);		
	}
	
	public boolean hasVariable(String v){
		return variables.contains(v);
	}
	
	public void addAllVariables(Set<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());				
	}
	
	public List<String> getVariables(){
		return variables;
	}
	
	public boolean addComponent(Component c)
	{
		ITree t = (ITree)c;
		if (variables.size() == 0)
			addVariables(t.getVariables());
		return super.addComponent(t);
	}
	
	public boolean equals(final Object o) {
		if (!(o instanceof UnionDescription)) 
			return false;
		UnionDescription ud = (UnionDescription)o;

		if (this.getVariables().size() != ud.getVariables().size())
			return false;
		for (int i = 0; i < this.getVariables().size(); i++)
			if(!(this.getVariables().get(i)).equals(ud.getVariables().get(i)))
				return false;
	
		return super.equals(ud);
	}
	
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("UNION"); 
		buffer.append("{");
		for (int i = 0; i < this.variables.size(); i++) {
			buffer.append(this.variables.get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		buffer.append(" \n{(");
		for (int i = 0; i < this.getChildren().size(); i++) 
		{
			buffer.append(this.getChildren().get(i).toString());
			buffer.append("),\n(");
		}
		buffer.delete(buffer.length() - 3, buffer.length());
		buffer.append("}");
		
		return buffer.toString();
	}
}
