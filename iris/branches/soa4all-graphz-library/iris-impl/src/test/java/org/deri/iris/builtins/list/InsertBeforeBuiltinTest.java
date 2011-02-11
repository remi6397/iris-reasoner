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
package org.deri.iris.builtins.list;

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.terms.concrete.IntTerm;

public class InsertBeforeBuiltinTest extends AbstractListBuiltinTest {

	private InsertBeforeBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new InsertBeforeBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new InsertBeforeBuiltin(EMPTY_LIST, EMPTY_LIST, EMPTY_LIST);

		list_1 = new org.deri.iris.terms.concrete.List();
		list_2 = new org.deri.iris.terms.concrete.List();
		expected = new org.deri.iris.terms.concrete.List();

		// External( func:insert-before(List(0 1 2 3 4) 0 99) ) = List(99 0 1 2
		// 3 4)
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.add(new IntTerm(99));
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1, ZERO, new IntTerm(
				99)));

		// External( func:insert-before(List(0 1 2 3 4) 1 99) ) = List(0 99 1 2
		// 3 4)
		expected.clear();
		expected.add(ZERO);
		expected.add(new IntTerm(99));
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1, ONE, new IntTerm(
				99)));

		// External( func:insert-before(List(0 1 2 3 4) 5 99) ) = (unspecified)
		expected.clear();
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(5),
				new IntTerm(99)));

		// External( func:insert-before(List(0 1 2 3 4) -5 99) )= List(99 0 1 2
		// 3 4)
		expected.clear();
		expected.add(new IntTerm(99));
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-5),
				new IntTerm(99)));

		// External( func:insert-before(List(0 1 2 3 4) -10 99) ) =
		// (unspecified)
		expected.clear();
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(-10),
				new IntTerm(99)));

		list_2.clear();
		list_2.add(ONE);
		expected.clear();
		expected.add(ZERO);
		expected.add(list_2);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, ONE, list_2));

	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new org.deri.iris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);

		list_2 = new org.deri.iris.terms.concrete.List();
		list_2.add(ONE);

		expected = new org.deri.iris.terms.concrete.List();
		expected.add(ONE);
		expected.add(list_2);
		expected.add(TWO);
		expected.add(TWO);
		expected.add(THREE);

		check(list_1, ONE, list_2, expected);
	}

	private void check(ITerm listOne, ITerm term2, ITerm term3,
			ITerm expectedResult) throws EvaluationException {
		builtin = new InsertBeforeBuiltin(listOne, term2, term3);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
