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
import org.deri.iris.api.evaluation.seminaive.model.IJoin;
import org.deri.iris.api.evaluation.seminaive.model.ITree;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.terms.Variable;

/**
 * 
 * @author Paco Garcia, University of Murcia 
 * @author Darko Anicic, DERI Innsbruck
 * 
 * @date 01-sep-2006
 *
 * @date $Date$
 * @id $Id$ 
 */
public class JoinDescription extends Composite implements IJoin{

	private JoinCondition condition = null;
	
	private int[] indexes = null;
	
	private int[] projectIndexes = null;
	
	private List<String> variables = new LinkedList<String>();
	
	JoinDescription(int[] i, JoinCondition c) {
		if (i == null || c == null) {
			throw new IllegalArgumentException("All constructor " +
				"parameters must not be specified (non null values");
		}
		this.condition = c;
		this.indexes = i;
	}

	JoinDescription(int[] i, JoinCondition c, int[] pi) {
		if (i == null || c == null || pi == null) {
			throw new IllegalArgumentException("All constructor " +
				"parameters must not be specified (non null values");
		}
		this.condition = c;
		this.indexes = i;
		this.projectIndexes = pi;
	}
	
	public String getName() {
		return "join";
	}

	public int getArity() {
		return this.variables.size();
	}

	public int[] getIndexes() {
		return this.indexes;
	}
	
	public int[] getProjectIndexes() {
		return this.projectIndexes;
	}
	
	protected void setProjectIndexes(int[] pi) {
		this.projectIndexes = pi;
	}
	
	public JoinCondition getCondition() {
		return this.condition;
	}
	
	public void addVariable(String v){
		variables.add(v);
	}
	public void addVariable(IVariable v){
		addVariable(((Variable)v).getName());
	}
	
	public void addVariables(List<String> lv){
		this.variables.addAll(lv);	
	}
	
	public List<String> getVariables(){
		return this.variables;
	}
	
	public void addAllVariables(List<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());
	}
	
	public boolean addComponent(Component c){
		ITree t = (ITree)c;
		addVariables(t.getVariables());
		return super.addComponent(t);
	}
	
	public void addAllVariables(Set<IVariable> lv){
		for (IVariable v: lv)
			addVariable(((Variable)v).getName());				
	}
	
	public boolean hasVariable(String v){
		return this.variables.contains(v);
	}

	public boolean equals(final Object o) {
		if (!(o instanceof JoinDescription)) 
			return false;
		JoinDescription sd = (JoinDescription)o;

		if (this.getArity() != sd.getArity())
			return false;
		if (this.getIndexes() != sd.getIndexes())
			return false;
		if (this.getCondition() != sd.getCondition())
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
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("JOIN");
		buffer.append("{");
		for (int i = 0; i < this.variables.size(); i++) {
			buffer.append(this.variables.get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		buffer.append(this.condition);
		buffer.append("[");
		for(int i = 0; i < this.indexes.length; i++){
			buffer.append(indexes[i]);
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		if(this.projectIndexes != null){
			buffer.append("][");
			for(int i = 0; i < this.projectIndexes.length; i++){
				buffer.append(this.projectIndexes[i]);
				buffer.append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
		}
		buffer.append("]\n{(");
		buffer.append(this.getChildren().get(0).toString());
		buffer.append("),(");
		buffer.append(this.getChildren().get(1).toString());
		buffer.append(")}");
		return buffer.toString();
	}
}
