package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericModulusBuiltinTest extends AbstractNumericTest {

	public NumericModulusBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(24.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(3.0);
		ITerm result = Factory.CONCRETE.createDecimal(0.4);

		NumericModulusBuiltin builtin = new NumericModulusBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(32);
		term2 = Factory.CONCRETE.createInteger(4);
		result = Factory.CONCRETE.createInteger(0);

		builtin = new NumericModulusBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(10);
		term2 = Factory.CONCRETE.createInteger(3);
		result = Factory.CONCRETE.createInteger(1);

		builtin = new NumericModulusBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(10.0);
		term2 = Factory.CONCRETE.createInteger(3);
		result = Factory.CONCRETE.createInteger(1);

		builtin = new NumericModulusBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(23.0);
		term2 = Factory.CONCRETE.createInteger(3);
		result = Factory.CONCRETE.createInteger(2);

		builtin = new NumericModulusBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}

}
