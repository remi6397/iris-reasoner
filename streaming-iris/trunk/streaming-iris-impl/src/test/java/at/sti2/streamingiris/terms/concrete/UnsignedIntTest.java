package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the UnsignedInt data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedIntTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new UnsignedInt(1337);
	}

	@Override
	protected String createBasicString() {
		return "1337";
	}

	@Override
	protected INumericTerm createEqual() {
		return new UnsignedInt(1337);
	}

	@Override
	protected String createEqualString() {
		return "1337";
	}

	@Override
	protected INumericTerm createGreater() {
		return new UnsignedInt(123423236);
	}

	@Override
	protected String createGreaterString() {
		return "123423236";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_INT.toUri();
	}

	public void testValidity() {
		try {
			new UnsignedInt(-1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedInt(-12131);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
