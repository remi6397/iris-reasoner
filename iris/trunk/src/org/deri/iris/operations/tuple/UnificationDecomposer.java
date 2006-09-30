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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * This class is used for calculating a common and frontier part of a set 
 * of two terms.
 * The common part of a multiset of terms M is a term which, intuitively, 
 * is obtained by superimposing all terms of M and by taking the part
 * which is common to all of them starting from the root. For instance, 
 * given the multiset of terms:
 * 
 * (f(xl, g(a, f(x5, b))), f(h(c), g(x2, f(b, x5))), f(h(x4), g(x6, x3)))
 * 
 * the common part is
 * 
 * f(xl, g(x2, x3)).
 * 
 * The frontier is a set of multiequations, where every multiequation is associated
 * with a leaf of the common part and consists of all subterms (one for each term of
 * M) corresponding to that leaf. The frontier of the above multiset of terms is
 * 
 * {{x1} = (h(c), h(x4)),
 * {x2, x6} = (a),
 * {x3} = (f(x5, b), f(b, x5)).
 * 
 * Note:
 * The terminology used in this class is borrowed from:
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   16.08.2006 16:40:03
 */
public class UnificationDecomposer {

	private ITerm term0 = null;
	
	private ITerm term1 = null;
	
	public UnificationDecomposer(){
	}
	
	public UnificationDecomposer(final Multiequation m){
		if (m == null) {
			throw new IllegalArgumentException("Input parameter must not be null");
		}
		if(m.getM().size()>0)this.term0 = m.getM().get(0);
		if(m.getM().size()>1)this.term1 = m.getM().get(1);
		
	}
	
	public CFholder decompose(){
		MultiequationSystem multiequationSystem = null;
		List<CFholder> cfList = new ArrayList<CFholder>();
		CFholder cf = null;
		CFholder cfTemp = null;
		
		cf = checkVariables(term0, term1);
		
		if(cf == null){
			IPredicate predicate = head(term0, term1);
			if(predicate.getPredicateSymbol() != null){
				// term is a constant (arity=0)
				if(predicate.getArity() == 0){
					return new CFholder(
							TERM.createString(predicate.getPredicateSymbol()), 
							null);
				}else{
					IConstructedTerm ct0 = (IConstructedTerm) term0;
					IConstructedTerm ct1 = (IConstructedTerm) term1;
					
					// terms are not constants (arity is not 0)
					for(int i=0; i<predicate.getArity(); i++){
						cfTemp = subDecompose(((List<ITerm>)ct0.getValue()).get(i),
								((List<ITerm>)ct1.getValue()).get(i));
						
						if(cfTemp != null)
							cfList.add(cfTemp);
						// failure: inside recursive call of subDecompose
						else return null;
					}
					return createMultiequationSystem(
							predicate.getPredicateSymbol(), cfList);
				}
			}
			// failure: there is no common part for term0 & term1.
			else return null;	
		}else{
			return cf;
		}
	}
	
	private CFholder subDecompose(ITerm term0, ITerm term1){
		CFholder cfTemp = null;
		List<CFholder> cfList = new ArrayList<CFholder>();
		CFholder cf = null;
		
		cf = checkVariables(term0, term1);
		
		if(cf == null){
			IPredicate predicate = head(term0, term1);
			if(predicate.getPredicateSymbol() != null){
				// term is a constant (arity=0)
				if(predicate.getArity() == 0){
					return new CFholder(
							TERM.createString(predicate.getPredicateSymbol()), 
							null);
				}else{
					// terms are not constants (arity is not 0)
					for(int i=0; i<predicate.getArity(); i++){
						IConstructedTerm ct0 = (IConstructedTerm) term0;
						IConstructedTerm ct1 = (IConstructedTerm) term1;
						cfTemp = subDecompose(((List<ITerm>)ct0.getValue()).get(i),
								((List<ITerm>)ct1.getValue()).get(i));
						if(cfTemp != null)
							cfList.add(cfTemp);
						// failure: inside recursive call of subDecompose
						else return null;
					}
					return createMultiequationSystem(
							predicate.getPredicateSymbol(), cfList);
				}
			}
			// failure: there is no common part for term0 & term1.
			else return null;	
		}else{
			return cf;
		}
	}
	
	private CFholder checkVariables(ITerm term0, ITerm term1){
		if(term0 instanceof IVariable || term1 instanceof IVariable){
			List<Multiequation> f = new ArrayList<Multiequation>();
			Set<ITerm> s = new HashSet<ITerm>();
			List<ITerm> m = new ArrayList<ITerm>();
			ITerm common = null;
			
			if(term0 instanceof IVariable){
				s.add((IVariable)term0);
				common = (IVariable)term0;
			}else{
				m.add(term0);
			}
			if(term1 instanceof IVariable){
				s.add((IVariable)term1);
				if(common == null)common = (IVariable)term1;
			}else{
				m.add(term1);
			}
			f.add(new Multiequation(s, m));
			return new CFholder(common, f);
		}
		return null;
	}
	
	protected Set getVariables(ITerm t){
		Set<IVariable> vars = new HashSet<IVariable>();
		if(t instanceof IVariable) vars.add((IVariable)t);
		if(t instanceof IConstructedTerm) vars.addAll(
				((IConstructedTerm)t).getVariables());
		return vars;
	}
	
	private IPredicate head(ITerm term0, ITerm term1) {
		if (!(term0 instanceof IVariable) && 
				!(term0 instanceof IConstructedTerm)) {
			return BASIC.createPredicate(term0.toString(), 0);
		}
		if (!(term1 instanceof IVariable) && 
				!(term1 instanceof IConstructedTerm)) {	
			return BASIC.createPredicate(term1.toString(), 0);
		}
		if (term0 instanceof IConstructedTerm
				&& term0 instanceof IConstructedTerm) {

			IConstructedTerm ct0 = (IConstructedTerm) term0;
			IConstructedTerm ct1 = (IConstructedTerm) term1;
			if (!ct0.getFunctionSymbol().equals(ct1.getFunctionSymbol()))
				return null;
			else
				return BASIC.createPredicate(ct0.getFunctionSymbol(), ct0
						.getArity());
		}
		return null;
	}

	private CFholder createMultiequationSystem(String ps, List<CFholder> cfList){
		List commonList = new ArrayList<IVariable>(cfList.size());
		List<CFholder> cfl = cfList;
		ITerm common = null;
		List<Multiequation> frontier = new ArrayList<Multiequation>();
		
		for(CFholder cf : cfl){
			commonList.add(cf.common);
		}
		List<ITerm> temp = null;
		CFholder cf1 = null;
		CFholder cf2 = null;
		
		for(int i=0; i<cfl.size(); i++){
			cf1 = cfl.get(i);
			temp = new ArrayList<ITerm>();
			for(int j=i+1; j<cfl.size(); j++){
				cf2 = cfl.get(j);
				if(contains(cf2.frontier.get(0).getS(), cf1.frontier) ){
						
					temp.addAll(cf2.frontier.get(0).getM());
					cfl.remove(cf2);
				}
			}
			if(cf1.frontier != null){
				temp.addAll(cf1.frontier.get(0).getM());
				frontier.add(new Multiequation(cf1.frontier.get(0).getS(), temp));
			}
		}
		common = TERM.createConstruct(ps, commonList);
		return new CFholder(common, frontier);
	}
	
	private boolean contains(Set<ITerm> terms, List<Multiequation> frontier){
		if(frontier != null){
			for(Multiequation me : frontier){
				if(me.getS().containsAll(terms) && 
						me.getS().size()==terms.size())
					return true;
			}
		}
		return false;
	}
	
	/**
	 * This is a container for a pair <common, frontier>
	 * 
	 * @author Darko Anicic, DERI Innsbruck
	 * @date   08.09.2006 16:40:03
	 */
	public class CFholder{
		
		private ITerm common = null;
		
		private List<Multiequation> frontier = null;
		
		public CFholder(final ITerm c, final List<Multiequation> f){
			this.common = c;
			this.frontier = f;
		}

		/**
		 * @return Returns the common.
		 */
		public ITerm getCommon() {
			return common;
		}

		/**
		 * @return Returns the frontier.
		 */
		public List<Multiequation> getFrontier() {
			return frontier;
		}
		
		public String toString(){
			StringBuilder buffer = new StringBuilder();
			buffer.append("<");
			buffer.append(this.common.toString());
			buffer.append(", ");
			if(this.frontier != null)
				buffer.append(this.frontier.toString());
			else
				buffer.append("null");
			buffer.append(">");
			return buffer.toString();
		}
	}
}
