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

public class MakeListBuiltinTest extends AbstractListBuiltinTest {

	private MakeListBuiltin builtin;

	private IList list_1, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new MakeListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new MakeListBuiltin(EMPTY_LIST, EMPTY_LIST);

		// External( func:make-list() ) = List()
		list_1 = new org.deri.iris.terms.concrete.List();
		expected = new org.deri.iris.terms.concrete.List();

		assertEquals(expected, builtin.computeResult());

		list_1.clear();
		expected.clear();
		expected.add(EMPTY_LIST);

		assertEquals(expected, builtin.computeResult(EMPTY_LIST));

		// External( func:make-list(0 1 List(20 21))) = List(0 1 List(20 21))
		list_1.clear();
		list_1.add(new IntTerm(20));
		list_1.add(new IntTerm(21));
		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(list_1);

		assertEquals(expected, builtin.computeResult(ZERO, ONE, list_1));
	}

	public void testTupleBuiltin() throws EvaluationException {
		expected = new org.deri.iris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(TWO);
		expected.add(THREE);

		check(expected, ONE, TWO, TWO, THREE);
	}

	private void check(ITerm expectedResult, ITerm... values)
			throws EvaluationException {
		builtin = new MakeListBuiltin(values);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
