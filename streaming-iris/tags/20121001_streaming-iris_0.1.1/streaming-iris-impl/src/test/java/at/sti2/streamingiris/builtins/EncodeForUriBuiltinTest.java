package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;


import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.EncodeForUriBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringEscapeHtmlUriBuiltin.
 */
public class EncodeForUriBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public EncodeForUriBuiltinTest(String name) {
		super(name);
	}

	public void testUri() throws EvaluationException {
		check("http%3A%2F%2Fwww.example.com%2F00%2FWeather%2FCA%2FLos%2520Angeles%23ocean",
				"http://www.example.com/00/Weather/CA/Los%20Angeles#ocean");
	}

	public void testApostrophe() throws EvaluationException {
		check("~b%C3%A9b%C3%A9", "~bébé");
	}

	public void testPercentSign() throws EvaluationException {
		check("100%25%20organic", "100% organic");
	}

	private void check(String expected, String input)
			throws EvaluationException {
		IStringTerm actualTerm = Factory.TERM.createString(input);

		EncodeForUriBuiltin builtin = new EncodeForUriBuiltin(actualTerm, Y);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
