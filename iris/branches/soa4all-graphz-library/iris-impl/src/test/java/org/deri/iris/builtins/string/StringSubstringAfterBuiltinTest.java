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
 * Test for StringSubstringAfterBuiltin.
 */
public class StringSubstringAfterBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public static ITerm R = Factory.TERM.createVariable("R");

	public StringSubstringAfterBuiltinTest(String name) {
		super(name);
	}

	public void testSubstringAfter1() throws EvaluationException {
		check("too", "tattoo", "tat");
		check("", "tattoo", "tattoo");
		check("tattoo", "tattoo", "");
		check("", "tattoo", "foobar");
	}

	public void testSubstringAfter2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check("too", "tattoo", "tat", collation);
			check("", "tattoo", "tattoo", collation);
			check("tattoo", "tattoo", "", collation);
			check("", "tattoo", "foobar", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(String expected, String haystack, String needle)
			throws EvaluationException {
		StringSubstringAfterWithoutCollationBuiltin substring = new StringSubstringAfterWithoutCollationBuiltin(
				Factory.TERM.createString(haystack), Factory.TERM
						.createString(needle), R);

		assertEquals(Factory.BASIC.createTuple(Factory.TERM
				.createString(expected)), substring.evaluate(Factory.BASIC
				.createTuple(X, Y, R)));
	}

	private void check(String expected, String haystack, String needle,
			String collation) throws EvaluationException {
		StringSubstringAfterBuiltin substring = new StringSubstringAfterBuiltin(
				Factory.TERM.createString(haystack), Factory.TERM
						.createString(needle), Factory.TERM
						.createString(collation), R);

		assertEquals(Factory.BASIC.createTuple(Factory.TERM
				.createString(expected)), substring.evaluate(Factory.BASIC
				.createTuple(X, Y, Z, R)));
	}
}
