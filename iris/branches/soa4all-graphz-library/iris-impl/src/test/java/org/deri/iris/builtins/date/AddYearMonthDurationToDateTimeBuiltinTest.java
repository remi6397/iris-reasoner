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
public class AddYearMonthDurationToDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddYearMonthDurationToDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm yearMonthDuration = Factory.CONCRETE.createYearMonthDuration(true, 5,2);
		ITerm date1 = Factory.CONCRETE.createDateTime(10, 4, 2, 1, 23, 34.5, 0, 0);
		ITerm result = Factory.CONCRETE.createDateTime(15, 6, 2, 1, 23, 34.5, 0, 0);

		AddBuiltin builtin = new AddYearMonthDurationToDateTimeBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, yearMonthDuration, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}
}