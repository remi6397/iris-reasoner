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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.RELATION;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.api.operations.relation.IBuiltinEvaluator;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Evaluates a built-in.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   22.04.2007 21:12:43
 */
public class BuiltinEvaluator implements IBuiltinEvaluator{

	private IBuiltInAtom builtin = null;
	
	private IRelation relation0 = null;
	
	private List<IVariable> relVars = null;
	
	// TODO: remove this
	private List<IVariable> inVras = null;
	
	private List<IVariable> outVras = null;
	
	/**
	 * <p>
	 * First positive literals are evaluated and then result (a relation) 
	 * with a list of attributes (variables) is used for the evaluation of
	 * a built-in subgoal.
	 * </p>
	 * @param built-in A built-in subgoal to be evaluated. 
	 * @param relVars  A list of attributes (variables). 	
	 * @param rel	   A relation derived during the evaluation of positive 
	 * literals. Serves as input for the evaluation of a built-in subgoal.
	 */
	BuiltinEvaluator(IBuiltInAtom builtin,
			List<IVariable> relVars, IRelation rel) {
		
		if (builtin == null || relVars == null || rel == null) {
			throw new IllegalArgumentException("Constructor "
					+ "parameters are not specified correctly!");
		}
		this.builtin = builtin;
		this.relation0 = rel;
		this.relVars = relVars;
		this.inVras = getInVars();
		this.outVras = getOVars();
	}
	
	/**
	 * <p>
	 * TODO: Correct this so that the loop does not go through each tuple of 
	 * the input relation relation0. Depending on a particular built-in, 
	 * the halting condition may be fulfilled much before the end of the 
	 * relation0 (e.g., add(5, 2, ?X). 
	 * </p>
	 * <p>
	 * Apart from this, the add built-in 
	 * in this case, would have only one tuple as a result, thus a result 
	 * relation containing this tuple should be the outer relation in the 
	 * join evaluation loop. 
	 * </p>
	 * <p>
	 * Also a projection node above a join of two or 
	 * more relations and built-ins could be integrated here, avoiding an
	 * explicite projection after such a join. 
	 * </p>
	 * <p>
	 * Try to delete the input relation
	 * rel from memory, as it just waits the memory space. 
	 * </p>
	 * 
	 * @see org.deri.iris.api.operations.relation#evaluate().
	 */
	public IRelation evaluate(){
		IRelation resultRel = RELATION.getRelation(this.relation0.getArity() + this.outVras.size());
		ITuple t0, t1 = null;
		ITuple tRes = null;
		if(this.relation0.size() > 0){
			Iterator<ITuple> it0 = this.relation0.iterator();
			while(it0.hasNext()){
				t0 = it0.next();
				tRes = BASIC.createTuple(t0.getArity() + this.outVras.size());
				tRes.setTerms(0, t0.getTerms());
				t1 = this.builtin.evaluate(getInTuple(t0));
				if(t1 != null){
					tRes.setTerms(t0.getArity(), t1.getTerms());
					resultRel.add(tRes);
				}
			}
		}else{
			// e.g., add(3, 4, ?X)
			//tRes = this.builtin.evaluate(null, this.inVras.toArray(new IVariable[this.inVras.size()]));
			tRes = this.builtin.evaluate(this.builtin.getTuple());
			resultRel.add(tRes);	
		}
		return resultRel;
	}
	
	/**
	 * <p>
	 * Transforms/re-order an input tuple so to extract its relevant 
	 * subset for evaluation of a given builtin. For instance, if a built-in is: 
	 * add(X,4,Y) and
	 * relVars<X,Z,W> with a tuple <3,6,9> are given, then the transformed tuple
	 * will be: <3,4,Y>.
	 * </p>
	 * @param tup	An input tuple. 
	 * @return		A transformed tuple.
	 */
	private ITuple getInTuple(ITuple tup){
		List<ITerm> termList = new ArrayList<ITerm>(this.inVras.size());
		int i = 0;
		for(ITerm t : this.builtin.getTuple().getTerms()){
			if(t.isGround()){
				termList.add(t);
			}else{
				i = this.relVars.indexOf((IVariable)t);
				if(i != -1){
					termList.add(tup.getTerm(i));
				}else{
					termList.add(t);
				}
			}
		}
		return BASIC.createTuple(termList);
	}
	
	/**
	 * @return	A list of output variables of a built-in.
	 */
	private List<IVariable> getOVars(){
		List<IVariable> vars = new ArrayList<IVariable>();
		for(IVariable v : this.builtin.getTuple().getAllVariables()){
			if(! this.relVars.contains(v)){
				vars.add(v);
			}
		}
		return vars;
	}
	
	/**
	 * Defines builtin input variables of a built-in.
	 * @return A list of builtin input variables.
	 */
	private List<IVariable> getInVars(){
		List<IVariable> inVras = new ArrayList<IVariable>();
		for(IVariable v : this.builtin.getTuple().getAllVariables()){
			if(this.relVars.contains(v)){
				inVras.add(v);
			}
		}
		return inVras;
	}
	
	public List<IVariable> getOutVars(){
		return this.outVras;
	}
}
