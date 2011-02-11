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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;
import static org.deri.iris.factory.Factory.TERM;
import junit.framework.TestCase;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;

/**
 * Test for StringConcatBuiltin.
 */
public class StringConcatBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	private static final ITerm Z = TERM.createVariable("Z");

	public StringConcatBuiltinTest(String name) {
		super(name);
	}

	public void testString() throws EvaluationException {
		check("foobar", "foo", "bar");
	}

	public void testInteger() throws EvaluationException {
		check("foobar1337", TERM.createString("foobar"), CONCRETE
				.createInteger(1337));
	}

	private void check(String expected, ITerm term1, ITerm term2)
			throws EvaluationException {
		StringConcatBuiltin length = new StringConcatBuiltin(term1, term2, Z);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		IStringTerm expectedTerm = TERM.createString(expected);
		ITuple expectedTuple = BASIC.createTuple(expectedTerm);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

	private void check(String expected, String string1, String string2)
			throws EvaluationException {
		check(expected, TERM.createString(string1), TERM.createString(string2));
	}

}
