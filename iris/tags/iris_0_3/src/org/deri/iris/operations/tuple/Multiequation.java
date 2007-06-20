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
package org.deri.iris.operations.tuple;

import java.util.List;
import java.util.Set;

import org.deri.iris.api.terms.ITerm;
import org.deri.iris.terms.ConstructedTerm;

/**
 * A multiequation is the generalization of an equation, and it allows 
 * us to group together many terms which should be unified. To represent 
 * multiequations we use the notation S = M where the left-hand side S 
 * is a nonempty set of variables or a constant and the right-hand 
 * side M is a multiset of nonvariable terms. An example is:
 * 
 * {xl, x2, x3} = (tl, t2).
 * 
 * Note:
 * The terminology used in this class is borrowed from:
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   09.08.2006 16:30:42
 */
public class Multiequation implements Comparable<Multiequation>{

	/**
	 * s is a nonempty set of terms (variables or a constant)
	 */
	private Set<ITerm> s = null;
	/**
	 * m is a multiset of nonvariable terms
	 */
	private List<ITerm> m = null;
	
	/**
	 * n is number of occurrences of variables from s in all 
	 * m lists of multiequations of one multiequation system
	 */
	private int n = 0;
	
	public Multiequation(final Set<ITerm> s, final List<ITerm> m) {
		if(s == null || s.size() == 0){
			throw new IllegalArgumentException("Input parameter s " +
					"must not be null or an empty set");
		}
		this.s = s;
		this.m = m;
	}

	/**
	 * @param m The m to set.
	 */
	public void setM(List<ITerm> m) {
		this.m = m;
	}

	/**
	 * @return Returns the m.
	 */
	public List<ITerm> getM() {
		return m;
	}

	/**
	 * @param s The s to set.
	 */
	public void setS(Set<ITerm> s) {
		this.s = s;
	}
	
	/**
	 * @return Returns the s.
	 */
	public Set<ITerm> getS() {
		return s;
	}
	
	/**
	 * @param n The n to set.
	 */
	public void setN(int n) {
		this.n = n;
	}
	
	/**
	 * @return Returns the n.
	 */
	public int getN() {
		return n;
	}

	/**
	 * @return Returns size of the multiset m.
	 */
	public int sizeOfM() {
		return m.size();
	}
	
	public boolean detectCycles() {
		Set vars = null;
		for(ITerm t : s){
			if(m != null){
				if(m.contains(t)) return true;
				for(ITerm term : m){
					if(term instanceof ConstructedTerm){
						vars = ((ConstructedTerm)term).getVariables();
						if(vars.contains(t)) return true;
					}
				}
			}
		}
		return false;
	}

	public int compareTo(Multiequation o) {
		if (o == null) {
			throw new IllegalArgumentException("Cannot compare with null");
		}
		int res = 0;
		if((res = this.n - o.n) != 0) return res; 
		
		return res;
	}
	
	public boolean equals(final Object o) {
		if (this == o) {
			return true;
		}
		if (!(o instanceof Multiequation)) {
			return false;
		}
		Multiequation multiequation = (Multiequation) o;
		if((this.n - multiequation.n) != 0) return false;
		if(!this.s.equals(multiequation.s)) return false;
		if(!this.m.equals(multiequation.m)) return false;
		
		return true;
	}

	/**
	 * This returns a simple string representation of a multiequation. 
	 * The return of this method will be set of all variables plus a 
	 * &lt;list of all terms of a multiequation. 
	 * 
	 * @return the string representation
	 */
	public String toString() {
		StringBuilder buffer = new StringBuilder();
		buffer.append("[").append(this.n).append("]");
		buffer.append("{");
		for (ITerm t : this.s) {
			buffer.append(t);
			buffer.append(", ");
		}
		buffer.delete(buffer.length() - 2, buffer.length());
		buffer.append("}").append(" = ");
		buffer.append("(").append(this.m).append(")");
		return buffer.toString();
	}
}
