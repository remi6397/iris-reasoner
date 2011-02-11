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
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 */
public class DateGreaterEqualBuiltinTest extends AbstractDateBuiltinTest {

	public DateGreaterEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm date2 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm date3 = Factory.CONCRETE.createDate(2010, 5, 26);
		ITerm date4 = Factory.CONCRETE.createDate(1997, 3, 12);

		DateGreaterEqualBuiltin builtin = new DateGreaterEqualBuiltin(X, Y);

		ITuple args = Factory.BASIC.createTuple(date1, date2);
		ITuple actual = builtin.evaluate(args);
		// (date1 = date2) -> null
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DateGreaterEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date3, date4);
		actual = builtin.evaluate(args);
		// (date3 > date4) -> iTuple()
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DateGreaterEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date3);
		actual = builtin.evaluate(args);
		// (date4 < date3) -> null
		assertEquals(null, actual);
	}

}
