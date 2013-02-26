package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the ENTITY data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ENTITIYTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new ENTITY("sti");
	}

	@Override
	protected String createBasicString() {
		return "sti";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new ENTITY("sti");
	}

	@Override
	protected String createEqualString() {
		return "sti";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new ENTITY("xml");
	}

	@Override
	protected String createGreaterString() {
		return "xml";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.ENTITY.toUri();
	}

}
