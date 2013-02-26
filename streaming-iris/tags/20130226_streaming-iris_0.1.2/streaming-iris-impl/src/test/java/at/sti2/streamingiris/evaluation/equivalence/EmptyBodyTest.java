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
public class EmptyBodyTest extends ProgramEvaluationTest {

	public EmptyBodyTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("foo('R').");

		// Create rules.
		expressions.add("'R' = 'S' :- .");

		return expressions;
	}

	public void testFoo() throws Exception {
		// The result should be: R, S

		IRelation relation = evaluate("?- foo(?X).");

		assertTrue("R not in relation.",
				relation.contains(Helper.createConstantTuple("R")));
		assertTrue("S not in relation.",
				relation.contains(Helper.createConstantTuple("S")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

}
