package at.sti2.streamingiris.basics;

import static at.sti2.streamingiris.factory.Factory.BASIC;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.MiscHelper;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;

/**
 * <p>
 * Tests for the atom.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class AtomTest extends TestCase {

	private static final int ARITY = 3;

	private static final String SYMBOL = "date";

	private static final ITuple TUPLE = MiscHelper.createTuple("a", "b", "c");

	private static final ITuple TUPLEMORE = MiscHelper.createTuple("a", "b",
			"d");

	private static final IPredicate PREDICATE = BASIC.createPredicate(SYMBOL,
			ARITY);

	private static final IPredicate PREDICATEMORE = BASIC.createPredicate(
			SYMBOL + 1, ARITY);

	public static Test suite() {
		return new TestSuite(AtomTest.class, AtomTest.class.getSimpleName());
	}

	public void testIsGround() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The isGround method doesn't work properly", true,
				REF.isGround());
	}

	public void testIsBuiltin() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The isBuiltin method doesn't work properly", false,
				REF.isBuiltin());
	}

	public void testGetTuple() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The getTuple method doesn't work properly", TUPLE,
				REF.getTuple());
	}

	public void testGetPredicate() {
		Atom REF = new Atom(PREDICATE, TUPLE);
		assertEquals("The getPredicate method doesn't work properly",
				PREDICATE, REF.getPredicate());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Atom(PREDICATE, TUPLE), new Atom(
				PREDICATE, TUPLE), new Atom(PREDICATE, TUPLEMORE));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Atom(PREDICATE, TUPLE), new Atom(
				PREDICATE, TUPLE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Atom(PREDICATE, TUPLE), new Atom(
				PREDICATE, TUPLE), new Atom(PREDICATE, TUPLEMORE), new Atom(
				PREDICATEMORE, TUPLE));
	}
}
