package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the NegativeInteger data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NegativeIntegerTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new NegativeInteger(-1337);
	}

	@Override
	protected String createBasicString() {
		return "-1337";
	}

	@Override
	protected INumericTerm createEqual() {
		return new NegativeInteger(-1337);
	}

	@Override
	protected String createEqualString() {
		return "-1337";
	}

	@Override
	protected INumericTerm createGreater() {
		return new NegativeInteger(-1);
	}

	@Override
	protected String createGreaterString() {
		return "-1";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.NEGATIVE_INTEGER.toUri();
	}

	public void testValidity() {
		try {
			new NegativeInteger(0);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new NegativeInteger(1);
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
