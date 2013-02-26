package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the NMTOKEN data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NMTOKENTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new NMTOKEN("sti");
	}

	@Override
	protected String createBasicString() {
		return "sti";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new NMTOKEN(" sti  \t  \r    \n ");
	}

	@Override
	protected String createEqualString() {
		return "sti";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new NMTOKEN("xml");
	}

	@Override
	protected String createGreaterString() {
		return "xml";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.NMTOKEN.toUri();
	}

}
