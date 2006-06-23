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

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;

/**
 * @author richi
 * 
 */
public class ConstructedTerm implements IConstructedTerm<IConstructedTerm>, Cloneable {

	private List<ITerm> terms = new ArrayList<ITerm>();

	private String symbol = "";

	ConstructedTerm(final String symbol, final Collection<ITerm> terms) {
		if (terms == null) {
			throw new IllegalArgumentException("The terms must not be null");
		}
		setFunctionSymbol(symbol);
		this.terms.addAll(terms);
	}

	public void setFunctionSymbol(String arg) {
		if (arg == null) {
			throw new IllegalArgumentException("The symbol must not be null");
		}
		symbol = arg;
	}

	public String getFunctionSymbol() {
		return symbol;
	}

	public int getArity() {
		return terms.size();
	}

	public ITerm getParameter(int arg) throws IndexOutOfBoundsException {
		return terms.get(arg);
	}

	public List<ITerm> getParameters() {
		return terms;
	}

	public boolean isGround() {
		for (ITerm t : terms) {
			if (!t.isGround()) {
				return false;
			}
		}
		return true;
	}

	public int hashCode() {
		int result = 17;
		result = result * 37 + symbol.hashCode();
		for (ITerm t : terms) {
			result = result * 37 + t.hashCode();
		}
		return result;
	}

	public Object clone() {
		try {
			ConstructedTerm ct = (ConstructedTerm) super.clone();
			ct.terms = new ArrayList<ITerm>(terms.size());
			for (ITerm t : terms) {
				ct.terms.add((ITerm) runClone(t));
			}
			return ct;
		} catch (CloneNotSupportedException e) {
			assert true : "Will never happen";
		}
		return null;
	}

	public String toString() {
		return getClass().getName() + "[symbol=" + symbol + ",terms="
				+ terms.toString() + "]";
	}

	public boolean equals(final Object o) {
		if (!(o instanceof ConstructedTerm)) {
			return false;
		}
		ConstructedTerm t = (ConstructedTerm) o;
		return (symbol.equals(t.symbol)) && (terms.size() == t.terms.size())
				&& (terms.containsAll(t.terms));
	}

	@SuppressWarnings("unchecked")
	public int compareTo(final IConstructedTerm o) {
		int result = symbol.compareTo(o.getFunctionSymbol());
		if (result != 0) {
			return result;
		}
		
		int min = Math.min(terms.size(), o.getParameters().size());
		
		for (int iCounter = 0; iCounter < min; iCounter++) {
			result = terms.get(iCounter).compareTo(o.getParameter(iCounter));
			if (result != 0) {
				return result;
			}
		}
		return terms.size() - o.getParameters().size();
	}

	/**
	 * Helpermethod to clone an object, because the Object.clone() method is
	 * protected. This Method clones a object using reflection.
	 * 
	 * @param o
	 *            the object to clone
	 * @return the clone
	 */
	private static Object runClone(final Object o) {
		// TODO: think about caching the methods
		try {
			return o.getClass().getMethod("clone", (Class[]) null).invoke(o,
					(Object[]) null);
		} catch (SecurityException e) {
			throw new IllegalArgumentException(e);
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException(e);
		} catch (InvocationTargetException e) {
			throw new IllegalArgumentException(e);
		} catch (NoSuchMethodException e) {
			throw new IllegalArgumentException(e);
		}
	}

	public IConstructedTerm getMinValue() {
		return null;
	}
}
