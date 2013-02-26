package at.sti2.streamingiris.terms.concrete;

import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import at.sti2.streamingiris.ObjectTests;
import at.sti2.streamingiris.TermTests;

public class IriTest extends TestCase {
	private static final URI REFERENCE;

	private static final String URISTR = "http://localhost:8080/test.jsp";

	private static final String URISTRMORE = "http://localhost:9080/test.jsp";

	private static final String URISTRMORE1 = "http://localhost:9080/test.jsp1";
	static {
		URI tmp = null;
		try {
			tmp = new URI("http://localhost:8080/test.jsp");
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		REFERENCE = tmp;
	}

	public void testBasic() {
		Iri fix = new Iri(REFERENCE);
		Iri test = new Iri(URISTR);

		assertEquals("Something wrong with getUri", REFERENCE, fix.getURI());
		assertEquals("Something wrong with The toString", URISTR,
				fix.getValue());

		assertEquals("Something wrong with parsing", fix, test);

		assertEquals("Something wrong with the creation", fix, new Iri(
				REFERENCE));
		assertEquals("Something wrong with the creation", fix, new Iri(URISTR));
	}

	public void testEquals() {
		ObjectTests.runTestEquals(new Iri(URISTR), new Iri(URISTR), new Iri(
				URISTRMORE));
	}

	public void testCompareTo() {
		ObjectTests.runTestCompareTo(new Iri(URISTR), new Iri(URISTR), new Iri(
				URISTRMORE), new Iri(URISTRMORE1));
	}

	public void testHashCode() {
		ObjectTests.runTestHashCode(new Iri(URISTR), new Iri(URISTR));
	}

	public static Test suite() {
		return new TestSuite(IriTest.class, IriTest.class.getSimpleName());
	}

	public void testGetMinValue() {
		TermTests.runTestGetMinValue(new Iri("a"));
	}
}
