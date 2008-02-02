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

import org.deri.iris.api.terms.concrete.IBooleanTerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 07.03.2006 12:00:27
 */

public class BooleanTerm implements IBooleanTerm, Cloneable {

	private boolean value = false;

	BooleanTerm() {
	}

	BooleanTerm(final boolean value) {
		this();
		setValue(value);
	}

	BooleanTerm(final String value) {
		this(Boolean.valueOf(value));
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert true : "Can not happen";
		}
		return null;
	}

	public boolean equals(final Object obj) {
		if (!(obj instanceof BooleanTerm)) {
			return false;
		}
		BooleanTerm bt = (BooleanTerm) obj;
		return value == bt.value;
	}

	public Boolean getValue() {
		return value;
	}

	public int hashCode() {
		return Boolean.valueOf(value).hashCode();
	}

	public void setValue(boolean arg) {
		this.value = arg;
	}

	public String toString() {
		return this.getClass().getName() + "[value=" + getValue() + "]";
	}

	/**
	 * Returns a new BooleanTerm object corresponding to the submitted String.
	 * This method uses the Boolean.valueOf Method to determine whether true of
	 * false was submitted.
	 * 
	 * @param str
	 *            of the value (will be matched caseinsensitive)
	 * @return the corresponding object
	 * @see Boolean#valueOf(java.lang.String)
	 */
	public static BooleanTerm parse(final String str) {
		if (str == null) {
			throw new NullPointerException("Can not convert null to a boolean");
		}
		return new BooleanTerm(str);
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(IBooleanTerm o) {
		if (o == null) {
			throw new NullPointerException("Can not compare with null");
		}
		return Boolean.valueOf(value).compareTo(o.getValue());
	}

	public IBooleanTerm getMinValue() {
		return new BooleanTerm(false);
	}

	public void setValue(Boolean t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		value = t;
	}
}
