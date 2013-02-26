package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class YearMonthDurationDivideByYearMonthDurationBuiltinTest extends
		AbstractDateBuiltinTest {

	public YearMonthDurationDivideByYearMonthDurationBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 2, 2);
		ITerm date2 = Factory.CONCRETE.createYearMonthDuration(true, 2, 2);
		ITerm result = Factory.CONCRETE.createDecimal(1.0);

		YearMonthDurationDivideByYearMonthDurationBuiltin builtin = new YearMonthDurationDivideByYearMonthDurationBuiltin(
				date1, date2, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}