package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class AddDayTimeDurationToDateTimeBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddDayTimeDurationToDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm dayTimeDuration = Factory.CONCRETE.createDayTimeDuration(true, 0,
				2, 0, 0, 0);
		ITerm date1 = Factory.CONCRETE.createDateTime(2010, 4, 10, 1, 12, 12,
				12, 1);
		ITerm result = Factory.CONCRETE.createDateTime(2010, 4, 10, 3, 12, 12,
				12, 1);

		AddBuiltin builtin = new AddDayTimeDurationToDateTimeBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, dayTimeDuration, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

	}

}