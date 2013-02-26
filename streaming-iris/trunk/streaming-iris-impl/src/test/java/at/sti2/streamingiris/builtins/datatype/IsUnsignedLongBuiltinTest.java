package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.math.BigInteger;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsUnsignedLongBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsUnsignedLongBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#UnsignedLong";
		String builtinName = IsUnsignedLongBuiltin.class.getName();

		try {
			CONCRETE.createUnsignedLong(BigInteger.valueOf(-24334));
			fail("A signed value should have thrown IllegalArgumentException!");
		} catch (IllegalArgumentException e) {
		}

		ITerm term = CONCRETE.createUnsignedLong(BigInteger.valueOf(24334));

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNonNegativeIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName(),
				IsUnsignedLongBuiltin.class.getName());
	}
}