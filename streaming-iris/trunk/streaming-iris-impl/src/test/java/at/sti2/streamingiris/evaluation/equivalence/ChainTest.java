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
public class ChainTest extends ProgramEvaluationTest {

	public ChainTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("a('A').");
		expressions.add("b('B').");
		expressions.add("c('C').");
		expressions.add("d('D').");

		expressions.add("foo('B').");
		expressions.add("baz('C').");

		// Create rules.
		expressions.add("?X = ?Y :- a(?X), b(?Y).");
		expressions.add("?X = ?Y :- b(?X), c(?Y).");
		expressions.add("?X = ?Y :- c(?X), d(?Y).");

		expressions.add("bar(?X) :- foo(?X), baz(?X), ?X = 'A'.");

		return expressions;
	}

	public void testFoo() throws Exception {
		// The result should be: A, B, C, D

		IRelation relation = evaluate("?- foo(?X).");

		assertTrue("A not in relation.",
				relation.contains(Helper.createConstantTuple("A")));
		assertTrue("B not in relation.",
				relation.contains(Helper.createConstantTuple("B")));
		assertTrue("C not in relation.",
				relation.contains(Helper.createConstantTuple("C")));
		assertTrue("D not in relation.",
				relation.contains(Helper.createConstantTuple("D")));

		assertEquals("Relation does not have correct size", 4, relation.size());
	}

	public void testBar() throws Exception {
		// The result should be: A, B, C, D

		IRelation relation = evaluate("?- bar(?X).");

		assertTrue("A not in relation.",
				relation.contains(Helper.createConstantTuple("A")));
		assertTrue("B not in relation.",
				relation.contains(Helper.createConstantTuple("B")));
		assertTrue("C not in relation.",
				relation.contains(Helper.createConstantTuple("C")));
		assertTrue("D not in relation.",
				relation.contains(Helper.createConstantTuple("D")));

		assertEquals("Relation does not have correct size", 4, relation.size());
	}

}
