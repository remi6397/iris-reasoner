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
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Evaluates a built-in subgoal.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 22.04.2007 21:12:43
 */
public class GeneralBuiltinEvaluator implements IBuiltinEvaluator {

	/** A built-in subgoal to be evaluated. */
	private IBuiltInAtom builtin = null;

	/** The input relation for the evaluation of a built-in subgoal. */
	private IMixedDatatypeRelation relation0 = null;

	/**  */
	private List<IVariable> relVars = null;

	private List<IVariable> outVras = null;
	
	/** Flag to indicate if this predicate is positive or negated. */
	private final boolean positive;

	/**
	 * <p>
	 * Constructs a BuiltinEvaluator.
	 * </p>
	 * <p>
	 * First positive literals are evaluated. Then result (a relation) with a
	 * list of attributes (variables) is used for the evaluation of a built-in
	 * subgoal.
	 * </p>
	 * 
	 * @param built-in
	 *            A built-in subgoal to be evaluated.
	 * @param relVars
	 *            A list of attributes (variables).
	 * @param rel
	 *            A relation derived during the evaluation of positive literals.
	 *            Serves as the input relation for the evaluation of a built-in
	 *            subgoal.
	 */
	GeneralBuiltinEvaluator(boolean positive, IBuiltInAtom builtin, List<IVariable> relVars,
			IMixedDatatypeRelation rel) {

		if (builtin == null || relVars == null || rel == null) {
			throw new IllegalArgumentException("Constructor "
					+ "parameters are not specified correctly!");
		}
		this.builtin = builtin;
		this.relation0 = rel;
		this.relVars = relVars;
		this.outVras = getOVars();
		this.positive = positive;
	}

	/**
	 * <p>
	 * The built-in evaluation is implemented w.r.t some universe U, such that U
	 * includes at least tuples we got from the evaluation of positive literals.
	 * Relation relation0 serves to define the universe U.
	 * </p>
	 * 
	 * (non-Javadoc)
	 * 
	 * @see org.deri.iris.api.operations.relation.IBuiltinEvaluator#evaluate()
	 */
	public IMixedDatatypeRelation evaluate()
	{
		final int resultArity = relation0.getArity() + outVras.size();
		
		final IMixedDatatypeRelation resultRel = RELATION.getMixedRelation( resultArity );

		if (relation0.isEmpty())
		{
			/*
			 If the rule only contains a built-in predicate, then there will
			 be no existing relation to join to.
			 This is allowed in certain circumstances, e.g.
				 p(?X) :- ?X = 3
				 p :- 1 = 2
				 p(?X) :- 1 + ?X = 2
			*/
			int numVariables = builtin.getTuple().getAllVariables().size();
			int maxUnknownVariables = builtin.maxUnknownVariables();
			if(	numVariables <= maxUnknownVariables )
			{
				ITuple inTuple = builtin.getTuple();
				ITuple outTuple = builtin.evaluate( inTuple );
				
				// TODO
				// This is certainly not correct, but it works.
				// The arity if the relation0 is not predictable, because it depends
				// on what literal (if any) was previously evaluated.
				if( outTuple != null )
				{
					if( resultArity == outTuple.size() )
						resultRel.add( outTuple );
					else
					{
						ITuple t2 = substituteTuple( outTuple );
						resultRel.add( t2 );
					}
				}
			}
		}
		else
		{
			for (final ITuple t0 : relation0) {
				final ITuple t1 = builtin.evaluate(getInTuple(t0));
				if (((t1 != null) && positive) || ((t1 == null) && !positive)) {
					resultRel.add((outVras.isEmpty()) ? t0 : t0.append(t1));
				}
			}
		}
		return resultRel;
	}

	/**
	 * <p>
	 * Transforms/re-order an input tuple so to extract its relevant subset for
	 * evaluation of a given builtin. For instance, if a built-in is: add(X,4,Y)
	 * and relVars<X,Z,W> with a tuple <3,6,9> are given, then the transformed
	 * tuple will be: <3,4,Y>.
	 * </p>
	 * 
	 * @param tup
	 *            An input tuple.
	 * @return A transformed tuple.
	 */
	private ITuple getInTuple(ITuple tup) {
		List<ITerm> termList = new ArrayList<ITerm>();
		int i = 0;
		for (ITerm t : builtin.getTuple()) {
			if (t.isGround()) {
				termList.add(t);
			} else {
				i = this.relVars.indexOf((IVariable) t);
				if (i != -1) {
					termList.add(tup.get(i));
				} else {
					termList.add(t);
				}
			}
		}
		return BASIC.createTuple(termList);
	}

	private ITuple substituteTuple( ITuple out ) {
//		if( out == null )
//			return builtin.getTuple();
		
		Iterator<ITerm> it = out.iterator();
		
		List<ITerm> termList = new ArrayList<ITerm>();
		int i = 0;
		for (ITerm t : builtin.getTuple()) {
			if (t.isGround()) {
				termList.add(t);
			} else {
				termList.add( it.next() );
//				i = relVars.indexOf((IVariable) t);
//				if (i != -1) {
//					termList.add(tup.get(i));
//				} else {
//					termList.add(t);
//				}
			}
		}
		return BASIC.createTuple(termList);
	}

	/**
	 * @return A list of output variables of a built-in.
	 */
	private List<IVariable> getOVars() {
		List<IVariable> vars = new ArrayList<IVariable>();
		for (IVariable v : this.builtin.getTuple().getAllVariables()) {
			if (!this.relVars.contains(v)) {
				vars.add(v);
			}
		}
		return vars;
	}

	/**
	 * <p>
	 * Returns output variables of a built-in.
	 * </p>
	 * 
	 * @return A list of builtin output variables.
	 */
	public List<IVariable> getOutVars() {
		return this.outVras;
	}
}
