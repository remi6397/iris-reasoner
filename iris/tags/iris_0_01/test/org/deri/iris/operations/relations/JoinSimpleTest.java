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

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date 26.06.2006 17:28:33
 */
public class JoinSimpleTest extends TestCase {

	public static Test suite() {
		return new TestSuite(JoinSimpleTest.class, JoinSimpleTest.class.getSimpleName());
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
		IRelation<ITuple> relation1 = new Relation(3);

		relation0.add(MiscHelper.createTuple("a", "b", "c"));
		relation0.add(MiscHelper.createTuple("a", "b", "b"));
		relation0.add(MiscHelper.createTuple("f", "g", "h"));
		relation0.add(MiscHelper.createTuple("h", "g", "f"));
		relation0.add(MiscHelper.createTuple("h", "g", "a"));

		relation1.add(MiscHelper.createTuple("c", "b", "b"));
		relation1.add(MiscHelper.createTuple("c", "b", "a"));
		relation1.add(MiscHelper.createTuple("a", "b", "c"));

		// test join operation with no duplicates handling
		IJoin joinSimpleOperator = RELATION.createJoinSimpleOperator(
				relation0, relation1, i, JoinCondition.EQUALS);
		IRelation result = joinSimpleOperator.join();
		assertResults(result, e);
	}

	public void testJoin_m1m10() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "a"));
		e.add(MiscHelper.createTuple("h", "g", "a", "a", "b", "c"));
		runJoin(new int[] { -1, -1, 0 }, e);
	}

	public void testJoin_210() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "a"));
		runJoin(new int[] { 2, 1, 0 }, e);
	}
	
	public void testJoin_m12m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "b", "c", "b", "b"));
		runJoin(new int[] { -1, 2, -1 }, e);
	}
	
	public void testJoin_m1m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "c", "c", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "c", "a", "b", "c"));
		e.add(MiscHelper.createTuple("a", "b", "b", "c", "b", "b"));
		e.add(MiscHelper.createTuple("a", "b", "b", "c", "b", "a"));
		e.add(MiscHelper.createTuple("a", "b", "b", "a", "b", "c"));
		
		e.add(MiscHelper.createTuple("f", "g", "h", "c", "b", "b"));
		e.add(MiscHelper.createTuple("f", "g", "h", "c", "b", "a"));
		e.add(MiscHelper.createTuple("f", "g", "h", "a", "b", "c"));
		e.add(MiscHelper.createTuple("h", "g", "f", "c", "b", "b"));
		e.add(MiscHelper.createTuple("h", "g", "f", "c", "b", "a"));
		e.add(MiscHelper.createTuple("h", "g", "f", "a", "b", "c"));
		e.add(MiscHelper.createTuple("h", "g", "a", "c", "b", "b"));
		e.add(MiscHelper.createTuple("h", "g", "a", "c", "b", "a"));
		e.add(MiscHelper.createTuple("h", "g", "a", "a", "b", "c"));
		// -1, -1, -1 means join all with all
		runJoin(new int[] { -1, -1, -1 }, e);
	}
	
	public void testJoin_111() {
		final List<ITuple> e = new ArrayList<ITuple>();
		runJoin(new int[] { 1, 1, 1 }, e);
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
