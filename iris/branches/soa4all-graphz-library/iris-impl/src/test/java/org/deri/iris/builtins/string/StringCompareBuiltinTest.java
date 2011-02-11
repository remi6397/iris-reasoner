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
package org.deri.iris.builtins.string;

import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for StringCompareBuiltin.
 */
public class StringCompareBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	private static final ITerm Z = TERM.createVariable("Z");

	public StringCompareBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		check(0, "foobar", "foobar");
		check(-1, "a", "b");
		check(1, "b", "a");
	}

	private void check(int expected, String string1, String string2)
			throws EvaluationException {
		StringCompareBuiltin compare = new StringCompareBuiltin(Factory.TERM
				.createString(string1), Factory.TERM.createString(string2), Z);

		assertEquals(Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected)), compare.evaluate(Factory.BASIC
				.createTuple(X, Y, Z)));
	}

}
