package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class YearMonthDurationSubtractBuiltinTest extends
		AbstractDateBuiltinTest {

	public YearMonthDurationSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 200, 4);
		ITerm date2 = Factory.CONCRETE.createYearMonthDuration(true, 100, 2);
		ITerm result = Factory.CONCRETE.createYearMonthDuration(true, 100, 2);

		YearMonthDurationSubtractBuiltin builtin = new YearMonthDurationSubtractBuiltin(
				X, Y, Z);
		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}