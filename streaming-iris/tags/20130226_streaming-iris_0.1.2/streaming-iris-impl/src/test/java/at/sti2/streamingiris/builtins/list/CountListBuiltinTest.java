package at.sti2.streamingiris.builtins.list;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public class CountListBuiltinTest extends AbstractListBuiltinTest {

	private CountListBuiltin builtin;

	private IList list_1, list_2, list_3;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new CountListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new CountListBuiltin(EMPTY_LIST);
		assertEquals(new IntTerm(0), builtin.computeResult(EMPTY_LIST));

		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		assertFalse(list_1.isEmpty());
		assertEquals(new IntTerm(1), builtin.computeResult(list_1));

		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		list_2.add(ONE);
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(list_1);

		assertEquals(new IntTerm(4), builtin.computeResult(list_2));

		list_3 = new at.sti2.streamingiris.terms.concrete.List();
		list_3.add(list_1);
		list_3.add(ONE);
		list_3.add(TWO);
		list_3.add(THREE);

		assertFalse(list_2.equals(list_3));
		assertEquals(builtin.computeResult(list_2),
				builtin.computeResult(list_3));

	}

	public void testTupleCount() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);

		IntTerm expected = new IntTerm(2);

		check(list_1, expected);
	}

	private void check(ITerm listOne, ITerm expectedResult)
			throws EvaluationException {
		builtin = new CountListBuiltin(listOne);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple expectedTuple = BASIC.createTuple(expectedResult);

		ITuple actualTuple = builtin.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}
}
