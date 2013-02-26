package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class YearMonthDurationDivideBuiltinTest extends AbstractDateBuiltinTest {

	public YearMonthDurationDivideBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 56, 4);
		ITerm date2 = Factory.CONCRETE.createDouble(2.0);
		ITerm result = Factory.CONCRETE.createYearMonthDuration(true, 28, 2);

		YearMonthDurationDivideBuiltin builtin = new YearMonthDurationDivideBuiltin(
				date1, date2, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}