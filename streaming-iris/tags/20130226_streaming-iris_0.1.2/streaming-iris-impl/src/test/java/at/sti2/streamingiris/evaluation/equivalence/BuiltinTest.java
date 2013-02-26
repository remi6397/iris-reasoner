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
public class BuiltinTest extends ProgramEvaluationTest {

	public BuiltinTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("lower('foobar').");
		expressions.add("lower('noob').");
		expressions.add("lower('pwnage').");

		expressions.add("upper('FOOBAR').");
		expressions.add("upper('NOOB').");
		expressions.add("upper('PWNAGE').");

		expressions.add("foo('foobar').");

		// Create rules.
		expressions
				.add("?X = ?Y :- lower(?X), upper(?Y), STRING_TO_UPPER(?X, ?Y).");

		return expressions;
	}

	public void testString() throws Exception {
		// The result should be: foobar, noob, pwnage, FOOBAR, NOOB, PWNAGE

		IRelation relation = evaluate("?- lower(?X).");

		assertTrue("foobar not in relation.",
				relation.contains(Helper.createConstantTuple("foobar")));
		assertTrue("noob not in relation.",
				relation.contains(Helper.createConstantTuple("noob")));
		assertTrue("pwnage not in relation.",
				relation.contains(Helper.createConstantTuple("pwnage")));
		assertTrue("FOOBAR not in relation.",
				relation.contains(Helper.createConstantTuple("FOOBAR")));
		assertTrue("NOOB not in relation.",
				relation.contains(Helper.createConstantTuple("NOOB")));
		assertTrue("PWNAGE not in relation.",
				relation.contains(Helper.createConstantTuple("PWNAGE")));

		assertEquals("Relation does not have correct size", 6, relation.size());
	}

	public void testFoo() throws Exception {
		// The result should be: foobar, FOOBAR

		IRelation relation = evaluate("?- foo(?X).");

		assertTrue("foobar not in relation.",
				relation.contains(Helper.createConstantTuple("foobar")));
		assertTrue("FOOBAR not in relation.",
				relation.contains(Helper.createConstantTuple("FOOBAR")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

}
