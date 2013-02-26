package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsXMLLiteralBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsXMLLiteralBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#XMLLiteral";
		String builtinName = IsXMLLiteralBuiltin.class.getName();
		ITerm term = CONCRETE.createXMLLiteral("xml literal");

		checkBuiltin(iri, term, builtinName);
	}
}