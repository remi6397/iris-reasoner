package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DayTimeDurationDivideByDayTimeDurationBuiltinTest extends
		AbstractDateBuiltinTest {

	public DayTimeDurationDivideByDayTimeDurationBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 4, 0, 0, 0);
		ITerm date2 = Factory.CONCRETE.createDayTimeDuration(true, 2, 0, 0, 0);
		ITerm result = Factory.CONCRETE.createDecimal(2.0);
		DayTimeDurationDivideByDayTimeDurationBuiltin builtin = new DayTimeDurationDivideByDayTimeDurationBuiltin(
				X, Y, Z);

		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}
