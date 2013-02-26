package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.INumericTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Int data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class IntTest extends AbstractConcreteTermTest {

	@Override
	protected INumericTerm createBasic() {
		return new IntTerm(-20454241);
	}

	@Override
	protected String createBasicString() {
		return "-20454241";
	}

	@Override
	protected INumericTerm createEqual() {
		return new IntTerm(-20454241);
	}

	@Override
	protected String createEqualString() {
		return "-20454241";
	}

	@Override
	protected INumericTerm createGreater() {
		return new IntTerm(1543);
	}

	@Override
	protected String createGreaterString() {
		return "1543";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.INT.toUri();
	}

}
