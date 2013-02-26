package at.sti2.streamingiris.builtins.date;

import static at.sti2.streamingiris.factory.Factory.TERM;
import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for SecondPartBuiltin.
 */
public class SecondPartBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = TERM.createVariable("Y");

	public SecondPartBuiltinTest(String name) {
		super(name);
	}

	public void testSecondsFromTime() throws EvaluationException {
		ITerm time = Factory.CONCRETE.createTime(8, 55, 23, 0, 0);
		check(23.0, time);
	}

	public void testSecondsFromDateTime() throws EvaluationException {
		ITerm dateTime = Factory.CONCRETE.createDateTime(1999, 5, 31, 13, 0,
				20, 0, 0);
		check(20, dateTime);
	}

	public void testSecondsFromDuration() throws EvaluationException {
		ITerm duration = Factory.CONCRETE.createDuration(true, 0, 0, 3, 10, 0,
				12.5);
		check(12.5, duration);

		duration = Factory.CONCRETE.createDuration(false, 0, 0, 5, 12, 0, 30);
		check(-30, duration);
	}

	private void check(double expected, ITerm time) throws EvaluationException {
		SecondPartBuiltin builtin = new SecondPartBuiltin(time, Y);

		ITuple arguments = Factory.BASIC.createTuple(X, Y);
		ITuple expectedTuple = Factory.BASIC.createTuple(Factory.CONCRETE
				.createDecimal(expected));
		ITuple actual = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actual);
	}
}
