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

import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for StringSubstringBuiltin.
 */
public class StringSubstringBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public static ITerm R = Factory.TERM.createVariable("R");

	public StringSubstringBuiltinTest(String name) {
		super(name);
	}

	public void testSubstring() throws EvaluationException {
		check("bar", "foobar", 3);
		check("foo", "foobar", 0, 3);
	}

	private void check(String expected, String string, int beginIndex)
			throws EvaluationException {
		StringSubstringUntilEndBuiltin builtin = new StringSubstringUntilEndBuiltin(
				Factory.TERM.createString(string), Factory.CONCRETE
						.createInteger(beginIndex), Z);

		assertEquals(Factory.BASIC.createTuple(Factory.TERM
				.createString(expected)), builtin.evaluate(Factory.BASIC
				.createTuple(X, Y, Z)));
	}

	private void check(String expected, String string, int beginIndex,
			int endIndex) throws EvaluationException {
		StringSubstringBuiltin substring = new StringSubstringBuiltin(
				Factory.TERM.createString(string), Factory.CONCRETE
						.createInteger(beginIndex), Factory.CONCRETE
						.createInteger(endIndex), R);

		assertEquals(Factory.BASIC.createTuple(Factory.TERM
				.createString(expected)), substring.evaluate(Factory.BASIC
				.createTuple(X, Y, Z, R)));
	}

}
