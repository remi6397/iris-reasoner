package at.sti2.streamingiris.terms;

import java.net.URI;

import junit.framework.TestCase;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.api.terms.IConcreteTerm;

/**
 * @author Adrian Marte
 */
public abstract class AbstractConcreteTermTest extends TestCase {

	protected abstract IConcreteTerm createBasic();

	protected abstract IConcreteTerm createEqual();

	protected abstract IConcreteTerm createGreater();

	protected abstract String createBasicString();

	protected abstract String createEqualString();

	protected abstract String createGreaterString();

	protected abstract URI getDatatypeIRI();

	private IConcreteTerm basic;

	private IConcreteTerm equal;

	private IConcreteTerm greater;

	private String basicString;

	private String equalString;

	private String greaterString;

	@Override
	protected void setUp() throws Exception {
		super.setUp();

		basic = createBasic();
		equal = createEqual();
		greater = createGreater();

		basicString = createBasicString();
		equalString = createEqualString();
		greaterString = createGreaterString();
	}

	public void testNotNull() {
		assertNotNull(basic);
		assertNotNull(equal);
		assertNotNull(greater);

		assertNotNull(basicString);
		assertNotNull(equalString);
		assertNotNull(greaterString);
	}

	public void testToCanonicalString() {
		assertEquals(basicString, basic.toCanonicalString());
		assertEquals(equalString, equal.toCanonicalString());
		assertEquals(greaterString, greater.toCanonicalString());
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(basic, equal, greater);
	}

	public void testEquals() {
		ObjectTests.runTestEquals(basic, equal, greater);
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(basic, equal);
	}

	public void testDatatypeIRI() {
		assertEquals(getDatatypeIRI(), basic.getDatatypeIRI());
		assertEquals(getDatatypeIRI(), equal.getDatatypeIRI());
		assertEquals(getDatatypeIRI(), greater.getDatatypeIRI());
	}

}
