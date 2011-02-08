package org.deri.iris.rdb.evaluation;

import java.util.ArrayList;
import java.util.Collection;

import org.deri.iris.storage.IRelation;
import org.junit.Assert;
import org.junit.Test;

public class RuleHeadEqualityTest extends ProgramEvaluationTest {

	@Override
	public Collection<String> createExpressions() {
		Collection<String> expressions = new ArrayList<String>();

		expressions.add("'A1' = 'A2' :- true.");
		expressions.add("foobar('A1').");
		expressions.add("foobar('A3').");
		expressions.add("?X = ?Y :- foobar(?X), foobar(?Y), ?X != ?Y.");
		expressions.add("foobar('A4') :- 'A1' = 'A3'.");

		return expressions;
	}

	@Test
	public void testFoobar() throws Exception {
		IRelation relation = evaluate("?- foobar(?X).");

		Assert.assertTrue("A1 not in relation.",
				relation.contains(Helper.createConstantTuple("A1")));

		Assert.assertTrue("A2 not in relation.",
				relation.contains(Helper.createConstantTuple("A2")));

		Assert.assertTrue("A3 not in relation.",
				relation.contains(Helper.createConstantTuple("A3")));

		Assert.assertTrue("A4 not in relation.",
				relation.contains(Helper.createConstantTuple("A4")));

		Assert.assertEquals("Relation does not have correct size", 4,
				relation.size());
	}

}
