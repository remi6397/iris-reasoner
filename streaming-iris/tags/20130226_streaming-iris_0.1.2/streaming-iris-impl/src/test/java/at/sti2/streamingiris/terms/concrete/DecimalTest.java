package at.sti2.streamingiris.terms.concrete;

import java.math.BigDecimal;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

/**
 * <p>
 * Tests the functionality of the <code>DecimalTerm</code>.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision$
 */
public class DecimalTest extends TestCase {

	private final static BigDecimal BASIC = new BigDecimal(0.1d);

	private final static BigDecimal MORE = new BigDecimal(0.2d);

	private final static BigDecimal MORE1 = new BigDecimal(0.3d);

	public static Test suite() {
		return new TestSuite(DecimalTest.class,
				DecimalTest.class.getSimpleName());
	}

	public void testBasic() {
		DecimalTerm basic = new DecimalTerm(BASIC);

		assertEquals("object not initialized correctly", BASIC,
				basic.getValue());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new DecimalTerm(BASIC),
				new DecimalTerm(BASIC), new DecimalTerm(MORE));
	}

	public void testCompare() {
		ObjectTests.runTestCompareTo(new DecimalTerm(BASIC), new DecimalTerm(
				BASIC), new DecimalTerm(MORE), new DecimalTerm(MORE1));
	}

	public void testEqualsPositiveNegativeZero() {
		ObjectTests.runTestCompareTo(new DecimalTerm(+0.0), new DecimalTerm(
				-0.0), new DecimalTerm(0.000000001), new DecimalTerm(
				0.000000002));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new DecimalTerm(BASIC), new DecimalTerm(
				BASIC));
	}

	public void testGetMinValue() {
		TermTests
				.runTestGetMinValue(new DecimalTerm(Double.MIN_VALUE + 0.0001));
	}
}
