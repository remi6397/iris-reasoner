package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotIDBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotIDBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#ID";
		String builtinName = IsNotIDBuiltin.class.getName();
		ITerm term = CONCRETE.createID(new String("ID name"));

		checkBuiltin(iri, term, builtinName,
				IsNotNCNameBuiltin.class.getName(),
				IsNotNormalizedStringBuiltin.class.getName(),
				IsNotStringBuiltin.class.getName(),
				IsNotTokenBuiltin.class.getName(),
				IsNotNameBuiltin.class.getName());
	}
}
