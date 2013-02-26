package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DateNotEqualBuiltinTest extends AbstractDateBuiltinTest {

	public DateNotEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm date2 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm date3 = Factory.CONCRETE.createDate(1997, 3, 12);

		DateNotEqualBuiltin builtin = new DateNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date1);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		builtin = new DateNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		builtin = new DateNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date1, date3);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);

	}

}
