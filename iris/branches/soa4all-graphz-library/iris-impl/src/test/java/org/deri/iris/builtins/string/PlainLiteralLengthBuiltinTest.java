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
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.concrete.IIntegerTerm;
import org.deri.iris.api.terms.concrete.IPlainLiteral;
import org.deri.iris.factory.Factory;

/**
 * Test for PlainLiteralLengthBuiltin.
 */
public class PlainLiteralLengthBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public PlainLiteralLengthBuiltinTest(String name) {
		super(name);
	}

	public void testLength() throws EvaluationException {
		check(6, "foobar@de");
		check(0, "@en");
		check(0, "@");
	}

	public void check(int expected, String string) throws EvaluationException {
		IIntegerTerm expectedTerm = Factory.CONCRETE.createInteger(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		IPlainLiteral stringTerm = Factory.CONCRETE.createPlainLiteral(string);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		PlainLiteralLengthBuiltin builtin = new PlainLiteralLengthBuiltin(stringTerm, Y);
		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
