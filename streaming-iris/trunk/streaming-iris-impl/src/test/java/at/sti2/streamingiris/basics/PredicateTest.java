package at.sti2.streamingiris.basics;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;

/**
 * @author richi
 * 
 *         Revision 1.1 26.07.2006 12:09:22 Darko Anicic, DERI Innsbruck
 */
public class PredicateTest extends TestCase {

	private static final int ARITY = 3;

	private static final int ARITYMORE = 4;

	private static final String SYMBOL = "date";

	private static final String SYMBOLMORE = "date1";

	public static Test suite() {
		return new TestSuite(PredicateTest.class,
				PredicateTest.class.getSimpleName());
	}

	public void testBasic() {
		final Predicate REFERENCE = new Predicate(SYMBOL, ARITY);

		assertEquals("getPredicateSymbol doesn't work properly", SYMBOL,
				REFERENCE.getPredicateSymbol());
		assertEquals("getArity doesn't work properly", ARITY,
				REFERENCE.getArity());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Predicate(SYMBOL, ARITY), new Predicate(
				SYMBOL, ARITY), new Predicate(SYMBOL, ARITYMORE));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Predicate(SYMBOL, ARITY),
				new Predicate(SYMBOL, ARITY));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Predicate(SYMBOL, ARITY),
				new Predicate(SYMBOL, ARITY), new Predicate(SYMBOL, ARITYMORE),
				new Predicate(SYMBOLMORE, ARITY));
	}
}
