package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringEndsWithBuiltin.
 */
public class StringEndsWithBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringEndsWithBuiltinTest(String name) {
		super(name);
	}

	public void testEndsWith1() throws EvaluationException {
		check(true, "tattoo", "tattoo");
		check(false, "tattoo", "atto");
		check(false, "", "t");
		check(true, "tattoo", "");

	}

	public void testEndsWith2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check(true, "tattoo", "tattoo", collation);
			check(false, "tattoo", "atto", collation);
			check(false, "", "t", collation);
			check(true, "tattoo", "", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(boolean expected, String haystack, String needle)
			throws EvaluationException {
		StringEndsWithWithoutCollationBuiltin endsWith = new StringEndsWithWithoutCollationBuiltin(
				X, Y);

		ITuple result = endsWith.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(haystack),
				Factory.TERM.createString(needle)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

	private void check(boolean expected, String haystack, String needle,
			String collation) throws EvaluationException {
		StringEndsWithBuiltin endsWith = new StringEndsWithBuiltin(X, Y, Z);

		ITuple result = endsWith.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(haystack),
				Factory.TERM.createString(needle),
				Factory.TERM.createString(collation)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
