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

public class ReverseBuiltinTest extends AbstractListBuiltinTest {

	private ReverseBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {

		try {
			builtin = new ReverseBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new ReverseBuiltin(EMPTY_LIST);
		//
		list_1 = new org.deri.iris.terms.concrete.List();
		list_2 = new org.deri.iris.terms.concrete.List();
		expected = new org.deri.iris.terms.concrete.List();

		// External( func:reverse(List()) ) = List()
		assertEquals(EMPTY_LIST, builtin.computeResult(EMPTY_LIST));

		// External( func:reverse(List(1)) ) = List(1)
		list_1.add(ONE);
		expected.add(ONE);
		assertEquals(expected, builtin.computeResult(list_1));

		// External( func:reverse(List(0 1 2 3 4)) ) = List(4 3 2 1 0)
		list_1.clear();
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.clear();
		expected.add(FOUR);
		expected.add(THREE);
		expected.add(TWO);
		expected.add(ONE);
		expected.add(ZERO);

		list_2.clear();
		list_2.addAll(list_1);

		assertEquals(list_1, list_2);
		assertEquals(expected, builtin.computeResult(list_1));
		assertEquals(list_1, list_2);

		// 
		list_1.add(list_2);
		expected.clear();
		expected.add(list_2);
		expected.add(FOUR);
		expected.add(THREE);
		expected.add(TWO);
		expected.add(ONE);
		expected.add(ZERO);
		assertEquals(expected, builtin.computeResult(list_1));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new org.deri.iris.terms.concrete.List();
		list_1.add(FOUR);
		list_1.add(THREE);
		list_1.add(TWO);
		list_1.add(ONE);

		expected = new org.deri.iris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		check(list_1, expected);
	}

	private void check(ITerm listOne, ITerm expectedResult)
			throws EvaluationException {
		builtin = new ReverseBuiltin(listOne);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
