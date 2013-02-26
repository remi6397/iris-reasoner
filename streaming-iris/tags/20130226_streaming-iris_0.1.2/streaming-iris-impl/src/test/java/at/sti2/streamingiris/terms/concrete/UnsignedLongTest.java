package at.sti2.streamingiris.terms.concrete;

import java.math.BigInteger;
import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the UnsignedLong data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class UnsignedLongTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new UnsignedLong(BigInteger.valueOf(1337));
	}

	@Override
	protected String createBasicString() {
		return "1337";
	}

	@Override
	protected INumericTerm createEqual() {
		return new UnsignedLong(BigInteger.valueOf(1337));
	}

	@Override
	protected String createEqualString() {
		return "1337";
	}

	@Override
	protected INumericTerm createGreater() {
		return new UnsignedLong(BigInteger.valueOf(123423236));
	}

	@Override
	protected String createGreaterString() {
		return "123423236";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.UNSIGNED_LONG.toUri();
	}

	public void testValidity() {
		try {
			new UnsignedLong(BigInteger.valueOf(-1));
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}

		try {
			new UnsignedLong(BigInteger.valueOf(-12131));
			fail("Did not recognize invalid value");
		} catch (IllegalArgumentException e) {
		}
	}

}
