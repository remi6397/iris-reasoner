package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Test for StringConcatBuiltin.
 */
public class StringConcatBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	private static final ITerm Z = TERM.createVariable("Z");

	public StringConcatBuiltinTest(String name) {
		super(name);
	}

	public void testString() throws EvaluationException {
		check("foobar", "foo", "bar");
	}

	public void testInteger() throws EvaluationException {
		check("foobar1337", TERM.createString("foobar"),
				CONCRETE.createInteger(1337));
	}

	private void check(String expected, ITerm term1, ITerm term2)
			throws EvaluationException {
		StringConcatBuiltin length = new StringConcatBuiltin(term1, term2, Z);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		IStringTerm expectedTerm = TERM.createString(expected);
		ITuple expectedTuple = BASIC.createTuple(expectedTerm);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

	private void check(String expected, String string1, String string2)
			throws EvaluationException {
		check(expected, TERM.createString(string1), TERM.createString(string2));
	}

}
