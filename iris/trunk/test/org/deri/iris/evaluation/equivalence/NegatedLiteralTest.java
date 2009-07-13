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

import java.util.ArrayList;
import java.util.Collection;

import org.deri.iris.EvaluationException;
import org.deri.iris.evaluation.ProgramEvaluationTest;

/**
 * Test for correct evaluation of examples with rule head equality.
 * 
 * @author Adrian Marte
 */
public class NegatedLiteralTest extends ProgramEvaluationTest {

	public NegatedLiteralTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("a('A').");
		expressions.add("a('D').");
		expressions.add("b('B').");
		expressions.add("c('C').");
		expressions.add("c('D').");
		expressions.add("d('A').");
		expressions.add("foo('B').");

		// Create rules.
		expressions.add("?X = ?Y :- not c(?X), a(?X), b(?Y).");

		return expressions;
	}

	public void testStratified() throws Exception {
		// The result should be: A, B

		try {
			evaluate("?- foo(?X).");
			fail("Program identified as stratified.");
		} catch (EvaluationException e) {
		}
	}

}
