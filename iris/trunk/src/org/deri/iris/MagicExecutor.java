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

package org.deri.iris;

import static org.deri.iris.factory.Factory.PROGRAM;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.evaluation.IBottomUpEvaluator;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.evaluation.common.AdornedProgram;
import org.deri.iris.evaluation.common.EvaluationUtilities;
import org.deri.iris.evaluation.magic.MagicSetImpl;
import org.deri.iris.evaluation.MiscOps;
import org.deri.iris.evaluation.seminaive.SeminaiveEvaluation;

/**
 * <p>
 * Executor with some improvement techniques.
 * </p>
 * <p>
 * If this executor is asked for a query with constants in it, it will try to 
 * run the evaluation with magic
 * sets.
 * </p>
 * <p>
 * The executor won't compute the whole fixed point at once. Instead it
 * evaluates only the rules neede to compute the actual query. It will also
 * remember completely evaluated predicates and won't include the evaluation of
 * the rules of those predicates into the upcomming query computations.
 * </p>
 * <p>
 * $Id: MagicExecutor.java,v 1.6 2007-10-19 07:37:16 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.6 $
 */
public class MagicExecutor implements IExecutor {

	/** The program to evaluate with this object. */
	private final IProgram program;

	/** The expression evaluator to use. */
	private final IExpressionEvaluator evaluator;

	/** Set of fully evaluated predicates until now. */
	private Set<IPredicate> fullyEvaluated = new HashSet<IPredicate>();

	/**
	 * Constructs a new executor with a given program and expression
	 * evaluator.
	 * @param p the program which should be evaluated with this executor
	 * @param e the expression evaluator to use
	 * @throws NullPointerException if the program is <code>null</code>
	 * @throws NullPointerException if the evaluarot is <code>null</code>
	 */
	public MagicExecutor(final IProgram p, final IExpressionEvaluator e) {
		if (p == null) {
			throw new NullPointerException("The program must not be null");
		}
		if (e == null) {
			throw new NullPointerException("The evaluator must not be null");
		}
		program = p;
		evaluator = e;
	}

	public IMixedDatatypeRelation computeSubstitution(final IQuery q) {
		if (!MiscOps.stratify(program)) {
			throw new RuntimeException("The input program is not strtifed");
		}
		// shrink the rules for the query
		final Set<IPredicate> depends = EvaluationUtilities.getDepends(program.getRules(), q);
		depends.removeAll(fullyEvaluated);
		final Set<IRule> minimalRules = EvaluationUtilities.getRulesForPredicates(program.getRules(), depends);
		// determining the final program and query to evaluate
		final IProgram finProgram;
		final IQuery finQuery;
		if (tryMagic(q)) { // try the magic sets
			final MagicSetImpl ms = new MagicSetImpl(new AdornedProgram(minimalRules, q));
			final IProgram tmp = ms.createProgram(program.getFacts());
			if (tmp.isStratified()) { // if the ms is stratified -> use the ms
				finProgram = tmp;
				finQuery = tmp.getQueries().iterator().next();
			} else { // if the ms prog is not stratified -> do the normal eval
				finProgram = PROGRAM.createProgram(program.getFacts(), minimalRules, null);
				finQuery = q;
				fullyEvaluated.addAll(depends);
			}
		} else { // do the normal evaluation
			finProgram = PROGRAM.createProgram(program.getFacts(), minimalRules, null);
			finQuery = q;
			fullyEvaluated.addAll(depends);
		}
		// compute the substitutions
		return runEvaluation(finProgram, finQuery, evaluator);
	}

	public Map<IPredicate, IMixedDatatypeRelation> computeSubstitutions() {
		final Map<IPredicate, IMixedDatatypeRelation> res = 
			new HashMap<IPredicate, IMixedDatatypeRelation>();
		for (final IQuery q : program.getQueries()) {
			res.put(shortenPredicate(q.getLiteral(0)), computeSubstitution(q));
		}
		return res;
	}

	public boolean execute() throws EvaluationException {
		// delete the cached data, to enforce the recomputation
		fullyEvaluated = new HashSet<IPredicate>();

		if( ! MiscOps.stratify( program ) )
			throw new ProgramNotStratifiedException( "The input program is not stratified" );

		for (IRule rule : program.getRules() )
			MiscOps.checkRuleSafe( rule );
		
		return true;
	}

	/**
	 * Shortens a predicate of a literal, to fit the ones expected by the
	 * {@link #computeSubstitutions() computeSubstitutions()} method. 
	 * The new arity of the predicate is equal to the number of unground 
	 * terms in it's tuple.
	 * @param l the literal whose predicate to shorten
	 * @return the shortened predicate
	 * @throws NullPointerException if the literal is <code>null</code>
	 * @see #computeSubstitutions()
	 */
	private static IPredicate shortenPredicate(final ILiteral l) {
		if (l == null) {
			throw new NullPointerException("The literal must not be null");
		}
		int i = 0;
		for (final ITerm t : l.getTuple()) {
			if (!t.isGround()) {
				i++;
			}
		}
		return org.deri.iris.factory.Factory.BASIC.createPredicate(
				l.getPredicate().getPredicateSymbol(), i);
	}

	/**
	 * Runs the evaluation for a program for a given query with a given
	 * expression evaluator.
	 * @param p the program to evaluate
	 * @param q the query to retrieve
	 * @param e the expression evaluator to use
	 * @return the computed relation
	 * @throws EvaluationException 
	 * @throws NullPointerException if the program is <code>null</code>
	 * @throws NullPointerException if the query is <code>null</code>
	 * @throws NullPointerException if the expression evaluator is <code>null</code>
	 * @throws IllegalArgumentException if the query doesn't have 1 literal
	 */
	private static IMixedDatatypeRelation runEvaluation(final IProgram p, 
			final IQuery q, final IExpressionEvaluator e) {
		if (p == null) {
			throw new NullPointerException("The program must not be null");
		}
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		if (e == null) {
			throw new NullPointerException("The expression evaluator must not be null");
		}
		final IBottomUpEvaluator eval = new SeminaiveEvaluation(e, p);
		eval.evaluate();
		eval.runQuery(q);
		return eval.getResultSet().getResults().get(q.getLiteral(0).getPredicate());
	}

	/**
	 * Checks whether we shold try to evaluate the query using magic sets.
	 * @param q the query to check
	 * @return <code>true</code> if magic sets should be tried, otherwise
	 * <code>false</code>
	 * @throws NullPointerException if the query is <code>null</code>
	 * @throws IllegalArgumentException if the query doesn't have 1 literal
	 */
	private static boolean tryMagic(final IQuery q) {
		if (q == null) {
			throw new IllegalArgumentException("The query must not be null");
		}
		for (final ITerm t : q.getLiteral(0).getTuple()) {
			if (t.isGround()) {
				return true;
			}
		}
		return false;
	}
}
