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
import org.deri.iris.api.terms.concrete.IDoubleTerm;

/**
 * @author richi
 *
 */
public class DoubleTerm implements IDoubleTerm, Cloneable {

	private double d = 0d;
	
	DoubleTerm(final double d) {
		setValue(d);
	}
	
	public void setValue(final Double arg) {
		if (arg == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		d = arg;
	}

	public Double getValue() {
		return d;
	}

	public TypeOfNumericTerm getTypeOfNumericTerm() {
		return TypeOfNumericTerm.IDoubleTerm;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(IDoubleTerm o) {
		return Double.compare(d, o.getValue());
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
		if (!(o instanceof DoubleTerm)) {
			return false;
		}
		DoubleTerm dt = (DoubleTerm) o;
		return Double.doubleToLongBits(d) == Double.doubleToLongBits(dt.d);
	}
	
	public int hashCode() {
		return Double.valueOf(d).hashCode();
	}
	
	/**
	 * Simply returns the String representation of the holded double.
	 * 
	 * @return the String representation of the holded double
	 */
	public String toString() {
		return Double.toString(d);
	}

	public IDoubleTerm getMinValue() {
		return new DoubleTerm(Double.MIN_VALUE);
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
	public IDoubleTerm add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			return new DoubleTerm(d + TermHelper.getDouble((INumericTerm) t));
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
	public IDoubleTerm divide(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			final double d = TermHelper.getDouble((INumericTerm) t);
			if (d == 0) {
				throw new IllegalArgumentException(
						"A division by 0 is not allowed, but was " + t);
			}
			return new DoubleTerm(this.d / TermHelper.getDouble((INumericTerm) t));
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
	public IDoubleTerm multiply(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			return new DoubleTerm(d * TermHelper.getDouble((INumericTerm) t));
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
	public IDoubleTerm subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			return new DoubleTerm(d - TermHelper.getDouble((INumericTerm) t));
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}
}
