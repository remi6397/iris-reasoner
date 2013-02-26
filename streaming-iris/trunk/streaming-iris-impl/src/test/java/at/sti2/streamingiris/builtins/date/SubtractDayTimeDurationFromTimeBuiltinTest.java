package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class SubtractDayTimeDurationFromTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public SubtractDayTimeDurationFromTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 0, 1, 0, 0);
		ITerm date2 = Factory.CONCRETE.createTime(3, 1, 2, 0, 0);
		ITerm result = Factory.CONCRETE.createTime(2, 1, 2, 0, 0);

		SubtractDayTimeDurationFromTimeBuiltin builtin = new SubtractDayTimeDurationFromTimeBuiltin(
				date2, date1, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}

}
