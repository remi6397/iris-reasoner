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
package org.deri.iris.api.factory;

import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.IStringTerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   17.03.2006 11:55:35
 */
public interface IBuiltInsFactory {

	// public IBuiltinFunction createBuiltinFunction(IAtom atom);
	
	public IBuiltInAtom numericEqual(INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom numericInequal(INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom numericGreaterThan(INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom numericLessThan(INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom stringEqual(IStringTerm x, IStringTerm y);
	
	public IBuiltInAtom stringInequal(IStringTerm x, IStringTerm y);
	
	public IBuiltInAtom numericAdd(INumericTerm z, INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom numericSubtract(INumericTerm z, INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom numericMultiply(INumericTerm z, INumericTerm x, INumericTerm y);
	
	public IBuiltInAtom numericDivide(INumericTerm z, INumericTerm x, INumericTerm y);
}
