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

import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.tuple.IUnification;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.operations.relations.UnificationDecomposer.CFholder;

/**
 * In its essence, the unification problem in first-order logic can be expressed as
 * follows: Given two terms containing some variables, find, if it exists, the simplest
 * substitution (i.e., an assignment of some term to every variable) which makes the
 * two terms equal. The resulting substitution is called the most general unifier and
 * is unique up to variable renaming.
 * 
 * This is an implementation of an unification algorithm according to:
 *
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI.
 * 
 * This implementation also includes the following transformations: 
 * 
 * 1.	Multiequation reduction;
 * 2.	Compactification. 
 * 
 * Performance of this algorithm has been compared with the other well-known algorithms 
 * in three extreme cases: high probability of stopping with success, 
 * high probability of detecting a cycle, and high probability of detecting a clash. 
 * This algorithm was shown to have a good performance in all the cases.

 * Reference: 
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI
 * 
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   10.08.2006 16:28:06
 */
public class Unification implements IUnification{

	private ITuple tuple0 = null;
	
	private ITuple tuple1 = null;
	
	private UnificationDecomposer decomposer = null;
	
	private IVariable initialVariable = TERM.createVariable("initialVariable");
	

	Unification(final ITuple arg0, final ITuple arg1) {
		if (arg0 == null || arg1 == null) {
			throw new IllegalArgumentException("Input parameters must not be null");
		}
		if ((arg0.getArity() - arg1.getArity())!=0) {
			throw new IllegalArgumentException("Unification not possible " +
					"due to different arities of input parameters");
		}
		this.tuple0 = arg0;
		this.tuple1 = arg1;
	}

	Unification(final IAtom arg0, final IAtom arg1) {
		if (arg0 == null || arg1 == null) {
			throw new IllegalArgumentException("Input parameters must not be null");
		}
		if ((arg0.getTuple().getArity() - arg1.getTuple().getArity())!=0) {
			throw new IllegalArgumentException("Unification not possible " +
					"due to different arities of input parameters");
		}
		this.tuple0 = arg0.getTuple();
		this.tuple1 = arg1.getTuple();
	}

	public List<List> unify() {
		List<List> result = new ArrayList<List>();
		for(int i=0; i<this.tuple0.getTerms().size(); i++){
			result.add(
					unifyTerms(this.tuple0.getTerm(i), this.tuple1.getTerm(i)));
		}
		return result;
	}
	
	private List<Multiequation> unifyTerms(ITerm t0, ITerm t1) {
		MultiequationSystem ms = 
			createInitialMultiequationSystem(t0, t1);
		Multiequation m = null;
		CFholder cf = null;
		
		while(! ms.getUnsolved().isEmpty()){
			m = ms.getTop();
			
			// failure: cycle
			if(m.getN() != 0) return null;
			if(m.getM() == null || m.getM().size() == 0){
				ms.getUnsolved().remove(m);
			}else{
				decomposer = new UnificationDecomposer(m);
				
				// compute C(M) and F(M):
				cf = decomposer.decompose();
				
				// failure: clash
				if(cf.getCommon() == null) return null;
				ms = compactify(
						reduce(m, cf, ms));
				ms.setOccurrences();
			}
		}
		return ms.getSolved();
	}
	
	private MultiequationSystem createInitialMultiequationSystem(ITerm t0, ITerm t1){
		MultiequationSystem multiequationSystem = null;
		Multiequation me = null; 
		Set<ITerm> s = null;
		List<ITerm> m = null;
		decomposer = new UnificationDecomposer ();
		Set<IVariable> vars = decomposer.getVariables(t0);
		vars.addAll(decomposer.getVariables(t1));
		List<Multiequation> unsolved = new ArrayList<Multiequation>();
		
		s = new HashSet<ITerm>();
		s.add(initialVariable);
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
		//ms1.getUnsolved().remove(m);
		unsolved.remove(m);
		
		// "add" {S = (C)} (Immediately added to the solved part)
		// "add operation" represents union
		List<ITerm> c = new ArrayList<ITerm>();
		c.add(cf.getCommon());
		solved.add(new Multiequation(m.getS(), c));
		
		// "add" F
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
}
