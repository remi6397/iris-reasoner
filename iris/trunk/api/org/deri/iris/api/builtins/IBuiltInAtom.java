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

/**
 * <p>
 * Defines a Builtin.
 * </p>
 * <p>
 * $Id: IBuiltInAtom.java,v 1.3 2007-03-13 16:55:15 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.3 $
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
	 * The returned list contains the tuples for which the builtin holds (e.g. if
	 * it is a binary builing like A &lt; B and the input was &lt;1, 2&gt; it will return
	 * &lt;1, 2&gt;, but it the input was &lt;3, 2&gt; it will return a empty list), or 
	 * tuples where the missing fields were calculated (e.g. we had a add builtin A + 5 = C
	 * and the input was &lt;null, null, 7&gt; it will return the tuple &lt;2, 5, 7&gt;).
	 * </p>
	 * 
	 * @param t the substitutes for the variables of the builtin
	 * @return the list of tuples where this builtin holds or the calculated results
	 * @throws IllegalArgumentException if the builtin couldn't be evaluated
	 * @throws NullPointerException if the collection was <code>null</code>
	 */
	public List<ITuple> evaluate(final Collection<ITuple> t);

	/**
	 * Checks whether the builtin is evaluable.
	 * 
	 * @return <code>true</code> if the evaluation could be run correctly,
	 *         otherwise <code>false</code>
	 * @deprecated now it is checked at evaluation time whether the builtin
	 * 		is evaluable
	 */
	public boolean isEvaluable();
}
