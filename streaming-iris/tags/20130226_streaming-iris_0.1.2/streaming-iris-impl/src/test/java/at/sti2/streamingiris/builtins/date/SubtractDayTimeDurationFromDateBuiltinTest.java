package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class SubtractDayTimeDurationFromDateBuiltinTest extends
		AbstractDateBuiltinTest {

	public SubtractDayTimeDurationFromDateBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 1, 0, 0, 0);
		ITerm date2 = Factory.CONCRETE.createDate(3, 1, 2);
		ITerm result = Factory.CONCRETE.createDate(3, 1, 1);

		SubtractDayTimeDurationFromDateBuiltin builtin = new SubtractDayTimeDurationFromDateBuiltin(
				date2, date1, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
