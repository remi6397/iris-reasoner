package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsTimeBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsTimeBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Time";
		String builtinName = IsTimeBuiltin.class.getName();
		ITerm term = CONCRETE.createTime(14, 12, 45.5, 0, 0);

		checkBuiltin(iri, term, builtinName);
	}
}