package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericIntegerDivideBuiltinTest extends AbstractNumericTest {

	public NumericIntegerDivideBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createInteger(24);
		ITerm term2 = Factory.CONCRETE.createDecimal(2.0);
		ITerm result = Factory.CONCRETE.createInteger(12);

		NumericIntegerDivideBuiltin builtin = new NumericIntegerDivideBuiltin(
				X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		term1 = Factory.CONCRETE.createInteger(32);
		term2 = Factory.CONCRETE.createInteger(4);
		result = Factory.CONCRETE.createInteger(8);

		builtin = new NumericIntegerDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(100.5);
		term2 = Factory.CONCRETE.createInteger(2);
		result = Factory.CONCRETE.createDecimal(50.25);

		builtin = new NumericIntegerDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(10);
		term2 = Factory.CONCRETE.createInteger(2);
		result = Factory.CONCRETE.createInteger(5);

		builtin = new NumericIntegerDivideBuiltin(X, Y, Z);
		args = Factory.BASIC.createTuple(term1, term2, result);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}
}
