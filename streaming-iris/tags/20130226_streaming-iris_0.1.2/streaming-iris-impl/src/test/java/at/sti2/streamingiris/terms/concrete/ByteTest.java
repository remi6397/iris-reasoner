package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Byte data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ByteTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new ByteTerm((byte) 1);
	}

	@Override
	protected String createBasicString() {
		return "1";
	}

	@Override
	protected INumericTerm createEqual() {
		return new ByteTerm((byte) 1);
	}

	@Override
	protected String createEqualString() {
		return "1";
	}

	@Override
	protected INumericTerm createGreater() {
		return new ByteTerm((byte) 2);
	}

	@Override
	protected String createGreaterString() {
		return "2";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.BYTE.toUri();
	}

}
