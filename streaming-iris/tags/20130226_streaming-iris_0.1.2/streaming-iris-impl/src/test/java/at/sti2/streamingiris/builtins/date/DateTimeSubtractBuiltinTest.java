package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

public class DateTimeSubtractBuiltinTest extends AbstractDateBuiltinTest {

	public DateTimeSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm date1 = Factory.CONCRETE.createDateTime(2000, 10, 30, 6, 12, 0,
				0, 0);
		ITerm date2 = Factory.CONCRETE.createDateTime(2000, 10, 30, 6, 12, 0,
				0, 0);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 0, 0, 0, 0);

		DateTimeSubtractBuiltin builtin = new DateTimeSubtractBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}
}