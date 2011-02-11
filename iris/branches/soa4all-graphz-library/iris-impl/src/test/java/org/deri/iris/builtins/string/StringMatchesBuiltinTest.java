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
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;

/**
 * Test for StringMatchesBuiltin.
 */
public class StringMatchesBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringMatchesBuiltinTest(String name) {
		super(name);
	}

	public void testMatches() throws EvaluationException {
		check(true, "abracadabra", "bra");
		check(true, "abracadabra", "^a.*a$");
		check(false, "abracadabra", "^bra");
	}

	private void check(boolean expected, String string, String pattern)
			throws EvaluationException {
		IBuiltinAtom matches = new StringMatchesWithoutFlagsBuiltin(X, Y);

		ITuple result = matches.evaluate(Factory.BASIC.createTuple(Factory.TERM
				.createString(string), Factory.TERM.createString(pattern)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
