package at.sti2.streamingiris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class GYearTest extends TestCase {

	private static final int YEAR = 2005;

	public void testBasic() {
		final GYear gyear = new GYear(YEAR);

		assertEquals("Something wrong with getYear", YEAR, gyear.getYear());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new GYear(YEAR), new GYear(YEAR), new GYear(
				YEAR + 1));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new GYear(YEAR), new GYear(YEAR),
				new GYear(YEAR + 1), new GYear(YEAR + 2));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new GYear(YEAR), new GYear(YEAR));
	}

	public static Test suite() {
		return new TestSuite(GYearTest.class, GYearTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new GYear(2));
	}

	/**
	 * <p>
	 * This test checks whether it is possible to specify inconsisntent
	 * timezones. E.g. a timezone with positive hours and negative minutes.
	 * </p>
	 * 
	 * @see <a
	 *      href="http://sourceforge.net/tracker/index.php?func=detail&aid=1778705&group_id=167309&atid=842434">bug
	 *      #1778705: it is possible to specify inconsistent timezones</a>
	 */
	public void testConsistentTimezones() {
		try {
			new GYear(2000, -1, 1);
			fail("It is possible to create a year with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new GYear(2000, 1, -1);
			fail("It is possible to create a year with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new GYear(2000, 0, 0);
		new GYear(2000, 1, 0);
		new GYear(2000, 0, 1);
		new GYear(2000, 1, 1);
		new GYear(2000, -1, 0);
		new GYear(2000, 0, -1);
		new GYear(2000, -1, -1);
	}
}
