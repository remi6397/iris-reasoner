package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotUnsignedIntBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotUnsignedIntBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#UnsignedInt";
		String builtinName = IsNotUnsignedIntBuiltin.class.getName();
		ITerm term = CONCRETE.createUnsignedInt(32);

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName(),
				IsNotNonNegativeIntegerBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName(),
				IsNotUnsignedIntBuiltin.class.getName(),
				IsNotUnsignedLongBuiltin.class.getName());
	}
}