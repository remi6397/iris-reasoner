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
package org.deri.iris.operations.relations;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * Of an unsolved and a solved part which contains multiequations S = M.
 * When the unsolved part of the system is empty, the system is 
 * essentially solved. In fact, the solution (final result of the 
 * unification) can be obtained by substituting the variables backward 
 * (from the solved part).
 * 
 * Note:
 * The terminology used in this class is borrowed from:
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   09.08.2006 16:47:17
 */
public class MultiequationSystem {

	/**
	 * unsolved is a list of multiequations (possibly empty). The multiequations
	 * are used during the process of finding a unifier.
	 */
	private List<Multiequation> unsolved = null;
	
	/**
	 * solved is a list of multiequations (empty at the begining of the process 
	 * of finding a unifier). This list contains multiequations that define 
	 * the final result of the unfification (at the end of the process of 
	 * finding a unifier).
	 */
	private List<Multiequation> solved = null;
	
	
	public MultiequationSystem(final List<Multiequation> s){
		if(s == null){
			throw new IllegalArgumentException("Input parameter " +
					"must not be null.");
		}
		this.unsolved = new ArrayList<Multiequation>();
		this.solved = s;
	}

	public MultiequationSystem(final List<Multiequation> u, 
			final List<Multiequation> s){
		
		if(u == null && s == null){
			throw new IllegalArgumentException("Input parameters " +
					"must not be null.");
		}
		this.unsolved = u;
		this.solved = s;
	}

	/**
	 * @return Returns the solved.
	 */
	public List<Multiequation> getSolved() {
		return solved;
	}


	/**
	 * @return Returns the unsolved.
	 */
	public List<Multiequation> getUnsolved() {
		return unsolved;
	}
	
	/**
	 * Set multiequation property n for each multiequation
	 * in the entire MultiequationSystem.
	 */
	public void setOccurrences() {
		IVariable v = null;
		ITerm term = null;
		for(Multiequation m1 : unsolved){
			for(Multiequation m2 : unsolved){
				if(m2.getM() != null)
				for(ITerm t : m2.getM()){
					term = m1.getS().iterator().next();
					if(term instanceof IVariable){
						v = (IVariable) term;
						m1.setN(m1.getN() + 
								getVarOccurrences(v, t));
					}
				}
			}
		}
	}
	
	private int getVarOccurrences(IVariable v, ITerm t){
		int n = 0;
		if(t == null) return n;
		if(t.equals(v)) n++;
		else{
			if(t instanceof IConstructedTerm){
				IConstructedTerm ct = (IConstructedTerm)t;
				List<ITerm> terms = (List<ITerm>)t.getValue();
				for(ITerm ct1 : terms){
					n = n + getVarOccurrences(v, ct1);
				}
			}
		}
		return n;
	}
	
	/**
	 * @return Multiequation m with the smallest n 
	 * 			(from the multiequation system)
	 */
	public Multiequation getTop() {
		Multiequation m = this.unsolved.get(0);
		int n = m.getN();
		for(Multiequation me : this.unsolved){
			if(me.getN() < n){
				n = me.getN();
				m = me;
			}
		}
		return m;
	}
	
	/**
	 * This returns a simple string representation of a multiequation system. 
	 * The return of this method will look something like:<br/>&lt;set of 
	 * all unsolved multiequations separated by newlines&gt;<br/>
	 * <code>newline</code><br/>&lt;list of all solved multiequations 
	 * separated by newlines&gt;<br/><code>newline</code><br/>&lt;the query&gt;
	 * 
	 * @return the string representation
	 */
	public String toString() {
		final String NEWLINE = System.getProperty("line.separator");
		StringBuilder buffer = new StringBuilder();
		for (Multiequation me : this.unsolved) {
			buffer.append(me).append(NEWLINE);
		}
		buffer.append(NEWLINE);
		if(this.solved != null)
		for (Multiequation me : this.solved) {
			buffer.append(me).append(NEWLINE);
		}
		return buffer.toString();
	}
}
