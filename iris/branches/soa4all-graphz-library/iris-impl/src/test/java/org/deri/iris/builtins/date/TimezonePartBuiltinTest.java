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
 * Test for TimezonePartBuiltin.
 */
public class TimezonePartBuiltinTest extends TestCase {

	private static ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public TimezonePartBuiltinTest(String name) {
		super(name);
	}

	public void testTimezoneFromTime() throws EvaluationException {
		ITerm time = Factory.CONCRETE.createTime(8, 55, 23, -3, -30);
		ITerm duration = Factory.CONCRETE.createDuration(false, 0, 0, 0, 3, 30,
				0);
		check(duration, time);

		time = Factory.CONCRETE.createTime(8, 55, 23, 2, 0);
		duration = Factory.CONCRETE.createDuration(true, 0, 0, 0, 2, 0, 0);
		check(duration, time);
	}

	public void testTimezoneFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 20, 0,
				05, -4, -30);
		ITerm duration = Factory.CONCRETE.createDuration(false, 0, 0, 0, 4, 30,
				0);
		check(duration, dateTime);

		dateTime = Factory.CONCRETE.createDateTime(2000, 06, 12, 13, 20, 00, 0,
				0);
		duration = Factory.CONCRETE.createDuration(true, 0, 0, 0, 0, 0, 0);
		check(duration, dateTime);
	}

	public void testimezoneFromDate() throws EvaluationException {
		ITerm date = Factory.CONCRETE.createDate(1999, 5, 31, -5, 0);
		ITerm duration = Factory.CONCRETE.createDuration(false, 0, 0, 0, 5, 0,
				0);
		check(duration, date);

		date = Factory.CONCRETE.createDate(2000, 6, 12);
		duration = Factory.CONCRETE.createDuration(true, 0, 0, 0, 0, 0, 0);
		check(duration, date);
	}

	private void check(ITerm expected, ITerm time) throws EvaluationException {
		TimezonePartBuiltin builtin = new TimezonePartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(expected);
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
