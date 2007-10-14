/*
 * Integrated Rule Inference System (IRIS): An extensible rule inference system
 * for datalog with extensions by built-in predicates, default negation (under
 * well-founded semantics), function symbols and contexts.
 * 
 * Copyright (C) 2006 Digital Enterprise Research Institute (DERI),
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, A-6020
 * Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.deri.iris.basics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.deri.iris.api.basics.IHead;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.IVariable;

/**
 * Represents a head of a rule. </br></br>$Id$
 * 
 * @author richi
 * @version $Revision$
 * @date $Date$
 */
public class Head implements IHead {

	/** Holds all literals of the head. */
	private final List<ILiteral> literals;

	/** List to cache the variable computation */
	private List<IVariable> variables = null;

	/**
	 * Constructs a head with the given literals. The list of literals will be
	 * copied while construction of this object.
	 * 
	 * @param literals
	 *            the literals for the body
	 * @throws NullPointerException
	 *             if the list is null
	 * @throws IllegalArgumentException
	 *             if the list contains null
	 */
	Head(final List<ILiteral> literals) {
		if (literals == null) {
			throw new NullPointerException("The literals must not be null");
		}
		if (literals.contains(null)) {
			throw new IllegalArgumentException(
					"The literals must not contain null");
		}
		this.literals = new ArrayList<ILiteral>(literals);
	}

	public int getLength() {
		return literals.size();
	}

	public ILiteral getLiteral(int arg) {
		return literals.get(arg);
	}

	public List<ILiteral> getLiterals() {
		return Collections.unmodifiableList(literals);
	}

	public List<IVariable> getVariables() {
		if (variables == null) {
			variables = new ArrayList<IVariable>();
			for (ILiteral l : literals) {
				for (Object o : l.getTuple().getTerms()) {
					if (o instanceof IVariable) {
						variables.add((IVariable) o);
					}
				}
			}
		}
		return Collections.unmodifiableList(variables);
	}

	public int hashCode() {
		int result = 37;
		result = result * 17 + literals.hashCode();
		return result;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Head)) {
			return false;
		}
		Head h = (Head) o;
		return literals.equals(h.literals);
	}

	/**
	 * Returns a short summary of this object. The format of the string is not
	 * documented and subject to change. The string returned by this method
	 * could be for example a list of all literals separated by &quot;, &quot;.
	 * 
	 * @return the summary of this object
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (ILiteral l : literals) {
			buffer.append(l).append(", ");
		}
		return buffer.substring(0, buffer.length() - 2);
	}
}
