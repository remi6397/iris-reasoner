package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class SubtractYearMonthDurationFromDateBuiltinTest extends
		AbstractDateBuiltinTest {

	public SubtractYearMonthDurationFromDateBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 2, 2);
		ITerm date2 = Factory.CONCRETE.createDate(1987, 4, 1);
		ITerm result = Factory.CONCRETE.createDate(1985, 2, 1);

		SubtractYearMonthDurationFromDateBuiltin builtin = new SubtractYearMonthDurationFromDateBuiltin(
				date2, date1, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
