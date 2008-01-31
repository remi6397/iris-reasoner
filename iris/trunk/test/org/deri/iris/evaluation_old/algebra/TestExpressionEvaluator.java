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
package org.deri.iris.evaluation_old.algebra;


import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.TERM;
import static org.deri.iris.factory.Factory.CONCRETE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.Executor;
import org.deri.iris.NaiveExecutor;
import org.deri.iris.api.IExecutor;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.evaluation_old.algebra.IExpressionEvaluator;
import org.deri.iris.api.storage_old.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation_old.algebra.ExpressionEvaluator;
import org.deri.iris.factory.Factory;
import org.deri.iris.parser.ProgramTest;

/**
 */
public class TestExpressionEvaluator {

	public static void main(final String[] arg) throws Exception
	{
		// Rule
		// p( X, Y, Z ) :- q( X, Y ) & r( Y, Z ) & not s( X, Z )
		Set<IRule> rules = new HashSet<IRule>(3);
		List<ILiteral> headPredicates = Arrays.asList(createLiteral( "p", "X", "Y", "Z" ) );
		
		List<ILiteral> bodyPredicates = Arrays.asList(
						createLiteral("q", "X", "Y"),
						createLiteral("r", "Y", "Z"),
						BASIC.createLiteral(false,
										BASIC.createPredicate("s", 2),
										BASIC.createTuple(TERM.createVariable("X"), TERM.createVariable("Z")))
										);

		IRule rule = BASIC.createRule(headPredicates, bodyPredicates);
		rules.add(rule);

		// Create facts

		/////////////
		Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
		// r(1), r(2)
		IPredicate q = BASIC.createPredicate("q", 2);
		IMixedDatatypeRelation relq = RELATION.getMixedRelation(2);
		IPredicate r = BASIC.createPredicate("r", 2);
		IMixedDatatypeRelation relr = RELATION.getMixedRelation(2);
		IPredicate s = BASIC.createPredicate("s", 2);
		IMixedDatatypeRelation rels = RELATION.getMixedRelation(2);

		int v = 0;
		final int count = 10000;
		for( int i = 0; i < count; ++i, ++v )
		{
			relq.add( createIntegerTuple(v, v+1) );
			relr.add( createIntegerTuple(v+1, v) );
			rels.add( createIntegerTuple(v*2, v*2) );
		}
		
		facts.put(q, relq);
		facts.put(r, relr);
		facts.put(s, rels);

		// ?- q(x)
		IQuery query = BASIC.createQuery(createLiteral("p", "X", "Y", "Z"));
		
		Set<IQuery> queries = new HashSet<IQuery>();
		queries.add(query);
		
		final IProgram e = Factory.PROGRAM.createProgram(facts, rules, queries);

		System.out.println("--- input ---");
		for (final IRule rul : e.getRules()) {
			System.out.println(rul);
		}

		IExpressionEvaluator method = new ExpressionEvaluator();
		IExecutor exec = new NaiveExecutor(e, method);
		exec.execute();
		
//		Map<IPredicate,IMixedDatatypeRelation> m = exec.computeSubstitutions();
//		System.out.println("*****" + "RESULT: " + "*****");
//		ProgramTest.printResults(m);
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

	private static ITuple createIntegerTuple(final int... ints) {
		if ((ints == null) || Arrays.asList(ints).contains(null)) {
			throw new NullPointerException(
					"The strings must not be null or contain null");
		}
		final List<ITerm> t = new ArrayList<ITerm>(ints.length);
		for (final int i : ints) {
			t.add(CONCRETE.createInteger( i));
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
