package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for MonthPartBuiltin.
 */
public class MonthPartBuiltinTest extends TestCase {

	private static ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public MonthPartBuiltinTest(String name) {
		super(name);
	}

	public void testMonthFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 20, 0,
				05, 0, 0);
		check(5, dateTime);
	}

	public void testMonthFromDate() throws EvaluationException {
		ITerm date = Factory.CONCRETE.createDate(1999, 5, 31);
		check(5, date);
	}

	public void testMonthsFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 20, 15, 0, 0, 0,
				0);
		check(3, duration);

		duration = Factory.CONCRETE.createDuration(false, 20, 18, 0, 0, 0, 0);
		check(-6, duration);

		duration = Factory.CONCRETE.createDuration(true, 0, 0, 2, 15, 0, 0);
		check(0, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		MonthPartBuiltin builtin = new MonthPartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
