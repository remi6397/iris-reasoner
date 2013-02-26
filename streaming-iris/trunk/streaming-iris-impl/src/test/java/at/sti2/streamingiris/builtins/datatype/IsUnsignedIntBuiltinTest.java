package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsUnsignedIntBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsUnsignedIntBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#UnsignedInt";
		String builtinName = IsUnsignedIntBuiltin.class.getName();

		try {
			CONCRETE.createUnsignedInt(-24334);
			fail("A signed value should have thrown IllegalArgumentException!");
		} catch (IllegalArgumentException e) {
		}

		ITerm term = CONCRETE.createUnsignedInt(234);

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNonNegativeIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName(),
				IsUnsignedIntBuiltin.class.getName(),
				IsUnsignedLongBuiltin.class.getName());
	}
}