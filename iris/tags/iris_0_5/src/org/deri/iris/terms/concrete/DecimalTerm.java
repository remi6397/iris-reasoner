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
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Simple implementation of the IDecimalTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class DecimalTerm implements IDecimalTerm {

	private final Double d;

	DecimalTerm(final double d) {
		this.d = d;
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

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		DecimalTerm dt = (DecimalTerm) o;
		return d.compareTo(dt.getValue());
	}

	public boolean equals(final Object o) {
		if (!(o instanceof DecimalTerm)) {
			return false;
		}
		DecimalTerm dt = (DecimalTerm) o;
		return d.equals( dt.d );
	}

	public int hashCode() {
		return d.hashCode();
	}

	/**
	 * Simply returns the String representation of the holded double.
	 * 
	 * @return the String representation of the holded double
	 */
	public String toString() {
		return d.toString();
	}
}
