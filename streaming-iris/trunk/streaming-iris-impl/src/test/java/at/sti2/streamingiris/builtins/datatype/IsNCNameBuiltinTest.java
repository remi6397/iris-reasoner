package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNCNameBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNCNameBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#NCName";
		String builtinName = IsNCNameBuiltin.class.getName();
		ITerm term = CONCRETE.createNCName("NC name");

		checkBuiltin(iri, term, builtinName,
				IsNormalizedStringBuiltin.class.getName(),
				IsStringBuiltin.class.getName(),
				IsTokenBuiltin.class.getName(), IsNameBuiltin.class.getName());
	}
}
