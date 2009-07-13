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
package org.deri.iris.rules.stratification;

import java.util.List;

import junit.framework.TestCase;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.compiler.Parser;
import org.deri.iris.rules.IRuleStratifier;

/**
 * A test if stratified rules with rule head equality are identified as such.
 * 
 * @author Adrian Marte
 */
public class CorrectStratificationTest extends TestCase {

	private List<IRule> rules;

	public CorrectStratificationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		Parser parser = new Parser();

		String program = "";
		program += "q(?X) :- r(?X), t(?X).";
		program += "s(?X, ?Y) :- t(?X, ?Y), q(?X).";
		program += "?X = ?Y :- s(?X, ?Y).";

		parser.parse(program);

		rules = parser.getRules();
	}

	public void testLocalStratifier() {
		IRuleStratifier stratifier = new LocalStratifier(true);
		List<List<IRule>> result = stratifier.stratify(rules);
		assertNotNull(result);

		stratifier = new LocalStratifier(false);
		result = stratifier.stratify(rules);
		assertNotNull(result);
	}

	public void testGlobalStratifier() {
		IRuleStratifier stratifier = new GlobalStratifier();
		List<List<IRule>> result = stratifier.stratify(rules);
		assertNotNull(result);
	}

}
