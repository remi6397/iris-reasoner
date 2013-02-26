package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Test for the PlainLiteralFromStringBuiltin.
 */
public class PlainLiteralFromStringBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public PlainLiteralFromStringBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		ITerm expected = CONCRETE.createPlainLiteral("foobar", "de");
		check(expected, "foobar@de");
	}

	private void check(ITerm expectedTerm, String string)
			throws EvaluationException {
		ITuple expectedTuple = BASIC.createTuple(expectedTerm);
		ITerm textTerm = TERM.createString(string);

		PlainLiteralFromStringBuiltin builtin = new PlainLiteralFromStringBuiltin(
				textTerm, Y);
		ITuple arguments = BASIC.createTuple(X, Y);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
