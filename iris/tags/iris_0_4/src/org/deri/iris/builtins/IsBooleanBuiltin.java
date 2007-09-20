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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.api.terms.concrete.IBooleanTerm;

/**
 * <p>
 * Checks whether a term is a boolean.
 * </p>
 * <p>
 * $Id: IsBooleanBuiltin.java,v 1.1 2007-07-13 09:12:43 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 * @since 0.4
 */
public class IsBooleanBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = 
		org.deri.iris.factory.Factory.BASIC.createPredicate("ISBOOLEAN", 1);

	public IsBooleanBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	public ITuple evaluate(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		// merging the constants of the builtin and the submitted term
		final ITerm[] complete = BuiltinHelper.merge(getTuple(), t);
		// if we don't have any vars -> run eval
		if (BuiltinHelper.determineUnground(Arrays.asList(complete)).length == 0) {
			return (complete[0] instanceof IBooleanTerm) ? BuiltinHelper.EMPTY_TUPLE : null;
		}
		throw new IllegalArgumentException("Can not evaluate a " + PREDICATE + 
				" with any variables");
	}

	/**
	 * Returns the predicate for this builtin.
	 * @return the predicate
	 */
	public static IPredicate getBuiltinPredicate() {
		return PREDICATE;
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
