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
package org.deri.iris.evaluation_old.algebra;

import org.deri.iris.api.evaluation_old.algebra.IConstantDescriptor;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;


/**
* @author Darko Anicic, DERI Innsbruck
* @date 14.12.2006 16:19:34
*/
public class ConstantDescriptor extends Component implements IConstantDescriptor{

	private ITerm constant = null;
	
	ConstantDescriptor(ITerm c, IVariable v){
		super(ComponentType.CONSTANT, v);
		if(! c.isGround()){
			throw new IllegalArgumentException("The term must not be grounded!");
		}
		if(v == null){
			throw new IllegalArgumentException("The variable must not be null!");
		}
		this.constant = c;
	}
	
	public ITerm getConstant() {
		return this.constant;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(ComponentType.CONSTANT);
		buffer.append("{");
		buffer.append(this.constant.toString());
		buffer.append("}");
		buffer.append("(");
		buffer.append(this.getVariables().get(0).toString());
		buffer.append(")");
		return buffer.toString();
	}
}
