package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;

public class AppendBuiltinTest extends AbstractListBuiltinTest {

	private AppendBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new AppendBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new AppendBuiltin(EMPTY_LIST, EMPTY_LIST);

		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(EMPTY_LIST);

		assertEquals(expected, builtin.computeResult(list_1, EMPTY_LIST));

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);

		assertEquals(expected, builtin.computeResult(list_1, TWO));

		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(TWO);
		list_1.add(list_2);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(list_2);
		expected.add(TWO);
		expected.add(THREE);

		assertFalse(expected.equals(builtin.computeResult(list_1, TWO)));
		assertFalse(expected.equals(list_1));
		assertFalse(expected.equals(builtin.computeResult(list_1, TWO, THREE,
				list_1)));
		assertEquals(expected, builtin.computeResult(list_1, TWO, THREE));
	}

	public void testTuple1() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(TWO);

		check(list_1, TWO, expected);
	}

	public void testTuple2() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_2 = new at.sti2.streamingiris.terms.concrete.List();

		expected = new at.sti2.streamingiris.terms.concrete.List();
		expected.add(ONE);
		expected.add(list_2);

		check(list_1, list_2, expected);
	}

	private void check(ITerm listOne, ITerm term2, ITerm expectedResult)
			throws EvaluationException {
		builtin = new AppendBuiltin(listOne, term2);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
