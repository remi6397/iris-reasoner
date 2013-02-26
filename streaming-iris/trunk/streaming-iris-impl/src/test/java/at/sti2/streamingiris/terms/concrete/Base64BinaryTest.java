package at.sti2.streamingiris.terms.concrete;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class Base64BinaryTest extends TestCase {
	private static final String BASIC = "adf3";

	private static final String BASICMORE = "adf4";

	private static final String BASICMORE1 = "adf5";

	private static final String[] INVALID = new String[] {
			"0123456789abcdefaaa)", "asdfa", "as/ffA==a", "SGVsbG8gV29ybGQ==" };

	private static final String[] VALID = new String[] { "0123456789abcdef",
			"asdf", "as/ffA==", "SGVsbG8gV29ybGQ=" };

	public void testBasic() {
		Base64Binary bb = new Base64Binary(BASIC);
		assertEquals("Something wrong with getBase64Binary", BASIC,
				bb.getValue());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Base64Binary(BASIC), new Base64Binary(
				BASIC), new Base64Binary(BASICMORE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Base64Binary(BASIC), new Base64Binary(
				BASIC), new Base64Binary(BASICMORE), new Base64Binary(
				BASICMORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Base64Binary(BASIC), new Base64Binary(
				BASIC));
	}

	public void testInValidParsing() {
		for (String str : INVALID) {
			boolean gotException = false;
			try {
				new Base64Binary(str);
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
			assertEquals("The parsing of " + str + " should work", str,
					new Base64Binary(str).getValue());
		}
	}

	public static Test suite() {
		return new TestSuite(Base64BinaryTest.class,
				Base64BinaryTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new Base64Binary("0A=="));
	}
}
