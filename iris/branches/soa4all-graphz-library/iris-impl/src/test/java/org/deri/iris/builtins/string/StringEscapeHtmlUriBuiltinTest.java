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
import org.deri.iris.factory.Factory;

/**
 * Test for StringEscapeHtmlUriBuiltin.
 */
public class StringEscapeHtmlUriBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public StringEscapeHtmlUriBuiltinTest(String name) {
		super(name);
	}

	public void testEscape() throws EvaluationException {
		check("http://www.example.com/00/Weather/CA/Los Angeles#ocean",
				"http://www.example.com/00/Weather/CA/Los Angeles#ocean");
		check(
				"javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~b%C3%A9b%C3%A9');",
				"javascript:if (navigator.browserLanguage == 'fr') window.open('http://www.example.com/~bébé');");
	}

	private void check(String expected, String actual)
			throws EvaluationException {
		IStringTerm actualTerm = Factory.TERM.createString(actual);

		StringEscapeHtmlUriBuiltin builtin = new StringEscapeHtmlUriBuiltin(
				actualTerm, Y);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
