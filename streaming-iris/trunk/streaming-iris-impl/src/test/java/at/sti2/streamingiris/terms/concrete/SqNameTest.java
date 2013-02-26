package at.sti2.streamingiris.terms.concrete;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;
import at.sti2.streamingiris.api.terms.concrete.IIri;

public class SqNameTest extends TestCase {

	private static final String NAME = "sqName";

	private static final String NAMEMORE = "sqNbme";

	private static final String NAMEMORE1 = "sqNbmf";

	private static final String SPACE = "http://www.deri.org/reasoner";

	private static final IIri SPACEIRI = new Iri(SPACE);

	public void testBasic() {
		SqName fix = new SqName(SPACEIRI, NAME);
		SqName test = new SqName(SPACE + "#" + NAME);

		assertEquals("Something wrong whith instanciation", fix, test);
		assertEquals("Something wrong with name", NAME, test.getName());
		assertEquals("Something wrong with name", fix.getName(), test.getName());
		assertEquals("Something wrong with namespace", SPACEIRI,
				test.getNamespace());
		assertEquals("Something wrong with namespace", fix.getNamespace(),
				test.getNamespace());
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new SqName(SPACE, NAME), new SqName(SPACE,
				NAME), new SqName(SPACE, NAMEMORE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new SqName(SPACE, NAME), new SqName(SPACE,
				NAME), new SqName(SPACE, NAMEMORE),
				new SqName(SPACE, NAMEMORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new SqName(SPACE, NAME), new SqName(SPACE,
				NAME));
	}

	public static Test suite() {
		return new TestSuite(SqNameTest.class, SqNameTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new SqName("", "a"));
	}
}
