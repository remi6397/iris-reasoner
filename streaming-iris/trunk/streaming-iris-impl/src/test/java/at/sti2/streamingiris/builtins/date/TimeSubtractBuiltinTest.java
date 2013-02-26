package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class TimeSubtractBuiltinTest extends AbstractDateBuiltinTest {

	public TimeSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createTime(11, 0, 0, 0, 0);
		ITerm date2 = Factory.CONCRETE.createTime(4, 0, 0, 0, 0);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 0, 7, 0, 0);

		TimeSubtractBuiltin builtin = new TimeSubtractBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}