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

public class CountListBuiltinTest extends AbstractListBuiltinTest {

	private CountListBuiltin builtin;

	private IList list_1, list_2, list_3;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new CountListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new CountListBuiltin(EMPTY_LIST);
		assertEquals(new IntTerm(0), builtin.computeResult(EMPTY_LIST));

		list_1 = new org.deri.iris.terms.concrete.List();
		list_1.add(ONE);
		assertFalse(list_1.isEmpty());
		assertEquals(new IntTerm(1), builtin.computeResult(list_1));

		list_2 = new org.deri.iris.terms.concrete.List();
		list_2.add(ONE);
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(list_1);

		assertEquals(new IntTerm(4), builtin.computeResult(list_2));

		list_3 = new org.deri.iris.terms.concrete.List();
		list_3.add(list_1);
		list_3.add(ONE);
		list_3.add(TWO);
		list_3.add(THREE);

		assertFalse(list_2.equals(list_3));
		assertEquals(builtin.computeResult(list_2), builtin
				.computeResult(list_3));

	}
	
	public void testTupleCount() throws EvaluationException {
		list_1 = new org.deri.iris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		
		IntTerm expected = new IntTerm(2);
		
		check(list_1, expected);
	}
	
	
	private void check(ITerm listOne, ITerm expectedResult) throws EvaluationException {
		builtin = new CountListBuiltin(listOne);
		
		ITuple arguments = BASIC.createTuple(X, Y, Z);
		
		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
