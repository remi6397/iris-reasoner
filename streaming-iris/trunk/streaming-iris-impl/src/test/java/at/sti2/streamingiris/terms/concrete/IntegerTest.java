package at.sti2.streamingiris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

/**
 * <p>
 * Tests the functionality of the <code>IntegerTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class IntegerTest extends TestCase {

	private final static int BASIC = 1;

	private final static int MORE = 2;

	private final static int MORE1 = 3;

	public static Test suite() {
		return new TestSuite(IntegerTest.class,
				IntegerTest.class.getSimpleName());
	}

	public void testBasic() {
		IntegerTerm basic = new IntegerTerm(BASIC);
		assertEquals("object not initialized correctly",
				Integer.valueOf(BASIC),
				Integer.valueOf(basic.getValue().intValue()));
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new IntegerTerm(BASIC),
				new IntegerTerm(BASIC), new IntegerTerm(MORE));
	}

	public void testCompare() {
		ObjectTests.runTestCompareTo(new IntegerTerm(BASIC), new IntegerTerm(
				BASIC), new IntegerTerm(MORE), new IntegerTerm(MORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new IntegerTerm(BASIC), new IntegerTerm(
				BASIC));
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new IntegerTerm(Integer.MIN_VALUE + 1));
	}
}
