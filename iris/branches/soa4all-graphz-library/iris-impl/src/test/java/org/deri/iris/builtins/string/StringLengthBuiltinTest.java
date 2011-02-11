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
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.factory.Factory;

/**
 * Test for StringLengthBuiltin.
 */
public class StringLengthBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public StringLengthBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		check(6, "foobar");
		check(0, "");
	}

	private void check(int expected, String string) throws EvaluationException {

		IIntegerTerm expectedTerm = Factory.CONCRETE.createInteger(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		IStringTerm stringTerm = Factory.TERM.createString(string);
		StringLengthBuiltin length = new StringLengthBuiltin(stringTerm, Y);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
