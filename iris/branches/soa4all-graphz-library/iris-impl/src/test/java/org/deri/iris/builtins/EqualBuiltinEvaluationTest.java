/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.BASIC;
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

import org.deri.iris.KnowledgeBaseFactory;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.simple.SimpleRelationFactory;

/**
 * <p>
 * Tests for the {@code LessBuiltin Evaluation}.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   04.05.2007 12:416:07
 */

public class EqualBuiltinEvaluationTest extends TestCase {
	public static Test suite() {
		return new TestSuite(EqualBuiltinEvaluationTest.class, EqualBuiltinEvaluationTest.class
				.getSimpleName());
	}

	public void testEvaluate0() throws Exception{
		// constructing the rules
		List<IRule> rules = new ArrayList<IRule>(3);
		
		// p(U,V,W) :- r(V,W), EQ(U, 'a').
		List<ILiteral> h = Arrays.asList(createLiteral("p", "U", "V", "W"));
		List<ILiteral> b = Arrays.asList(
				createLiteral("r", "V", "W"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
					createEqual(
						TERM.createVariable("U"),
						TERM.createString("a"))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();
		// r(b,b), r(c,c).
		IPredicate p = Factory.BASIC.createPredicate("r", 2);
		IRelation rel = new SimpleRelationFactory().createRelation();
		rel.add(BASIC.createTuple(TERM.createString("b"),TERM.createString("b")));
		rel.add(BASIC.createTuple(TERM.createString("c"),TERM.createString("c")));
		facts.put(p, rel);

		IQuery q = Factory.BASIC.createQuery(createLiteral("p", "U", "V", "W"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IKnowledgeBase pr = KnowledgeBaseFactory.createKnowledgeBase( facts, rules );
		
		// Result: p(b,b,a),p(c,c,a)
		IRelation res = new SimpleRelationFactory().createRelation();
		res.add(BASIC.createTuple(TERM.createString("a"),TERM.createString("b"),TERM.createString("b")));
		res.add(BASIC.createTuple(TERM.createString("a"),TERM.createString("c"),TERM.createString("c")));
		
		System.out.println("******** TEST 0: ********");
		ExecutionHelper.executeTest(pr, q, res);
	}
	
	public void testEvaluate1() throws Exception{
		// constructing the rules
		List<IRule> rules = new ArrayList<IRule>(3);
		
		// p(U,V,W) :- r(V,W), EQ(W, U).
		List<ILiteral> h = Arrays.asList(createLiteral("p", "U", "V", "W"));
		List<ILiteral> b = Arrays.asList(
				createLiteral("r", "V", "W"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
					createEqual(
						TERM.createVariable("W"),
						TERM.createVariable("U"))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();
		// r(b,b), r(c,c).
		IPredicate p = Factory.BASIC.createPredicate("r", 2);
		IRelation rel = new SimpleRelationFactory().createRelation();
		rel.add(BASIC.createTuple(TERM.createString("b"),TERM.createString("b")));
		rel.add(BASIC.createTuple(TERM.createString("c"),TERM.createString("c")));
		facts.put(p, rel);

		IQuery q = Factory.BASIC.createQuery(createLiteral("p", "U", "V", "W"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IKnowledgeBase pr = KnowledgeBaseFactory.createKnowledgeBase( facts, rules );
		
		// Result: p(b,b,b),p(c,c,c)
		IRelation res = new SimpleRelationFactory().createRelation();
		res.add(BASIC.createTuple(TERM.createString("b"),TERM.createString("b"),TERM.createString("b")));
		res.add(BASIC.createTuple(TERM.createString("c"),TERM.createString("c"),TERM.createString("c")));
		
		System.out.println("******** TEST 1: ********");
		ExecutionHelper.executeTest(pr, q, res);
	}
	
	public void testEvaluate2() throws Exception{
		// constructing the rules
		List<IRule> rules = new ArrayList<IRule>(3);
		
		// p(V,W) :- r(V,W), EQ('a', V).
		List<ILiteral> h = Arrays.asList(createLiteral("p", "V", "W"));
		List<ILiteral> b = Arrays.asList(
				createLiteral("r", "V", "W"),
				Factory.BASIC.createLiteral(true, Factory.BUILTIN.
					createEqual(
							TERM.createString("a"),
							TERM.createVariable("V"))));
		
		IRule r = Factory.BASIC.createRule(h, b);
		rules.add(r);

		// create facts
		Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();
		// r(a,a), r(b,b), r(c,c).
		IPredicate p = Factory.BASIC.createPredicate("r", 2);
		IRelation rel = new SimpleRelationFactory().createRelation();
		rel.add(BASIC.createTuple(TERM.createString("a"),TERM.createString("a")));
		rel.add(BASIC.createTuple(TERM.createString("b"),TERM.createString("b")));
		rel.add(BASIC.createTuple(TERM.createString("c"),TERM.createString("c")));
		facts.put(p, rel);

		IQuery q = Factory.BASIC.createQuery(createLiteral("p", "V", "W"));
		Set<IQuery> queries = new HashSet<IQuery>(1);
		queries.add(q);
		final IKnowledgeBase pr = KnowledgeBaseFactory.createKnowledgeBase( facts, rules );
		
		// Result: p(a,a)
		IRelation res = new SimpleRelationFactory().createRelation();
		res.add(BASIC.createTuple(TERM.createString("a"),TERM.createString("a")));
		
		System.out.println("******** TEST 2: ********");
		ExecutionHelper.executeTest(pr, q, res);
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

