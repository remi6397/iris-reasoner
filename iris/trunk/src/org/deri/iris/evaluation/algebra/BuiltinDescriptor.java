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

import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.evaluation.algebra.IBuiltinDescriptor;
import org.deri.iris.evaluation.seminaive.Complementor;

/**
 * <p>
 * A Descriptor for builtin atoms.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   12.04.2007 09:58:07
 */
public class BuiltinDescriptor extends Component implements IBuiltinDescriptor{

	IBuiltInAtom builtin = null;
	
	boolean positive = true;
	
	BuiltinDescriptor (boolean isPositive, IBuiltInAtom builtin) {
		super(ComponentType.BUILTIN, builtin.getTuple().getAllVariables());
		
		if (builtin == null) {
			throw new IllegalArgumentException("BuiltinDescriptor input parameters must not be null");
		}
		this.positive = isPositive;
		this.builtin = builtin;
	}

	public IBuiltInAtom getBuiltin(){
		return this.builtin;
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append(ComponentType.BUILTIN + 
				"[");
		if (!this.positive)
			buffer.append(Complementor.NOT_PREFIX);
		buffer.append(this.builtin.toString());
		buffer.append("]");
		return buffer.toString();
	}
}
