package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DayTimeDurationMultiplyBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationMultiplyBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm daytimeduration1 = Factory.CONCRETE.createDayTimeDuration(true,
				0, 2, 10, 0.0);
		ITerm double1 = Factory.CONCRETE.createDouble(2.1);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 0, 4, 33,
				0.0);

		DayTimeDurationMultiplyBuiltin builtin = new DayTimeDurationMultiplyBuiltin(
				X, Y, Z);

		args = Factory.BASIC.createTuple(daytimeduration1, double1, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}