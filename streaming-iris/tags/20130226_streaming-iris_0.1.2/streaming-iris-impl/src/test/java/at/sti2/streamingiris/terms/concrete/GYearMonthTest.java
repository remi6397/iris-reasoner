package at.sti2.streamingiris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class GYearMonthTest extends TestCase {
	private static final int MONTH = 2;

	private static final int YEAR = 2005;

	public void testBasic() {
		final GYearMonth yearmonth = new GYearMonth(YEAR, MONTH);

		assertEquals("Something wrong with getYear", YEAR, yearmonth.getYear());
		assertEquals("Something wrong with getMonth", MONTH,
				yearmonth.getMonth());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new GYearMonth(YEAR, MONTH), new GYearMonth(
				YEAR, MONTH), new GYearMonth(YEAR, MONTH + 1));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new GYearMonth(YEAR, MONTH),
				new GYearMonth(YEAR, MONTH), new GYearMonth(YEAR, MONTH + 1),
				new GYearMonth(YEAR, MONTH + 2));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new GYearMonth(YEAR, MONTH),
				new GYearMonth(YEAR, MONTH));
	}

	public static Test suite() {
		return new TestSuite(GYearMonthTest.class,
				GYearMonthTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new GYearMonth(1, 2));
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
			new GYearMonth(2000, 1, -1, 1);
			fail("It is possible to create a yearmonth with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new GYearMonth(2000, 1, 1, -1);
			fail("It is possible to create a yearmonth with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new GYearMonth(2000, 1, 0, 0);
		new GYearMonth(2000, 1, 1, 0);
		new GYearMonth(2000, 1, 0, 1);
		new GYearMonth(2000, 1, 1, 1);
		new GYearMonth(2000, 1, -1, 0);
		new GYearMonth(2000, 1, 0, -1);
		new GYearMonth(2000, 1, -1, -1);
	}

	/**
	 * <p>
	 * Chechs whether the months are handeled correctly. The months should count
	 * from 1-12.
	 * </p>
	 * 
	 * @see <a
	 *      href="http://sourceforge.net/tracker/index.php?func=detail&aid=1792385&group_id=167309&atid=842434">bug
	 *      #1792385: GYearMonth datatype handles months incorrectly</a>
	 */
	public void testCorrectMonthBehaviour() {
		final GYearMonth y2000m1 = new GYearMonth(2000, 1);
		// if mounthts woule be from 0 - 11 this would shift to year 2001
		final GYearMonth y2000m12 = new GYearMonth(2000, 12);
		assertTrue(y2000m1 + " must be smaller than " + y2000m12,
				y2000m1.compareTo(y2000m12) < 0);
	}
}
