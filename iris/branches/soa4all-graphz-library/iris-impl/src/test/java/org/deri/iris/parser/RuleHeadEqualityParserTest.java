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
package org.deri.iris.parser;

import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.rules.RuleHeadEquality;

/**
 * Test for correct parsing of rules with rule head equality.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualityParserTest extends TestCase {

	private Parser parser;

	private List<IRule> rules;

	public RuleHeadEqualityParserTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		parser = new Parser();

		String program = "?X = ?Y :- foo(?X), bar(?Y), ?X = ?Y.";
		program += "'A' = 'B' :- bar(?X).";
		program += "'A' = 'B' :- .";

		parser.parse(program);
		rules = parser.getRules();
	}

	public void testPredicate() {
		IRule rule = rules.get(0);

		assertTrue("Rule head equality not recognized.", RuleHeadEquality
				.hasRuleHeadEquality(rule));
		
		rule = rules.get(1);

		assertTrue("Rule head equality not recognized.", RuleHeadEquality
				.hasRuleHeadEquality(rule));
		
		rule = rules.get(2);

		assertTrue("Rule head equality not recognized.", RuleHeadEquality
				.hasRuleHeadEquality(rule));
	}

}
