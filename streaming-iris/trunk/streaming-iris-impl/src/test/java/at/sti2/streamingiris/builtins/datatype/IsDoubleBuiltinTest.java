package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsDoubleBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsDoubleBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Double";
		String builtinName = IsDoubleBuiltin.class.getName();
		ITerm term = CONCRETE.createDouble(34.45);

		// is also numeric
		checkBuiltin(iri, term, builtinName, IsNumericBuiltin.class.getName());
	}

}
