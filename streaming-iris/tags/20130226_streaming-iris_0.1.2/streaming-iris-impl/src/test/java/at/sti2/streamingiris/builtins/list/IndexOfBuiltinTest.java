package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public class IndexOfBuiltinTest extends AbstractListBuiltinTest {

	private IndexOfBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new IndexOfBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new IndexOfBuiltin(EMPTY_LIST, EMPTY_LIST);

		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();
		assertEquals(expected, builtin.computeResult(EMPTY_LIST, ONE));

		//
		list_1.add(ONE);
		expected.add(ZERO);
		assertEquals(expected, builtin.computeResult(list_1, ONE));

		// External( func:index-of(List(0 1 2 3 4) 2) ) = List(2)
		list_1.clear();
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.clear();
		expected.add(TWO);
		assertEquals(expected, builtin.computeResult(list_1, TWO));

		// External( func:index-of(List(0 1 2 3 4 5 2 2) 2) ) = List(2 6 7)
		list_1.add(new IntTerm(5));
		list_1.add(TWO);
		list_1.add(TWO);
		expected.clear();
		expected.add(TWO);
		expected.add(new IntTerm(6));
		expected.add(new IntTerm(7));
		assertEquals(expected, builtin.computeResult(list_1, TWO));

		// External( func:index-of(List(2 2 3 4 2 2) 1) ) = List()
		list_1.clear();
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);
		list_1.add(TWO);
		list_1.add(TWO);
		expected.clear();
		assertEquals(expected, builtin.computeResult(list_1, ONE));

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ONE);
		list_2.add(THREE);

		list_1.add(list_2);
		assertEquals(expected, builtin.computeResult(list_1, ONE));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(THREE);

		check(list_1, new IntTerm(3), expected);
	}

	private void check(ITerm listOne, ITerm term2, ITerm expectedResult)
			throws EvaluationException {
		builtin = new IndexOfBuiltin(listOne, term2);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
