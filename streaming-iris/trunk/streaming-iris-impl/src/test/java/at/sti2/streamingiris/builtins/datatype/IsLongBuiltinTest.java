package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsLongBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsLongBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Language";
		String builtinName = IsLongBuiltin.class.getName();
		ITerm term = CONCRETE.createLong((long) 23145);

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName(), IsLongBuiltin.class.getName());
	}
}
