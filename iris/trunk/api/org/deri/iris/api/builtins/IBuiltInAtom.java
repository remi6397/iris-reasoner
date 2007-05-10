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
package org.deri.iris.api.builtins;

import java.util.List;
import java.util.Collection;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Defines a Builtin.
 * </p>
 * <p>
 * $Id: IBuiltInAtom.java,v 1.7 2007-05-10 15:58:00 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.7 $
 */
public abstract interface IBuiltInAtom extends IAtom {

	/**
	 * <p>
	 * Runns the evaluation.
	 * </p>
	 * <p>
	 * This method takes a input a collection of tuples for which it should run the
	 * evaluation. Each tuple must contain the substitutes for the variables of 
	 * this builtin at the corresponding possition. Substitutes where this builtin 
	 * already has a constant might be ignored.
	 * </p>
	 * <p>
	 * The returned tuple contains the calculated substitutions for the
	 * remaining variables (after replacing the variables with the passed in
	 * constants) in the builtin in the order their variables
	 * appear in the builtin. If there are no variables left to calculate
	 * the builtin will check the correctness of the terms and return a
	 * tuple containing all constants if it is correct, otherwise
	 * <code>null</code>.
	 * For example if you evaluate 
	 * <code>4 + X = 9</code> you would get back <code>&lt;5&gt;</code>. 
	 * The only difference are the binary builtins: For a binary builtin 
	 * if you try to evaluate <code>3 &tl; 4</code> you will get back 
	 * <code>&lt;3, 4&gt;</code>, but if you try to evaluate <code>3 &lt; 2</code> 
	 * you will get back <code>null</code>.
	 * </p>
	 * 
	 * @param t the substitutes for the variables of the builtin
	 * @return the calculated constans or <code>null</code> if the builtin
	 * isn't evaluable
	 * @throws IllegalArgumentException if the builtin couldn't be evaluated
	 * @throws NullPointerException if the collection was <code>null</code>
	 */
	public ITuple evaluate(final ITuple t);
}
