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
import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Builtin to compare two terms for equality.
 * </p>
 * <p>
 * $Id: EqualBuiltin.java,v 1.14 2007-10-09 20:38:17 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @author Darko Anicic, DERI Innsbruck
 * 
 * @version $Revision: 1.14 $
 */
public class EqualBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"EQUAL", 2);

	/**
	 * Constructs a builtin. Two terms must be passed to the constructor,
	 * otherweise an exception will be thrown
	 * 
	 * @param t the terms
	 * @throws NullPointerException if one of the terms is null
	 * @throws IllegalArgumentException if the number of terms submitted is
	 * not 2
	 * @throws NullPointerException if t is <code>null</code>
	 */
	public EqualBuiltin(final ITerm... t) {
		super(PREDICATE, t);
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
		if (vars.length == 0) { // if there are no variables -> check for equality
			return BuiltinHelper.equal(complete[0], complete[1]) ? 
				BuiltinHelper.EMPTY_TUPLE : null;
		} else if(vars.length > 1) { // we can only handle one variable
			throw new IllegalArgumentException("Can not evaluate an EQUAL with 2 variables");
		}
		// return the substitution for the variable
		switch(vars[0]) {
			case 0:
				return BASIC.createTuple(complete[1]);
			case 1:
				return BASIC.createTuple(complete[0]);
			default:
				throw new IllegalArgumentException("The variable must be at possition " + 
						"0 to 1, but was on " + vars[0]);
		}
	}

	public ITuple evaluate(ITuple tup, IVariable... vars) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEvaluable(final Collection<IVariable> v) {
		if (v == null) {
			throw new NullPointerException("The variables must not be null");
		}
		final List<IVariable> var = getTuple().getAllVariables();
		var.removeAll(v);
		return var.size() <= 1;
	}

	public static IPredicate getBuiltinPredicate() {
		return PREDICATE;
	}
}
