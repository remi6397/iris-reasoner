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
package org.deri.iris.evaluation.seminaive;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.Executor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;
import org.deri.iris.parser.ProgramTest;

/**
 * <p>
 * </p>
 * <p>
 * $Id: TestNeg.java,v 1.6 2007-10-30 08:28:32 poettler_ric Exp $
 * </p>
 * 
 * @author richi
 * @version $Revision: 1.6 $
 * @date $Date: 2007-10-30 08:28:32 $
 */
public class TestNeg {

	public static void main(final String[] arg) throws Exception{
		// constructing the rules
		// q(X) :- s(X), -p(X)
		Set<IRule> rules = new HashSet<IRule>(3);
		List<ILiteral> h = Arrays.asList(createLiteral("q", "X"));
		List<ILiteral> b = Arrays.asList(createLiteral("s", "X"),
				BASIC.createLiteral(false, BASIC
						.createPredicate("p", 1), BASIC
						.createTuple(TERM.createVariable("X"))));

		IRule r = BASIC.createRule(h, b);
		rules.add(r);

		// p(X) :- r(X)
		r = BASIC.createRule(Arrays.asList(createLiteral(
				"p", "X")), Arrays.asList(createLiteral("r", "X")));
		rules.add(r);

		// p(X) :- p(X)
		r = BASIC.createRule(Arrays.asList(createLiteral(
				"p", "X")), Arrays.asList(createLiteral("p", "X")));
		rules.add(r);

		// create facts
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// r(1), r(2)
		IPredicate p = BASIC.createPredicate("r", 1);
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(1);
		rel.add(createStringTuple("1"));
		rel.add(createStringTuple("2"));
		facts.put(p, rel);

		// s(3), s(4)
		p = BASIC.createPredicate("s", 1);
		rel = RELATION.getMixedRelation(1);
		rel.add(createStringTuple("3"));
		rel.add(createStringTuple("4"));
		facts.put(p, rel);

		// ?- q(x)
		IQuery q0 = BASIC.createQuery(createLiteral("q", "X"));
		
		Set<IQuery> queries = new HashSet<IQuery>();
		queries.add(q0);
		
		final IProgram e = Factory.PROGRAM.createProgram(facts, rules, queries);

		System.out.println("--- input ---");
		for (final IRule rule : e.getRules()) {
			System.out.println(rule);
		}

		System.out.println("--- computing ---");
		
		System.out.println("--- facts ---");
		System.out.println(e.getPredicates());
		for (final IPredicate pred : e.getPredicates()) {
			System.out.printf("%s -> %s\n", pred.getPredicateSymbol(), e
					.getFacts(pred));
			for (ITuple t : e.getFacts(pred)) {
				System.out.println(pred.getPredicateSymbol() + t);
			}
		}
		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(e, method);
		exec.execute();
		Map<IPredicate,IMixedDatatypeRelation> m = exec.computeSubstitutions();
		System.out.println("*****" + "RESULT: " + "*****");
		ProgramTest.printResults(m);
	}

	private static ITuple createStringTuple(final String... s) {
		if ((s == null) || Arrays.asList(s).contains(null)) {
			throw new NullPointerException(
					"The strings must not be null or contain null");
		}
		final List<ITerm> t = new ArrayList<ITerm>(s.length);
		for (final String str : s) {
			t.add(TERM.createString(str));
		}
		return BASIC.createTuple(t);
	}

	/**
	 * Creates a positive literal out of a predicate name and a set of variable
	 * strings.
	 * 
	 * @param pred
	 *            the predicate name
	 * @param vars
	 *            the variable names
	 * @return the constructed literal
	 * @throws NullPointerException
	 *             if the predicate name or the set of variable names is
	 *             {@code null}
	 * @throws NullPointerException
	 *             if the set of variable names contains {@code null}
	 * @throws IllegalArgumentException
	 *             if the name of the predicate is 0 characters long
	 */
	private static ILiteral createLiteral(final String pred,
			final String... vars) {
		if ((pred == null) || (vars == null)) {
			throw new NullPointerException(
					"The predicate and the vars must not be null");
		}
		if (pred.length() <= 0) {
			throw new IllegalArgumentException(
					"The predicate name must be longer than 0 chars");
		}
		if (Arrays.asList(vars).contains(null)) {
			throw new NullPointerException("The vars must not contain null");
		}

		return BASIC.createLiteral(true, BASIC.createPredicate(pred,
				vars.length), BASIC.createTuple(new ArrayList<ITerm>(
				createVarList(vars))));
	}

	/**
	 * Creates a list of IVariables out of a list of strings.
	 * 
	 * @param vars
	 *            the variable names
	 * @return the list of correspoinding variables
	 * @throws NullPointerException
	 *             if the vars is null, or contains null
	 */
	private static List<IVariable> createVarList(final String... vars) {
		if ((vars == null) || Arrays.asList(vars).contains(null)) {
			throw new NullPointerException(
					"The vars must not be null and must not contain null");
		}
		final List<IVariable> v = new ArrayList<IVariable>(vars.length);
		for (final String var : vars) {
			v.add(TERM.createVariable(var));
		}
		return v;
	}
}
