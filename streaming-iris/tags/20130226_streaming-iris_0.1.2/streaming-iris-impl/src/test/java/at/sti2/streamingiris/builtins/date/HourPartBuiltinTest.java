package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for HourPartBuiltin.
 */
public class HourPartBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public HourPartBuiltinTest(String name) {
		super(name);
	}

	public void testHoursFromTime() throws EvaluationException {
		ITerm time = Factory.CONCRETE.createTime(8, 53, 23.5, 0, 0);
		check(8, time);

		time = Factory.CONCRETE.createTime(24, 0, 0.0, 0, 0);
		check(0, time);
	}

	public void testHoursFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 24, 0,
				00, 0, 0);
		check(0, dateTime);
	}

	public void testHoursFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 10, 0,
				0);
		check(10, duration);

		duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 12, 32, 12);
		check(12, duration);

		duration = Factory.CONCRETE.createDuration(true, 0, 0, 0, 123, 0, 0);
		check(3, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 0, 3, 10, 0, 0);
		check(-10, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		HourPartBuiltin builtin = new HourPartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
