package at.sti2.streamingiris.builtins.list;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public class SubListFromBuiltinTest extends AbstractListBuiltinTest {

	private SubListFromBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {

		try {
			builtin = new SubListFromBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new SubListFromBuiltin(EMPTY_LIST, new IntTerm(0));
		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		// External( func:sublist(List(0 1 2 3 4) 0) ) = List(0 1 2 3 4)
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

		assertEquals(expected, builtin.computeResult(list_1, ZERO));

		// External( func:sublist(List(0 1 2 3 4) 3) ) = List(3 4)
		expected.clear();
		expected.add(THREE);
		expected.add(FOUR);

		assertEquals(expected, builtin.computeResult(list_1, THREE));

		// External( func:sublist(List(0 1 2 3 4) -2) ) = List(3 4)
		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-2)));

		//
		assertEquals(null, builtin.computeResult(list_1, new IntTerm(-10)));

		list_2.add(ONE);
		list_2.add(THREE);

		list_1.add(list_2);
		expected.add(list_2);

		assertEquals(expected, builtin.computeResult(list_1, new IntTerm(-3)));

	}
}
