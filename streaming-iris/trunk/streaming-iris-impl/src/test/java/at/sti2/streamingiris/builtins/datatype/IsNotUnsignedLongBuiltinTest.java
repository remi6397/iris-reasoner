package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotUnsignedLongBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotUnsignedLongBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#UnsignedLong";
		String builtinName = IsNotUnsignedLongBuiltin.class.getName();
		ITerm term = CONCRETE.createUnsignedLong((BigInteger
				.valueOf((long) 4234232)));

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName(),
				IsNotNonNegativeIntegerBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName(),
				IsNotUnsignedLongBuiltin.class.getName());
	}
}