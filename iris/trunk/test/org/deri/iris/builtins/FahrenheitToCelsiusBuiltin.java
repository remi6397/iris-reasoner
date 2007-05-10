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

import static org.deri.iris.builtins.BuiltinHelper.add;
import static org.deri.iris.builtins.BuiltinHelper.divide;
import static org.deri.iris.builtins.BuiltinHelper.equal;
import static org.deri.iris.builtins.BuiltinHelper.multiply;
import static org.deri.iris.builtins.BuiltinHelper.subtract;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;

import java.util.Arrays;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * A simple fahrenheit to celsius builtin. The first index is the fahrenheit
 * value, the second is the celsius value.
 * </p>
 * <p>
 * $Id: FahrenheitToCelsiusBuiltin.java,v 1.4 2007-05-10 15:58:02 poettler_ric Exp $
 * </p>
 * @version $Revision: 1.4 $
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 */
public class FahrenheitToCelsiusBuiltin extends AbstractBuiltin {

	/** Predicate holding the information about this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate("ftoc", 2);

	/** Term representing an int(5). */
	private static final ITerm t5 = CONCRETE.createInteger(5);

	/** Term representing an int(9). */
	private static final ITerm t9 = CONCRETE.createInteger(9);

	/** Term representing an int(32). */
	private static final ITerm t32 = CONCRETE.createInteger(32);

	/**
	 * Constructs this builtin.
	 * @param t the terms for this builtin. The first index is the
	 * fahrenheit value, the second is the celsius value.
	 */
	public FahrenheitToCelsiusBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	public ITuple evaluate(final ITuple c) {
		if (c == null) {
			throw new NullPointerException("The collection of terms must not be null");
		}
		// determines all positions of this builtin which should be computed
		int[] outstanding = BuiltinHelper.determineUnground(getTuple().getTerms());
		// getting the constants of this builtin
		final ITerm[] bCons = BuiltinHelper.getIndexes(getTuple().getTerms(), 
				BuiltinHelper.complement(outstanding, getTuple().getArity()));

		// concat the terms
		final ITerm[] complete = BuiltinHelper.concat(outstanding, 
				BuiltinHelper.getIndexes(c.getTerms(), outstanding), bCons);
		// determine the messing term positions
		final int[] missing = BuiltinHelper.determineUnground(Arrays.asList(complete));
		// run the evaluation
		if (missing.length == 0) { // check the validity of the constants
			return equal(complete[1], divide(multiply(subtract(complete[0], t32), t5), t9)) ?
				BuiltinHelper.EMPTY_TUPLE : null;
		} else if (missing.length > 1) { // we are only able to calculate one missing position
			throw new IllegalArgumentException("Only one variable is allowed, but was: " + 
					missing.length + " " + Arrays.toString(complete));
		}
		switch (missing[0]) {
			case 0: // fahrenheit are requested
				return BASIC.createTuple(add(divide(multiply(complete[1], t9), t5), t32));
			case 1: // celsius are requested
				return BASIC.createTuple(divide(multiply(subtract(complete[0], t32), t5), t9));
			default:
				throw new IllegalArgumentException("This builtin only has 2 positions, but " + 
						missing[0] + " was requested");
		}
	}

	public static IPredicate getBuiltinPredicate() {
		return PREDICATE;
	}
}
