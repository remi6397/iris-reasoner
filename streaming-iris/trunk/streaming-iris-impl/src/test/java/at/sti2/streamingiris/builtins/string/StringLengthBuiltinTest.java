package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IIntegerTerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringLengthBuiltin.
 */
public class StringLengthBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public StringLengthBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		check(6, "foobar");
		check(0, "");
	}

	private void check(int expected, String string) throws EvaluationException {

		IIntegerTerm expectedTerm = Factory.CONCRETE.createInteger(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		IStringTerm stringTerm = Factory.TERM.createString(string);
		StringLengthBuiltin length = new StringLengthBuiltin(stringTerm, Y);
		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
