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
public class ObviousTest extends ProgramEvaluationTest {

	public ObviousTest(String name) {
		super(name);
	}

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		// Create facts.
		expressions.add("person('B1').");
		expressions.add("person('B2').");
		expressions.add("person('A1').");
		expressions.add("person('C3').");

		expressions.add("hasName('B1', 'barry').");
		expressions.add("hasName('B2', 'barry').");
		expressions.add("hasName('A1', 'anne').");
		expressions.add("hasName('C3', 'charlie').");

		expressions.add("atOffsite('B1').");
		expressions.add("atOffsite('A1').");

		expressions.add("atEswc('B2').");
		expressions.add("atEswc('C3').");

		// Create rules.
		expressions
				.add("?X = ?Y :- person(?X), person(?Y), hasName(?X, ?N1), hasName(?Y, ?N2), ?N1 = ?N2.");

		return expressions;
	}

	public void testAtOffSite() throws Exception {
		// The result should be: A1, B1, B2.
		// B1 and B2 are both persons and have both name "barry", therefore they
		// are equal according to rule1. Therefore B2 is also in the relation.

		IRelation relation = evaluate("?- atOffsite(?X).");

		assertTrue("A1 not in relation.",
				relation.contains(Helper.createConstantTuple("A1")));
		assertTrue("B1 not in relation.",
				relation.contains(Helper.createConstantTuple("B1")));
		assertTrue("B2 not in relation.",
				relation.contains(Helper.createConstantTuple("B2")));

		assertEquals("Relation does not have correct size", 3, relation.size());
	}

	public void testAtOffSiteAndAtEswc() throws Exception {
		// The result should be: B1, B2.
		// B1 and B2 are both persons and have both name "barry", therefore they
		// are equal according to rule1. Therefore, if B1 is atOffSite and B2 is
		// atEswc, both are atOffSite AND atEswc.

		IRelation relation = evaluate("?- atOffsite(?X), atEswc(?X).");

		assertTrue("B1 not in relation.",
				relation.contains(Helper.createConstantTuple("B1")));
		assertTrue("B2 not in relation.",
				relation.contains(Helper.createConstantTuple("B2")));

		assertEquals("Relation does not have correct size", 2, relation.size());
	}

}
