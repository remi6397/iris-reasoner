package storage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.Tuple;
import org.deri.iris.operations.relations.Join;
import org.deri.iris.operations.relations.JoinCondition;
import org.deri.iris.storage.Relation;
import org.deri.iris.terms.StringTerm;

/**
 * @author Richard Pöttler
 * @author Darko Anicic, DERI Innsbruck
 * @date 26.05.2006 13:59:43
 */
public class JoinTest extends TestCase {

	public static Test suite() {
		return new TestSuite(JoinTest.class, JoinTest.class.getSimpleName());
	}

	/**
	 * Joins two relations and then checks the result against the submitted
	 * Collection of tuples.
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

		relation0.add(createTuple("a", "b", "c"));
		relation0.add(createTuple("a", "b", "b"));
		relation0.add(createTuple("f", "g", "h"));
		relation0.add(createTuple("h", "g", "f"));
		relation0.add(createTuple("h", "g", "a"));

		relation1.add(createTuple("c", "b", "b"));
		relation1.add(createTuple("c", "b", "a"));
		relation1.add(createTuple("a", "b", "c"));

		IJoin joiner = new Join();
		IRelation result = joiner.join(relation0, relation1, i,
				JoinCondition.EQUALS);

		assertResults(result, e);
	}

	public void testJoin_m1m10() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(createTuple("a", "b", "c", "c", "b", "b"));
		e.add(createTuple("a", "b", "c", "c", "b", "a"));
		e.add(createTuple("h", "g", "a", "a", "b", "c"));
		runJoin(new int[] { -1, -1, 0 }, e);
	}

	public void testJoin_210() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(createTuple("a", "b", "c", "c", "b", "a"));
		runJoin(new int[] { 2, 1, 0 }, e);
	}
	
	public void testJoin_m12m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(createTuple("a", "b", "c", "c", "b", "b"));
		e.add(createTuple("a", "b", "b", "c", "b", "b"));
		runJoin(new int[] { -1, 2, -1 }, e);
	}
	
	public void testJoin_m1m1m1() {
		final List<ITuple> e = new ArrayList<ITuple>();
		e.add(createTuple("a", "b", "c", "c", "b", "b"));
		e.add(createTuple("a", "b", "c", "c", "b", "a"));
		e.add(createTuple("a", "b", "c", "a", "b", "c"));
		e.add(createTuple("a", "b", "b", "c", "b", "b"));
		e.add(createTuple("a", "b", "b", "c", "b", "a"));
		e.add(createTuple("a", "b", "b", "a", "b", "c"));
		
		e.add(createTuple("f", "g", "h", "c", "b", "b"));
		e.add(createTuple("f", "g", "h", "c", "b", "a"));
		e.add(createTuple("f", "g", "h", "a", "b", "c"));
		e.add(createTuple("h", "g", "f", "c", "b", "b"));
		e.add(createTuple("h", "g", "f", "c", "b", "a"));
		e.add(createTuple("h", "g", "f", "a", "b", "c"));
		e.add(createTuple("h", "g", "a", "c", "b", "b"));
		e.add(createTuple("h", "g", "a", "c", "b", "a"));
		e.add(createTuple("h", "g", "a", "a", "b", "c"));
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

	/**
	 * Creates a tuple consisting of StringTerms of the submitted strings
	 * 
	 * @param s
	 *            the Strings to add to the tuple
	 * @return the tuple
	 */
	protected static ITuple createTuple(final String... s) {
		List<ITerm> termList = new LinkedList<ITerm>();
		for (String str : s) {
			termList.add(new StringTerm(str));
		}
		return new Tuple(termList);
	}
}
