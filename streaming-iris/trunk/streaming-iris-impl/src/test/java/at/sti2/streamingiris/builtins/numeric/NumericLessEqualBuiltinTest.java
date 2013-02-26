package at.sti2.streamingiris.builtins.numeric;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class NumericLessEqualBuiltinTest extends AbstractNumericTest {

	public NumericLessEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {
		ITerm term1 = Factory.CONCRETE.createDecimal(23.4);
		ITerm term2 = Factory.CONCRETE.createDecimal(23.4);

		NumericLessEqualBuiltin builtin = new NumericLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createDecimal(15.5);
		term2 = Factory.CONCRETE.createInteger(15);

		builtin = new NumericLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(15);
		term2 = Factory.CONCRETE.createInteger(15);

		builtin = new NumericLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);

		term1 = Factory.CONCRETE.createInteger(153);
		term2 = Factory.CONCRETE.createInteger(15);

		builtin = new NumericLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
		assertEquals(null, actual);

		term1 = Factory.CONCRETE.createInteger(50);
		term2 = Factory.CONCRETE.createInteger(123);

		builtin = new NumericLessEqualBuiltin(X, Y);
		args = Factory.BASIC.createTuple(term1, term2);
		actual = builtin.evaluate(args);
		assertEquals(EMPTY_TUPLE, actual);
	}

}
