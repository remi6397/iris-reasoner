package at.sti2.streamingiris.builtins.date;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for DayPartBuiltin.
 */
public class DayPartBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	public DayPartBuiltinTest(String name) {
		super(name);
	}

	public void testDayFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 20, 0,
				05, 0, 0);
		check(31, dateTime);
	}

	public void testDayFromDate() throws EvaluationException {
		ITerm date = Factory.CONCRETE.createDate(1999, 5, 31);
		check(31, date);
	}

	public void testDaysFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 10, 0,
				0);
		check(3, duration);

		duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 55, 0, 0);
		check(5, duration);

		duration = Factory.CONCRETE.createDuration(true, 3, 5, 0, 0, 0, 0);
		check(0, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		DayPartBuiltin builtin = new DayPartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}

}
