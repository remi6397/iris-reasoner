package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringSubstringBuiltin.
 */
public class StringSubstringBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public static ITerm R = Factory.TERM.createVariable("R");

	public StringSubstringBuiltinTest(String name) {
		super(name);
	}

	public void testSubstring() throws EvaluationException {
		check("bar", "foobar", 3);
		check("foo", "foobar", 0, 3);
	}

	private void check(String expected, String string, int beginIndex)
			throws EvaluationException {
		StringSubstringUntilEndBuiltin builtin = new StringSubstringUntilEndBuiltin(
				Factory.TERM.createString(string),
				Factory.CONCRETE.createInteger(beginIndex), Z);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				builtin.evaluate(Factory.BASIC.createTuple(X, Y, Z)));
	}

	private void check(String expected, String string, int beginIndex,
			int endIndex) throws EvaluationException {
		StringSubstringBuiltin substring = new StringSubstringBuiltin(
				Factory.TERM.createString(string),
				Factory.CONCRETE.createInteger(beginIndex),
				Factory.CONCRETE.createInteger(endIndex), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				substring.evaluate(Factory.BASIC.createTuple(X, Y, Z, R)));
	}

}
