package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class SubtractDayTimeDurationFromDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public SubtractDayTimeDurationFromDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 1, 0, 0, 0);
		ITerm date2 = Factory.CONCRETE.createDateTime(3, 4, 5, 0, 0, 0, 0, 0);
		ITerm result = Factory.CONCRETE.createDateTime(3, 4, 4, 0, 0, 0, 0, 0);

		SubtractDayTimeDurationFromDateTimeBuiltin builtin = new SubtractDayTimeDurationFromDateTimeBuiltin(
				date2, date1, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
