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

import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 */
public class StringTerm implements IStringTerm<StringTerm>, Cloneable {

	private String value = "";

	StringTerm(final String value) {
		setValue(value);
	}

	public void setValue(String arg) {
		this.value = arg;
	}

	public String getValue() {
		return value;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(StringTerm o) {
		return value.compareTo(o.value);
	}

	public int hashCode() {
		return value.hashCode();
	}

	public boolean equals(final Object o) {
		if (!(o instanceof StringTerm)) {
			return false;
		}
		StringTerm st = (StringTerm) o;
		return value.equals(st.value);
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	/**
	 * Simple toString() method wich only returns the holded value
	 * 
	 * @return the containing String
	 */
	public String toString() {
		return value;
	}

	public StringTerm getMinValue() {
		return new StringTerm("");
	}

	/**
	 * Concats the given term to the end of this term.
	 * 
	 * @param t
	 *            the term to add to the end
	 * @return the concated term
	 * @throws NullPointerException
	 *             if the term is null
	 */
	public StringTerm add(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		return new StringTerm(value + t.getValue());
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public StringTerm divide(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * Copies the actual string n times. E.g. if the actual string is &quot;<code>ab</code>&quot;
	 * and the submitted term is 3, it will result in &quot;<code>ababab</code>&quot;.
	 * 
	 * @param how
	 *            many times the string should be copied
	 * @throws NullPointerException
	 *             if the term is <code>null</code>
	 * @throws IllegalArgumentException
	 *             if the submitted term isn't a <code>INumbericTerm</code>
	 * @throws IllegalArgumentException
	 *             if the number is negative
	 */
	public StringTerm multiply(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			final int count = TermHelper.getInt((INumericTerm) t);
			if (count < 0) {
				throw new IllegalArgumentException(
						"The number must be bigger than 0, but was " + count);
			}

			final StringBuilder buffer = new StringBuilder(value.length()
					* count);
			for (int i = 0; i < count; i++) {
				buffer.append(value);
			}
			return new StringTerm(buffer.toString());
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}

	/**
	 * Cuts n letters at the end of the string off.
	 * 
	 * @param the
	 *            amount of letters which should be cut off
	 * @return the partial cut string
	 * @throws NullPointerException
	 *             if the given term is null
	 * @throws IllegalArgumentException
	 *             if the given term isn't a <code>INumericTerm</code>
	 * @throws IllegalArgumentException
	 *             if the number is negative
	 */
	public StringTerm subtract(final ITerm t) {
		if (t == null) {
			throw new NullPointerException("The term must not be null");
		}
		if (t instanceof INumericTerm) {
			int tmp = TermHelper.getInt((INumericTerm) t);
			final int count = (tmp > value.length()) ? value.length() : tmp;
			if (count < 0) {
				throw new IllegalArgumentException(
						"The number must be bigger than 0, but was " + count);
			}

			return new StringTerm(value.substring(0, value.length() - count));
		}
		throw new IllegalArgumentException(
				"Can perform this task only with INumericTerm's, but was "
						+ t.getClass());
	}
}
