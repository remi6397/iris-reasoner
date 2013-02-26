package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotShortBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotShortBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Short";
		String builtinName = IsNotShortBuiltin.class.getName();
		ITerm term = CONCRETE.createShort((short) 12);

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotIntBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName(),
				IsNotLongBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName());
	}
}
