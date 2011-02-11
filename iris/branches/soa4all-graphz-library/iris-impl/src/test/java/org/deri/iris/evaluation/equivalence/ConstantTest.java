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
package org.deri.iris.evaluation.equivalence;

import java.util.ArrayList;
import java.util.Collection;

import org.deri.iris.evaluation.ProgramEvaluationTest;
import org.deri.iris.rules.compiler.Helper;
import org.deri.iris.storage.IRelation;

/**
 * Test for correct evaluation of examples with rule head equality.
 * 
 * @author Adrian Marte
 */
public class ConstantTest extends ProgramEvaluationTest {

	public ConstantTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("p('A').");
		expressions.add("q('C').");
		expressions.add("r('C', '1', 'A').");

		// Create rules.
		expressions.add("'A' = 'B' :- q('C').");

		return expressions;
	}

	public void testP() throws Exception {
		// The result should be: a, b

		IRelation relation = evaluate("?- p(?X).");

		assertTrue("A not in relation.", relation.contains(Helper
				.createConstantTuple("A")));
		assertTrue("B not in relation.", relation.contains(Helper
				.createConstantTuple("B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}
	
	public void testR() throws Exception {
		// The result should be: (C, 1, A), (C, 1, B)

		IRelation relation = evaluate("?- r(?X, ?Y, ?Z).");

		assertTrue("(C, 1, A) not in relation.", relation.contains(Helper
				.createConstantTuple("C", "1", "A")));
		assertTrue("(C, 1, B) not in relation.", relation.contains(Helper
				.createConstantTuple("C", "1", "B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

}
