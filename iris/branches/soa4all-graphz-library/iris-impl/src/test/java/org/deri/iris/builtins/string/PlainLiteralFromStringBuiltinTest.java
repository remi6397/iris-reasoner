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
import org.deri.iris.api.terms.ITerm;

/**
 * Test for the PlainLiteralFromStringBuiltin.
 */
public class PlainLiteralFromStringBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public PlainLiteralFromStringBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		ITerm expected = CONCRETE.createPlainLiteral("foobar", "de");
		check(expected, "foobar@de");
	}

	private void check(ITerm expectedTerm, String string)
			throws EvaluationException {
		ITuple expectedTuple = BASIC.createTuple(expectedTerm);
		ITerm textTerm = TERM.createString(string);

		PlainLiteralFromStringBuiltin builtin = new PlainLiteralFromStringBuiltin(textTerm, Y);
		ITuple arguments = BASIC.createTuple(X, Y);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
