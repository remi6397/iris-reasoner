package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.string.StringEscapeHtmlUriBuiltin;
import at.sti2.streamingiris.factory.Factory;

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
