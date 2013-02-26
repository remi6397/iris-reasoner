package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DayTimeDurationAddBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationAddBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 1, 0, 0, 0);
		ITerm date2 = Factory.CONCRETE.createDayTimeDuration(true, 2, 1, 0, 0);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 3, 1, 0, 0);

		DayTimeDurationAddBuiltin builtin = new DayTimeDurationAddBuiltin(X, Y,
				Z);

		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
