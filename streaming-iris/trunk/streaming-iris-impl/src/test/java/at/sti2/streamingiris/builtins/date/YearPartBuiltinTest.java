package at.sti2.streamingiris.builtins.date;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for YearPartBuiltin.
 */
public class YearPartBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	public YearPartBuiltinTest(String name) {
		super(name);
	}

	public void testYearFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 20, 0,
				05, 0, 0);
		check(1999, dateTime);

		dateTime = Factory.CONCRETE
				.createDateTime(1999, 12, 31, 24, 0, 0, 0, 0);
		check(2000, dateTime);
	}

	public void testYearFromDate() throws EvaluationException {
		ITerm date = Factory.CONCRETE.createDate(1999, 5, 31);
		check(1999, date);
	}

	public void testYearsFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 20, 15, 0, 0, 0,
				0);
		check(21, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 15, 0, 0, 0, 0);
		check(-1, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 0, 2, 15, 0, 0);
		check(0, duration);
	}

	private void check(int expected, ITerm time) throws EvaluationException {
		YearPartBuiltin builtin = new YearPartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createInteger(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
