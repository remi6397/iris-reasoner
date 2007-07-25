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
 * $Id: GreaterEqualBuiltin.java,v 1.11 2007-07-25 08:16:56 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.11 $
 */
public class GreaterEqualBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"GREATER_EQUAL", 2);

	/**
	 * Constructs a builtin. Two terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param t the terms
	 * @throws NullPointerException if one of the terms is null
	 * @throws IllegalArgumentException if the number of terms submitted is
	 * not 2
	 * @throws NullPointerException if t is <code>null</code>
	 */
	public GreaterEqualBuiltin(final ITerm... t) {
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
		if (vars.length == 0) {
			return BuiltinHelper.compare(complete[0], complete[1]) >= 0 ? 
				BuiltinHelper.EMPTY_TUPLE : null;
		}
		throw new IllegalArgumentException("Can not evaluate a GREATEREQUAL with any variables");
	}

	public boolean isEvaluable(final Collection<IVariable> v) {
		if (v == null) {
			throw new NullPointerException("The variables must not be null");
		}
		final List<IVariable> var = getTuple().getAllVariables();
		var.removeAll(v);
		return var.isEmpty();
	}
}
