package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Long data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class LongTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new LongTerm(1337);
	}

	@Override
	protected String createBasicString() {
		return "1337";
	}

	@Override
	protected INumericTerm createEqual() {
		return new LongTerm(1337);
	}

	@Override
	protected String createEqualString() {
		return "1337";
	}

	@Override
	protected INumericTerm createGreater() {
		return new LongTerm(1338);
	}

	@Override
	protected String createGreaterString() {
		return "1338";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.LONG.toUri();
	}

}
