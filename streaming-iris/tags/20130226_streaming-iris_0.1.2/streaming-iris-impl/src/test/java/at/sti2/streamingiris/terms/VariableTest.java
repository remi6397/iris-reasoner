package at.sti2.streamingiris.terms;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;

/**
 * @author richi
 * 
 */
public class VariableTest extends TestCase {

	private static final String BASIC = "aaa";

	private static final String MORE = "aab";

	private static final String MORE1 = "aac";

	public static Test suite() {
		return new TestSuite(VariableTest.class,
				VariableTest.class.getSimpleName());
	}

	// public void testBasic() {
	// Variable basic = new Variable(BASIC);
	// Variable changed = new Variable(MORE);
	// changed.setName(BASIC);
	// assertEquals("object not initialized correctly", BASIC, basic
	// .getName());
	// assertEquals("setValue(..) doesn't work correctly", basic, changed);
	// }

	public void testEquals() {
		ObjectTests.runTestEquals(new Variable(BASIC), new Variable(BASIC),
				new Variable(MORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Variable(BASIC), new Variable(BASIC));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Variable(BASIC), new Variable(BASIC),
				new Variable(MORE), new Variable(MORE1));
	}
}
