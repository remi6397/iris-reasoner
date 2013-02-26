package at.sti2.streamingiris.terms.concrete;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class HexBinaryTest extends TestCase {
	private static final String BASIC = "adf3";

	private static final String BASICMORE = "adf4";

	private static final String BASICMORE1 = "adf5";

	private static final String[] INVALID = new String[] { "12fff", "45ka" };

	private static final String[] VALID = new String[] { "0123456789abcdef" };

	public void testBasic() {
		HexBinary hb = new HexBinary(BASIC);
		assertEquals("Something wrong with getHexBinary", BASIC.toUpperCase(),
				hb.getValue());
		assertEquals("Something wrong with the parsing", hb, new HexBinary(
				BASIC));
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new HexBinary(BASIC), new HexBinary(BASIC),
				new HexBinary(BASICMORE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new HexBinary(BASIC),
				new HexBinary(BASIC), new HexBinary(BASICMORE), new HexBinary(
						BASICMORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new HexBinary(BASIC), new HexBinary(BASIC));
	}

	public void testInValidParsing() {
		for (String str : INVALID) {
			boolean gotException = false;
			try {

				new HexBinary(str);
			} catch (IllegalArgumentException e) {
				gotException = true;
			}
			if (!gotException) {
				throw new AssertionFailedError("The parsing of " + str
						+ " shouldn't work");
			}
		}
	}

	public void testValidParsing() {
		for (String str : VALID) {
			assertEquals("The parsing of " + str + "should work",
					str.toUpperCase(), new HexBinary(str).getValue());
		}
	}

	public static Test suite() {
		return new TestSuite(HexBinaryTest.class,
				HexBinaryTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new HexBinary("0a"));
	}
}
