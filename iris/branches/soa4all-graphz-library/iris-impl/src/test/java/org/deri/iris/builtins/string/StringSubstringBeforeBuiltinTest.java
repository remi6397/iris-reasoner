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
 * Test for StringSubstringBeforBuiltin.
 */
public class StringSubstringBeforeBuiltinTest extends TestCase {

	public static ITerm X = TERM.createVariable("X");

	public static ITerm Y = TERM.createVariable("Y");

	public static ITerm Z = TERM.createVariable("Z");

	public static ITerm R = TERM.createVariable("R");

	public StringSubstringBeforeBuiltinTest(String name) {
		super(name);
	}

	public void testSubstringBefore1() throws EvaluationException {
		check("t", "tattoo", "attoo");
		check("", "tattoo", "tattoo");
		check("", "tattoo", "");
		check("", "tattoo", "foobar");
	}

	public void testSubstringBefore2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check("t", "tattoo", "attoo", collation);
			check("", "tattoo", "tattoo", collation);
			check("", "tattoo", "", collation);
			check("", "tattoo", "foobar", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(String expected, String haystack, String needle)
			throws EvaluationException {
		StringSubstringBeforeWithoutCollationBuiltin substring = new StringSubstringBeforeWithoutCollationBuiltin(
				TERM.createString(haystack), TERM.createString(needle), R);

		assertEquals(Factory.BASIC.createTuple(Factory.TERM
				.createString(expected)), substring.evaluate(Factory.BASIC
				.createTuple(X, Y, R)));
	}

	private void check(String expected, String haystack, String needle,
			String collation) throws EvaluationException {
		StringSubstringBeforeBuiltin substring = new StringSubstringBeforeBuiltin(
				TERM.createString(haystack), TERM.createString(needle), TERM
						.createString(collation), R);

		assertEquals(Factory.BASIC.createTuple(Factory.TERM
				.createString(expected)), substring.evaluate(Factory.BASIC
				.createTuple(X, Y, Z, R)));
	}
}
