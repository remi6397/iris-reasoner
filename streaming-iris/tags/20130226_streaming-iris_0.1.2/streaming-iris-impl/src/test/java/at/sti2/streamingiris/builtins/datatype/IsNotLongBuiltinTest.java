package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotLongBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotLongBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Long";
		String builtinName = IsNotLongBuiltin.class.getName();
		ITerm term = CONCRETE.createLong((long) 432532);

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName());
	}
}