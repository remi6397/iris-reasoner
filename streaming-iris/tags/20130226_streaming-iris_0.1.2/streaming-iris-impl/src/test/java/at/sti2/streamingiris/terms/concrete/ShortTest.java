package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Short data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class ShortTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new ShortTerm((short) -2);
	}

	@Override
	protected String createBasicString() {
		return "-2";
	}

	@Override
	protected INumericTerm createEqual() {
		return new ShortTerm((short) -2);
	}

	@Override
	protected String createEqualString() {
		return "-2";
	}

	@Override
	protected INumericTerm createGreater() {
		return new ShortTerm((short) 222);
	}

	@Override
	protected String createGreaterString() {
		return "222";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.SHORT.toUri();
	}

}
