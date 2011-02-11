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
public class AddDayTimeDurationToTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddDayTimeDurationToTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		
		ITerm daytimeduration = Factory.CONCRETE.createDayTimeDuration(true, 0, 2, 0, 0, 0);
		ITerm time1 = Factory.CONCRETE.createTime(10, 4, 26.3, 0, 0);
		ITerm result = Factory.CONCRETE.createTime(12, 4, 26.3, 0, 0);

		AddBuiltin builtin = new AddDayTimeDurationToTimeBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(time1, daytimeduration, result);
		actual = builtin.evaluate(args);
		
		assertEquals(EMPTY_TUPLE, actual);

	}

}