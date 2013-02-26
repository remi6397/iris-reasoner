package at.sti2.streamingiris.terms.concrete;

import junit.framework.TestCase;
import at.sti2.streamingiris.ObjectTests;

/**
 * Simple tests for the implementation of the rdf:XMLLiteral
 * 
 * @author gigi
 * 
 */
public class XMLLiteralTest extends TestCase {

	private static final String STRING = "<tag>Text</tag>";
	private static final String LANG = "en";

	public void testEquals() {
		XMLLiteral literal1 = new XMLLiteral(STRING, LANG);
		XMLLiteral literal2 = new XMLLiteral(STRING, LANG);
		XMLLiteral literal3 = new XMLLiteral(STRING, "de");
		XMLLiteral literal4 = new XMLLiteral(STRING, "en");
		XMLLiteral literal5 = new XMLLiteral("<tag>Foobar</tag>", LANG);

		ObjectTests.runTestEquals(literal1, literal2, literal5);
		ObjectTests.runTestEquals(literal3, literal4, literal5);
	}

	public void testHashCode() {
		XMLLiteral literal1 = new XMLLiteral(STRING, LANG);
		XMLLiteral literal2 = new XMLLiteral(STRING, LANG);

		ObjectTests.runTestHashCode(literal1, literal2);
	}

}
