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

import org.deri.iris.api.evaluation.seminaive.model.IRule;

/**
 * 
 * @author Paco Garcia, University of Murcia
 * @date 01-sep-2006
 *
 */
public class Rule extends Leaf implements IRule{
	private String name;
	private int arity;
	
	Rule(String name, int arity) {
		this.name = name;
		this.arity = arity;
	}
	
	public String getName() {
		return name;
	}
	
	public int getArity() {
		return arity;
	}
	
	public boolean equals(final Object o) {
		return (this.name.equalsIgnoreCase(((IRule)o).getName()) &&
				this.arity == ((IRule)o).getArity());
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("RELATION['");
		buffer.append(name);
		buffer.append("', ");
		buffer.append(arity);
		buffer.append("]");
		return buffer.toString();
	}
}
