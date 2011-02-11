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
public class DayTimeDurationLessBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationLessBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 15, 23, 19, 12.0);
		ITerm date2 = Factory.CONCRETE.createDayTimeDuration(true, 15, 23, 19, 12.0);
		ITerm date3 = Factory.CONCRETE.createDayTimeDuration(true, 10, 3, 2, 0.9);
		ITerm date4 = Factory.CONCRETE.createDayTimeDuration(false, 4, 9, 56, 47.3);
		ITerm date5 = Factory.CONCRETE.createDayTimeDuration(false, 20, 19, 6, 3.3);
		ITerm date6 = Factory.CONCRETE.createDayTimeDuration(false, 0, 0, 0, 0);
		ITerm date7 = Factory.CONCRETE.createDayTimeDuration(false, 0, 0, 0, 0);

		DayTimeDurationLessBuiltin builtin = new DayTimeDurationLessBuiltin(X,Y);
		args = Factory.BASIC.createTuple(date1, date1);
		actual = builtin.evaluate(args);
		// (date1 = date1) -> null
		assertEquals(null, actual);

		builtin = new DayTimeDurationLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);
		// (date1 = date2) -> null
		assertEquals(null, actual);

		builtin = new DayTimeDurationLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date3, date4);
		actual = builtin.evaluate(args);
		// (date3 > date4) -> null
		assertEquals(null, actual);

		builtin = new DayTimeDurationLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date3);
		actual = builtin.evaluate(args);
		// (date4 < date3) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DayTimeDurationLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date5);
		actual = builtin.evaluate(args);
		// (date4 < date5) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DayTimeDurationLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date5, date4);
		actual = builtin.evaluate(args);
		// (date5 > date4) -> null
		assertEquals(null, actual);

		builtin = new DayTimeDurationLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date6, date7);
		actual = builtin.evaluate(args);
		// (date6 = date7) -> null
		assertEquals(null, actual);
	}

}