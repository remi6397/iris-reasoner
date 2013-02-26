package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DurationEqualBuiltinTest extends AbstractDateBuiltinTest {

	public DurationEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm duration1 = Factory.CONCRETE.createDuration(true, 1988, 5, 12, 6,
				2, 11);
		ITerm duration2 = Factory.CONCRETE.createDuration(true, 1988, 5, 12, 6,
				2, 11);
		ITerm duration3 = Factory.CONCRETE.createDuration(true, 1979, 10, 28,
				12, 56, 23);
		ITerm duration4 = Factory.CONCRETE.createDuration(false, 1991, 1, 8,
				12, 56, 23);

		DurationEqualBuiltin builtin = new DurationEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(duration1, duration1);
		actual = builtin.evaluate(args);
		// duration1 = duration1
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DurationEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(duration1, duration2);
		actual = builtin.evaluate(args);
		// duration1 = duration2
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DurationEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(duration4, duration3);
		actual = builtin.evaluate(args);
		// duration1 > duration3
		assertEquals(null, actual);

		builtin = new DurationEqualBuiltin(duration3, duration4);
		args = Factory.BASIC.createTuple(X);
		actual = builtin.evaluate(args);
		// duration3 < duration4
		assertEquals(null, actual);

	}

}