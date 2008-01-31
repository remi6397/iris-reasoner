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

import org.deri.iris.api.terms.concrete.IFloatTerm;

/**
 * @author richi
 *
 */
public class FloatTerm implements IFloatTerm, Cloneable {

	private float f = 0f;
	
	FloatTerm(final Float f) {
		setValue(f);
	}
	
	public void setValue(final Float arg) {
		if (arg == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		f = arg;
	}

	public Float getValue() {
		return f;
	}

	public TypeOfNumericTerm getTypeOfNumericTerm() {
		return TypeOfNumericTerm.IFloatTerm;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(IFloatTerm o) {
		return Float.compare(f, o.getValue());
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert true : "Will never happen";
		}
		return null;
	}
	
	public int hashCode() {
		return Float.valueOf(f).hashCode();
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof FloatTerm)) {
			return false;
		}
		FloatTerm ft = (FloatTerm) o;
		return Float.floatToIntBits(f) == Float.floatToIntBits(ft.f);
	}
	
	public String toString() {
		return Float.toString(f);
	}

	public IFloatTerm getMinValue() {
		return new FloatTerm(Float.MIN_VALUE);
	}
}
