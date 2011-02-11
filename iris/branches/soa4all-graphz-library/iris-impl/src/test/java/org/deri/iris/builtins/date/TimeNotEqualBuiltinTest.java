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
public class TimeNotEqualBuiltinTest extends AbstractDateBuiltinTest {

	public TimeNotEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm time1 = Factory.CONCRETE.createTime(20, 12, 43.5, 0, 0);
		ITerm time2 = Factory.CONCRETE.createTime(20, 12, 43.5, 0, 0);
		ITerm time3 = Factory.CONCRETE.createTime(6, 34, 20.8, 0, 0);

		// time1 = time1
		TimeNotEqualBuiltin builtin = new TimeNotEqualBuiltin(time1, time1);
		args = Factory.BASIC.createTuple(X, Y);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		// time1 = time2
		builtin = new TimeNotEqualBuiltin(time1, time2);
		args = Factory.BASIC.createTuple(X, Y);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		// time1 != time3
		builtin = new TimeNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(time1, time3);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}

}
