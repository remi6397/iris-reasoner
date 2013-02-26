package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class TimeLessBuiltinTest extends AbstractDateBuiltinTest {

	public TimeLessBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm time1 = Factory.CONCRETE.createTime(20, 12, 43.5, 0, 0);
		ITerm time2 = Factory.CONCRETE.createTime(20, 12, 43.5, 0, 0);
		ITerm time3 = Factory.CONCRETE.createTime(6, 34, 20.8, 0, 0);

		// time1 = time1
		TimeLessBuiltin builtin = new TimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(time1, time1);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		// time1 = time2
		builtin = new TimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(time1, time2);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		// time1 > time3
		builtin = new TimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(time1, time3);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		// time3 < time2
		builtin = new TimeLessBuiltin(X, Y);
		args = Factory.BASIC.createTuple(time3, time2);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);

	}
}