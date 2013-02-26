package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsSqNameBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsSqNameBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#SqName";
		String builtinName = IsSqNameBuiltin.class.getName();
		ITerm term = CONCRETE.createSqName("Sq#name");

		checkBuiltin(iri, term, builtinName);
	}
}