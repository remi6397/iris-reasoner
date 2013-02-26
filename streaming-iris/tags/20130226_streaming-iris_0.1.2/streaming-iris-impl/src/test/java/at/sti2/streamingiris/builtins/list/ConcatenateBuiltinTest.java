package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;

public class ConcatenateBuiltinTest extends AbstractListBuiltinTest {

	private ConcatenateBuiltin builtin;

	private IList list_1, list_2, list_3, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new ConcatenateBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new ConcatenateBuiltin(EMPTY_LIST, EMPTY_LIST);

		// concatenation of 2 empty lists:
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		assertEquals(expected, builtin.computeResult(list_1, EMPTY_LIST));

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);

		assertEquals(expected, builtin.computeResult(list_1, EMPTY_LIST));

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ONE);
		list_2.add(TWO);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(ONE);
		expected.add(TWO);

		assertEquals(expected, builtin.computeResult(list_1, list_2));

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(TWO);

		list_3 = new at.sti2.streamingiris.terms.concrete.List();
		list_3.add(ONE);
		list_3.add(THREE);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(ONE);
		expected.add(THREE);

		assertEquals(expected, builtin.computeResult(list_1, list_2, list_3));

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(TWO);
		list_2.add(list_1);

		list_3 = new at.sti2.streamingiris.terms.concrete.List();
		list_3.add(THREE);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(TWO);
		expected.add(list_1);
		expected.add(THREE);

		assertFalse(list_3
				.equals(builtin.computeResult(list_1, list_2, list_3)));
		assertEquals(expected, builtin.computeResult(list_1, list_2, list_3));

		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(TWO);
		list_2.add(list_1);

		list_3 = new at.sti2.streamingiris.terms.concrete.List();
		list_3.add(list_2);
		list_3.add(list_1);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(TWO);
		expected.add(list_1);
		expected.add(list_2);
		expected.add(list_1);

		assertFalse(list_3
				.equals(builtin.computeResult(list_1, list_2, list_3)));
		assertEquals(expected, builtin.computeResult(list_1, list_2, list_3));
	}

	public void testTupleConcatenation() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(FOUR);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);
		expected.add(FOUR);

		check(list_1, list_2, expected);
	}

	private void check(ITerm listOne, ITerm term2, ITerm expectedResult)
			throws EvaluationException {
		builtin = new ConcatenateBuiltin(listOne, term2);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
