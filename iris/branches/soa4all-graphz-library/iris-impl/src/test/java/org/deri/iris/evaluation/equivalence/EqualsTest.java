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
public class EqualsTest extends ProgramEvaluationTest {

	public EqualsTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("a('A').");
		expressions.add("b('B').");

		// Create rules.
		expressions.add("?X = ?Y :- a(?X), b(?Y), ?X = 'A', ?Y = 'B'.");
		expressions.add("?X = ?Y :- b(?Y), ?X = 'A', ?Y = 'B'.");
		
		expressions.add("'Peter' = 'Peter_Griffin' :- .");
		expressions.add("unsat('foo') :- 'Peter' = 'Peter_Griffin'.");

		return expressions;
	}

	public void testA() throws Exception {
		// The result should be: A, B

		IRelation relation = evaluate("?- a(?X).");

		assertTrue("A not in relation.", relation.contains(Helper
				.createConstantTuple("A")));
		assertTrue("B not in relation.", relation.contains(Helper
				.createConstantTuple("B")));
		
		assertEquals("Relation does not have correct size", 2, relation.size());
	}

	public void testB() throws Exception {
		// The result should be: A, B

		IRelation relation = evaluate("?- b(?X).");

		assertTrue("A not in relation.", relation.contains(Helper
				.createConstantTuple("A")));
		assertTrue("B not in relation.", relation.contains(Helper
				.createConstantTuple("B")));
		
		assertEquals("Relation does not have correct size", 2, relation.size());
	}
	
	public void testUnsat() throws Exception {
		// The result should be: foo

		IRelation relation = evaluate("?- unsat(?X).");

		assertTrue("foo not in relation.", relation.contains(Helper
				.createConstantTuple("foo")));
		
		assertEquals("Relation does not have correct size", 1, relation.size());
	}

}
