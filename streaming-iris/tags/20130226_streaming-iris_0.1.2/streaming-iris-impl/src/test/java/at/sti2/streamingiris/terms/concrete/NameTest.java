package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Name data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NameTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new Name("sti");
	}

	@Override
	protected String createBasicString() {
		return "sti";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new Name(" sti  \t  \r    \n ");
	}

	@Override
	protected String createEqualString() {
		return "sti";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new Name("xml");
	}

	@Override
	protected String createGreaterString() {
		return "xml";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.NAME.toUri();
	}

}
