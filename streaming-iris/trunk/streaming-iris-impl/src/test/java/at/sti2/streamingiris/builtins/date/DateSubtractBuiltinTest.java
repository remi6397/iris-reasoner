package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DateSubtractBuiltinTest extends AbstractDateBuiltinTest {

	public DateSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDate(1978, 5, 27);
		ITerm date2 = Factory.CONCRETE.createDate(1978, 5, 26);
		ITerm result = Factory.CONCRETE.createDayTimeDuration(true, 1, 0, 0, 0);

		DateSubtractBuiltin builtin = new DateSubtractBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, date2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}
