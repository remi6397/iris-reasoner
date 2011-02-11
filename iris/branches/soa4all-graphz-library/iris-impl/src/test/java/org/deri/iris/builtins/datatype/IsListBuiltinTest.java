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
package org.deri.iris.builtins.datatype;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.builtins.datatype.IsListBuiltin;
import org.deri.iris.builtins.list.AbstractListBuiltinTest;

public class IsListBuiltinTest extends AbstractListBuiltinTest {

	private IsListBuiltin builtin;

	private IList list_1, list_2;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new IsListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new IsListBuiltin(EMPTY_LIST);
		ITerm[] terms = {EMPTY_LIST, null};
		
		assertEquals(true, builtin.computeResult(terms));
		list_1 = new org.deri.iris.terms.concrete.List();
		list_2 = new org.deri.iris.terms.concrete.List();

		// External(pred:is-list(1)) will evaluate to f in any interpretation.
		terms[0] = ONE;
		assertEquals(false, builtin.computeResult(terms));
		terms[1] = list_1;
		assertEquals(false, builtin.computeResult(terms));

		list_1.add(ONE);
		assertFalse(list_1.isEmpty());
		terms[0] = list_1;
		terms[1] = null;
		assertEquals(true, builtin.computeResult(terms));

		list_2.add(ONE);
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(list_1);
		terms[0] = list_2;
		terms[1] = null;

		ITerm[] terms2 = {list_2};
		assertEquals(true, builtin.computeResult(terms));
		assertFalse(list_2.equals(list_1));
		assertEquals(builtin.computeResult(terms), builtin
				.computeResult(terms2));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new org.deri.iris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);

		check(list_1, true);
	}

	private void check(ITerm listOne, boolean expected)
			throws EvaluationException {
		builtin = new IsListBuiltin(listOne);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple actualTuple = builtin.evaluate(arguments);

		if (expected) {
			assertNotNull(actualTuple);
		} else {
			assertNull(actualTuple);
		}
	}
}
