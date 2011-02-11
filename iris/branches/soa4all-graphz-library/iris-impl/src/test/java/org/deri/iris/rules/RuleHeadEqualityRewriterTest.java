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
package org.deri.iris.rules;

import java.util.Collection;

import junit.framework.TestCase;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;

/**
 * Test for EquivalenceRuleRewriter.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualityRewriterTest extends TestCase {

	private RuleHeadEqualityRewriter rewriter;

	public RuleHeadEqualityRewriterTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		rewriter = new RuleHeadEqualityRewriter(false, true);
	}

	// Just a simple test, to test if enough rules are created.
	public void testRewrite() throws ParserException {
		String program = "q(?X, ?Y, ?Z) :- p(?X, ?Y), r(?Z).";

		Parser parser = new Parser();
		parser.parse(program);

		Collection<IRule> rules = parser.getRules();
		Collection<IRule> newRules = rewriter.rewrite(rules);

		assertEquals("Incorrect number of rules created.", 10, newRules.size());
	}

}
