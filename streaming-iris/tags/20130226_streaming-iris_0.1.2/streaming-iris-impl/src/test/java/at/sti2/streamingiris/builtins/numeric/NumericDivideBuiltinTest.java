package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericDivideBuiltinTest extends AbstractNumericTest {

	public NumericDivideBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(24.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(2.0);
		ITerm result = Factory.CONCRETE.createDecimal(12.2);

		NumericDivideBuiltin builtin = new NumericDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(32);
		term2 = Factory.CONCRETE.createInteger(4);
		result = Factory.CONCRETE.createInteger(8);

		builtin = new NumericDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(100.5);
		term2 = Factory.CONCRETE.createInteger(2);
		result = Factory.CONCRETE.createDecimal(50.25);

		builtin = new NumericDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(1.5);
		term2 = Factory.CONCRETE.createInteger(3);
		result = Factory.CONCRETE.createDecimal(50.25);

		builtin = new NumericDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);
	}

}
