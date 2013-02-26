package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsBooleanBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsBooleanBuiltinTest(String name) {
		super(name);
	}

	public void testBoolean() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#boolean";
		String builtinName = IsBooleanBuiltin.class.getName();
		ITerm term = CONCRETE.createBoolean(true);

		checkBuiltin(iri, term, builtinName);
	}

}
