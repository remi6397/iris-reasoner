package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringCompareBuiltin.
 */
public class StringCompareBuiltinTest extends TestCase {

	private static final ITerm X = TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	private static final ITerm Z = TERM.createVariable("Z");

	public StringCompareBuiltinTest(String name) {
		super(name);
	}

	public void testEvaluation() throws EvaluationException {
		check(0, "foobar", "foobar");
		check(-1, "a", "b");
		check(1, "b", "a");
	}

	private void check(int expected, String string1, String string2)
			throws EvaluationException {
		StringCompareBuiltin compare = new StringCompareBuiltin(
				Factory.TERM.createString(string1),
				Factory.TERM.createString(string2), Z);

		assertEquals(Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected)), compare.evaluate(Factory.BASIC
				.createTuple(X, Y, Z)));
	}

}
