package at.sti2.streamingiris.builtins;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;
import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * <p>
 * Tests for the {@code MaxBuiltin}.
 * </p>
 */
public class MaxBuiltinTest extends TestCase {

	private final ITerm X = TERM.createVariable("X");
	private final ITerm Y = TERM.createVariable("Y");
	private final ITerm Z = TERM.createVariable("Z");
	private final ITerm T_1 = CONCRETE.createInteger(1);
	private final ITerm T_5 = CONCRETE.createInteger(5);

	public void testEvaluate() throws Exception {

		// max(1,5) = 5
		MaxBuiltin maxBuiltin = new MaxBuiltin(T_1, T_5, T_5);
		ITuple result = maxBuiltin.evaluate(Factory.BASIC.createTuple(T_1, T_5,
				T_5));
		assertNotNull(result);

		// max(5,1) = 5
		maxBuiltin = new MaxBuiltin(T_5, T_1, T_5);
		result = maxBuiltin.evaluate(Factory.BASIC.createTuple(T_5, T_1, T_5));
		assertNotNull(result);

		// max(1,5) != 1
		maxBuiltin = new MaxBuiltin(T_1, T_5, T_1);
		result = maxBuiltin.evaluate(Factory.BASIC.createTuple(T_1, T_5, T_1));
		assertNull(result);

		// max(5,1) != 1
		maxBuiltin = new MaxBuiltin(T_5, T_1, T_1);
		result = maxBuiltin.evaluate(Factory.BASIC.createTuple(T_5, T_1, T_1));
		assertNull(result);

		maxBuiltin = new MaxBuiltin(X, Y, Z);
		result = maxBuiltin.evaluate(Factory.BASIC.createTuple(T_1, T_5, Z));
		assertEquals(Factory.BASIC.createTuple(T_5), result);
	}

	public void testWrongNumberOfArgument() throws Exception {

		try {
			new MaxBuiltin(T_1, T_5);
		} catch (IllegalArgumentException e) {
			// Failed correctly
		}

		try {
			new MaxBuiltin(T_1, T_5, T_1, T_5);
		} catch (IllegalArgumentException e) {
			// Failed correctly
		}
	}
}
