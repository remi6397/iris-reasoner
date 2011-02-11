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
 * Test for PlainLiteralCompareBuiltin.
 */
public class PlainLiteralCompareBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	private static final ITerm Z = Factory.TERM.createVariable("Z");

	public PlainLiteralCompareBuiltinTest(String name) {
		super(name);
	}

	public void testCompare() throws EvaluationException {
		check(0, "foobar@de", "foobar@en");
		check(0, "foobar@de", "foobar@de");
		check(-1, "a@de", "b@en");
		check(1, "b@de", "a@en");
	}

	private void check(int expected, String string1, String string2)
			throws EvaluationException {
		PlainLiteralCompareBuiltin compare = new PlainLiteralCompareBuiltin(Factory.CONCRETE
				.createPlainLiteral(string1), Factory.CONCRETE.createPlainLiteral(string2), Z);

		assertEquals(Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected)), compare.evaluate(Factory.BASIC
				.createTuple(X, Y, Z)));
	}

}
