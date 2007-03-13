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

import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Builtin to compare two terms and determine which one is bigger.
 * </p>
 * <p>
 * $Id: GreaterBuiltin.java,v 1.3 2007-03-13 16:57:15 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.3 $
 */
public class GreaterBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = Factory.BASIC.createBuiltinPredicate(
			"GREATER", 2);

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
	GreaterBuiltin(final ITerm t0, final ITerm t1) {
		super(PREDICATE, t0, t1);
	}

	/**
	 * This is an empty method stub to keep the src directory compileable.
	 * @return at the moment it always returns <code>null</code>
	 */
	public List<ITuple> evaluate(final Collection<ITuple> t) {
		// TODO: not implemented yet
		return null;
	}

	/**
	 * Runns the evaluation. If the two terms are <code>INumberTerm</code>s
	 * their values will be converted to doubles, otherwise they will be
	 * compared.
	 * 
	 * @return <code>true</code> if the two terms are comparable and the
	 *         first one is bigger than the second one, otherwise
	 *         <code>false</code>
	 */
	public boolean evaluate() {
		if (isEvaluable()) {
			if ((getTerm(0) instanceof INumericTerm)
					&& (getTerm(1) instanceof INumericTerm)) {
				return BuiltinHelper.numbersCompare((INumericTerm) getTerm(0),
						(INumericTerm) getTerm(1)) > 0;
			}
			return getTerm(0).compareTo(getTerm(1)) > 0;
		}
		return false;
	}
}
