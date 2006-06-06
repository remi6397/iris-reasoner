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

import org.deri.iris.api.terms.concrete.IDecimalTerm;

/**
 * @author richi
 * 
 */
public class DecimalTerm implements IDecimalTerm, Cloneable {

	private double d = 0d;

	public DecimalTerm(final double d) {
		setValue(d);
	}

	public void setValue(double arg) {
		d = arg;
	}

	public double getValue() {
		return d;
	}

	public TypeOfNumericTerm getTypeOfNumericTerm() {
		return TypeOfNumericTerm.IDoubleTerm;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(IDecimalTerm o) {
		return Double.compare(d, o.getValue());
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert true : "Will never happen";
		}
		return null;
	}

	public boolean equals(final Object o) {
		if (!(o instanceof DecimalTerm)) {
			return false;
		}
		DecimalTerm dt = (DecimalTerm) o;
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

	public IDecimalTerm getMinValue() {
		return new DecimalTerm(Double.MIN_VALUE);
	}
}
