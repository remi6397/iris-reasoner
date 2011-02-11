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
package org.deri.iris.builtins.date;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 */
public class DurationEqualBuiltinTest extends AbstractDateBuiltinTest {

	public DurationEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm duration1 = Factory.CONCRETE.createDuration(true, 1988, 5, 12, 6,
				2, 11);
		ITerm duration2 = Factory.CONCRETE.createDuration(true, 1988, 5, 12, 6,
				2, 11);
		ITerm duration3 = Factory.CONCRETE.createDuration(true, 1979, 10, 28,
				12, 56, 23);
		ITerm duration4 = Factory.CONCRETE.createDuration(false, 1991, 1, 8,
				12, 56, 23);

		DurationEqualBuiltin builtin = new DurationEqualBuiltin(X,Y);
		args = Factory.BASIC.createTuple(duration1, duration1);
		actual = builtin.evaluate(args);
		// duration1 = duration1
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DurationEqualBuiltin(X,Y);
		args = Factory.BASIC.createTuple(duration1, duration2);
		actual = builtin.evaluate(args);
		// duration1 = duration2
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DurationEqualBuiltin(X,Y);
		args = Factory.BASIC.createTuple(duration4, duration3);
		actual = builtin.evaluate(args);
		// duration1 > duration3
		assertEquals(null, actual);

		builtin = new DurationEqualBuiltin(duration3, duration4);
		args = Factory.BASIC.createTuple(X);
		actual = builtin.evaluate(args);
		// duration3 < duration4
		assertEquals(null, actual);

	}

}