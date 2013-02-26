package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringMatchesBuiltin.
 */
public class StringMatchesBuiltinTest extends TestCase {

	public static ITerm X = Factory.TERM.createVariable("X");

	public static ITerm Y = Factory.TERM.createVariable("Y");

	public static ITerm Z = Factory.TERM.createVariable("Z");

	public StringMatchesBuiltinTest(String name) {
		super(name);
	}

	public void testMatches() throws EvaluationException {
		check(true, "abracadabra", "bra");
		check(true, "abracadabra", "^a.*a$");
		check(false, "abracadabra", "^bra");
	}

	private void check(boolean expected, String string, String pattern)
			throws EvaluationException {
		IBuiltinAtom matches = new StringMatchesWithoutFlagsBuiltin(X, Y);

		ITuple result = matches.evaluate(Factory.BASIC.createTuple(
				Factory.TERM.createString(string),
				Factory.TERM.createString(pattern)));

		if (expected) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
