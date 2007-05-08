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
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Represents a multiply operation. In at the evaluation time there must be only one
 * variable be left for computation, otherwise an exception will be thrown.
 * </p>
 * <p>
 * $Id: DivideBuiltin.java,v 1.6 2007-05-08 07:31:37 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.6 $
 */
public class DivideBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createBuiltinPredicate(
			"DIVIDE", 3);

	/**
	 * Constructs a builtin.
	 * 
	 * @param t0
	 *            the first term
	 * @param t1
	 *            the second term
	 * @param t2
	 *            the result of the division
	 * @throws NullPointerException
	 *             if one of the terms is {@code null}
	 */
	DivideBuiltin(final ITerm t0, final ITerm t1, final ITerm t2) {
		super(PREDICATE, 3, t0, t1, t2);
	}

	public ITuple evaluate(final ITuple c) {
		if(c == null) {
			throw new NullPointerException("The collection must not be null");
		}
		// calculating the needed term indexes from the submitted tuple
		int[] outstanding = BuiltinHelper.determineUnground(getTuple().getTerms());
		// retrieving the constants of this builin
		final ITerm[] bCons = BuiltinHelper.getIndexes(getTuple().getTerms(), 
				BuiltinHelper.complement(outstanding, getTuple().getArity()));

		// putting the term from this builtin and the submitted tuple together
		final ITerm[] complete = BuiltinHelper.concat(outstanding, 
				BuiltinHelper.getIndexes(c.getTerms(), outstanding), bCons);
		// determing the remaining vars of the terms
		final int[] vars = BuiltinHelper.determineUnground(Arrays.asList(complete));
		// run the evaluation
		if (vars.length == 0) { // checking whether the result is correct
			return BuiltinHelper.equal(complete[2], BuiltinHelper.divide(complete[0], complete[1])) ? 
				BASIC.createTuple(complete) : null;
		} else if(vars.length > 1) {
			throw new IllegalArgumentException("Can not evaluate an DIVIDE with >2 variables");
		}
		switch(vars[0]) {
			case 0:
				return BASIC.createTuple(BuiltinHelper.multiply(complete[1], complete[2]));
			case 1:
				return BASIC.createTuple(BuiltinHelper.divide(complete[0], complete[2]));
			case 2:
				return BASIC.createTuple(BuiltinHelper.divide(complete[0], complete[1]));
			default:
				throw new IllegalArgumentException("The variable must be at possition " + 
						"0 to 2, but was on " + vars[0]);
		}
	}

	public ITuple evaluate(ITuple tup, IVariable... vars) {
		// TODO Auto-generated method stub
		return null;
	}
}
