package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotDateBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotDateBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Date";
		String builtinName = "at.sti2.streamingiris.builtins.datatype.IsNotDateBuiltin";
		ITerm term = CONCRETE.createDate(1984, 3, 16);

		checkBuiltin(iri, term, builtinName);
	}
}
