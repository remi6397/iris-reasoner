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
public class NumericNotEqualBuiltinTest extends AbstractNumericTest {

	public NumericNotEqualBuiltinTest(String name) {
		super(name);
	}
	
	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(23.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(23.4);

		NumericNotEqualBuiltin builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
		
		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createDecimal(15.0);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);
		
		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createDecimal(15.342);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
		
		term1 = Factory.CONCRETE.createInteger(132);
		term2 = Factory.CONCRETE.createInteger(3);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
		
		term1 = Factory.CONCRETE.createDecimal(23.5);
		term2 = Factory.CONCRETE.createDecimal(23.3);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}


}
