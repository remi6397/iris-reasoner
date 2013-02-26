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
public class EqualsInQueryTest extends ProgramEvaluationTest {

	public EqualsInQueryTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("p('A1', 'A2', 'A').");
		expressions.add("p('A3', 'A4', 'C').");
		expressions.add("p('A', 'D', 'E').");

		expressions.add("q('A1', 'A2', 'B').");
		expressions.add("q('A3', 'A4', 'D').");
		expressions.add("q('B', 'C', 'F').");

		expressions.add("r('A').");
		expressions.add("r('C').");
		expressions.add("r('E').");

		// Create rules.
		expressions
				.add("?X3 = ?Y3 :- p(?X1, ?X2, ?X3), q(?Y1, ?Y2, ?Y3), ?X1 = ?Y1, ?X2 = ?Y2.");

		// The equivalence classes are {A, B}, {C, D}, {E, F}

		// {E, F} should be identified as equivalent, after the equivalence
		// classes {A, B} and {C, D} are identified.

		return expressions;
	}

	public void testR() throws Exception {
		// The result should be: A, B, C, D, E, F

		IRelation relation = evaluate("?- r(?X), ?X = 'A'.");

		assertTrue("A not in relation.",
				relation.contains(Helper.createConstantTuple("A")));
		assertTrue("B not in relation.",
				relation.contains(Helper.createConstantTuple("B")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

}
