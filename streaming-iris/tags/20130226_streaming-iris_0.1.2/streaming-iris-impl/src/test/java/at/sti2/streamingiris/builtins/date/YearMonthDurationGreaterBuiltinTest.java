package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class YearMonthDurationGreaterBuiltinTest extends
		AbstractDateBuiltinTest {

	public YearMonthDurationGreaterBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createYearMonthDuration(true, 20, 10);
		ITerm date2 = Factory.CONCRETE.createYearMonthDuration(true, 20, 10);
		ITerm date3 = Factory.CONCRETE.createYearMonthDuration(true, 0, 6);
		ITerm date4 = Factory.CONCRETE.createYearMonthDuration(true, 456, 4);
		ITerm date5 = Factory.CONCRETE.createYearMonthDuration(false, 11, 3);

		YearMonthDurationGreaterBuiltin builtin = new YearMonthDurationGreaterBuiltin(
				X, Y);
		args = Factory.BASIC.createTuple(date1, date1);
		actual = builtin.evaluate(args);
		// (date1 = date1) -> null
		assertEquals(null, actual);

		builtin = new YearMonthDurationGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);
		// (date1 = date2) -> null
		assertEquals(null, actual);

		builtin = new YearMonthDurationGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date3, date4);
		actual = builtin.evaluate(args);
		// (date3 < date4) -> null
		assertEquals(null, actual);

		builtin = new YearMonthDurationGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date3);
		actual = builtin.evaluate(args);
		// (date4 > date3) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new YearMonthDurationGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date5);
		actual = builtin.evaluate(args);
		// (date4 > date5) -> null
		assertEquals(EMPTY_TUPLE, actual);
	}

}