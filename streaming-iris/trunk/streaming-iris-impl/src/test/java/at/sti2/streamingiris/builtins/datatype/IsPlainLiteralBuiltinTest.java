package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsPlainLiteralBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsPlainLiteralBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#PlainLiteral";
		String builtinName = IsPlainLiteralBuiltin.class.getName();
		ITerm term = CONCRETE.createPlainLiteral("plain literal");

		checkBuiltin(iri, term, builtinName, IsStringBuiltin.class.getName());
	}
}