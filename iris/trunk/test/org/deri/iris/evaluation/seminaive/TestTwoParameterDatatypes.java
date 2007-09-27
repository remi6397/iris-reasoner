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
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   06.04.2007 16:00:07
 */
public class TestTwoParameterDatatypes {
	
	public static void main(final String[] arg) throws Exception{
		// constructing the rules
		// w(?X) :- s(?X), p(?X), ?X != 'd'
		Set<IRule> rules = new HashSet<IRule>(3);
		IHead h = Factory.BASIC.createHead(createLiteral("w", "X"));
		
		IBody b = Factory.BASIC.createBody(
				createLiteral("s", "X"),
				createLiteral("p", "X"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
						createUnequal(TERM.createString("d"),
								TERM.createVariable("X"))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		/** create facts:
		"s('d')." +
	    "s('b')." +
	    "s('a')." +
	    
	    "p('b')." +
	    "p('d')." +
	    */
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		IPredicate pr = Factory.BASIC.createPredicate("s", 1);
		IMixedDatatypeRelation rel = RELATION.getMixedRelation(1);
		rel.add(createStringTuple("d"));
		rel.add(createStringTuple("b"));
		rel.add(createStringTuple("a"));
		facts.put(pr, rel);

		pr = Factory.BASIC.createPredicate("p", 1);
		rel = RELATION.getMixedRelation(1);
		rel.add(createStringTuple("b"));
		rel.add(createStringTuple("d"));
		facts.put(pr, rel);

		IQuery q = Factory.BASIC.createQuery(createLiteral("w", "X"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		
		final IProgram e = Factory.PROGRAM.createProgram(facts, rules, queries);
		
		System.out.println("--- input ---");
		for (final IRule rule : e.getRules()) {
			System.out.println(rule);
		}

		System.out.println("--- facts ---");
		System.out.println(e.getPredicates());
		for (final IPredicate pred : e.getPredicates()) {
			System.out.printf("%s -> %s\n", pred.getPredicateSymbol(), e
					.getFacts(pred));
			for (ITuple t : e.getFacts(pred)) {
				System.out.println(pred.getPredicateSymbol() + t);
			}
		}
		System.out.println("--- computing ---");
		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new Executor(e, method);
		exec.execute();
		ProgramTest.printResults(exec.computeSubstitutions());
	}

	private static ITuple createStringTuple(final String... s) {
		if ((s == null) || Arrays.asList(s).contains(null)) {
			throw new NullPointerException(
					"The strings must not be null or contain null");
		}
		final List<ITerm> t = new ArrayList<ITerm>(s.length);
		for (final String str : s) {
			t.add(Factory.TERM.createString(str));
		}
		return Factory.BASIC.createTuple(t);
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

