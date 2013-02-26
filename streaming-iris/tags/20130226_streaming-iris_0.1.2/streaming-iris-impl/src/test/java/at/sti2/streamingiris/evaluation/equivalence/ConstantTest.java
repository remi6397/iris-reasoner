package at.sti2.streamingiris.evaluation.equivalence;

import java.util.ArrayList;
import java.util.Collection;

import at.sti2.streamingiris.evaluation.ProgramEvaluationTest;
import at.sti2.streamingiris.rules.compiler.Helper;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Test for correct evaluation of examples with rule head equality.
 * 
 * @author Adrian Marte
 */
public class ConstantTest extends ProgramEvaluationTest {

	public ConstantTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("p('A').");
		expressions.add("q('C').");
		expressions.add("r('C', '1', 'A').");

		// Create rules.
		expressions.add("'A' = 'B' :- q('C').");

		return expressions;
	}

	public void testP() throws Exception {
		// The result should be: a, b

		IRelation relation = evaluate("?- p(?X).");

		assertTrue("A not in relation.",
				relation.contains(Helper.createConstantTuple("A")));
		assertTrue("B not in relation.",
				relation.contains(Helper.createConstantTuple("B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

	public void testR() throws Exception {
		// The result should be: (C, 1, A), (C, 1, B)

		IRelation relation = evaluate("?- r(?X, ?Y, ?Z).");

		assertTrue("(C, 1, A) not in relation.",
				relation.contains(Helper.createConstantTuple("C", "1", "A")));
		assertTrue("(C, 1, B) not in relation.",
				relation.contains(Helper.createConstantTuple("C", "1", "B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

}
