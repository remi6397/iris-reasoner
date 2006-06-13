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
 * @author richi
 * 
 */
public class Predicate implements IPredicate, Cloneable {

	private String symbol;

	private boolean builtin;

	private final int arity;

	public Predicate(final String symbol, final int arity) {
		setPredicateSymbol(symbol);
		this.arity = arity;
	}

	// TODO: I think setting the symbol after initialisation shouldn't be
	// possible
	public void setPredicateSymbol(final String symbol) {
		if (symbol == null) {
			throw new IllegalArgumentException("The symbol must not be null");
		}
		this.symbol = symbol;
	}

	public String getPredicateSymbol() {
		return symbol;
	}

	public int getArity() {
		return arity;
	}

	public void setBuiltIn(boolean arg) {
		builtin = arg;
	}

	public boolean isBuiltIn() {
		return builtin;
	}

	public int hashCode() {
		int result = 37;
		result = result * 17 + Boolean.valueOf(builtin).hashCode();
		result = result * 17 + symbol.hashCode();
		return result;
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

	public String toString() {
		return getClass().getName() + "[symbol=" + symbol + ",arity=" + arity
				+ ",builtin=" + builtin + "]";
	}
}
