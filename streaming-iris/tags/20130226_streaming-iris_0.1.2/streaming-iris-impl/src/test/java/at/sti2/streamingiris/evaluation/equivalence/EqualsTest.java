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
public class EqualsTest extends ProgramEvaluationTest {

	public EqualsTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("a('A').");
		expressions.add("b('B').");

		// Create rules.
		expressions.add("?X = ?Y :- a(?X), b(?Y), ?X = 'A', ?Y = 'B'.");
		expressions.add("?X = ?Y :- b(?Y), ?X = 'A', ?Y = 'B'.");

		expressions.add("'Peter' = 'Peter_Griffin' :- .");
		expressions.add("unsat('foo') :- 'Peter' = 'Peter_Griffin'.");

		return expressions;
	}

	public void testA() throws Exception {
		// The result should be: A, B

		IRelation relation = evaluate("?- a(?X).");

		assertTrue("A not in relation.",
				relation.contains(Helper.createConstantTuple("A")));
		assertTrue("B not in relation.",
				relation.contains(Helper.createConstantTuple("B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

	public void testB() throws Exception {
		// The result should be: A, B

		IRelation relation = evaluate("?- b(?X).");

		assertTrue("A not in relation.",
				relation.contains(Helper.createConstantTuple("A")));
		assertTrue("B not in relation.",
				relation.contains(Helper.createConstantTuple("B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

	public void testUnsat() throws Exception {
		// The result should be: foo

		IRelation relation = evaluate("?- unsat(?X).");

		assertTrue("foo not in relation.",
				relation.contains(Helper.createConstantTuple("foo")));

		assertEquals("Relation does not have correct size", 1, relation.size());
	}

}
