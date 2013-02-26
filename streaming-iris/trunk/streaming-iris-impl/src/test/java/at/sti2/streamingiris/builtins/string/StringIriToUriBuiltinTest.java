package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringIriToUriBuiltin.
 */
public class StringIriToUriBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	public StringIriToUriBuiltinTest(String name) {
		super(name);
	}

	public void testConversion() throws EvaluationException {
		check("http://www.example.com/00/Weather/CA/Los%20Angeles#ocean",
				"http://www.example.com/00/Weather/CA/Los%20Angeles#ocean");
		check("http://www.example.com/~b%C3%A9b%C3%A9",
				"http://www.example.com/~bébé");
	}

	private void check(String expected, String actual)
			throws EvaluationException {
		IStringTerm actualTerm = Factory.TERM.createString(actual);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		StringIriToUriBuiltin builtin = new StringIriToUriBuiltin(actualTerm, Y);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
