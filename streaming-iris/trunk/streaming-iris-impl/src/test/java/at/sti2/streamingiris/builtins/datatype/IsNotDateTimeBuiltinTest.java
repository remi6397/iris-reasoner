package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotDateTimeBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotDateTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#DateTime";
		String builtinName = IsNotDateTimeBuiltin.class.getName();
		ITerm term = CONCRETE.createDateTime(1912, 8, 17, 22, 13, 2.3, 0, 0);

		checkBuiltin(iri, term, builtinName);
	}
}
