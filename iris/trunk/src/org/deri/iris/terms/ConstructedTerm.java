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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Simple implementation of the IConstructedTerm.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class ConstructedTerm implements IConstructedTerm {

	/**
	 * A constructed term consist of a list of terms, where these terms 
	 * can be constructed or non-constructed ones in a general case.
	 */
	private List<ITerm> terms = new ArrayList<ITerm>();

	private String symbol = "";
	
	
	ConstructedTerm(final String symbol, final Collection<ITerm> terms) {
		if (symbol == null) {
			throw new NullPointerException("The symbol must not be nulL");
		}
		if (terms == null) {
			throw new NullPointerException("The terms must not be null");
		}
		this.symbol = symbol;
		this.terms.addAll(terms);
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
		for(Object term : this.terms){
			if(term instanceof ConstructedTerm){
				if(!isGround((IConstructedTerm)term)) return false;
			}else{
				if (!( (ITerm) term).isGround()) return false;
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
		for (Object t : terms) {
			result = result * 37 + t.hashCode();
		}
		return result;
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

	public int compareTo(final ITerm oo) {
		if (oo == null) {
			return 1;
		}
		
		final IConstructedTerm o = (IConstructedTerm) oo;
		
		int result = symbol.compareTo(o.getFunctionSymbol());
		if (result != 0) {
			return result;
		}
		
		int min = Math.min(terms.size(), ((List<ITerm>)o.getValue()).size());
		
		for (int iCounter = 0; iCounter < min; iCounter++) {
			result = terms.get(iCounter).compareTo(o.getParameter(iCounter));
			if (result != 0)
				return result;
		}
		return terms.size() - ((List<ITerm>)o.getValue()).size();
	}

	public List<ITerm> getValue() {
		return this.terms;
	}

	public Set<IVariable> getVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		for(Object term : this.terms){
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
}
