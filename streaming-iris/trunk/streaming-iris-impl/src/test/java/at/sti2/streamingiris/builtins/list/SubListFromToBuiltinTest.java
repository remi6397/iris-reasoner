package at.sti2.streamingiris.builtins.list;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.terms.concrete.IntTerm;

public class SubListFromToBuiltinTest extends AbstractListBuiltinTest {

	private SubListFromToBuiltin builtin;

	private IList list_1, list_2, expected;

	public void testBuiltin() throws EvaluationException {

		try {
			builtin = new SubListFromToBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if built-in has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}

		builtin = new SubListFromToBuiltin(EMPTY_LIST, new IntTerm(0),
				new IntTerm(0));
		//
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();
		expected = new at.sti2.streamingiris.terms.concrete.List();

		// External( func:sublist(List(0 1 2 3 4) 0 0) ) = List()
		assertEquals(expected, builtin.computeResult(list_1, ZERO, ZERO));
		list_1.add(ZERO);
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(THREE);
		list_1.add(FOUR);
		assertEquals(expected, builtin.computeResult(list_1, ZERO, ZERO));

		// External( func:sublist(List(0 1 2 3 4) 0 1) ) = List(0)
		list_2.clear();
		list_2.add(ZERO);
		assertEquals(list_2, builtin.computeResult(list_1, ZERO, ONE));

		// External( func:sublist(List(0 1 2 3 4) 0 4) ) = List(0 1 2 3)
		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		expected.add(THREE);
		assertEquals(expected, builtin.computeResult(list_1, ZERO, FOUR));

		// External( func:sublist(List(0 1 2 3 4) 0 10) ) = List(0 1 2 3 4)
		expected.add(FOUR);
		assertEquals(expected,
				builtin.computeResult(list_1, ZERO, new IntTerm(10)));

		// External( func:sublist(List(0 1 2 3 4) 0 -2) ) = List(0 1 2)
		expected.clear();
		expected.add(ZERO);
		expected.add(ONE);
		expected.add(TWO);
		assertEquals(expected,
				builtin.computeResult(list_1, ZERO, new IntTerm(-2)));

		// External( func:sublist(List(0 1 2 3 4) 2 4) ) = List(2 3)
		expected.clear();
		expected.add(TWO);
		expected.add(THREE);
		assertEquals(expected, builtin.computeResult(list_1, TWO, FOUR));

		// External( func:sublist(List(0 1 2 3 4) 2 -2) ) = List(2)
		expected.clear();
		expected.add(TWO);
		assertEquals(expected,
				builtin.computeResult(list_1, TWO, new IntTerm(-2)));

	}
}
