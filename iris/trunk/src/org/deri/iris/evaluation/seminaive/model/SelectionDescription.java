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
 */package org.deri.iris.evaluation.seminaive.model;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.evaluation.seminaive.model.Component;
import org.deri.iris.api.evaluation.seminaive.model.ISelection;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.terms.Variable;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 * @date $Date$
 * @id $Id$ 
 */
public class SelectionDescription extends Composite implements ISelection{

	private ITuple pattern = null;
	private int[] indexes = null;
	private List<String> variables = new LinkedList<String>();

	SelectionDescription(ITuple pattern) {
		if (pattern == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		this.pattern = pattern;
	}
	
	SelectionDescription(int[] indexes) {
		if (indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		this.indexes = indexes;
	}

	SelectionDescription(ITuple pattern, int[] indexes) {
		if (pattern == null && indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		this.pattern = pattern;
		this.indexes = indexes;
	}

	
	
	public String getName() {
		return "selection";
	}

	public int getArity() {
		return variables.size();
	}

	public int[] getIndexes() {
		return indexes;
	}

	public ITuple getPattern() {
		return pattern;
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
		return super.addComponent(t);
	}
	
	public boolean equals(final Object o) {
		if (!(o instanceof SelectionDescription)) 
			return false;
		SelectionDescription sd = (SelectionDescription)o;

		if (this.getArity() != sd.getArity())
			return false;
		if (this.getPattern() != sd.getPattern())
			return false;
		if (this.getIndexes().length != sd.getIndexes().length)
			return false;
		for (int i = 0; i < this.getIndexes().length; i++)
			if(this.getIndexes()[i] != sd.getIndexes()[i])
				return false;
		if (this.getVariables().size() != sd.getVariables().size())
			return false;
		for (int i = 0; i < this.getVariables().size(); i++)
			if(!(this.getVariables().get(i)).equals(sd.getVariables().get(i)))
				return false;
	
		return super.equals(sd);
	}

	public String toString()
	{
		StringBuilder buffer = new StringBuilder();
		buffer.append("SELECTION");
		buffer.append("{");
		for (int i = 0; i < this.variables.size(); i++) {
			buffer.append(this.variables.get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		if (pattern != null) {
			buffer.append("[");
			buffer.append(pattern);
			buffer.append("]");
		} 
		if (indexes != null) {
			buffer.append("[");
			for (int i = 0; i < indexes.length; i++){
				buffer.append(indexes[i]);
				buffer.append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
			buffer.append("]");
		}
		buffer.append("\n(");
		//buffer.append(this.getChildren().get(0).toString());
		buffer.append(")");
		return buffer.toString();
	}
}
