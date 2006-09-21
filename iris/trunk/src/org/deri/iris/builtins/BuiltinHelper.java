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

import org.deri.iris.api.terms.INumericTerm;

/**
 * <p>
 * Some helper methods common to some Builtins.
 * </p>
 * <p>
 * $Id: BuiltinHelper.java,v 1.1 2006-09-21 08:58:55 richardpoettler Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.1 $
 * @date $Date: 2006-09-21 08:58:55 $
 */
class BuiltinHelper {
	private BuiltinHelper() {
		// prevent subclassing
	}

	/**
	 * Compares two numerics by casting them to doubles and then comparing them
	 * for equality. <b>This method assumes that only numbers are stored in
	 * <code>INumericTerm</code>.</b>
	 * 
	 * @param n0
	 *            the first number
	 * @param n1
	 *            the second number
	 * @return whether the numbers are equal
	 * @throws NullPointerException
	 *             if one of the numbers is null
	 * @see Number
	 */
	static boolean numbersEqual(final INumericTerm n0, final INumericTerm n1) {
		if ((n0 == null) || (n1 == null)) {
			throw new NullPointerException("The numbers must not be null");
		}
		return Double.doubleToLongBits(getDouble(n0)) == Double
				.doubleToLongBits(getDouble(n1));
	}

	/**
	 * Compares two numerics by casting them to doubles and then comparing them.
	 * <b>This method assumes that only numbers are stored in
	 * <code>INumericTerm</code>.</b>
	 * 
	 * @param n0
	 *            the first number
	 * @param n1
	 *            the second number
	 * @return <code>0</code> if they are equal, a value <code>&lt; 0</code>
	 *         if n0 is smaller than n1 and a value <code>&gt; 0</code> if n0
	 *         is bigger than n1
	 * @throws NullPointerException
	 *             if one of the numbers is null
	 * @see Number
	 */
	static int numbersCompare(final INumericTerm n0, final INumericTerm n1) {
		if ((n0 == null) || (n1 == null)) {
			throw new NullPointerException("The numbers must not be null");
		}
		return Double.compare(getDouble(n0), getDouble(n1));
	}

	/**
	 * Returns the Double value from a <code>INumericTerm</code> <b>This
	 * method assumes that only numbers are stored in <code>INumericTerm</code>.</b>
	 * 
	 * @param n
	 *            the term
	 * @return the double value
	 * @throws NullPointerException
	 *             if the term is null
	 * @see Number
	 */
	static double getDouble(final INumericTerm n) {
		if (n == null) {
			throw new NullPointerException("The term must not be null");
		}
		// TODO: maybe instance check for Number
		return ((Number) n.getValue()).doubleValue();
	}
}
