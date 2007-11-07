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
package org.deri.iris.querycontainment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.deri.iris.EvaluationException;
import org.deri.iris.NaiveExecutor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.querycontainment.IQueryContainment;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * Checks two queries for query containment, based on a given 
 * knowledge base.
 * </p>
 * 
 * @author Nathalie Steinmetz, DERI Innsbruck
 */
public class QueryContainment implements IQueryContainment {

	/** The program to use with the query containment check. */
	private final IProgram knowledgebase;
	
	/** The substitution resulting from the query evaluation */
	private Map<IPredicate, IMixedDatatypeRelation> result;
	
	/** Namespace containing the constants that are used to replace variables */
	private final String namespace = "http://queryContainment/constants#";
	
	/**
	 * <p>
	 * Creates a new query containment checker with a given knowledge base.
	 * </p>
	 * 
	 * @param program
	 *            The program
	 * @throws IllegalArgumentException
	 *             If the program or the evaluator is {@code null}, or
	 *             If the program contains queries
	 */
	public QueryContainment(final IProgram program) {
		if (program == null) {
			throw new IllegalArgumentException(
					"The program must not be null");
		}
		if (program.getQueries().size() > 0) {
			throw new IllegalArgumentException( 
					"The program must not contain queries");
		}
		this.knowledgebase = program;
	}
	
	/*
	 * We use the Frozen Fact Algorithm for checking query containment. The 
	 * algorithm can be summarized as follows:
	 * Input: query1 and query2.
	 * Output: true if query1 is contained within query2, false otherwise.
	 * Algorithm: we 'freeze' query1 by substituting each of its variables 
	 * with fresh constants. Then we add this 'frozen' query to the knowledge 
	 * base and evaluate query2. If the result contains the frozen terms, 
	 * query1 is contained within query2. Otherwise we get an empty result. 
	 * 
	 * (non-Javadoc)
	 * @see org.deri.iris.api.IQueryContainment#checkQueryContainment(org.deri.iris.api.basics.IQuery, org.deri.iris.api.basics.IQuery)
	 */
	public boolean checkQueryContainment(IQuery query1, IQuery query2) throws EvaluationException 
	{
		if (query1 == null || query2 == null) {
			throw new IllegalArgumentException(
					"The two queries may not be null");
		}
		
		// query 1 is 'frozen' and added to the knowledge base
		knowledgebase.addFacts(freezeQuery(query1));
		
		// add query 2 to the knowledge base to be evaluated
		knowledgebase.addQuery(query2);
		
		// evaluate query2
		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new NaiveExecutor( knowledgebase, method );
		exec.execute();
		result =  exec.computeSubstitutions();

		
		return result.entrySet().iterator().next().getValue().size() > 0;
	}
	
	public Map<IPredicate, IMixedDatatypeRelation> getContainmentMappings() {
		return result;
	}
	
	/*
	 * Method to substitute the variables in query1 by constants
	 */
	private Set<IAtom> freezeQuery(IQuery query) {
		Set<IAtom> facts = new HashSet<IAtom>();
		Map<IVariable, ITerm> variableMapping = new HashMap<IVariable, ITerm>();
		IAtom atom = null;
		List<ITerm> terms = new Vector<ITerm>();
		for (ILiteral literal : query.getLiterals()) {
			
			// build mapping for all variables
			for (IVariable variable : literal.getAtom().getTuple().getVariables()) {
				if (! variableMapping.containsKey(variable))
					variableMapping.put(variable, Factory.CONCRETE.createIri(
								namespace + new java.rmi.dgc.VMID().toString()));
			}
			
			// build 'frozen' Atom by actually substituting the variables
			for (ITerm term : literal.getAtom().getTuple()) {
				if (term instanceof IVariable)
					terms.add(variableMapping.get((IVariable) term));
				else
					terms.add(term);
			}
			atom = Factory.BASIC.createAtom(literal.getAtom().getPredicate(), 
					Factory.BASIC.createTuple(terms));
			facts.add(atom);
		}
		return facts;
	}
	
}
