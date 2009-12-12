/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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

import java.net.URI;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IDoubleTerm;
import org.deri.iris.utils.StandardFloatingPointComparator;

/**
 * <p>
 * Simple implementation of the IDoubleTerm.
 * </p>
 */
public class DoubleTerm implements IDoubleTerm {

	private final Double mValue;

	DoubleTerm(final double d) {
		mValue = d;
	}

	public Double getValue() {
		return mValue;
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}

		DoubleTerm dt = (DoubleTerm) o;
		return mValue.compareTo(dt.mValue);
	}

	public boolean equals(final Object o) {
		if (!(o instanceof DoubleTerm)) {
			return false;
		}
		DoubleTerm dt = (DoubleTerm) o;

		return mValue.equals( dt.mValue );
	}

	public int hashCode() {
		return mValue.hashCode();
	}

	/**
	 * Simply returns the String representation of the holded double.
	 * 
	 * @return the String representation of the holded double
	 */
	public String toString() {
		return mValue.toString();
	}

	public URI getDatatypeIRI() {
		return URI.create("http://www.w3.org/2001/XMLSchema#double");
	}

	public String toCanonicalString() {
		return mValue.toString();
	}
}
