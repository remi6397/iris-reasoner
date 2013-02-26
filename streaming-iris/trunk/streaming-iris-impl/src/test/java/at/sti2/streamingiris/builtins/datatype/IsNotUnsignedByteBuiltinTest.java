package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotUnsignedByteBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotUnsignedByteBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#UnsignedByte";
		String builtinName = IsNotUnsignedByteBuiltin.class.getName();
		ITerm term = CONCRETE.createUnsignedByte((short) 2);

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName(),
				IsNotNonNegativeIntegerBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName(),
				IsNotUnsignedIntBuiltin.class.getName(),
				IsNotUnsignedLongBuiltin.class.getName(),
				IsNotUnsignedShortBuiltin.class.getName());
	}
}