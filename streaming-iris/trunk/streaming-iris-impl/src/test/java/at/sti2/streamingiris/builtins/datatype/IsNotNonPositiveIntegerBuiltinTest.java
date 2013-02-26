package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotNonPositiveIntegerBuiltinTest extends
		AbstractBooleanBuiltinTest {

	public IsNotNonPositiveIntegerBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#NonPositiveInteger";
		String builtinName = IsNotNonPositiveIntegerBuiltin.class.getName();
		ITerm term = CONCRETE.createNonPositiveInteger(BigInteger.ZERO);

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName());
	}
}
