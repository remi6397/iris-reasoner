package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DayTimeDurationLessEqualBuiltinTest extends
		AbstractDateBuiltinTest {

	public DayTimeDurationLessEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDayTimeDuration(true, 15, 23, 19,
				12.0);
		ITerm date2 = Factory.CONCRETE.createDayTimeDuration(true, 15, 23, 19,
				12.0);
		ITerm date3 = Factory.CONCRETE.createDayTimeDuration(true, 10, 3, 2,
				0.9);
		ITerm date4 = Factory.CONCRETE.createDayTimeDuration(false, 4, 9, 56,
				47.3);
		ITerm date5 = Factory.CONCRETE.createDayTimeDuration(false, 20, 19, 6,
				3.3);
		ITerm date6 = Factory.CONCRETE.createDayTimeDuration(false, 0, 0, 0, 0);
		ITerm date7 = Factory.CONCRETE.createDayTimeDuration(false, 0, 0, 0, 0);

		DayTimeDurationLessEqualBuiltin builtin = new DayTimeDurationLessEqualBuiltin(
				X, Y);
		args = Factory.BASIC.createTuple(date1, date1);
		actual = builtin.evaluate(args);
		// (date1 = date1) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DayTimeDurationLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);
		// (date1 = date2) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DayTimeDurationLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date3, date4);
		actual = builtin.evaluate(args);
		// (date3 > date4) -> null
		assertEquals("Should be null", null, actual);

		builtin = new DayTimeDurationLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date3);
		actual = builtin.evaluate(args);
		// (date4 < date3) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DayTimeDurationLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date5);
		actual = builtin.evaluate(args);
		// (date4 < date5) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DayTimeDurationLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date5, date4);
		actual = builtin.evaluate(args);
		// (date5 > date4) -> null
		assertEquals(null, actual);

		builtin = new DayTimeDurationLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date6, date7);
		actual = builtin.evaluate(args);
		// (date6 = date7) -> EMPTY_TUPLE
		assertEquals(EMPTY_TUPLE, actual);
	}

}