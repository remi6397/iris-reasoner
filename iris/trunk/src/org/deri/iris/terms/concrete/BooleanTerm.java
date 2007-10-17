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
import org.deri.iris.api.terms.ITerm;

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
public class BooleanTerm implements IBooleanTerm {

	/** The boolean value represented by this object */
	private boolean value;
	
	/** One of the legal values. */
	private static final String TRUE = "true"; 

	/** One of the legal values. */
	private static final String FALSE = "false"; 

	/** One of the legal values. */
	private static final String ONE = "1"; 

	/** One of the legal values. */
	private static final String ZERO = "0"; 

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
	 * Constructs a boolean with the given value according to http://www.w3.org/TR/xmlschema-2/#boolean  
	 * 
	 * @param value String representation of the boolean, one of {true, false, 1, 0}
	 * @throws IllegalArgumentException If the string is null
	 * @throws IllegalArgumentException If the string does not contain one of the legal values
	 * {true, false, 1, 0}
	 */
	BooleanTerm(final String strValue) {
		if (strValue == null)
			throw new IllegalArgumentException( "Constructor parameter 'value' must not be null" );

		if( strValue.equalsIgnoreCase( TRUE ) || strValue.equalsIgnoreCase( ONE ) )
			value = true;
		else if( strValue.equalsIgnoreCase( FALSE ) || strValue.equalsIgnoreCase( ZERO ) )
			value = false;
		else
			throw new IllegalArgumentException( "Constructor parameter 'value' must be one of {" +
							TRUE + ", " + FALSE + ", " + ONE + ", " + ZERO + "}" );
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

	public String toString() {
		return Boolean.toString(value);
	}

	public boolean isGround() {
		return true;
	}

	public int compareTo(ITerm o) {
		if (o == null) {
			return 1;
		}
		
		BooleanTerm b = (BooleanTerm) o;
		return Boolean.valueOf(value).compareTo(b.getValue());
	}
}
