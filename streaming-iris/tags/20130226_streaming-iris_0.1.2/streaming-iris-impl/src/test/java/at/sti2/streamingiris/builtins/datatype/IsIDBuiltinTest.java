package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsIDBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsIDBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#ID";
		String builtinName = IsIDBuiltin.class.getName();
		ITerm term = CONCRETE.createID(new String("ID 0815"));

		checkBuiltin(iri, term, builtinName, IsNCNameBuiltin.class.getName(),
				IsNormalizedStringBuiltin.class.getName(),
				IsStringBuiltin.class.getName(),
				IsTokenBuiltin.class.getName(), IsNameBuiltin.class.getName());
	}
}