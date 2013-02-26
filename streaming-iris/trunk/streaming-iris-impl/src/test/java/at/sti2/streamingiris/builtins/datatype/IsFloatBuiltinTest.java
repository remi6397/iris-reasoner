package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsFloatBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsFloatBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Float";
		String builtinName = IsFloatBuiltin.class.getName();
		ITerm term = CONCRETE.createFloat((float) 35.2);

		// is also numeric
		checkBuiltin(iri, term, builtinName, IsNumericBuiltin.class.getName());
	}
}
