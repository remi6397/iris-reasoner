package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotGYearBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotGYearBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#GYear";
		String builtinName = IsNotGYearBuiltin.class.getName();
		ITerm term = CONCRETE.createGYear(1977);

		checkBuiltin(iri, term, builtinName);
	}
}
