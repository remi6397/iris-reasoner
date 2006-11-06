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
import org.deri.iris.api.evaluation.seminaive.model.IProjection;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.terms.Variable;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 */
public class ProjectionDescription extends Composite implements IProjection{

	private int[] indexes = null;
	private List<String> variables = new LinkedList<String>();

	ProjectionDescription(int[] indexes) {
		if (indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		
		this.indexes = indexes;
	}

	
	public String getName() {
		return "projection";
	}

	public int getArity() {
		return variables.size();
	}

	public int[] getIndexes() {
		return indexes;
	}
	
	public void addVariable(String v){
		variables.add(v);
		checkIndexes();
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
	
	public void addAllVariables(Set<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());		
	}
	
	public boolean hasVariable(String v){
		return variables.contains(v);
	}
	
	public boolean addComponent(Component c)
	{
		ITree t = (ITree)c;
		if (variables.size() == 0)
			addVariables(t.getVariables());
		else {
			List<String> vl = t.getVariables();
			for (String v: vl)
				if (!variables.contains(v))
					addVariable(v);
		}
		boolean result = super.addComponent(c);
		checkIndexes();
		return result;
	}

	
	private void checkIndexes(){
		if ((variables.size() > 0) && (!this.getChildren().isEmpty())) {			
			ITree t = (ITree)this.getChildren().get(0);
			indexes = new int[t.getArity()];
			for (int i = 0; i < indexes.length; i++)
				indexes[i] = -1;
			for (String v: variables)
			{
				int j = t.getVariables().indexOf(v);
				if (j != -1)
					indexes[j] = 0;
			}
		}
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("PROJECTION");
		buffer.append("{");
		for (int i = 0; i < this.variables.size(); i++) {
			buffer.append(this.variables.get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");

		buffer.append(" [");
		for(int i = 0; i < indexes.length; i++)
		{
			buffer.append(indexes[i]);
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("]\n(");
		buffer.append(this.getChildren().get(0).toString());
		buffer.append(")");
		return buffer.toString();
	}
}
