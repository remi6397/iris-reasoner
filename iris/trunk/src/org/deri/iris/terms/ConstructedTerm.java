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
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * @author richi
 * @author Darko Anicic, DERI Innsbruck
 * 
 */
public class ConstructedTerm implements IConstructedTerm, Cloneable {

	/**
	 * A constructed term consist of a list of terms, where these terms 
	 * can be constructed or non-constructed ones in a general case.
	 */
	private List<ITerm> terms = new ArrayList<ITerm>();

	private String symbol = "";
	
	
	ConstructedTerm(final String symbol, final Collection<ITerm> terms) {
		if (symbol == null || terms == null) {
			throw new IllegalArgumentException("Input parameters must " +
					"not be null");
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
		for(ITerm term : this.terms){
			if(term instanceof ConstructedTerm){
				if(!isGround((IConstructedTerm)term)) return false;
			}else{
				if (!term.isGround()) return false;
			}
				
		}
		return true;
	}
	
	private boolean isGround(IConstructedTerm t) {
		List<ITerm> termList = (List<ITerm>)t.getValue();
		for(ITerm term : termList){
			if(term instanceof ConstructedTerm){
				if(!isGround((IConstructedTerm)term)) return false;
			}else{
				if (!term.isGround()) return false;
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
			assert false : "Object is always cloneable";
		}
		return null;
	}

	public String toString() {
		return symbol + terms.toString();
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
		
		int min = Math.min(terms.size(), ((List<ITerm>)o.getValue()).size());
		
		for (int iCounter = 0; iCounter < min; iCounter++) {
			result = terms.get(iCounter).compareTo(o.getParameter(iCounter));
			if (result != 0) {
				return result;
			}
		}
		return terms.size() - ((List<ITerm>)o.getValue()).size();
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

	public List<ITerm> getValue() {
		return this.terms;
	}

	public void setValue(List<ITerm> t) {
		this.terms = t;
	}

	public Set<IVariable> getVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		for(ITerm term : this.terms){
			if(term instanceof IVariable)
				variables.add((IVariable)term);
			if(term instanceof IConstructedTerm)
				variables.addAll(getVariables((IConstructedTerm)term));
				
		}
		return variables;
	}
	
	private Set<IVariable> getVariables(IConstructedTerm t) {
		Set<IVariable> variables = new HashSet<IVariable>();
		List<ITerm> termList = (List<ITerm>)t.getValue();
		for(ITerm term : termList){
			if(term instanceof IVariable)
				variables.add((IVariable)term);
			if(term instanceof IConstructedTerm)
				variables.addAll(getVariables((IConstructedTerm)term));
		}
		return variables;
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IConstructedTerm add(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IConstructedTerm divide(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IConstructedTerm multiply(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}

	/**
	 * <b>This operation is not supported by this term.</b>
	 * 
	 * @throws UnsupportedOperationException
	 *             this operation is not supported
	 */
	public IConstructedTerm subtract(final ITerm t) {
		throw new UnsupportedOperationException(
				"Can't perform this operation on that term");
	}
}
