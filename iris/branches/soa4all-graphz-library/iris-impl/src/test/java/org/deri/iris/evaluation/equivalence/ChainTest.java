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
public class ChainTest extends ProgramEvaluationTest {

	public ChainTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("a('A').");
		expressions.add("b('B').");
		expressions.add("c('C').");
		expressions.add("d('D').");

		expressions.add("foo('B').");
		expressions.add("baz('C').");

		// Create rules.
		expressions.add("?X = ?Y :- a(?X), b(?Y).");
		expressions.add("?X = ?Y :- b(?X), c(?Y).");
		expressions.add("?X = ?Y :- c(?X), d(?Y).");

		expressions.add("bar(?X) :- foo(?X), baz(?X), ?X = 'A'.");

		return expressions;
	}

	public void testFoo() throws Exception {
		// The result should be: A, B, C, D

		IRelation relation = evaluate("?- foo(?X).");
		
		assertTrue("A not in relation.", relation.contains(Helper
				.createConstantTuple("A")));
		assertTrue("B not in relation.", relation.contains(Helper
				.createConstantTuple("B")));
		assertTrue("C not in relation.", relation.contains(Helper
				.createConstantTuple("C")));
		assertTrue("D not in relation.", relation.contains(Helper
				.createConstantTuple("D")));
		
		assertEquals("Relation does not have correct size", 4, relation.size());
	}

	public void testBar() throws Exception {
		// The result should be: A, B, C, D

		IRelation relation = evaluate("?- bar(?X).");
		
		assertTrue("A not in relation.", relation.contains(Helper
				.createConstantTuple("A")));
		assertTrue("B not in relation.", relation.contains(Helper
				.createConstantTuple("B")));
		assertTrue("C not in relation.", relation.contains(Helper
				.createConstantTuple("C")));
		assertTrue("D not in relation.", relation.contains(Helper
				.createConstantTuple("D")));
		
		assertEquals("Relation does not have correct size", 4, relation.size());
	}

}
