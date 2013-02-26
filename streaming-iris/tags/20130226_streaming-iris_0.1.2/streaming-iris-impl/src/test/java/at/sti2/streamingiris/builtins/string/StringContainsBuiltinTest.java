package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringContainsBuiltin.
 */
public class StringContainsBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringContainsBuiltinTest(String name) {
		super(name);
	}

	public void testContains1() throws EvaluationException {
		check(true, "tattoo", "t");
		check(false, "tattoo", "ttt");
		check(false, "", "t");
		check(true, "tattoo", "");
	}

	public void testContains2() throws EvaluationException {
		try {
			String collation = "http://www.w3.org/2005/xpath-functions/collation/codepoint";

			check(true, "tattoo", "t", collation);
			check(false, "tattoo", "ttt", collation);
			check(false, "", "t", collation);
			check(true, "tattoo", "", collation);
		} catch (IllegalArgumentException iae) {
			fail("Unicode code point collation not supported.");
		}
	}

	private void check(boolean expected, String haystack, String needle)
			throws EvaluationException {
		StringContainsWithoutCollationBuiltin contains = new StringContainsWithoutCollationBuiltin(
				Factory.TERM.createString(haystack),
				Factory.TERM.createString(needle));

		ITuple result = contains.evaluate(Factory.BASIC.createTuple(X, Y));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

	private void check(boolean expected, String haystack, String needle,
			String collation) throws EvaluationException,
			IllegalArgumentException {
		StringContainsBuiltin contains = new StringContainsBuiltin(
				Factory.TERM.createString(haystack),
				Factory.TERM.createString(needle),
				Factory.TERM.createString(collation));

		ITuple result = contains.evaluate(Factory.BASIC.createTuple(X, Y, Z));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
