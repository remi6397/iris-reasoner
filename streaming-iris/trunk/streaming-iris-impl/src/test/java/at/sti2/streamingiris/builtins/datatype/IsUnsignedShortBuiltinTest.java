package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsUnsignedShortBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsUnsignedShortBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#UnsignedShort";
		String builtinName = IsUnsignedShortBuiltin.class.getName();

		try {
			CONCRETE.createUnsignedShort(-24);
			fail("A signed value should have thrown IllegalArgumentException!");
		} catch (IllegalArgumentException e) {
		}

		ITerm term = CONCRETE.createUnsignedShort(4);

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntegerBuiltin.class.getName(),
				IsNonNegativeIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName(),
				IsUnsignedIntBuiltin.class.getName(),
				IsUnsignedLongBuiltin.class.getName());
	}
}