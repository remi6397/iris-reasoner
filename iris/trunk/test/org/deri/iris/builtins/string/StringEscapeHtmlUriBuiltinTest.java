package org.deri.iris.builtins.string;

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

	private static final ITerm X = Factory.TERM.createVariable("X");

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
		StringEscapeHtmlUriBuiltin builtin = new StringEscapeHtmlUriBuiltin(X);

		IStringTerm actualTerm = Factory.TERM.createString(actual);
		ITuple arguments = Factory.BASIC.createTuple(actualTerm);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
