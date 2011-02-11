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
package org.deri.iris.rules.stratification;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.compiler.ParserException;
import org.deri.iris.rules.IRuleStratifier;

/**
 * A test for correct stratificiation of rules with head equality.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualityStratificationTest extends TestCase {
	
	private Parser parser;
	
	private List<IRule> rules;
	
 	public RuleHeadEqualityStratificationTest(String name) {
		super(name);
	}
	
	public void setUp() {
		parser = new Parser();
		rules = new ArrayList<IRule>();
	}
	
	public void testSuccessful() throws ParserException {
		String program = "";
		program += "q(?X) :- r(?X), t(?X).";
		program += "s(?X, ?Y) :- t(?X, ?Y), q(?X).";
		program += "?X = ?Y :- s(?X, ?Y).";

		parser.parse(program);
		rules = parser.getRules();
		
		stratify(rules, true);
	}
	
	public void testUnsuccessful() throws ParserException {
		String program = "";
		program += "q(?X) :- r(?X), t(?X).";
		program += "s(?X, ?Y) :- t(?X, ?Y), not q(?X).";
		program += "?X = ?Y :- s(?X, ?Y).";

		parser.parse(program);
		rules = parser.getRules();
		
		stratify(rules, false);
	}
	
	public void testNegatedLiteral() throws ParserException {
		String program = "?X = ?Y :- not c(?X), a(?X), b(?Y).";

		parser.parse(program);
		rules = parser.getRules();
		
		stratify(rules, false);
	}

	public static void stratify(List<IRule> rules, boolean succeeds) {
		IRuleStratifier stratifier = new LocalStratifier(true);
		List<List<IRule>> result = stratifier.stratify(rules);

		if (succeeds) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}

		stratifier = new LocalStratifier(false);
		result = stratifier.stratify(rules);

		if (succeeds) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}

		stratifier = new GlobalStratifier();
		result = stratifier.stratify(rules);

		if (succeeds) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}
	
}
