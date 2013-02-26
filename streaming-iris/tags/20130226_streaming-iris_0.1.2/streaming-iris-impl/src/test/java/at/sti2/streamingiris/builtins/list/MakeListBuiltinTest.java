package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public class MakeListBuiltinTest extends AbstractListBuiltinTest {

	private MakeListBuiltin builtin;

	private IList list_1, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new MakeListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new MakeListBuiltin(EMPTY_LIST, EMPTY_LIST);

		// External( func:make-list() ) = List()
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		assertEquals(expected, builtin.computeResult());

		list_1.clear();
		expected.clear();
		expected.add(EMPTY_LIST);

		assertEquals(expected, builtin.computeResult(EMPTY_LIST));

		// External( func:make-list(0 1 List(20 21))) = List(0 1 List(20 21))
		list_1.clear();
		list_1.add(new IntTerm(20));
		list_1.add(new IntTerm(21));
		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(list_1);

		assertEquals(expected, builtin.computeResult(ZERO, ONE, list_1));
	}

	public void testTupleBuiltin() throws EvaluationException {
		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(TWO);
		expected.add(THREE);

		check(expected, ONE, TWO, TWO, THREE);
	}

	private void check(ITerm expectedResult, ITerm... values)
			throws EvaluationException {
		builtin = new MakeListBuiltin(values);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
