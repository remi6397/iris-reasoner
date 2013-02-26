package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the NonPositiveInteger data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NonPositiveIntegerTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new NonPositiveInteger(-1337);
	}

	@Override
	protected String createBasicString() {
		return "-1337";
	}

	@Override
	protected INumericTerm createEqual() {
		return new NonPositiveInteger(-1337);
	}

	@Override
	protected String createEqualString() {
		return "-1337";
	}

	@Override
	protected INumericTerm createGreater() {
		return new NonPositiveInteger(0);
	}

	@Override
	protected String createGreaterString() {
		return "0";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.NON_POSITIVE_INTEGER.toUri();
	}

	public void testValidity() {
		try {
			new NonPositiveInteger(0);
		} catch (IllegalArgumentException e) {
			fail("0 is a valid value for nonPositiveInteger");
		}
		try {
			new NonPositiveInteger(1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new NonPositiveInteger(12131);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
