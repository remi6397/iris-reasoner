package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DayTimeDurationSubtractBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm daytimeduration1 = Factory.CONCRETE.createDayTimeDuration(true,
				15, 23, 19, 12.0);
		ITerm daytimeduration2 = Factory.CONCRETE.createDayTimeDuration(true,
				14, 22, 18, 11.0);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 1, 1, 1,
				1.0);

		DayTimeDurationSubtractBuiltin builtin = new DayTimeDurationSubtractBuiltin(
				X, Y, Z);

		args = Factory.BASIC.createTuple(daytimeduration1, daytimeduration2,
				result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}