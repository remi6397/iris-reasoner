package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsPositiveIntegerBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsPositiveIntegerBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#PositiveInteger";
		String builtinName = IsPositiveIntegerBuiltin.class.getName();
		ITerm term = CONCRETE.createPositiveInteger(BigInteger.valueOf(4556));

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNonNegativeIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName());
	}
}