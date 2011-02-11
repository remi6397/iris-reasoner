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

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.concrete.IList;
import org.deri.iris.terms.concrete.IntTerm;

public class SubListFromBuiltinTest extends AbstractListBuiltinTest {

	private SubListFromBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {

		try {
			builtin = new SubListFromBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new SubListFromBuiltin(EMPTY_LIST, new IntTerm(0));
		//
		list_1 = new org.deri.iris.terms.concrete.List();
		list_2 = new org.deri.iris.terms.concrete.List();
		expected = new org.deri.iris.terms.concrete.List();

		// External( func:sublist(List(0 1 2 3 4) 0) ) = List(0 1 2 3 4)
		list_1.clear();
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1, ZERO));

		// External( func:sublist(List(0 1 2 3 4) 3) ) = List(3 4)
		expected.clear();
		expected.add(THREE);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1, THREE));

		// External( func:sublist(List(0 1 2 3 4) -2) ) = List(3 4)
		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-2)));

		// 
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(-10)));

		list_2.add(ONE);
		list_2.add(THREE);

		list_1.add(list_2);
		expected.add(list_2);

		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-3)));

	}
}
