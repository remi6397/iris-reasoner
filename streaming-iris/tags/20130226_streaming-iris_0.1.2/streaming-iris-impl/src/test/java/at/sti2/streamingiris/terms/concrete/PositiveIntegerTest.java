package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the PositiveInteger data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class PositiveIntegerTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new PositiveInteger(1);
	}

	@Override
	protected String createBasicString() {
		return "1";
	}

	@Override
	protected INumericTerm createEqual() {
		return new PositiveInteger(1);
	}

	@Override
	protected String createEqualString() {
		return "1";
	}

	@Override
	protected INumericTerm createGreater() {
		return new PositiveInteger(1337);
	}

	@Override
	protected String createGreaterString() {
		return "1337";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.POSITIVE_INTEGER.toUri();
	}

	public void testValidity() {
		try {
			new PositiveInteger(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new PositiveInteger(-12131);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
