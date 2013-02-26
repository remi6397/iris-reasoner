package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the UnsignedShort data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedShortTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new UnsignedShort(65534);
	}

	@Override
	protected String createBasicString() {
		return "65534";
	}

	@Override
	protected INumericTerm createEqual() {
		return new UnsignedShort(65534);
	}

	@Override
	protected String createEqualString() {
		return "65534";
	}

	@Override
	protected INumericTerm createGreater() {
		return new UnsignedShort(65535);
	}

	@Override
	protected String createGreaterString() {
		return "65535";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_SHORT.toUri();
	}

	public void testValidity() {
		try {
			new UnsignedShort(65536);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedShort(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedShort(-121);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
