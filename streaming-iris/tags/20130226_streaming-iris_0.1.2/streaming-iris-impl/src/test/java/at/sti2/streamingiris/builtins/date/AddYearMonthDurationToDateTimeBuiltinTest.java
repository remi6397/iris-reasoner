package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class AddYearMonthDurationToDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddYearMonthDurationToDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm yearMonthDuration = Factory.CONCRETE.createYearMonthDuration(
				true, 5, 2);
		ITerm date1 = Factory.CONCRETE.createDateTime(10, 4, 2, 1, 23, 34.5, 0,
				0);
		ITerm result = Factory.CONCRETE.createDateTime(15, 6, 2, 1, 23, 34.5,
				0, 0);

		AddBuiltin builtin = new AddYearMonthDurationToDateTimeBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, yearMonthDuration, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}
}