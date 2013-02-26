package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class YearMonthDurationAddBuiltinTest extends AbstractDateBuiltinTest {

	public YearMonthDurationAddBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 56, 5);
		ITerm date2 = Factory.CONCRETE.createYearMonthDuration(true, 12, 1);
		ITerm result = Factory.CONCRETE.createYearMonthDuration(true, 68, 6);

		YearMonthDurationAddBuiltin builtin = new YearMonthDurationAddBuiltin(
				date1, date2, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}