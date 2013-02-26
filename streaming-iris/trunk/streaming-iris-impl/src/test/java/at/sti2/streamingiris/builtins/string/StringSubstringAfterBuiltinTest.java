package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringSubstringAfterBuiltin.
 */
public class StringSubstringAfterBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public static ITerm R = Factory.TERM.createVariable("R");

	public StringSubstringAfterBuiltinTest(String name) {
		super(name);
	}

	public void testSubstringAfter1() throws EvaluationException {
		check("too", "tattoo", "tat");
		check("", "tattoo", "tattoo");
		check("tattoo", "tattoo", "");
		check("", "tattoo", "foobar");
	}

	public void testSubstringAfter2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check("too", "tattoo", "tat", collation);
			check("", "tattoo", "tattoo", collation);
			check("tattoo", "tattoo", "", collation);
			check("", "tattoo", "foobar", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(String expected, String haystack, String needle)
			throws EvaluationException {
		StringSubstringAfterWithoutCollationBuiltin substring = new StringSubstringAfterWithoutCollationBuiltin(
				Factory.TERM.createString(haystack),
				Factory.TERM.createString(needle), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				substring.evaluate(Factory.BASIC.createTuple(X, Y, R)));
	}

	private void check(String expected, String haystack, String needle,
			String collation) throws EvaluationException {
		StringSubstringAfterBuiltin substring = new StringSubstringAfterBuiltin(
				Factory.TERM.createString(haystack),
				Factory.TERM.createString(needle),
				Factory.TERM.createString(collation), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				substring.evaluate(Factory.BASIC.createTuple(X, Y, Z, R)));
	}
}
