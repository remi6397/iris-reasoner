package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.concrete.IList;
import at.sti2.streamingiris.builtins.list.AbstractListBuiltinTest;

public class IsListBuiltinTest extends AbstractListBuiltinTest {

	private IsListBuiltin builtin;

	private IList list_1, list_2;

	public void testBuiltin() throws EvaluationException {
		try {
			builtin = new IsListBuiltin();
			System.out.println(builtin.toString());
			fail("An IllegalArgumentException should be thrown if builtin has the wrong amount of paramenters.");
		} catch (IllegalArgumentException e) {
		}
		builtin = new IsListBuiltin(EMPTY_LIST);
		ITerm[] terms = { EMPTY_LIST, null };

		assertEquals(true, builtin.computeResult(terms));
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_2 = new at.sti2.streamingiris.terms.concrete.List();

		// External(pred:is-list(1)) will evaluate to f in any interpretation.
		terms[0] = ONE;
		assertEquals(false, builtin.computeResult(terms));
		terms[1] = list_1;
		assertEquals(false, builtin.computeResult(terms));

		list_1.add(ONE);
		assertFalse(list_1.isEmpty());
		terms[0] = list_1;
		terms[1] = null;
		assertEquals(true, builtin.computeResult(terms));

		list_2.add(ONE);
		list_2.add(ONE);
		list_2.add(TWO);
		list_2.add(list_1);
		terms[0] = list_2;
		terms[1] = null;

		ITerm[] terms2 = { list_2 };
		assertEquals(true, builtin.computeResult(terms));
		assertFalse(list_2.equals(list_1));
		assertEquals(builtin.computeResult(terms),
				builtin.computeResult(terms2));
	}

	public void testTupleBuiltin() throws EvaluationException {
		list_1 = new at.sti2.streamingiris.terms.concrete.List();
		list_1.add(ONE);
		list_1.add(TWO);
		list_1.add(TWO);
		list_1.add(THREE);

		check(list_1, true);
	}

	private void check(ITerm listOne, boolean expected)
			throws EvaluationException {
		builtin = new IsListBuiltin(listOne);

		ITuple arguments = BASIC.createTuple(X, Y, Z);

		ITuple actualTuple = builtin.evaluate(arguments);

		if (expected) {
			assertNotNull(actualTuple);
		} else {
			assertNull(actualTuple);
		}
	}
}
