package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the greater equal builtin.
 * </p>
 * <p>
 * $Id: GreaterEqualBuiltinTest.java,v 1.3 2007-10-10 14:58:27 bazbishop237 Exp
 * $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.3 $
 */
public class GreaterEqualBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(GreaterEqualBuiltinTest.class,
				GreaterEqualBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final GreaterEqualBuiltin xy = new GreaterEqualBuiltin(
				TERM.createVariable("X"), TERM.createVariable("Y"));

		assertNotNull("5 should be greater-equal to 5", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createInteger(5))));
		assertNotNull("5 should be greater-equal to 5.0", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createDouble(5d))));

		assertNull("2 shouldn't be greater than 5.0", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(2),
						CONCRETE.createDouble(5d))));
		assertNotNull("5 should be greater than 2", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createInteger(2))));

		assertNull(
				"a shouldn't be greater than b",
				xy.evaluate(BASIC.createTuple(TERM.createString("a"),
						TERM.createString("b"))));
		assertNotNull(
				"a should be greater-equal to a",
				xy.evaluate(BASIC.createTuple(TERM.createString("a"),
						TERM.createString("a"))));

		assertEquals(
				null,
				xy.evaluate(BASIC.createTuple(CONCRETE.createInteger(5),
						TERM.createString("a"))));
		// boolean exceptionThrown = false;
		// try {
		// xy.evaluate(BASIC.createTuple(CONCRETE.createInteger(5),
		// TERM.createString("a")));
		// } catch (IllegalArgumentException e) {
		// exceptionThrown = true;
		// }
		// assertTrue("5 >= a should not be evaluable", exceptionThrown);
	}
}
