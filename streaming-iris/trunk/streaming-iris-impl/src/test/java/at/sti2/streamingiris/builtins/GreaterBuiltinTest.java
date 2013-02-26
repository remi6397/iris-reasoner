package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * <p>
 * Tests for the greater builtin.
 * </p>
 * <p>
 * $Id: GreaterBuiltinTest.java,v 1.5 2007-10-10 14:58:27 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.5 $
 */
public class GreaterBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(GreaterBuiltinTest.class,
				GreaterBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final GreaterBuiltin xy = new GreaterBuiltin(TERM.createVariable("X"),
				TERM.createVariable("Y"));

		assertNull("5 shouldn't be greater than 5", xy.evaluate(BASIC
				.createTuple(CONCRETE.createInteger(5),
						CONCRETE.createInteger(5))));
		assertNull("5 shouldn't be greater than 5.0", xy.evaluate(BASIC
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
		assertNull(
				"a shouldn't be greater to a",
				xy.evaluate(BASIC.createTuple(TERM.createString("a"),
						TERM.createString("a"))));

		assertEquals(
				null,
				xy.evaluate(BASIC.createTuple(CONCRETE.createInteger(5),
						TERM.createString("a"))));
	}

	public void test_isBuiltin() {
		assertTrue(
				"buitin predicates should be identifiable as builtins",
				(new GreaterBuiltin(CONCRETE.createInteger(2), CONCRETE
						.createInteger(5)).isBuiltin()));
	}

}
