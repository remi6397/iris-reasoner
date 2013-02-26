package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsDateTimeBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#DateTime";
		String builtinName = IsDateTimeBuiltin.class.getName();
		ITerm term = CONCRETE.createDateTime(2010, 9, 6, 3, 5, 0.0, 0, 0);

		checkBuiltin(iri, term, builtinName);
	}

}
