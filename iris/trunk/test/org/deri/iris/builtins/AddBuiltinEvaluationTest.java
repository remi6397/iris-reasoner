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
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.Executor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IBody;
import org.deri.iris.api.basics.IHead;
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
 * Tests for the {@code AddBuiltin Evaluation}.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   12.04.2007 09:58:07
 */
public class AddBuiltinEvaluationTest extends TestCase {

	public static Test suite() {
		return new TestSuite(AddBuiltinEvaluationTest.class, AddBuiltinEvaluationTest.class
				.getSimpleName());
	}

	public void testEvaluate0() throws Exception{
		// constructing the rules
		Set<IRule> rules = new HashSet<IRule>(3);
		// p(X) :- r(X)
		IRule r = Factory.BASIC.createRule(Factory.BASIC.createHead(createLiteral(
				"p", "X")), Factory.BASIC.createBody(createLiteral("r", "X")));
		rules.add(r);
		// p(X) :- s(X), add(3, 4, X)
		IHead h = Factory.BASIC.createHead(createLiteral("p", "X"));
		IBody b = Factory.BASIC.createBody(createLiteral("s", "X"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
						createAddBuiltin(
								CONCRETE.createInteger(3),
								CONCRETE.createInteger(4),
								TERM.createVariable("X"))));
		
		r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// s(3), s(7), s(11), s(15)
		IPredicate p = Factory.BASIC.createPredicate("s", 1);
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(1);
		rel = RELATION.getMixedRelation(1);
		rel.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(7)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(11)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(15)));
		facts.put(p, rel);

		// r(1), r(2)
		p = Factory.BASIC.createPredicate("r", 1);
		rel = RELATION.getMixedRelation(p.getArity());
		rel.add(BASIC.createTuple(CONCRETE.createInteger(1)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(2)));
		facts.put(p, rel);
		
		IQuery q = Factory.BASIC.createQuery(createLiteral("p", "X"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IProgram pr = Factory.PROGRAM.createProgram(facts, rules, queries);
		
		// Result: p(1), p(2), p(7)
		IMixedDatatypeRelation res = RELATION.getMixedRelation(1);
		res.add(BASIC.createTuple(CONCRETE.createInteger(1)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(2)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(7)));
		
		System.out.println("******** TEST 0: ********");
		executeTest(pr, res);
	}
	
	public void testEvaluate1() throws Exception{
		// constructing the rules
		Set<IRule> rules = new HashSet<IRule>(3);
		
		// q(X, Y, Z) :- s(X), p(Y), add(X, Y, Z)
		IHead h = Factory.BASIC.createHead(createLiteral("q", "X", "Y", "Z"));
		IBody b = Factory.BASIC.createBody(
				createLiteral("s", "X"),
				createLiteral("p", "Y"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
						createAddBuiltin(
								TERM.createVariable("X"),
								TERM.createVariable("Y"),
								TERM.createVariable("Z"))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// s(3), s(7)
		IPredicate p = Factory.BASIC.createPredicate("s", 1);
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(1);
		rel = RELATION.getMixedRelation(1);
		rel.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(7)));
		facts.put(p, rel);

		/// p(1), p(2)
		p = Factory.BASIC.createPredicate("p", 1);
		rel = RELATION.getMixedRelation(p.getArity());
		rel.add(BASIC.createTuple(CONCRETE.createInteger(1)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(2)));
		facts.put(p, rel);
		
		IQuery q = Factory.BASIC.createQuery(createLiteral("q", "X", "Y", "Z"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IProgram pr = Factory.PROGRAM.createProgram(facts, rules, queries);
		
		// Result: q(3,1,4), q(3,2,5), q(7,1,8), q(7,2,9)
		IMixedDatatypeRelation res = RELATION.getMixedRelation(3);
		res.add(BASIC.createTuple(CONCRETE.createInteger(3),CONCRETE.createInteger(1),CONCRETE.createInteger(4)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(3),CONCRETE.createInteger(2),CONCRETE.createInteger(5)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(7),CONCRETE.createInteger(1),CONCRETE.createInteger(8)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(7),CONCRETE.createInteger(2),CONCRETE.createInteger(9)));
		
		System.out.println("******** TEST 1: ********");
		executeTest(pr, res);
	}

	public void testEvaluate2() throws Exception{
		// constructing the rules
		Set<IRule> rules = new HashSet<IRule>(3);
		
		// p(X) :- add(3, 4, X)
		IHead h = Factory.BASIC.createHead(createLiteral("p", "X"));
		IBody b = Factory.BASIC.createBody(
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
						createAddBuiltin(
								CONCRETE.createInteger(3),
								CONCRETE.createInteger(4),
								TERM.createVariable("X"))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// p(3), p(7)
		IPredicate p = Factory.BASIC.createPredicate("p", 1);
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(p.getArity());
		rel.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(9)));
		facts.put(p, rel);
		
		IQuery q = Factory.BASIC.createQuery(createLiteral("p", "X"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IProgram pr = Factory.PROGRAM.createProgram(facts, rules, queries);
		
		// Result: p(3), p(7), p(9)
		IMixedDatatypeRelation res = RELATION.getMixedRelation(1);
		res.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(7)));
		res.add(BASIC.createTuple(CONCRETE.createInteger(9)));
		
		System.out.println("******** TEST 2: ********");
		executeTest(pr, res);
	}
	
	public void testEvaluate3() throws Exception{
		// constructing the rules
		Set<IRule> rules = new HashSet<IRule>(3);
		
		// q(X, Y, Z) :- add(X, 4, Z), s(X), p(Y), add(X, Y, 10)
		IHead h = Factory.BASIC.createHead(createLiteral("q", "X", "Y", "Z"));
		IBody b = Factory.BASIC.createBody(
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
						createAddBuiltin(
								TERM.createVariable("X"),
								CONCRETE.createInteger(4),
								TERM.createVariable("Z"))),
				createLiteral("s", "X"),
				createLiteral("p", "Y"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
						createAddBuiltin(
								TERM.createVariable("X"),
								TERM.createVariable("Y"),
								CONCRETE.createInteger(10))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// s(3), s(6), s(9)
		IPredicate p = Factory.BASIC.createPredicate("s", 1);
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(p.getArity());
		rel.add(BASIC.createTuple(CONCRETE.createInteger(3)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(6)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(9)));
		facts.put(p, rel);
		
		// p(2), p(4)
		p = Factory.BASIC.createPredicate("p", 1);
		rel = RELATION.getMixedRelation(p.getArity());
		rel.add(BASIC.createTuple(CONCRETE.createInteger(2)));
		rel.add(BASIC.createTuple(CONCRETE.createInteger(4)));
		facts.put(p, rel);
		
		IQuery q = Factory.BASIC.createQuery(createLiteral("q", "X", "Y", "Z"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IProgram pr = Factory.PROGRAM.createProgram(facts, rules, queries);
		
		// Result: q(6,4,10)
		IMixedDatatypeRelation res = RELATION.getMixedRelation(q.getQueryLiteral(0).getPredicate().getArity());
		res.add(BASIC.createTuple(CONCRETE.createInteger(6),CONCRETE.createInteger(4),CONCRETE.createInteger(10)));
		
		System.out.println("******** TEST 3: ********");
		executeTest(pr, res);
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
	
	/**
	 * Eexecutes a program and print results.
	 * 
	 * @param p	A program to be evaluated.
	 */
	private static void executeTest(final IProgram p, IMixedDatatypeRelation res)throws Exception{
		System.out.println("--- input ---");
		for (final IRule rule : p.getRules()) {
			System.out.println(rule);
		}
		System.out.println("--- facts ---");
		System.out.println(p.getPredicates());
		for (final IPredicate pred : p.getPredicates()) {
			System.out.printf("%s -> %s\n", pred.getPredicateSymbol(), p
					.getFacts(pred));
			for (ITuple t : p.getFacts(pred)) {
				System.out.println(pred.getPredicateSymbol() + t);
			}
		}
		
		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(p, method);
		exec.execute();
		System.out.println("Result: ");
		Map<IPredicate, IMixedDatatypeRelation> results = exec.computeSubstitutions();
		ProgramTest.printResults(results);
		
		assertTrue(results.get(results.keySet().iterator().next()).containsAll(res));
		assertTrue(res.containsAll(results.get(results.keySet().iterator().next())));
	}
			
}
