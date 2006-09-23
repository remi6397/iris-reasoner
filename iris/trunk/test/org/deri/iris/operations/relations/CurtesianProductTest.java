package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.MiscHelper;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;

public class CurtesianProductTest extends TestCase {

	public static Test suite() {
		return new TestSuite(CurtesianProductTest.class, CurtesianProductTest.class.getSimpleName());
	}

	/**
	 * Joins two relations (no duplicates handling) and then checks the 
	 * result against the submitted Collection of tuples. 
	 * 
	 * @param i
	 *            the indexes on which to join (see documentation for
	 *            IJoin.join(..) for the computation of this array)
	 * @param e
	 *            the Collection of expected tuples
	 */
	protected static void runJoin(final int[] i, final Collection<ITuple> e) {
		IRelation<ITuple> relation0 = new Relation(3);
		IRelation<ITuple> relation1 = new Relation(4);

	
		relation0.add(MiscHelper.createTuple("a", "b", "i"));
		relation0.add(MiscHelper.createTuple("a", "b", "j"));	
		
		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("f", "b", "a"));
		relation1.add(MiscHelper.createTuple("e", "b", "b"));
		relation1.add(MiscHelper.createTuple("h", "a", "a"));
		
		
		
		IJoin joinOperator = RELATION.createJoinOperator(
				relation0, relation1, i, JoinCondition.EQUALS);
		IRelation result = joinOperator.join();
		assertResults(result, e);
	}

	/**
	 * Curtesian Product
	 */
	public void testJoin_m1m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "i", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "i", "f", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "i", "e", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "i", "h", "a", "a"));
		
		e.add(MiscHelper.createTuple("a", "b", "j", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "j", "f", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "j", "e", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "j", "h", "a", "a"));
		
		runJoin(new int[] { -1, -1, -1}, e);
	}

	/**
	 * Tests the relation against a list of tuples using the assert methods of
	 * JUnit. The length of the relation and the list must be equal, and the
	 * relation must contain all tuples of the list.
	 * 
	 * @param r
	 *            the relation to check
	 * @param e
	 *            the Collection containing all expected tuples
	 */
	protected static void assertResults(final IRelation<ITuple> r,
			final Collection<ITuple> e) {
		Assert.assertEquals("The length of relation and the list of"
				+ " expected tuples must be equal", e.size(), r.size());
		Assert.assertTrue("The relation must contain all expected tuples", r
				.containsAll(e));
	}
}


