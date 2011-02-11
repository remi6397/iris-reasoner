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
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for StringStartsWithBuiltin.
 */
public class StringStartsWithBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringStartsWithBuiltinTest(String name) {
		super(name);
	}

	public void testStartsWith1() throws EvaluationException {
		check(true, "tattoo", "tat");
		check(false, "tattoo", "att");
		check(false, "", "t");
		check(true, "tattoo", "");
	}

	public void testStartsWith2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check(true, "tattoo", "tat", collation);
			check(false, "tattoo", "att", collation);
			check(false, "", "t", collation);
			check(true, "tattoo", "", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(boolean expected, String haystack, String needle)
			throws EvaluationException {
		StringStartsWithWithoutCollationBuiltin startsWith = new StringStartsWithWithoutCollationBuiltin(
				X, Y);

		ITuple result = startsWith.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(haystack), Factory.TERM
						.createString(needle)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

	private void check(boolean expected, String haystack, String needle,
			String collation) throws EvaluationException {
		StringStartsWithBuiltin startsWith = new StringStartsWithBuiltin(X, Y,
				Z);

		ITuple result = startsWith.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(haystack), Factory.TERM
						.createString(needle), Factory.TERM
						.createString(collation)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
