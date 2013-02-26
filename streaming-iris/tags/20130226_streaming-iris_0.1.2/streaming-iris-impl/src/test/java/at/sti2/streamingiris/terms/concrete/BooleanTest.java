package at.sti2.streamingiris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class BooleanTest extends TestCase {

	public void testBasic() {
		BooleanTerm trueTerm = new BooleanTerm(true);
		BooleanTerm trueTermParsed = new BooleanTerm("TRUE");

		assertEquals("Instanciation didn't work", Boolean.TRUE,
				trueTerm.getValue());
		assertEquals("The parsing didn't work", trueTerm, trueTermParsed);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new BooleanTerm(true), new BooleanTerm(true),
				new BooleanTerm(false));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new BooleanTerm(false), new BooleanTerm(
				false), new BooleanTerm(true));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new BooleanTerm(true),
				new BooleanTerm(true));
	}

	public static Test suite() {
		return new TestSuite(BooleanTest.class,
				BooleanTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new BooleanTerm(true));
	}
}
