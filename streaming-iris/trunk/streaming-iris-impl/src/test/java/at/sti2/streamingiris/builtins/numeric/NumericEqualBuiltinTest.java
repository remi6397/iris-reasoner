package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericEqualBuiltinTest extends AbstractNumericTest {

	public NumericEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(23.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(23.4);

		NumericEqualBuiltin builtin = new NumericEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createDecimal(15.0);

		builtin = new NumericEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createDecimal(15.342);

		builtin = new NumericEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);
	}

}
