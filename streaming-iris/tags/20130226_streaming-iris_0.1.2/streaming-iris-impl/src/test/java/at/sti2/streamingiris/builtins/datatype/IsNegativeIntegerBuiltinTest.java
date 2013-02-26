package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNegativeIntegerBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNegativeIntegerBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#NegativeInteger";
		String builtinName = IsNegativeIntegerBuiltin.class.getName();
		ITerm term = CONCRETE.createNegativeInteger(BigInteger
				.valueOf((long) -2435));

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNonPositiveIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName());
	}
}
