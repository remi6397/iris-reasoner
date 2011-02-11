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
public class DayTimeDurationSubtractBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm daytimeduration1 = Factory.CONCRETE.createDayTimeDuration(true,
				15, 23, 19, 12.0);
		ITerm daytimeduration2 = Factory.CONCRETE.createDayTimeDuration(true,
				14, 22, 18, 11.0);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 1, 1, 1,
				1.0);

		DayTimeDurationSubtractBuiltin builtin = new DayTimeDurationSubtractBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(daytimeduration1, daytimeduration2,
				result);
		actual = builtin.evaluate(args);
		
		assertEquals(EMPTY_TUPLE, actual);

	

	}
}