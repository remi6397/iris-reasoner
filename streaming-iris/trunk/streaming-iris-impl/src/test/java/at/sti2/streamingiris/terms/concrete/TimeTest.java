package at.sti2.streamingiris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class TimeTest extends TestCase {

	private static final int HOUR = 13;

	private static final int MINUTE = 56;

	private static final int SECOND = 0;

	private static final int TZ_HOUR = 1;

	private static final int TZ_MINUTE = 0;

	public void testBasic() {
		final Time t = new Time(HOUR, MINUTE, SECOND, TZ_HOUR, TZ_MINUTE);

		assertEquals("Something wrong with getHour", HOUR, t.getHour());
		assertEquals("Something wrong with getMinute", MINUTE, t.getMinute());
		assertEquals("Something wrong with getSecond", SECOND, t.getSecond());
	}

	public void testFloatingPointSeconds() {
		double seconds = 12.34567;
		final Time t = new Time(HOUR, MINUTE, seconds, TZ_HOUR, TZ_MINUTE);

		assertEquals(HOUR, t.getHour());
		assertEquals(MINUTE, t.getMinute());
		assertEquals(seconds, t.getDecimalSecond());

		final Time t2 = new Time(HOUR, MINUTE, seconds, TZ_HOUR, TZ_MINUTE);
		assertEquals(t, t2);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Time(HOUR, MINUTE, SECOND, TZ_HOUR,
				TZ_MINUTE), new Time(HOUR, MINUTE, SECOND, TZ_HOUR, TZ_MINUTE),
				new Time(HOUR, MINUTE, SECOND + 1, TZ_HOUR, TZ_MINUTE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Time(HOUR - 1, MINUTE, SECOND,
				TZ_HOUR - 1, TZ_MINUTE), new Time(HOUR, MINUTE, SECOND,
				TZ_HOUR, TZ_MINUTE), new Time(HOUR, MINUTE, SECOND + 1,
				TZ_HOUR, TZ_MINUTE), new Time(HOUR, MINUTE, SECOND + 2,
				TZ_HOUR, TZ_MINUTE));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Time(HOUR, MINUTE, SECOND, TZ_HOUR,
				TZ_MINUTE), new Time(HOUR, MINUTE, SECOND, TZ_HOUR, TZ_MINUTE));
	}

	public static Test suite() {
		return new TestSuite(TimeTest.class, TimeTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new Time(0, 0, 1, 0, 0));
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
			new Time(0, 0, 0, -1, 1);
			fail("It is possible to create a time with a negative tzHour and positive tzMinute");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Time(0, 0, 0, 1, -1);
			fail("It is possible to create a time with a positive tzHour and negative tzMinute");
		} catch (IllegalArgumentException e) {
		}

		// the following should be possible
		new Time(0, 0, 0, 0, 0);
		new Time(0, 0, 0, 1, 0);
		new Time(0, 0, 0, 0, 1);
		new Time(0, 0, 0, 1, 1);
		new Time(0, 0, 0, -1, 0);
		new Time(0, 0, 0, 0, -1);
		new Time(0, 0, 0, -1, -1);
	}
}
