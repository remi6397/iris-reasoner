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
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.BASIC;

import java.util.Arrays;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Builtin to compare two terms and determine which one is bigger or if they are
 * equal.
 * </p>
 * <p>
 * $Id: LessEqualBuiltin.java,v 1.5 2007-05-07 13:23:08 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.5 $
 */
public class LessEqualBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = Factory.BASIC.createBuiltinPredicate(
			"LESS_EQUAL", 2);

	/**
	 * Constructs a builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @throws NullPointerException
	 *             if one of the terms is null
	 */
	LessEqualBuiltin(final ITerm t0, final ITerm t1) {
		super(PREDICATE, t0, t1);
	}

	/**
	 * This is an empty method stub to keep the src directory compileable.
	 * @return at the moment it always returns <code>null</code>
	 */
	public ITuple evaluate(final ITuple t) {
		if(t == null) {
			throw new NullPointerException("The collection must not be null");
		}
		// calculating the needed term indexes from the submitted tuple
		int[] outstanding = BuiltinHelper.determineUnground(getTuple().getTerms());
		// retrieving the constants of this builin
		final ITerm[] bCons = BuiltinHelper.getIndexes(getTuple().getTerms(), 
				BuiltinHelper.complement(outstanding, getTuple().getArity()));

		// putting the term from this builtin and the submitted tuple together
		final ITerm[] complete = BuiltinHelper.concat(outstanding, 
				BuiltinHelper.getIndexes(t.getTerms(), outstanding), bCons);
		// determing the remaining vars of the terms
		final int[] vars = BuiltinHelper.determineUnground(Arrays.asList(complete));

		// run the evaluation
		if (vars.length == 0) {
			if ((complete[0] instanceof INumericTerm) && (complete[1] instanceof INumericTerm)) {
				return BuiltinHelper.numbersCompare((INumericTerm) complete[0], (INumericTerm) complete[1]) <= 0 ? 
					BASIC.createTuple(complete) : null;
			} else if (complete[0].getClass().isAssignableFrom(complete[1].getClass())) {
				return complete[0].compareTo(complete[1]) <= 0 ? BASIC.createTuple(complete) : null;
			}
			throw new IllegalArgumentException("Couldn't compare " + complete[0].getClass().getName() + 
				" and " + complete[1].getClass().getName());
		}
		throw new IllegalArgumentException("Can not evaluate a LESSEQUAL with any variables");
	}

	public ITuple evaluate(ITuple tup, IVariable... vars) {
		// TODO Auto-generated method stub
		return null;
	}
}
