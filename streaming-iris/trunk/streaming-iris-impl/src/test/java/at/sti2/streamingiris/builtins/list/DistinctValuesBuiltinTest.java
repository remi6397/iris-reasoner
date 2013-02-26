package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;

public class DistinctValuesBuiltinTest extends AbstractListBuiltinTest {

	private DistinctValuesBuiltin builtin;

	private IList list_1, list_2, list_3, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new DistinctValuesBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new DistinctValuesBuiltin(EMPTY_LIST);

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		assertEquals(expected, builtin.computeResult(list_1));

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ONE);
		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		assertEquals(expected, builtin.computeResult(list_2));

		list_3 = new at.sti2.streamingiris.terms.concrete.List();
		list_3.add(ONE);
		list_3.add(ONE);
		assertEquals(expected, builtin.computeResult(list_3));
		assertEquals(list_2, builtin.computeResult(list_3));

		// External( func:distinct-values(List(0 1 2 3 4)) ) = List(0 1 2 3 4)
		list_1.clear();
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1));

		// External( func:distinct-values(List(0 1 2 3 4 0 4)) ) = List(0 1 2 3
		// 4)
		list_1.add(ZERO);
		list_1.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1));

		// External( func:distinct-values(List(3 3 3)) ) = List(3)
		list_1.clear();
		list_1.add(THREE);
		list_1.add(THREE);
		list_1.add(THREE);

		expected.clear();
		expected.add(THREE);
		assertEquals(expected, builtin.computeResult(list_1));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(FOUR);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(FOUR);

		check(list_1, expected);
	}

	private void check(ITerm listOne, ITerm expectedResult)
			throws EvaluationException {
		builtin = new DistinctValuesBuiltin(listOne);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
