package at.sti2.streamingiris.terms;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

/**
 * <p>
 * Tests the functionality of the <code>StringTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class StringTermTest extends TestCase {

	private static final String BASIC = "aaa";

	private static final String MORE = "aab";

	private static final String MORE1 = "aac";

	public static Test suite() {
		return new TestSuite(StringTermTest.class,
				StringTermTest.class.getSimpleName());
	}

	public void testBasic() {
		assertEquals("Object not initialized correct", BASIC, new StringTerm(
				BASIC).getValue());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new StringTerm(BASIC), new StringTerm(BASIC),
				new StringTerm(MORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new StringTerm(BASIC),
				new StringTerm(BASIC));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new StringTerm(BASIC), new StringTerm(
				BASIC), new StringTerm(MORE), new StringTerm(MORE1));
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new StringTerm("a"));
	}
}
