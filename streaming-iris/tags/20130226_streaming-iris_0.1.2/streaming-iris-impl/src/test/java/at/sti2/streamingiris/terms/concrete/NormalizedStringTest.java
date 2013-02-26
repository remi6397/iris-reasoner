package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the NormalizedString data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NormalizedStringTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new NormalizedString("\nbla bla \tbla  \r     foo");
	}

	@Override
	protected String createBasicString() {
		return "bla bla bla       foo";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new NormalizedString("bla bla bla       foo");
	}

	@Override
	protected String createEqualString() {
		return "bla bla bla       foo";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new NormalizedString("lulz");
	}

	@Override
	protected String createGreaterString() {
		return "lulz";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.NORMALIZED_STRING.toUri();
	}

}
