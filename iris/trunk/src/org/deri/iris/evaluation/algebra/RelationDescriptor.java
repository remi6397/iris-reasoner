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

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.evaluation.algebra.IRelationDescriptor;
import org.deri.iris.evaluation.seminaive.Complementor;

/**
* @author Darko Anicic, DERI Innsbruck
* @date 14.12.2006 17:47:34
*/
public class RelationDescriptor extends Component implements IRelationDescriptor{
	
	private IPredicate p = null;
	
	private boolean positive = true;
	
	RelationDescriptor(final boolean isPositive, final IPredicate p){
		
		super(ComponentType.RELATION);
		if (p == null) {
			throw new IllegalArgumentException("Predicate p must not be null!");
		}
		this.positive = isPositive;
		this.p = p;
	}

	public boolean isPositive() {
		return this.positive;
	}
	
	public IPredicate getPredicate() {
		return this.p;
	}
	
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(ComponentType.RELATION + 
				"['");
		if (!this.positive)
			buffer.append(Complementor.NOT_PREFIX);
		buffer.append(this.p);
		buffer.append("', ");
		buffer.append(p.getArity());
		buffer.append("]");
		buffer.append("{");
		for (int i = 0; i < this.getVariables().size(); i++) {
			buffer.append(this.getVariables().get(i).toString());
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}");
		return buffer.toString();
	}
}
