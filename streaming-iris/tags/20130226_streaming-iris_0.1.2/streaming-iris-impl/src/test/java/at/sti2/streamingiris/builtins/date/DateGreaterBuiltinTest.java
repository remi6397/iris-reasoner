package at.sti2.streamingiris.builtins.date;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class DateGreaterBuiltinTest extends AbstractDateBuiltinTest {

	public DateGreaterBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm date1 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm date2 = Factory.CONCRETE.createDate(2010, 4, 26);
		ITerm date3 = Factory.CONCRETE.createDate(2010, 5, 26);
		ITerm date4 = Factory.CONCRETE.createDate(1997, 3, 12);

		DateGreaterBuiltin builtin = new DateGreaterBuiltin(X, Y);

		args = Factory.BASIC.createTuple(date1, date2);
		actual = builtin.evaluate(args);
		// (date1 = date2) -> null
		assertEquals(null, actual);

		builtin = new DateGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date3, date4);
		actual = builtin.evaluate(args);
		// (date3 > date4) -> iTuple()
		assertEquals(EMPTY_TUPLE, actual);

		builtin = new DateGreaterBuiltin(X, Y);
		args = Factory.BASIC.createTuple(date4, date3);
		actual = builtin.evaluate(args);
		// (date4 < date3) -> null
		assertEquals(null, actual);
	}

}
