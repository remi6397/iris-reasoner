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
public class DayTimeDurationMultiplyBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationMultiplyBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm daytimeduration1 = Factory.CONCRETE.createDayTimeDuration(true, 0, 2, 10, 0.0);
		ITerm double1 = Factory.CONCRETE.createDouble(2.1);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 0, 4, 33, 0.0);

		DayTimeDurationMultiplyBuiltin builtin = new DayTimeDurationMultiplyBuiltin(X, Y, Z);
		
		args = Factory.BASIC.createTuple(daytimeduration1, double1, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}