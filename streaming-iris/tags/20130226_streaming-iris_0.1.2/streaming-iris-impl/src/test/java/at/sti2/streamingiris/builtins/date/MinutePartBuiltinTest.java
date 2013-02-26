package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for MinutePartBuiltin.
 */
public class MinutePartBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public MinutePartBuiltinTest(String name) {
		super(name);
	}

	public void testMinutesFromTime() throws EvaluationException {
		ITerm time = Factory.CONCRETE.createTime(8, 55, 23.5, 0, 0);
		check(55, time);
	}

	public void testMinutesFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 13, 20,
				0, 0, 0);
		check(20, dateTime);
	}

	public void testMinutesFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 10, 0,
				0);
		check(0, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 0, 5, 12, 30, 0);
		check(-30, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		MinutePartBuiltin builtin = new MinutePartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
