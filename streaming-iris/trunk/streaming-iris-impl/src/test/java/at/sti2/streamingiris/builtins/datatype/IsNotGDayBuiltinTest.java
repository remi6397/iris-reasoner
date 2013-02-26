package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotGDayBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotGDayBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#GDay";
		String builtinName = IsNotGDayBuiltin.class.getName();
		ITerm term = CONCRETE.createGDay(17);

		checkBuiltin(iri, term, builtinName);
	}
}
