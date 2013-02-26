package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.api.builtins.IBuiltinAtom;

/**
 * Tests for the exact equal built-in.
 */
public class ExactEqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(ExactEqualBuiltinTest.class,
				ExactEqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final IBuiltinAtom xy = BuiltinsFactory.getInstance().createExactEqual(
				TERM.createVariable("X"), TERM.createVariable("Y"));

		assertNotNull("5 should be exactly equal to 5", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createInteger(5))));

		assertNotNull("10.0d should be exactly equal to 10.0d",
				xy.evaluate(BASIC.createTuple(CONCRETE.createDouble(10),
						CONCRETE.createDouble(10))));

		assertNotNull("10.0f should be exactly equal to 10.0f",
				xy.evaluate(BASIC.createTuple(CONCRETE.createFloat(10),
						CONCRETE.createFloat(10))));

		assertNotNull("+0.0d should be exactly equal to -0.0d",
				xy.evaluate(BASIC.createTuple(CONCRETE.createDouble(+0.0d),
						CONCRETE.createDouble(-0.0d))));

		assertNotNull("+0.0f should be exactly equal to -0.0f",
				xy.evaluate(BASIC.createTuple(CONCRETE.createFloat(+0.0f),
						CONCRETE.createFloat(-0.0f))));

		assertNull("5 should not be exactly equal to 5.0", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createDouble(5))));

		assertNull(
				"5.0f should not be exactly equal to 5.0d",
				xy.evaluate(BASIC.createTuple(CONCRETE.createFloat(5),
						CONCRETE.createDouble(5))));

		assertNull("5 should not exactly equal to 2", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(2),
						CONCRETE.createInteger(5))));

		assertNull(
				"5 should not be exactly equal to a",
				xy.evaluate(BASIC.createTuple(CONCRETE.createInteger(5),
						TERM.createString("a"))));
	}

	public void test_isBuiltin() {
		final IBuiltinAtom xy = BuiltinsFactory.getInstance().createExactEqual(
				TERM.createVariable("X"), TERM.createVariable("Y"));
		assertTrue("buitin predicates should be identifiable as builtins",
				xy.isBuiltin());
	}
}
