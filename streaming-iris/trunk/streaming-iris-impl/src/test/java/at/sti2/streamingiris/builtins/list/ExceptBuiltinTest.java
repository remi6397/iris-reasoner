package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;

public class ExceptBuiltinTest extends AbstractListBuiltinTest {
	private ExceptBuiltin builtin;

	private IList list_1, list_2, list_3, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new ExceptBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new ExceptBuiltin(EMPTY_LIST, EMPTY_LIST);

		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		assertEquals(expected, builtin.computeResult(list_1, list_2));

		// External( func:except(List(0 1 2 3 4) List(1 3)) ) = List(0 2 4)
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ONE);
		list_2.add(THREE);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ZERO);
		expected.add(TWO);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1, list_2));

		// External( func:except(List(0 1 2 3 4) List()) ) = List(0 1 2 3 4)
		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, EMPTY_LIST));

		// External( func:except(List(0 1 2 3 4) List(0 1 2 3 4)) ) = List()
		expected = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ZERO);
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(THREE);
		list_2.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, list_2));

		// External( func:except(List(1,[0]) List([0],[1])) ) = List(1)
		expected = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_1 = new at.sti2.streamingiris.terms.concrete.List();

		list_2.add(ZERO);

		list_1.add(ONE);
		list_1.add(list_2);

		list_3 = new at.sti2.streamingiris.terms.concrete.List();
		list_3.add(list_2);
		list_3.add(list_1);

		expected.add(ONE);
		assertEquals(expected, builtin.computeResult(list_1, list_3));

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(list_1);
		assertEquals(expected, builtin.computeResult(list_3, list_1));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(FOUR);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(THREE);

		check(list_1, list_2, expected);
	}

	private void check(ITerm listOne, ITerm term2, ITerm expectedResult)
			throws EvaluationException {
		builtin = new ExceptBuiltin(listOne, term2);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
