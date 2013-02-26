package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsLanguageBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsLanguageBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Language";
		String builtinName = IsLanguageBuiltin.class.getName();
		ITerm term = CONCRETE.createLanguage("en");

		// is also String,..
		checkBuiltin(iri, term, builtinName,
				IsNormalizedStringBuiltin.class.getName(),
				IsStringBuiltin.class.getName(), IsTokenBuiltin.class.getName());
	}
}
