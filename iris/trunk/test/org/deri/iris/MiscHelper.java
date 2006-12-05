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
package org.deri.iris;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Summary of some helpermethods to overcome the shortcommings of the basics and
 * term factory.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler
 * @version $Revision$
 */
public final class MiscHelper {

	private MiscHelper() {
		// prevent subclassing
	}

	/**
	 * Creates a tuple consisting of IStringTerms of the submitted strings
	 * 
	 * @param s
	 *            the Strings to add to the tuple
	 * @return the tuple
	 */
	public static ITuple createTuple(final String... s) {
		List<ITerm> termList = new LinkedList<ITerm>();
		for (String str : s) {
			termList.add(TERM.createString(str));
		}
		return BASIC.createTuple(termList);
	}

	public static ITuple createTuple(final ITerm... t) {
		List<ITerm> termList = new LinkedList<ITerm>();
		for (ITerm term : t) {
			termList.add(term);
		}
		return BASIC.createTuple(termList);
	}

	/**
	 * Creates a positive literal out of a predicate name and a set of variable
	 * names.
	 * 
	 * @param pred
	 *            the predicate name
	 * @param vars
	 *            the variable names
	 * @return the constructed literal
	 * @throws NullPointerException
	 *             if the predicate name or the set of variable names is
	 *             {@code null}
	 * @throws NullPointerException
	 *             if the set of variable names contains {@code null}
	 * @throws IllegalArgumentException
	 *             if the name of the predicate is 0 characters long
	 */
	public static ILiteral createLiteral(final String pred,
			final String... vars) {
		if ((pred == null) || (vars == null)) {
			throw new NullPointerException(
					"The predicate and the vars must not be null");
		}
		if (pred.length() <= 0) {
			throw new IllegalArgumentException(
					"The predicate name must be longer than 0 chars");
		}
		if (Arrays.asList(vars).contains(null)) {
			throw new NullPointerException("The vars must not contain null");
		}

		return BASIC.createLiteral(true, BASIC.createPredicate(pred,
				vars.length), BASIC.createTuple(new ArrayList<ITerm>(
				createVarList(vars))));
	}

	/**
	 * Creates a list of IVariables out of a list of strings.
	 * 
	 * @param vars
	 *            the variable names
	 * @return the list of correspoinding variables
	 * @throws NullPointerException
	 *             if the vars is null, or contains null
	 */
	public static List<IVariable> createVarList(final String... vars) {
		if ((vars == null) || Arrays.asList(vars).contains(null)) {
			throw new NullPointerException(
					"The vars must not be null and must not contain null");
		}
		final List<IVariable> v = new ArrayList<IVariable>(vars.length);
		for (final String var : vars) {
			v.add(TERM.createVariable(var));
		}
		return v;
	}

	/**
	 * Creates a atom with string constants.
	 * 
	 * @param symbol
	 *            the predicate symbol
	 * @param cons
	 *            the constants for this atom
	 * @return the computed atom
	 * @throws NullPointerException
	 *             if the symbol is {@code null}
	 * @throws IllegalArgumentException
	 *             if the predicate symbol is an empty stirng
	 * @throws NullPointerException
	 *             if the constans are {@code null}
	 */
	public static IAtom createFact(final String symbol, final String... cons) {
		if (symbol == null) {
			throw new NullPointerException("The symbol must not be null");
		}
		if (symbol.length() == 0) {
			throw new IllegalArgumentException(
					"The symbol must not be an empty string");
		}
		if ((cons == null) || (Arrays.asList(cons).contains(null))) {
			throw new NullPointerException(
					"The constanst must not be or contain null");
		}
		return BASIC.createAtom(BASIC.createPredicate(symbol, cons.length),
				createTuple(cons));
	}

}
