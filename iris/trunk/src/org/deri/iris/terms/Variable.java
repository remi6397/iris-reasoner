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

import org.deri.iris.api.terms.IVariable;

/**
 * @author richi
 * 
 */
public class Variable implements IVariable<IVariable>, Cloneable {

	private String name = "";

	public Variable(final String name) {
		setName(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isGround() {
		return false;
	}

	public int compareTo(IVariable o) {
		return name.compareTo(o.getName());
	}

	public int hashCode() {
		return name.hashCode();
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
		if (!(o instanceof Variable)) {
			return false;
		}
		Variable v = (Variable) o;
		return name.equals(v.name);
	}

	/**
	 * Returns a String representation of this object. The subject of the string
	 * format is to change. An example return value might be
	 * &quot;org.deri.iris.terms.Variable[name=unknown0]&quot;
	 * 
	 * @return the String representation
	 */
	public String toString() {
		return getClass().getName() + "[name=" + name + "]";
	}
}
