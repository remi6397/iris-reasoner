package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for PlainLiteralCompareBuiltin.
 */
public class PlainLiteralCompareBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	private static final ITerm Z = Factory.TERM.createVariable("Z");

	public PlainLiteralCompareBuiltinTest(String name) {
		super(name);
	}

	public void testCompare() throws EvaluationException {
		check(0, "foobar@de", "foobar@en");
		check(0, "foobar@de", "foobar@de");
		check(-1, "a@de", "b@en");
		check(1, "b@de", "a@en");
	}

	private void check(int expected, String string1, String string2)
			throws EvaluationException {
		PlainLiteralCompareBuiltin compare = new PlainLiteralCompareBuiltin(
				Factory.CONCRETE.createPlainLiteral(string1),
				Factory.CONCRETE.createPlainLiteral(string2), Z);

		assertEquals(Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected)), compare.evaluate(Factory.BASIC
				.createTuple(X, Y, Z)));
	}

}
