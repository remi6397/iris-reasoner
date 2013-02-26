package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import junit.framework.TestCase;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.api.terms.concrete.IAnyURI;

/**
 * <p>
 * Test the implementation of the AnyURI data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class AnyURITest extends TestCase {

	private static final String URI1_STRING = "http://www.sti-innsbruck.at";

	private static final URI URI1 = URI.create(URI1_STRING);

	private static final String URI2_STRING = "http://www.sti2.at";

	private static final URI URI2 = URI.create(URI2_STRING);

	public void testBasic() {
		IAnyURI anyUri = new AnyURI(URI1);

		assertEquals(URI1, anyUri.getValue());
	}

	public void testToCanonicalString() {
		IAnyURI anyUri = new AnyURI(URI1);
		assertEquals(URI1_STRING, anyUri.toCanonicalString());
	}

	public void testCompareTo() {
		IAnyURI uri1 = new AnyURI(URI1);
		IAnyURI uri2 = new AnyURI(URI1);
		IAnyURI uri3 = new AnyURI(URI2);

		ObjectTests.runTestCompareTo(uri1, uri2, uri3);
	}

	public void testEquals() {
		IAnyURI uri1 = new AnyURI(URI1);
		IAnyURI uri2 = new AnyURI(URI2);
		IAnyURI uri3 = new AnyURI(URI2);

		ObjectTests.runTestEquals(uri2, uri3, uri1);
	}

	public void testHashCode() {
		IAnyURI uri1 = new AnyURI(URI1);
		IAnyURI uri2 = new AnyURI(URI1);

		ObjectTests.runTestHashCode(uri1, uri2);
	}

}
