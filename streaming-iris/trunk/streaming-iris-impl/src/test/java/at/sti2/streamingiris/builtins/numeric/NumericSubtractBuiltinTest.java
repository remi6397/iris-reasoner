package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericSubtractBuiltinTest extends AbstractNumericTest {

	public NumericSubtractBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(23.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(20.1);
		ITerm result = Factory.CONCRETE.createDecimal(3.3);

		NumericSubtractBuiltin builtin = new NumericSubtractBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(23.4);
		term2 = Factory.CONCRETE.createDecimal(0.1);
		result = Factory.CONCRETE.createDecimal(43.5);

		builtin = new NumericSubtractBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(20);
		term2 = Factory.CONCRETE.createDecimal(2.1);
		result = Factory.CONCRETE.createDecimal(17.9);

		builtin = new NumericSubtractBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}

}
