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
 * <p>
 * Simple implementation of the IBooleanTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class BooleanTerm implements IBooleanTerm, Cloneable {

	/** The boolean value represented by this object */
	private boolean value;

	/**
	 * Constructs a boolean with the value <code>false</code>.
	 */
	BooleanTerm() {
		this(false);
	}

	/**
	 * Constructs a boolean with the given value.
	 * 
	 * @param value
	 *            the boolean to which to set the value to
	 */
	BooleanTerm(final boolean value) {
		this.value = value;
	}

	/**
	 * Constructs a boolean with the given value. If the string isn't a valid
	 * boolean representation the default will be false (according to
	 * Boolean.valueOf(String)).
	 * 
	 * @param value
	 *            string representation of the boolean
	 * @throws NullPointerException
	 *             if the string is null
	 * @see Boolean.valueOf(String)
	 */
	BooleanTerm(final String value) {
		if (value == null) {
			throw new NullPointerException();
		}
		this.value = Boolean.valueOf(value);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
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
		return Boolean.toString(value);
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
			return 1;
		}
		return Boolean.valueOf(value).compareTo(o.getValue());
	}

	public void setValue(Boolean t) {
		if (t == null) {
			throw new IllegalArgumentException("The value must not be null");
		}
		value = t;
	}
}
