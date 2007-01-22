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
import java.util.List;
import java.util.Set;

import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * This class calculates a common and frontier part out of a set 
 * of two terms.
 * The common part of a multiset of terms M is a term which, intuitively, 
 * is obtained by superimposing all terms of M, and by taking the part
 * which is common to all of them starting from the root. 
 * For instance, given the multiset of terms:
 * </p>
 * <p>
 * (f(xl, g(a, f(x5, b))), f(h(c), g(x2, f(b, x5))), f(h(x4), g(x6, x3)))
 * </p>
 * <p>
 * the common part is
 * </p>
 * <p>
 * f(xl, g(x2, x3)).
 * </p>
 * <p>
 * The frontier is a set of multiequations, where every multiequation is associated
 * with a leaf of the common part and consists of all subterms (one for each term of
 * M) corresponding to that leaf. The frontier of the above multiset of terms is
 * </p>
 * <p>
 * {{x1} = (h(c), h(x4)),<br/> 
 * {x2, x6} = (a),<br /> 
 * {x3} = (f(x5, b), f(b, x5)).<br /> 
 * </p>
 * <p>
 * Note:
 * The terminology used in this class is borrowed from:
 * An Efficient Unification Algorithm, ALBERTO MARTELLI and UGO MONTANARI
 * </p>
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
	
	public CFholder decompose(ITerm term0, ITerm term1){
		MultiequationSystem multiequationSystem = null;
		List<CFholder> cfList = new ArrayList<CFholder>();
		CFholder cf = null;
		CFholder cfTemp = null;
		
		cf = checkVariables(term0, term1);
		
		if(cf == null){
			CTdesc ct = head(term0, term1);
			if(ct != null && ct.getSymbol() != null){
				// if term is a constant (arity=0)
				if(ct.getArity() == 0){
					return new CFholder(
							TERM.createString(ct.getSymbol()), 
							null);
				}else{
					IConstructedTerm ct0 = (IConstructedTerm) term0;
					IConstructedTerm ct1 = (IConstructedTerm) term1;
					
					// terms are not constants (arity is not 0)
					for(int i=0; i<ct.getArity(); i++){
						cfTemp = decompose(
								((List<ITerm>)ct0.getValue()).get(i),
								((List<ITerm>)ct1.getValue()).get(i));
						
						if(cfTemp != null)
							cfList.add(cfTemp);
						// failure: inside recursive call of subDecompose
						else return null;
					}
					return createMultiequationSystem(
							ct.getSymbol(), cfList);
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
	
	/**
	 * @param t0	non variable term
	 * @param t1	non variable term
	 * @return		Returns (CTdesc object) the root term (e.g. function symbol)
	 * 				of t0 and t1 and its arity, or null if it does not exist.
	 */
	private CTdesc head(ITerm t0, ITerm t1) {
		if (t0 instanceof IVariable || t1 instanceof IVariable) {
			throw new IllegalArgumentException("Input tuples must not be variables");
		}
		if (t0 instanceof IStringTerm) {
				if (t1 instanceof IStringTerm){
						if(! t0.equals(t1)) return null;
				}
			return new CTdesc(t0.toString(), 0);
		}
		if (t1 instanceof IStringTerm){
				
			return new CTdesc(t1.toString(), 0);
		}
		if (t0 instanceof IConstructedTerm
				&& t0 instanceof IConstructedTerm) {

			IConstructedTerm ct0 = (IConstructedTerm) t0;
			IConstructedTerm ct1 = (IConstructedTerm) t1;
			if (!ct0.getFunctionSymbol().equals(ct1.getFunctionSymbol()))
				return null;
			else
				return new CTdesc(ct0.getFunctionSymbol(), ct0.getArity());
		}
		return null;
	}

	
	/**
	 * @param ps		The root function symbol
	 * @param cfList	List of sub CF holders made during recursive 
	 * 					call of decompose method
	 * @return			Returns a multiequation system, 
	 * 					a pair <common, frontier>
	 */
	private CFholder createMultiequationSystem(String ps, List<CFholder> cfList){
		List commonList = new ArrayList<IVariable>(cfList.size());
		List<CFholder> cfl = cfList;
		ITerm common = null;
		List<Multiequation> frontier = new ArrayList<Multiequation>();
		
		for(CFholder cf : cfl){
			commonList.add(cf.common);
		}
		frontier = getFrontier(cfList);
		common = TERM.createConstruct(ps, commonList);
		
		return new CFholder(common, frontier);
	}
	
	private List<Multiequation> getFrontier(List<CFholder> cfl){
		List<Multiequation> frontier = new ArrayList<Multiequation>();
		List<ITerm> temp = null;
		CFholder cf1 = null;
		Multiequation me1 = null;
		boolean added = false;
		
		//frontier.add(cfl.get(0).frontier.get(0));
		for(int i=0; i<cfl.size(); i++){
			cf1 = cfl.get(i);
			temp = new ArrayList<ITerm>();
			if(cf1.frontier != null){	
				for(int k=0; k<cf1.frontier.size(); k++){
					added = false;
					me1 = cf1.frontier.get(k);
					for(Multiequation me : frontier){
						if(me.getS().containsAll(me1.getS())
								&& me.getS().size()==me1.getS().size()){
							
							me.getM().addAll(me1.getM());
							added = true;
						}
					}
					if(!added) frontier.add(me1);
				}
			}
		}
		return frontier;
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
	
	/**
	 * This is a ConstructedTerm descriptor
	 * 
	 * @author Darko Anicic, DERI Innsbruck
	 * @date 16.10.2006 18:40:03
	 * 
	 */
	public class CTdesc{
		
		/**
		 * Function symbol
		 */
		private String symbol = "";
		
		private int arity = 0;
		
		
		public CTdesc(final String s, final int a){
			this.symbol = s;
			this.arity = a;
		}
		
		/**
		 * @return Returns the symbol.
		 */
		public String getSymbol() {
			return symbol;
		}

		/**
		 * @param symbol The symbol to set.
		 */
		public void setSymbol(String symbol) {
			this.symbol = symbol;
		}

		/**
		 * @return Returns the arity.
		 */
		public int getArity() {
			return arity;
		}
		
		
		/**
		 * @param arity The arity to set.
		 */
		public void setArity(int arity) {
			this.arity = arity;
		}
		
		public String toString() {
			return this.symbol + "_" + this.arity;
		}
	}
}
