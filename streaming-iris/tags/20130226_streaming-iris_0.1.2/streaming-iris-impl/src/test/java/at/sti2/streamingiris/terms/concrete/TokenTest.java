package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Token data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class TokenTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new Token("\nbla bla\tbla  \r     foo");
	}

	@Override
	protected String createBasicString() {
		return "bla blabla foo";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new Token("bla blabla foo");
	}

	@Override
	protected String createEqualString() {
		return "bla blabla foo";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new Token("lulz");
	}

	@Override
	protected String createGreaterString() {
		return "lulz";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.TOKEN.toUri();
	}

}
