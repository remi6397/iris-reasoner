package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.builtins.AddBuiltin;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class AddYearMonthDurationToDateBuiltinTest extends
		AbstractDateBuiltinTest {

	public AddYearMonthDurationToDateBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date0 = Factory.CONCRETE.createYearMonthDuration(true, 8, 2);
		ITerm date1 = Factory.CONCRETE.createDate(10, 4, 2);
		ITerm result = Factory.CONCRETE.createDate(18, 6, 2);

		AddBuiltin builtin = new AddYearMonthDurationToDateBuiltin(X, Y, Z);

		args = Factory.BASIC.createTuple(date1, date0, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}
}