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

import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for HourPartBuiltin.
 */
public class HourPartBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public HourPartBuiltinTest(String name) {
		super(name);
	}

	public void testHoursFromTime() throws EvaluationException {
		ITerm time = Factory.CONCRETE.createTime(8, 53, 23.5, 0, 0);
		check(8, time);

		time = Factory.CONCRETE.createTime(24, 0, 0.0, 0, 0);
		check(0, time);
	}

	public void testHoursFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 24, 0,
				00, 0, 0);
		check(0, dateTime);
	}

	public void testHoursFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 10, 0,
				0);
		check(10, duration);

		duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 12, 32, 12);
		check(12, duration);

		duration = Factory.CONCRETE.createDuration(true, 0, 0, 0, 123, 0, 0);
		check(3, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 0, 3, 10, 0, 0);
		check(-10, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		HourPartBuiltin builtin = new HourPartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
