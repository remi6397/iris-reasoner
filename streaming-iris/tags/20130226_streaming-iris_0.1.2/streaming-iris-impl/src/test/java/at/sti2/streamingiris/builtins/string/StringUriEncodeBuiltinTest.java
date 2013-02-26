package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringUriEncodeBuiltin.
 */
public class StringUriEncodeBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	public StringUriEncodeBuiltinTest(String name) {
		super(name);
	}

	public void testEncode() throws EvaluationException {
		/*
		 * Examples taken from
		 * http://www.w3.org/TR/xpath-functions/#func-encode-for-uri
		 */
		check("http%3A%2F%2Fwww.example.com%2F00%2FWeather%2FCA%2FLos%2520Angeles%23ocean",
				"http://www.example.com/00/Weather/CA/Los%20Angeles#ocean");
		check("~b%C3%A9b%C3%A9", "~bébé");
		check("100%25%20organic", "100% organic");
		check("-", "-");
	}

	private void check(String expected, String actual)
			throws EvaluationException {
		IStringTerm actualTerm = Factory.TERM.createString(actual);

		StringUriEncodeBuiltin encode = new StringUriEncodeBuiltin(actualTerm,
				Y);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = encode.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
