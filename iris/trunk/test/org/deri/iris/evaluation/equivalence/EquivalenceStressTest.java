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

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.deri.iris.Configuration;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.evaluation.ProgramEvaluationTest;
import org.deri.iris.rules.IgnoreRuleHeadEquality;
import org.deri.iris.rules.RuleHeadEqualityRewriter;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.rules.safety.StandardRuleSafetyProcessor;
import org.deri.iris.utils.equivalence.IgnoreTermEquivalenceFactory;
import org.deri.iris.utils.equivalence.TermEquivalenceFactory;

/**
 * @author Adrian Marte
 */
public class EquivalenceStressTest extends ProgramEvaluationTest {

	private static final int NUM_OF_RELATIONS = 20;

	private static final int NUM_OF_FACTS_PER_RELATION = 100;

	private static final int NUM_OF_RULES = 20;

	private static final int MIN_ARITY = 1;

	private static final int MAX_ARITY = 10;

	private static final int MAX_CONSTANTS = 10;

	private static final String CONSTANT_PREFIX = "C";

	private static final String PREDICATE_PREFIX = "p";

	private static final String RULE_PREFIX = "r";

	private static final String VARIABLE_PREFIX = "?X";

	private static final double HEAD_VARIABLE_PROBABILITY = 0.3;

	private static final int NUM_OF_EQUALITY_RULES = 2;

	public EquivalenceStressTest(String name) {
		super(name);
	}

	@Override
	protected Collection<String> createExpressions() {
		List<String> expressions = new LinkedList<String>();

		Map<String, Integer> arities = new HashMap<String, Integer>();

		Random random = new Random();

		// Create facts.
		for (int i = 1; i <= NUM_OF_RELATIONS; i++) {
			int arity = random.nextInt(MAX_ARITY + 1) + MIN_ARITY;
			String predicate = PREDICATE_PREFIX + i;
			arities.put(predicate, arity);

			for (int k = 1; k <= NUM_OF_FACTS_PER_RELATION; k++) {
				StringBuffer buffer = new StringBuffer();
				buffer.append(predicate + "(");

				for (int j = 1; j <= arity; j++) {
					if (j > 1) {
						buffer.append(", ");
					}

					int index = random.nextInt(MAX_CONSTANTS) + 1;

					buffer.append("'");
					buffer.append(CONSTANT_PREFIX + index);
					buffer.append("'");
				}

				buffer.append(").");

				expressions.add(buffer.toString());
			}
		}

		// Create rules.
		for (int i = 1; i <= NUM_OF_RULES; i++) {
			int numOfLiterals = random.nextInt(NUM_OF_RELATIONS) + 1;

			StringBuffer bodyBuffer = new StringBuffer();

			List<String> variablesInHead = new LinkedList<String>();

			for (int j = 1; j <= numOfLiterals; j++) {
				if (j > 1) {
					bodyBuffer.append(", ");
				}

				String predicate = PREDICATE_PREFIX + j;
				int arity = arities.get(predicate);

				bodyBuffer.append(predicate + "(");

				for (int k = 1; k <= arity; k++) {
					if (k > 1) {
						bodyBuffer.append(", ");
					}

					int rnd = random.nextInt(arity);
					String variable = VARIABLE_PREFIX + Math.max(1, k - rnd);

					// Do random joins.
					bodyBuffer.append(variable);

					if (random.nextDouble() < HEAD_VARIABLE_PROBABILITY
							|| (variablesInHead.size() == 0 && k == arity)) {
						variablesInHead.add(variable);
					}
				}

				bodyBuffer.append(")");
			}

			StringBuffer headBuffer = new StringBuffer();
			String predicate = RULE_PREFIX + i;
			headBuffer.append(predicate + "(");
			arities.put(predicate, variablesInHead.size());

			int n = 1;
			for (String variable : variablesInHead) {
				if (n++ > 1) {
					headBuffer.append(", ");
				}

				headBuffer.append(variable);
			}

			headBuffer.append(") :- ");

			expressions
					.add(headBuffer.toString() + bodyBuffer.toString() + ".");
		}

		// Create equality rules.
		for (int i = 1; i <= NUM_OF_EQUALITY_RULES; i++) {
			int numOfLiterals = random.nextInt(arities.size()) + 1;

			StringBuffer bodyBuffer = new StringBuffer();

			List<String> variablesInHead = new LinkedList<String>();

			for (int j = 1; j <= numOfLiterals; j++) {
				if (j > 1) {
					bodyBuffer.append(", ");
				}

				String predicate;

				// Use rule.
				if (random.nextDouble() < 0.5) {
					int index = random.nextInt(NUM_OF_RULES) + 1;
					predicate = RULE_PREFIX + index;
				}
				// Use relation.
				else {
					int index = random.nextInt(NUM_OF_RELATIONS) + 1;
					predicate = PREDICATE_PREFIX + index;
				}

				int arity = arities.get(predicate);
				bodyBuffer.append(predicate + "(");

				for (int k = 1; k <= arity; k++) {
					if (k > 1) {
						bodyBuffer.append(", ");
					}

					int rnd = random.nextInt(arity);
					String variable = VARIABLE_PREFIX + Math.max(1, k - rnd);

					// Do random joins.
					bodyBuffer.append(variable);

					if (!variablesInHead.contains(variable)
							&& random.nextDouble() < HEAD_VARIABLE_PROBABILITY
							&& variablesInHead.size() < 2) {
						variablesInHead.add(variable);
					}
				}

				bodyBuffer.append(")");
			}

			StringBuffer headBuffer = new StringBuffer();

			int n = 1;
			for (String variable : variablesInHead) {
				if (n++ > 1) {
					headBuffer.append(" = ");
				}

				headBuffer.append(variable);
			}

			headBuffer.append(" :- ");

			expressions
					.add(headBuffer.toString() + bodyBuffer.toString() + ".");
		}

		// Create query.
		String predicate;

		// Use rule.
		if (random.nextDouble() < 0.5) {
			int index = random.nextInt(NUM_OF_RULES) + 1;
			predicate = RULE_PREFIX + index;
		}
		// Use relation.
		else {
			int index = random.nextInt(NUM_OF_RELATIONS) + 1;
			predicate = PREDICATE_PREFIX + index;
		}

		int arity = arities.get(predicate);
		StringBuffer buffer = new StringBuffer();
		buffer.append("?- " + predicate + "(");

		for (int i = 1; i <= arity; i++) {
			if (i > 1) {
				buffer.append(", ");
			}

			buffer.append(VARIABLE_PREFIX + i);
		}

		buffer.append(").");

		expressions.add(buffer.toString());

		return expressions;
	}

	public void testQuery1() throws Exception {
		executeQuery(queries.get(0));
	}

	private void executeQuery(IQuery query) throws Exception {
		String queryString = query.toString();
		Configuration config = new Configuration();

		// Use rewriting technique.
		config.equivalentTermsFactory = new IgnoreTermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new RuleHeadEqualityRewriter();
		config.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

		evaluate(queryString, config);
		System.out.println("Rewriter: " + getDuration());

		// Use integrated rule head equality support.
		config.equivalentTermsFactory = new TermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new IgnoreRuleHeadEquality();
		config.ruleSafetyProcessor = new StandardRuleSafetyProcessor();

		evaluate(queryString, config);
		System.out.println("Integrated: " + getDuration());
	}

}
