package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.factory.Factory;

public class AddDayTimeDurationToDateBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddDayTimeDurationToDateBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm dayTimeDuration = Factory.CONCRETE.createDayTimeDuration(true, 3,
				0, 0, 0, 0);
		ITerm date1 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm result = Factory.CONCRETE.createDate(2010, 4, 29);

		AddBuiltin builtin = new AddDayTimeDurationToDateBuiltin(date1,
				dayTimeDuration, result);

		args = Factory.BASIC.createTuple(X, Y, Z);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}

}
