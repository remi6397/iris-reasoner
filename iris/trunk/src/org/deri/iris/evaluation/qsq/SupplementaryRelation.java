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
package org.deri.iris.evaluation.qsq;

import java.util.Iterator;
import java.util.List;

import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   28.07.2006 09:01:46
 */
public class SupplementaryRelation extends Relation{
	
	private int superscript;
	
	private int subscript;
	
	/**
	 * List of variables of the supplementary relation
	 */
	private List<IVariable> variables;
	
	
	SupplementaryRelation(
			final int sup, final int sub, final List<IVariable> v) {
		
		super(v.size());
		this.superscript = sup;
		this.subscript = sub;
		this.variables = v;
	}


	/**
	 * @return Returns the subscript.
	 */
	public int getSubscript() {
		return subscript;
	}


	/**
	 * @return Returns the superscript.
	 */
	public int getSuperscript() {
		return superscript;
	}


	/**
	 * @return Returns the variables.
	 */
	public List<IVariable> getVariables() {
		return variables;
	}
	
	public String toString(){
		String relation = "";
		Iterator i = this.variables.iterator();
		relation = "sup(" + this.superscript + "," + this.subscript + ")[";
		while(i.hasNext()){
			relation = relation + ((IVariable)i.next()).toString() + ", ";
		}
		relation = relation.substring(0, relation.length() - 2);
		relation = relation + "]";
		return relation;	}
}
