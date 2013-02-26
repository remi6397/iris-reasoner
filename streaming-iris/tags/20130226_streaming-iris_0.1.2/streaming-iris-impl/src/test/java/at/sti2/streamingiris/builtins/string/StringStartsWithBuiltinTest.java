package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringStartsWithBuiltin.
 */
public class StringStartsWithBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringStartsWithBuiltinTest(String name) {
		super(name);
	}

	public void testStartsWith1() throws EvaluationException {
		check(true, "tattoo", "tat");
		check(false, "tattoo", "att");
		check(false, "", "t");
		check(true, "tattoo", "");
	}

	public void testStartsWith2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check(true, "tattoo", "tat", collation);
			check(false, "tattoo", "att", collation);
			check(false, "", "t", collation);
			check(true, "tattoo", "", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(boolean expected, String haystack, String needle)
			throws EvaluationException {
		StringStartsWithWithoutCollationBuiltin startsWith = new StringStartsWithWithoutCollationBuiltin(
				X, Y);

		ITuple result = startsWith.evaluate(Factory.BASIC.createTuple(
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
		StringStartsWithBuiltin startsWith = new StringStartsWithBuiltin(X, Y,
				Z);

		ITuple result = startsWith.evaluate(Factory.BASIC.createTuple(
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
