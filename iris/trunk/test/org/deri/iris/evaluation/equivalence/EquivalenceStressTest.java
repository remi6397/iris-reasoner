/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2009 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package org.deri.iris.evaluation.equivalence;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.TERM;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.deri.iris.Configuration;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.evaluation.EvaluationTest;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.IgnoreRuleHeadEquality;
import org.deri.iris.rules.RuleHeadEqualityRewriter;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.equivalence.IgnoreTermEquivalenceFactory;
import org.deri.iris.utils.equivalence.TermEquivalenceFactory;

/**
 * Test that compares the evaluation of a set of rules containing rules with
 * head equality using different evaluation techniques.
 * 
 * @author Adrian Marte
 */
public class EquivalenceStressTest extends EvaluationTest {

	private static final int NUM_OF_RELATIONS = 20;

	private static final int NUM_OF_FACTS_PER_RELATION = 100;

	private static final int NUM_OF_RULES = 200;

	private static final int MIN_ARITY = 1;

	private static final int MAX_ARITY = 20;

	private static final int MAX_CONSTANTS = 100;

	private static final String CONSTANT_PREFIX = "C";

	private static final String RELATION_PREFIX = "p";

	private static final String RULE_PREFIX = "r";

	private static final String VARIABLE_PREFIX = "X";

	private static final int NUM_OF_EQUALITY_RULES = 2;

	private Random random;

	private List<IPredicate> relationPredicates;

	private List<IPredicate> rulePredicates;

	private List<IVariable> variables;

	public EquivalenceStressTest(String name) {
		super(name);

	}

	@Override
	protected void setUp() throws Exception {
		random = new Random();
		createPredicates();
		createVariables();

		super.setUp();
	}

	private void createVariables() {
		variables = new ArrayList<IVariable>();

		for (int i = 0; i < MAX_ARITY; i++) {
			String name = VARIABLE_PREFIX + i;
			IVariable variable = TERM.createVariable(name);
			variables.add(variable);
		}
	}

	private void createPredicates() {
		relationPredicates = new ArrayList<IPredicate>();
		rulePredicates = new ArrayList<IPredicate>();

		// Create fact predicates.
		for (int i = 1; i <= NUM_OF_RELATIONS; i++) {
			String symbol = RELATION_PREFIX + i;
			IPredicate predicate = createPredicate(symbol);
			relationPredicates.add(predicate);
		}

		// Create rule predicates.
		for (int i = 1; i <= NUM_OF_RULES; i++) {
			String symbol = RULE_PREFIX + i;
			IPredicate predicate = createPredicate(symbol);
			rulePredicates.add(predicate);
		}
	}

	private IPredicate createPredicate(String symbol) {
		int arity = random.nextInt(MAX_ARITY - MIN_ARITY + 1) + MIN_ARITY;
		IPredicate predicate = BASIC.createPredicate(symbol, arity);
		return predicate;
	}

	private <T> T pickRandom(List<T> list) {
		int index = random.nextInt(list.size());
		return list.get(index);
	}

	@Override
	protected IFacts createFacts() {
		IFacts facts = new Facts(defaultConfiguration.relationFactory);

		// Create facts.
		for (int i = 0; i < NUM_OF_RELATIONS; i++) {
			IPredicate predicate = relationPredicates.get(i);

			IRelation relation = facts.get(predicate);

			for (int k = 0; k < NUM_OF_FACTS_PER_RELATION; k++) {
				List<ITerm> terms = new ArrayList<ITerm>(predicate.getArity());

				for (int j = 0; j < predicate.getArity(); j++) {
					int index = random.nextInt(MAX_CONSTANTS) + 1;
					terms.add(TERM.createString(CONSTANT_PREFIX + index));
				}

				ITuple tuple = BASIC.createTuple(terms);
				relation.add(tuple);
			}
		}

		return facts;
	}

	@Override
	protected List<IRule> createRules() {
		List<IRule> rules = new ArrayList<IRule>();

		// Create rules.
		for (int i = 0; i < NUM_OF_RULES; i++) {
			IRule rule = createRule(false, i);

			rules.add(rule);
		}

		// Create equality rules.
		for (int i = 0; i < NUM_OF_EQUALITY_RULES; i++) {
			IRule rule = createRule(true, i);

			rules.add(rule);
		}

		return rules;
	}

	protected List<IQuery> createQueries() {
		// Create query.
		IPredicate predicate;

		// Use rule.
		if (random.nextDouble() < 0.5) {
			predicate = pickRandom(rulePredicates);
		}
		// Use relation.
		else {
			predicate = pickRandom(relationPredicates);
		}

		List<ITerm> terms = new ArrayList<ITerm>(predicate.getArity());

		for (int i = 0; i < predicate.getArity(); i++) {
			terms.add(TERM.createVariable(VARIABLE_PREFIX + i));
		}

		ITuple tuple = BASIC.createTuple(terms);
		ILiteral literal = BASIC.createLiteral(true, predicate, tuple);

		IQuery query = BASIC.createQuery(literal);
		return Collections.singletonList(query);
	}

	private IRule createRule(boolean isEqualityRule, int index) {
		int numOfLiterals = random.nextInt(NUM_OF_RELATIONS) + 1;

		Set<ITerm> variableSet = new HashSet<ITerm>();
		List<ILiteral> bodyLiterals = new ArrayList<ILiteral>(numOfLiterals);

		for (int j = 0; j < numOfLiterals; j++) {
			IPredicate predicate = pickRandom(relationPredicates);

			List<ITerm> terms = new ArrayList<ITerm>(predicate.getArity());

			for (int k = 0; k < predicate.getArity(); k++) {
				IVariable variable = pickRandom(variables);

				terms.add(variable);

				variableSet.add(variable);
			}

			ITuple tuple = BASIC.createTuple(terms);
			ILiteral literal = BASIC.createLiteral(true, predicate, tuple);
			bodyLiterals.add(literal);
		}

		IPredicate predicate;

		if (isEqualityRule) {
			EqualBuiltin builtin = new EqualBuiltin(TERM.createVariable("X"),
					TERM.createVariable("Y"));
			predicate = builtin.getPredicate();
		} else {
			predicate = pickRandom(rulePredicates);
		}

		List<ITerm> shuffledVariables = new ArrayList<ITerm>(variableSet);

		while (shuffledVariables.size() < predicate.getArity()) {
			shuffledVariables.add(pickRandom(shuffledVariables));
		}

		Collections.shuffle(shuffledVariables);
		List<ITerm> headVariables = shuffledVariables.subList(0, predicate
				.getArity());

		ITuple headTuple = BASIC.createTuple(headVariables);
		ILiteral headLiteral = BASIC.createLiteral(true, predicate, headTuple);

		IRule rule = BASIC.createRule(Collections.singletonList(headLiteral),
				bodyLiterals);

		return rule;
	}

	public void testQuery1() throws Exception {
		executeQuery(queries.get(0));
	}

	private void executeQuery(IQuery query) throws Exception {
		Configuration config = new Configuration();

		// Use rewriting technique.
		config.equivalentTermsFactory = new IgnoreTermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new RuleHeadEqualityRewriter();
		config.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

		evaluate(query, config);
		System.out.println("Rewriter: " + getDuration());
		
		config = new Configuration();

		// Use integrated rule head equality support.
		config.equivalentTermsFactory = new TermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new IgnoreRuleHeadEquality();

		evaluate(query, config);
		System.out.println("Integrated: " + getDuration());
	}

}
