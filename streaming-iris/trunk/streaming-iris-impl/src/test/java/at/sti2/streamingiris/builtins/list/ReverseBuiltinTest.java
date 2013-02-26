package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;

public class ReverseBuiltinTest extends AbstractListBuiltinTest {

	private ReverseBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {

		try {
			builtin = new ReverseBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new ReverseBuiltin(EMPTY_LIST);
		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		// External( func:reverse(List()) ) = List()
		assertEquals(EMPTY_LIST, builtin.computeResult(EMPTY_LIST));

		// External( func:reverse(List(1)) ) = List(1)
		list_1.add(ONE);
		expected.add(ONE);
		assertEquals(expected, builtin.computeResult(list_1));

		// External( func:reverse(List(0 1 2 3 4)) ) = List(4 3 2 1 0)
		list_1.clear();
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.clear();
		expected.add(FOUR);
		expected.add(THREE);
		expected.add(TWO);
		expected.add(ONE);
		expected.add(ZERO);

		list_2.clear();
		list_2.addAll(list_1);

		assertEquals(list_1, list_2);
		assertEquals(expected, builtin.computeResult(list_1));
		assertEquals(list_1, list_2);

		//
		list_1.add(list_2);
		expected.clear();
		expected.add(list_2);
		expected.add(FOUR);
		expected.add(THREE);
		expected.add(TWO);
		expected.add(ONE);
		expected.add(ZERO);
		assertEquals(expected, builtin.computeResult(list_1));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(FOUR);
		list_1.add(THREE);
		list_1.add(TWO);
		list_1.add(ONE);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		check(list_1, expected);
	}

	private void check(ITerm listOne, ITerm expectedResult)
			throws EvaluationException {
		builtin = new ReverseBuiltin(listOne);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
