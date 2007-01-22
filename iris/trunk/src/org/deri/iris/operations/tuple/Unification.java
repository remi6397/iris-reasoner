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

import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IUnification;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.operations.tuple.UnificationDecomposer.CFholder;

/**
 * <p>
 * This is an implementation of the unification interface. The algorithm finds 
 * the most general unifier for two atoms or two tupples.
 * </p>
 * <p>
 * This is an implementation of an unification algorithm according to:
 * 
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI.
 * This implementation also includes the following transformations: 
 * 
 * <ul>
 * <li> Multiequation reduction;</li>
 * <li> Compactification.</li>
 * </ul>
 * </p>
 * <p>
 * Performance of this algorithm has been compared with the other well-known 
 * algorithms in three extreme cases: high probability of stopping with success, 
 * high probability of detecting a cycle, and high probability of detecting a 
 * clash. This algorithm was shown to have a good performance in all the cases.
 * </p>
 * <p>
 * Reference: 
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   10.08.2006 16:28:06
 */
public class Unification implements IUnification{

	private UnificationDecomposer decomposer = null;
	
	public final static IVariable INIT_VAR_CONSTANT = TERM.createVariable("initVar");
	
	/**
	 * This unification algorithm has been implemented so that it 
	 * actually performs the unification on two terms. If two 
	 * tuples are given to be unified, then two constructed terms 
	 * will be created out of these two tuples, and the unification
	 * will again be performed on two terms (constructed terms). 
	 * initFunc is a function symbol used to create two auxiliary 
	 * constructed terms when it is needed.  
	 */
	public final static String INIT_FUNC_CONSTANT = "initFunc";
	
	private IConstructedTerm ct0 = null;

	private IConstructedTerm ct1 = null;
	

	Unification(final ITuple arg0, final ITuple arg1) {
		if (arg0 == null || arg1 == null) {
			throw new IllegalArgumentException("Input parameters must not be null");
		}
		if ((arg0.getArity() - arg1.getArity())!=0) {
			throw new IllegalArgumentException("Unification not possible " +
					"due to different arities of input parameters");
		}
		this.ct0 = TERM.createConstruct(INIT_FUNC_CONSTANT, arg0.getTerms());
		this.ct1 = TERM.createConstruct(INIT_FUNC_CONSTANT, arg1.getTerms());
	}

	Unification(final IAtom arg0, final IAtom arg1) {
		if (arg0 == null || arg1 == null) {
			throw new IllegalArgumentException("Input parameters must not be null");
		}
		if ((arg0.getTuple().getArity() - arg1.getTuple().getArity())!=0) {
			throw new IllegalArgumentException("Unification not possible " +
					"due to different arities of input parameters");
		}
		this.ct0 = TERM.createConstruct(INIT_FUNC_CONSTANT, arg0.getTuple().getTerms());
		this.ct1 = TERM.createConstruct(INIT_FUNC_CONSTANT, arg1.getTuple().getTerms());
	}

	Unification(final ITerm arg0, final ITerm arg1) {
		if (arg0 == null || arg1 == null) {
			throw new IllegalArgumentException("Input parameters must not be null");
		}
		this.ct0 = TERM.createConstruct(INIT_FUNC_CONSTANT, arg0);
		this.ct1 = TERM.createConstruct(INIT_FUNC_CONSTANT, arg1);
	}
	
	public UnificationResult unify() {
		UnificationResult result = null;
		List<Multiequation> meList = unifyTerms(this.ct0, this.ct1);
		
		if(meList != null){
			// Remove the auxiliary function symbol (INIT_FUNC_CONSTANT) from the meList
			IConstructedTerm ct = (IConstructedTerm)meList.get(0).getM().get(0);
			meList.remove(0);
			
			result = new UnificationResult(ct.getParameters(), meList);
		}
		return  result;
	}
	
	private List<Multiequation> unifyTerms(ITerm t0, ITerm t1) {
		MultiequationSystem ms = createInitMeSystem(t0, t1);
		Multiequation m = null;
		CFholder cf = null;
		
		while(! ms.getUnsolved().isEmpty()){
			m = ms.getTop();
			
			// failure: cycle
			if(m.getN() != 0) return null;
			if(m.getM() == null || m.getM().size() < 2){
				ms.getUnsolved().remove(m);
				ms.getSolved().add(m);
			}else{
				decomposer = new UnificationDecomposer(m);
				
				// compute C(M) and F(M):
				cf = decomposer.decompose(m.getM().get(0), m.getM().get(1));
				
				// failure: clash
				if(cf == null || cf.getCommon() == null) return null;
				ms = compactify(
						reduce(m, cf, ms));
				ms.setOccurrences();
				
				List<ITerm> c = new ArrayList<ITerm>();
				c.add(cf.getCommon());
				ms.getSolved().add(new Multiequation(m.getS(), c));
			}
		}
		return ms.getSolved();
	}
	
	/**
	 * @param t0	Term to be unified
	 * @param t1	Term to be unified
	 * @return		Returns an initial multiequation system
	 */
	private MultiequationSystem createInitMeSystem(ITerm t0, ITerm t1){
		MultiequationSystem multiequationSystem = null;
		Multiequation me = null; 
		Set<ITerm> s = null;
		List<ITerm> m = null;
		decomposer = new UnificationDecomposer ();
		Set<IVariable> vars = decomposer.getVariables(t0);
		vars.addAll(decomposer.getVariables(t1));
		List<Multiequation> unsolved = new ArrayList<Multiequation>();
		
		s = new HashSet<ITerm>();
		s.add(INIT_VAR_CONSTANT);
		m = new ArrayList<ITerm>();
		m.add(t0);
		m.add(t1);
		me = new Multiequation(s, m);
		unsolved.add(me);
		for(ITerm v : vars){
			s = new HashSet<ITerm>();
			s.add(v);
			m = new ArrayList<ITerm>();
			m.add(null);
			me = new Multiequation(s, m);
			unsolved.add(me);
		}
		List<Multiequation> solved = new ArrayList<Multiequation>();
		multiequationSystem = new MultiequationSystem(unsolved, solved);
		multiequationSystem.setOccurrences();
		
		return multiequationSystem;
	}
	
	/**
	 * Multiequation Reduction: Let Z be a set of multiequations containing a
	 * multiequation S = M such that M is nonempty and has a common part C and a
	 * frontier F. The new set Z' of multiequations is obtained by replacing S = M with
	 * the union of the multiequation S = (C) and of all the multiequations of F:
	 * 
	 * Z' = (Z- {S = M}) U {S = (C)} U F.
	 * 
	 * @param m Selected multiequation
	 * @param cf Common and frontier stored in one container
	 * @param ms Set of multiequations Z
	 * @return Set of multiequations Z'
	 */
	private MultiequationSystem reduce(
			final Multiequation m, 
				final CFholder  cf, 
					final MultiequationSystem  ms){
		
		List<Multiequation> solved = null;
		MultiequationSystem ms1 = null;
		List<Multiequation> unsolved = ms.getUnsolved();
		if(ms.getSolved() != null)
			solved = ms.getSolved();
		else
			solved = new ArrayList<Multiequation>();
		
		// Z - {S = M}
		unsolved.remove(m);
		
		// "add" {S = (C)} (Immediately added to the solved part)
		// "add operation" represents union
		if(cf.getFrontier() != null)
			unsolved.addAll(cf.getFrontier());
		
		ms1 = new MultiequationSystem(unsolved, solved);
		
		return ms1;
	}
	
	private MultiequationSystem compactify(final MultiequationSystem  ms){
		MultiequationSystem ms1 = ms;
		Iterator i = null;
		IVariable v = null;
		Multiequation me0 = null;
		Multiequation me1 = null;
		boolean mergeFurther = true;
		
		while(mergeFurther){
			mergeFurther = false;
			for(int j=0; j<ms1.getUnsolved().size();j++){
				me0  = ms1.getUnsolved().get(j);
				me0.setN(0);
				for(int k=j+1; k<ms1.getUnsolved().size(); k++){
					me1 = ms1.getUnsolved().get(k);
					i = me0.getS().iterator();
					while(i.hasNext()){
						v = (IVariable)i.next();
						
						// if me1 contains any of variables from
						// me0.getS() then merge me0 and me1:
						if(me1.getS().contains(v)){
							ms1.getUnsolved().remove(me0);
							ms1.getUnsolved().remove(me1);
							ms1.getUnsolved().add(
								mergeMultiequations(
									me0, me1));
							mergeFurther = true;
						}
					}
				}
			}
		}
		return ms1;
	}
	
	/**
	 * This method merges two multiequations (with a common
	 * variable/s in the left (S) part of the equation). It also doesn't
	 * allow null as a term (removes it if null exists) and sets 
	 * the counter n (property of a multiequation) to zero.
	 * 
	 * @param me0 Multiequation to be merged
	 * @param me1 Multiequation to be merged
	 * @return Merged multiequation
	 */
	private Multiequation mergeMultiequations(
			Multiequation me0, Multiequation me1){
		
		me0.getS().addAll(me1.getS());
		for(ITerm l : me1.getM()){
			if(! me0.getM().contains(l))
				me0.getM().add(l);
		}
		if(me0.getM().contains(null)) 
			me0.getM().remove(null);
		
		return me0;
	}
	
	/**
	 * This is a container class for a unification result. 
	 * If two terms are unify-able, result of the unification is 
	 * a non-empty head (the root term, such as a common function 
	 * symbol) and possible empty list of multiequations. 
	 * For instance, after the unification of the following terms:
	 * 
	 * f(x1, g(x2, x3), x2, b)
	 * f(g(h(a, x5), x2), x1, h(a, x4), x4)
	 * 
	 * we will have:
	 * f[x1, x1, x2, x4]
	 * in the head, as it is a common function symbol for unifying 
	 * terms, and we will have in the tail: 
	 * 
	 * [0]{x1} = ([g[x2, x3]]), 
	 * [0]{x2, x3} = ([h[a, x4]]), 
	 * [0]{x4, x5} = ([b])].
	 * 
	 * Note that in the process of unification of two atoms or 
	 * tuples whose arity is greater than 1, we will get a head 
	 * that is a list. Size of this list is equal to the arity of 
	 * atoms/tuples that are being unified.
	 * 
	 * @author Darko Anicic, DERI Innsbruck
	 * @date 03.11.2006 12:14:03
	 * 
	 */
	public class UnificationResult{
		
		private List<ITerm> head = null;
		
		private List<Multiequation> tail = null;

		public UnificationResult(final List<ITerm> h, 
				final List<Multiequation> t){
			
			if (h == null || t == null) {
				throw new IllegalArgumentException("Input parameters must not be null!");
			}
			this.head = h;
			this.tail = t;
		}
		
		/**
		 * @return Returns the head.
		 */
		public List<ITerm> getHeads() {
			return head;
		}

		/**
		 * @return Returns the tail.
		 */
		public List<Multiequation> getTails() {
			return tail;
		}		
		
		public String toString() {
			StringBuilder buffer = new StringBuilder();
			buffer.append("head: ");
			buffer.append("[");
			for (ITerm t : this.head) {
				buffer.append(t);
				buffer.append(", ");
			}
			buffer.delete(buffer.length() - 2, buffer.length());
			buffer.append("]");
			buffer.append(" tail: ");
			buffer.append("[");
			for (Multiequation me : this.tail) {
				buffer.append(me);
				buffer.append(", ");
			}
			buffer.append("]");
			return buffer.toString();
		}
	}
}
