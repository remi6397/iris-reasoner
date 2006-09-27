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

package org.deri.iris.terms.concrete;

import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;

/**
 * 
 * imple of integer
 * 
 * Created on 26.04.2006 Committed by $Author$ $Source$,
 * 
 * @author Holger Lausen
 * 
 * @version $Revision$ $Date$
 */
public class IntegerTerm implements IIntegerTerm, Cloneable {
	/** integer to represent this datatype */
	private int i;

	IntegerTerm(int z) {
		setValue(z);
	}

	public void setValue(final Integer arg) {
		if (arg == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		i = arg;
	}

	public Integer getValue() {
		return i;
	}

	public TypeOfNumericTerm getTypeOfNumericTerm() {
		return TypeOfNumericTerm.IIntegerTerm;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(IIntegerTerm o) {
		return Integer.valueOf(i).compareTo(o.getValue());
	}

	public int hashCode() {
		return i;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public boolean equals(final Object o) {
		if (!(o instanceof IntegerTerm)) {
			return false;
		}
		IntegerTerm it = (IntegerTerm) o;
		return i == it.i;
	}

	/**
	 * Simply returns the String representation of the holded int.
	 * 
	 * @return the String representation of the holded int
	 */
	public String toString() {
		return Integer.toString(i);
	}

	public IntegerTerm getMinValue() {
		return new IntegerTerm(Integer.MIN_VALUE);
	}

	/**
	 * Creates the sum of the two terms.
	 * 
	 * @param t
	 *            the other summand
	 * @return a new term representing the sum
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the term isn't a <code>INumeericTerm</code>
	 */
	public IntegerTerm add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			return new IntegerTerm(i + TermHelper.getInt((INumericTerm) t));
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}

	/**
	 * Creates the quotient of the two terms.
	 * 
	 * @param t
	 *            the divisor
	 * @return a new term representing the quotient
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the term isn't a <code>INumeericTerm</code>
	 * @throws IllegalArgumentException
	 *             if the divisor is 0
	 */
	public IntegerTerm divide(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			final int i = TermHelper.getInt((INumericTerm) t);
			if (i == 0) {
				throw new IllegalArgumentException(
						"A division by 0 is not allowed, but was " + t);
			}
			return new IntegerTerm(this.i / i);
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}

	/**
	 * Creates the product of the two terms.
	 * 
	 * @param t
	 *            the other factor
	 * @return a new term representing the product
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the term isn't a <code>INumeericTerm</code>
	 */
	public IntegerTerm multiply(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			return new IntegerTerm(i * TermHelper.getInt((INumericTerm) t));
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}

	/**
	 * Creates the difference of the two terms.
	 * 
	 * @param t
	 *            the subtrahend
	 * @return a new term representing the difference
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the term isn't a <code>INumeericTerm</code>
	 */
	public IntegerTerm subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			return new IntegerTerm(i - TermHelper.getInt((INumericTerm) t));
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}
}
