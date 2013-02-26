package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotNCNameBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotNCNameBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#NCName";
		String builtinName = IsNotNCNameBuiltin.class.getName();
		ITerm term = CONCRETE.createNCName("NC name");

		checkBuiltin(iri, term, builtinName,
				IsNotNormalizedStringBuiltin.class.getName(),
				IsNotStringBuiltin.class.getName(),
				IsNotTokenBuiltin.class.getName(),
				IsNotNameBuiltin.class.getName());
	}
}
