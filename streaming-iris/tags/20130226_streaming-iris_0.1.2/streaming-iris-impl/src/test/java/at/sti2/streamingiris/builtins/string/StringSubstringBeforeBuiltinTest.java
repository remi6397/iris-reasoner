package at.sti2.streamingiris.builtins.string;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringSubstringBeforBuiltin.
 */
public class StringSubstringBeforeBuiltinTest extends TestCase {

	public static ITerm X = TERM.createVariable("X");

	public static ITerm Y = TERM.createVariable("Y");

	public static ITerm Z = TERM.createVariable("Z");

	public static ITerm R = TERM.createVariable("R");

	public StringSubstringBeforeBuiltinTest(String name) {
		super(name);
	}

	public void testSubstringBefore1() throws EvaluationException {
		check("t", "tattoo", "attoo");
		check("", "tattoo", "tattoo");
		check("", "tattoo", "");
		check("", "tattoo", "foobar");
	}

	public void testSubstringBefore2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check("t", "tattoo", "attoo", collation);
			check("", "tattoo", "tattoo", collation);
			check("", "tattoo", "", collation);
			check("", "tattoo", "foobar", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(String expected, String haystack, String needle)
			throws EvaluationException {
		StringSubstringBeforeWithoutCollationBuiltin substring = new StringSubstringBeforeWithoutCollationBuiltin(
				TERM.createString(haystack), TERM.createString(needle), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				substring.evaluate(Factory.BASIC.createTuple(X, Y, R)));
	}

	private void check(String expected, String haystack, String needle,
			String collation) throws EvaluationException {
		StringSubstringBeforeBuiltin substring = new StringSubstringBeforeBuiltin(
				TERM.createString(haystack), TERM.createString(needle),
				TERM.createString(collation), R);

		assertEquals(
				Factory.BASIC.createTuple(Factory.TERM.createString(expected)),
				substring.evaluate(Factory.BASIC.createTuple(X, Y, Z, R)));
	}
}
