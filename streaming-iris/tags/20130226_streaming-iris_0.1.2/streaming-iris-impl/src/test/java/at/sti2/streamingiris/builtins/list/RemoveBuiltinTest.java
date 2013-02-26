package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public class RemoveBuiltinTest extends AbstractListBuiltinTest {

	private RemoveBuiltin builtin;

	private IList list_1, expected;

	public void testBuiltin() throws EvaluationException {

		try {
			builtin = new RemoveBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new RemoveBuiltin(EMPTY_LIST, EMPTY_LIST);

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();
		assertEquals(null, builtin.computeResult(EMPTY_LIST, ONE));

		// External( func:remove(List(0 1 2 3 4) 0) ) = List(1 2 3 4)
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, ZERO));

		// External( func:remove(List(0 1 2 3 4) 4) ) = List(0 1 2 3)
		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		assertEquals(expected, builtin.computeResult(list_1, FOUR));

		// External( func:remove(List(0 1 2 3 4) 5) ) = (unspecified)
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(5)));

		// External( func:remove(List(0 1 2 3 4) 6) ) = (unspecified)
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(6)));

		// External( func:remove(List(0 1 2 3 4) -1) ) = List(0 1 2 3)
		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-1)));

		// External( func:remove(List(0 1 2 3 4) -5) ) = List(1 2 3 4)
		expected.clear();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-5)));

		// External( func:remove(List(0 1 2 3 4) -6) ) = (unspecified)
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(-6)));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		expected.add(FOUR);

		check(list_1, TWO, expected);
	}

	private void check(ITerm listOne, ITerm term2, ITerm expectedResult)
			throws EvaluationException {
		builtin = new RemoveBuiltin(listOne, term2);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
