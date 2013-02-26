package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DateTimeEqualBuiltinTest extends AbstractDateBuiltinTest {

	public DateTimeEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDateTime(2001, 2, 12, 14, 34, 23,
				0, 0);
		ITerm date2 = Factory.CONCRETE.createDateTime(2001, 2, 12, 14, 34, 23,
				0, 0);
		ITerm date3 = Factory.CONCRETE.createDateTime(1965, 4, 1, 10, 6, 29, 0,
				0);

		DateTimeEqualBuiltin builtin = new DateTimeEqualBuiltin(X, Y);

		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DateTimeEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date3);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);
	}

}
