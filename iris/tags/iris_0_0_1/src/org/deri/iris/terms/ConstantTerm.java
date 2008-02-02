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
package org.deri.iris.terms;

import org.deri.iris.api.terms.IConstantTerm;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 */
public class ConstantTerm implements IConstantTerm<ITerm>, Cloneable {

	private ITerm value = null;

	ConstantTerm(final ITerm sconst) {
		value = sconst;
	}

	public ITerm getValue() {
		return value;
	}

	public boolean isGround() {
		return true;
	}

	/**
	 * This compareTo method uses the compareTo method of the constants stored
	 * by this object.
	 * 
	 * @param o
	 *            the other constant / term to compare with
	 * @return -1 if <code>this</code> is smaller, 0 if they are equal and 1
	 *         if <code>this</code> other is bigger
	 */
	public int compareTo(ITerm o) {
		return value.compareTo((String) o.getValue());
	}

	public boolean equals(final Object o) {
		if (!(o instanceof ConstantTerm)) {
			return false;
		}
		ConstantTerm ct = (ConstantTerm) o;
		return value.equals(ct.value);
	}

	public Object clone() {
		try {
			ConstantTerm c = (ConstantTerm) super.clone();
			c.value = (ITerm) value.clone();
			return c;
		} catch (CloneNotSupportedException e) {
			assert true : "Will never happen";
		}
		return null;
	}

	public int hashCode() {
		return value.hashCode();
	}

	/**
	 * Returns a String representation of this object. The subject of the string
	 * format is to change. An example return value might be
	 * &quot;org.deri.iris.terms.ConstantTerm[value=const1]&quot;
	 * 
	 * @return the String representation
	 */
	public String toString() {
		return value.toString();
	}

	public IConstantTerm getMinValue() {
		// or return value.getMinValue() ?
		return null;
	}

	public void setValue(ITerm t) {
		this.value = t;
	}
}
