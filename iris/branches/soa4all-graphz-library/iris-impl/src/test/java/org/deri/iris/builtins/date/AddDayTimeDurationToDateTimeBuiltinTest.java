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
import org.deri.iris.builtins.AddBuiltin;
import org.deri.iris.factory.Factory;

/**
 */
public class AddDayTimeDurationToDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddDayTimeDurationToDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm dayTimeDuration = Factory.CONCRETE.createDayTimeDuration(true, 0,
				2, 0, 0, 0);
		ITerm date1 = Factory.CONCRETE.createDateTime(2010, 4, 10, 1, 12, 12,
				12, 1);
		ITerm result = Factory.CONCRETE.createDateTime(2010, 4, 10, 3, 12, 12,
				12, 1);

		AddBuiltin builtin = new AddDayTimeDurationToDateTimeBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, dayTimeDuration, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}