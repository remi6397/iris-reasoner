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

import org.deri.iris.api.basics.IPredicate;

/**
 * This is a simple IPredicate implementation.</br>
 * <b>NOTE: This implementation is immutable</b>
 * </br></br>$Id$
 * @author richi
 * @version $Revision$
 */
public class Predicate implements IPredicate, Cloneable {

	private final String symbol;

	private final boolean builtin;

	private final int arity;
	
	private int stratum;

	Predicate(final String symbol, final int arity) {
		this.symbol = symbol;
		this.builtin = false;
		this.arity = arity;
	}

	Predicate(final String symbol, final int arity, boolean isBuiltin) {
		this.symbol = symbol;
		this.builtin = isBuiltin;
		this.arity = arity;
	}

	/**
	 * In this implementation the change of the symbol is not supported
	 * @throws UnsupportedOperationException because it should be immutable
	 */
	public void setPredicateSymbol(final String symbol) {
		throw new UnsupportedOperationException("This implementation should be immutable");
	}

	public String getPredicateSymbol() {
		return symbol;
	}

	public int getArity() {
		return arity;
	}

	/**
	 * In this implementation the changing of the symbol is not supported
	 * @throws UnsupportedOperationException because it should be immutable
	 */
	public void setBuiltIn(boolean arg) {
		throw new UnsupportedOperationException("This implementation should be immutable");
	}

	public boolean isBuiltIn() {
		return builtin;
	}

	public int hashCode() {
		int result = 17;
		result = result * 37 + (builtin ? 0 : 1);
		result = result * 37 + arity;
		result = result * 37 + symbol.hashCode();
		result = result * 37 + stratum;
		return result;
	}

	public Object clone() {
		try {
			return super.clone();
		} catch (CloneNotSupportedException e) {
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Predicate)) {
			return false;
		}
		Predicate p = (Predicate) o;
		return (builtin == p.builtin) && symbol.equals(p.symbol)
				&& (arity == p.arity);
	}

	public int compareTo(IPredicate o) {
		int res = 0;
		if ((res = symbol.compareTo(o.getPredicateSymbol())) != 0) {
			return res;
		}
		if ((res = arity - o.getArity()) != 0) {
			return res;
		}
		if ((res = Boolean.valueOf(builtin).compareTo(
				Boolean.valueOf(o.isBuiltIn()))) != 0) {
			return res;
		}
		return 0;
	}

	public int getStratum() {
		return stratum;
	}

	public int setStratum(int stratum) {
		return this.stratum = stratum;
	}

	public String toString() {
		return symbol;
	}
}
