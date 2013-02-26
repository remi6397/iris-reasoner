package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the less builtin.
 * </p>
 * <p>
 * $Id: LessBuiltinTest.java,v 1.4 2007-10-10 14:58:27 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.4 $
 */
public class LessBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(LessBuiltinTest.class,
				LessBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final LessBuiltin xy = new LessBuiltin(TERM.createVariable("X"),
				TERM.createVariable("Y"));

		assertNull("5 shouldn't be less than 5", xy.evaluate(BASIC.createTuple(
				CONCRETE.createInteger(5), CONCRETE.createInteger(5))));
		assertNull("5 shouldn't be less than 5.0", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createDouble(5d))));

		assertNotNull("2 should be less than 5.0", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(2),
						CONCRETE.createDouble(5d))));
		assertNull("5 shouldn't be less than 2", xy.evaluate(BASIC.createTuple(
				CONCRETE.createInteger(5), CONCRETE.createInteger(2))));

		assertNotNull(
				"a should be less than b",
				xy.evaluate(BASIC.createTuple(TERM.createString("a"),
						TERM.createString("b"))));
		assertNull(
				"a shouldn't be less to a",
				xy.evaluate(BASIC.createTuple(TERM.createString("a"),
						TERM.createString("a"))));

		assertEquals(
				null,
				xy.evaluate(BASIC.createTuple(CONCRETE.createInteger(5),
						TERM.createString("a"))));
	}
}
