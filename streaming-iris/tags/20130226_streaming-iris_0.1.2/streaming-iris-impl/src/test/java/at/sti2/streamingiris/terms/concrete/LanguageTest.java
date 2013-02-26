package at.sti2.streamingiris.terms.concrete;

import java.net.URI;

import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.concrete.XmlSchemaDatatype;
import at.sti2.streamingiris.terms.AbstractConcreteTermTest;

/**
 * <p>
 * Test the implementation of the Language data-type.
 * </p>
 * 
 * @author Adrian Marte
 */
public class LanguageTest extends AbstractConcreteTermTest {

	@Override
	protected IConcreteTerm createBasic() {
		return new Language("   en");
	}

	@Override
	protected String createBasicString() {
		return "en";
	}

	@Override
	protected IConcreteTerm createEqual() {
		return new Language("en");
	}

	@Override
	protected String createEqualString() {
		return "en";
	}

	@Override
	protected IConcreteTerm createGreater() {
		return new Language("es");
	}

	@Override
	protected String createGreaterString() {
		return "es";
	}

	@Override
	protected URI getDatatypeIRI() {
		return XmlSchemaDatatype.LANGUAGE.toUri();
	}

	public void testValidity() {
		try {
			new Language("123");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("de1");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("abcdefghi");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("abcdefgh-abcdefgh");
		} catch (IllegalArgumentException e) {
			fail("Did not recognize valid language");
		}

		try {
			new Language("abcdefgh-abcdefghi");
			fail("Did not recognize invalid language");
		} catch (IllegalArgumentException e) {
		}

		try {
			new Language("de-de");
		} catch (IllegalArgumentException e) {
			fail("Did not recognize valid language");
		}
	}

}
