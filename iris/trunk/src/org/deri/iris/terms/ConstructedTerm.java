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
	private final List<ITerm> terms = new ArrayList<ITerm>();

	/** The function symbol itself. */
	private final String symbol;
	
	/**
	 * Constructor (for factory method).
	 * @param symbol The function symbol
	 * @param terms The terms for this function symbols expression.
	 */
	ConstructedTerm(final String symbol, final Collection<ITerm> terms) {
		assert symbol != null : "The symbol must not be null";
		assert terms != null : "The terms must not be null";
		
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
		for(ITerm term : terms){
			if (! term.isGround() )
				return false;
				
		}
		return true;
	}
	
	public int hashCode() {
		int result = symbol.hashCode();
		for (Object t : terms) {
			result = result * 37 + t.hashCode();
		}
		return result;
	}

	public String toString()
	{
		StringBuilder result = new StringBuilder();
		
		result.append( symbol ).append( '(' );
		
		for( int i = 0; i < terms.size(); ++i )
		{
			if( i > 0 )
				result.append( ',' );
			result.append( terms.get( i ) );
		}
		
		result.append( ')' );
		
		return result.toString();
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
		
		assert oo instanceof IConstructedTerm : "Invalid argument type for ConstructedTerm.compare()";
	
		final IConstructedTerm o = (IConstructedTerm) oo;
		
		int result = symbol.compareTo(o.getFunctionSymbol());
		if (result != 0)
			return result;
		
		List<ITerm> oTerms = (List<ITerm>) o.getValue();
		
		int min = Math.min( terms.size(), oTerms.size() );
		
		for (int iCounter = 0; iCounter < min; iCounter++) {
			result = terms.get(iCounter).compareTo(oTerms.get(iCounter));
			if (result != 0)
				return result;
		}
		return terms.size() - o.getValue().size();
	}

	public List<ITerm> getValue() {
		return terms;
	}

	public Set<IVariable> getVariables() {
		Set<IVariable> variables = new HashSet<IVariable>();
		for(ITerm term : terms){
			if(term instanceof IVariable)
				variables.add((IVariable)term);
			else if(term instanceof IConstructedTerm)
			{
				IConstructedTerm childTerm = (IConstructedTerm) term;
				variables.addAll(childTerm.getVariables() );
			}
		}
		return variables;
	}
}
