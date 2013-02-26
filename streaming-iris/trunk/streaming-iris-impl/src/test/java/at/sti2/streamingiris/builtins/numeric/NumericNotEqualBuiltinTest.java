package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericNotEqualBuiltinTest extends AbstractNumericTest {

	public NumericNotEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(23.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(23.4);

		NumericNotEqualBuiltin builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createDecimal(15.0);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createDecimal(15.342);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(132);
		term2 = Factory.CONCRETE.createInteger(3);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(23.5);
		term2 = Factory.CONCRETE.createDecimal(23.3);

		builtin = new NumericNotEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);

		assertEquals(EMPTY_TUPLE, actual);
	}

}
