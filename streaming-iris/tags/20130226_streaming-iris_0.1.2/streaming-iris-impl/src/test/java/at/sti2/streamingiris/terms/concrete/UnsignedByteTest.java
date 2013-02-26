package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the UnsignedByte data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedByteTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new UnsignedByte((short) 254);
	}

	@Override
	protected String createBasicString() {
		return "254";
	}

	@Override
	protected INumericTerm createEqual() {
		return new UnsignedByte((short) 254);
	}

	@Override
	protected String createEqualString() {
		return "254";
	}

	@Override
	protected INumericTerm createGreater() {
		return new UnsignedByte((short) 255);
	}

	@Override
	protected String createGreaterString() {
		return "255";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_BYTE.toUri();
	}

	public void testValidity() {
		try {
			new UnsignedByte((short) 256);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedByte((short) -1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedByte((short) -121);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
