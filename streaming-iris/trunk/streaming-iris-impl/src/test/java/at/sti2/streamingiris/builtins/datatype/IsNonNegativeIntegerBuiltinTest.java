package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNonNegativeIntegerBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNonNegativeIntegerBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#NonNegativeInteger";
		String builtinName = IsNonNegativeIntegerBuiltin.class.getName();
		ITerm term = CONCRETE.createNonNegativeInteger(BigInteger
				.valueOf((long) 0));

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName());
	}

}
