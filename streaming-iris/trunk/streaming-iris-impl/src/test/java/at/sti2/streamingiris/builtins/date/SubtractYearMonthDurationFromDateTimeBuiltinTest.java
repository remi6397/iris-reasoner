package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class SubtractYearMonthDurationFromDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public SubtractYearMonthDurationFromDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 2, 2);
		ITerm date2 = Factory.CONCRETE
				.createDateTime(1987, 4, 2, 0, 0, 0, 0, 0);
		ITerm result = Factory.CONCRETE.createDateTime(1985, 2, 2, 0, 0, 0, 0,
				0);

		SubtractYearMonthDurationFromDateTimeBuiltin builtin = new SubtractYearMonthDurationFromDateTimeBuiltin(
				date2, date1, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
