package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotIDREFBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotIDREFBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#IDREF";
		String builtinName = IsNotIDREFBuiltin.class.getName();
		ITerm term = CONCRETE.createIDREF("id ref");

		checkBuiltin(iri, term, builtinName,
				IsNotNCNameBuiltin.class.getName(),
				IsNotNormalizedStringBuiltin.class.getName(),
				IsNotStringBuiltin.class.getName(),
				IsNotNameBuiltin.class.getName(),
				IsNotTokenBuiltin.class.getName());
	}
}
