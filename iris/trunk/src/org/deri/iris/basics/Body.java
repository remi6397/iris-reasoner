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
package org.deri.iris.basics;

import java.util.HashSet;
import java.util.Set;

import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.terms.IVariable;

/**
 * @author richi
 * 
 */
public class Body implements IBody {

	private Set<ILiteral> literals = new HashSet<ILiteral>();

	Body(final Set<ILiteral> literals) {
		this.literals = literals;
	}

	public int getBodyLenght() {
		return literals.size();
	}

	public ILiteral getBodyLiteral(int arg) {
		ILiteral[] l = new ILiteral[this.literals.size()];
		l = this.literals.toArray(l);
		
		return l[arg];
	}

	public Set<ILiteral> getBodyLiterals() {
		return literals;
	}

	public Set<IVariable> getBodyVariables() {
		Set<IVariable> vars = new HashSet<IVariable>();
		for (ILiteral l : literals) {
			for (Object o : l.getTuple().getTerms()) {
				if (o instanceof IVariable) {
					vars.add((IVariable) o);
				}
			}
		}
		return vars;
	}

	public int hashCode() {
		int result = 37;
		for (ILiteral l : literals) {
			if (l != null) {
				result = result * 17 + l.hashCode();
			}
		}
		return result;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Body)) {
			return false;
		}
		Body b = (Body) o;
		return literals.equals(b.literals);
	}

	public String toString() {
		StringBuilder buffer = new StringBuilder();
		for (ILiteral l : literals) {
			buffer.append(l).append(", ");
		}
		return buffer.substring(0, buffer.length() - 2);
	}
}
