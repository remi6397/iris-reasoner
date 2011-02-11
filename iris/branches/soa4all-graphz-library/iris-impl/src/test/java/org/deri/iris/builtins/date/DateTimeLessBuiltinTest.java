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
public class DateTimeLessBuiltinTest extends AbstractDateBuiltinTest {

	public DateTimeLessBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDateTime(2001, 2, 12, 14, 34, 23,
				0, 0);
		ITerm date2 = Factory.CONCRETE.createDateTime(2001, 2, 12, 14, 34, 23,
				0, 0);
		ITerm date3 = Factory.CONCRETE.createDateTime(1965, 4, 1, 10, 6, 29, 0,
				0);

		// date1 = date2
		DateTimeLessBuiltin builtin = new DateTimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		// date1 > date3
		builtin = new DateTimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date3);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		// date3 < date1
		builtin = new DateTimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date3, date1);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}