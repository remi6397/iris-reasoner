package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the NonNegativeInteger data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NonNegativeIntegerTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new NonNegativeInteger(0);
	}

	@Override
	protected String createBasicString() {
		return "0";
	}

	@Override
	protected INumericTerm createEqual() {
		return new NonNegativeInteger(0);
	}

	@Override
	protected String createEqualString() {
		return "0";
	}

	@Override
	protected INumericTerm createGreater() {
		return new NonNegativeInteger(1337);
	}

	@Override
	protected String createGreaterString() {
		return "1337";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.NON_NEGATIVE_INTEGER.toUri();
	}

	public void testValidity() {
		try {
			new NonNegativeInteger(0);
		} catch (IllegalArgumentException e) {
			fail("0 is a valid value for nonNegativeInteger");
		}

		try {
			new NonNegativeInteger(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new NonNegativeInteger(-12131);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
