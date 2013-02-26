package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Test for StringFromPlainLiteralBuiltin.
 */
public class StringFromPlainLiteralBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public StringFromPlainLiteralBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		check("foobar", "foobar", "de");
	}

	private void check(String expected, String text, String lang)
			throws EvaluationException {

		ITerm expectedTerm = TERM.createString(expected);
		ITuple expectedTuple = BASIC.createTuple(expectedTerm);

		ITerm textTerm = CONCRETE.createPlainLiteral(text, lang);
		ITuple arguments = BASIC.createTuple(X, Y);

		StringFromPlainLiteralBuiltin builtin = new StringFromPlainLiteralBuiltin(
				textTerm, Y);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
