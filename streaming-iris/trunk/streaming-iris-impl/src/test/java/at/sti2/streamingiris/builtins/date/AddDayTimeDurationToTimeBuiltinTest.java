package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class AddDayTimeDurationToTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddDayTimeDurationToTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm daytimeduration = Factory.CONCRETE.createDayTimeDuration(true, 0,
				2, 0, 0, 0);
		ITerm time1 = Factory.CONCRETE.createTime(10, 4, 26.3, 0, 0);
		ITerm result = Factory.CONCRETE.createTime(12, 4, 26.3, 0, 0);

		AddBuiltin builtin = new AddDayTimeDurationToTimeBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(time1, daytimeduration, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}