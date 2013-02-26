package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.api.basics.ITuple;

/**
 * <p>
 * Tests for the unequals builtin.
 * </p>
 * <p>
 * $Id: UnequalBuiltinTest.java,v 1.2 2007-05-10 07:01:18 poettler_ric Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision: 1.2 $
 */
public class UnequalBuiltinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(UnequalBuiltinTest.class,
				UnequalBuiltinTest.class.getSimpleName());
	}

	public void testEvaluation() throws Exception {
		final ITuple x2 = BASIC.createTuple(TERM.createVariable("X"),
				TERM.createVariable("X"));
		assertNull(
				"5 shouldn't be unequal to 5",
				(new NotEqualBuiltin(CONCRETE.createInteger(5), CONCRETE
						.createInteger(5))).evaluate(x2));
		assertNull(
				"5 shouldn't be unequal to 5.0",
				(new NotEqualBuiltin(CONCRETE.createInteger(5), CONCRETE
						.createDouble(5d))).evaluate(x2));
		assertNotNull(
				"5 should be unequal to 2",
				(new NotEqualBuiltin(CONCRETE.createInteger(2), CONCRETE
						.createInteger(5))).evaluate(x2));
		assertNotNull(
				"5 should be unequal to a",
				(new NotEqualBuiltin(CONCRETE.createInteger(5), TERM
						.createString("a"))).evaluate(x2));
	}
}
