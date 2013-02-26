package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DayTimeDurationDivideBuiltinTest extends AbstractDateBuiltinTest {

	public DayTimeDurationDivideBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 2, 4, 2, 2);
		ITerm date2 = Factory.CONCRETE.createDouble(2.0);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 1, 2, 1, 1);

		DayTimeDurationDivideBuiltin builtin = new DayTimeDurationDivideBuiltin(
				X, Y, Z);

		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
