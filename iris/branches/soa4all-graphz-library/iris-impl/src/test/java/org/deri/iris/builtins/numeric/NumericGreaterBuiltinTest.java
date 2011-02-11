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
package org.deri.iris.builtins.numeric;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 */
public class NumericGreaterBuiltinTest extends AbstractNumericTest {

	public NumericGreaterBuiltinTest(String name) {
		super(name);
	}
	
	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(23.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(23.4);

		NumericGreaterBuiltin builtin = new NumericGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
//		t1 = t2 equal 
		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createDecimal(15.5);
		term2 = Factory.CONCRETE.createInteger(15);

		builtin = new NumericGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
		
		
		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createInteger(15);

		builtin = new NumericGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);
		
		term1 = Factory.CONCRETE.createInteger(153);
		term2 = Factory.CONCRETE.createInteger(15);

		builtin = new NumericGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
		
		
		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createInteger(123);

		builtin = new NumericGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);
	}

}
