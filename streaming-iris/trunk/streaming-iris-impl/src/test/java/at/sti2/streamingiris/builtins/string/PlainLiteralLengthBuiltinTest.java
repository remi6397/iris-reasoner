package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IIntegerTerm;
import at.sti2.streamingiris.api.terms.concrete.IPlainLiteral;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for PlainLiteralLengthBuiltin.
 */
public class PlainLiteralLengthBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public PlainLiteralLengthBuiltinTest(String name) {
		super(name);
	}

	public void testLength() throws EvaluationException {
		check(6, "foobar@de");
		check(0, "@en");
		check(0, "@");
	}

	public void check(int expected, String string) throws EvaluationException {
		IIntegerTerm expectedTerm = Factory.CONCRETE.createInteger(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		IPlainLiteral stringTerm = Factory.CONCRETE.createPlainLiteral(string);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		PlainLiteralLengthBuiltin builtin = new PlainLiteralLengthBuiltin(
				stringTerm, Y);
		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
