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

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Builting to compare two terms
 * </p>
 * <p>
 * $Id: EqualBuiltin.java,v 1.1 2006-09-20 08:22:06 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @date $Date: 2006-09-20 08:22:06 $
 * @version $Revision: 1.1 $
 */
public class EqualBuiltin extends AbstractBuiltin {

	/** The predicate defining this builtin */
	public static final IPredicate PREDICATE = Factory.BASIC.createPredicate(
			"EQUAL", 2);

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
	EqualBuiltin(final ITerm t0, final ITerm t1) {
		super(PREDICATE, t0, t1);
	}

	/**
	 * Runns the evaluation. If the two terms are <code>INumberTerm</code>s
	 * their values will be converted to doubles, otherwise they will be checked
	 * for equality.
	 * 
	 * @return <code>true</code> if the two terms are comparable and they were
	 *         equal, otherwise <code>false</code>
	 */
	public boolean evaluate() {
		if (isEvaluable()) {
			if ((getTerm(0) instanceof INumericTerm)
					&& (getTerm(1) instanceof INumericTerm)) {
				return compareNumbers((Number) getTerm(0).getValue(),
						(Number) getTerm(1).getValue());
			}
			return getTerm(0).equals(getTerm(1));
		}
		return false;
	}

	/**
	 * Compares two numbers by casting them to doubles and then comparing them.
	 * 
	 * @param n0
	 *            the first number
	 * @param n1
	 *            the second number
	 * @return whether the numbers are equal
	 * @throws NullPointerException
	 *             if one of the numbers is null
	 */
	private boolean compareNumbers(final Number n0, final Number n1) {
		if ((n0 == null) || (n1 == null)) {
			throw new NullPointerException("The numbers must not be null");
		}
		return (new Double(n0.doubleValue())).equals(n1.doubleValue());
	}
}
