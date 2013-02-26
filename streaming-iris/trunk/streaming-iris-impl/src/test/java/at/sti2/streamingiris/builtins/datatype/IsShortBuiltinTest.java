package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsShortBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsShortBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Short";
		String builtinName = IsShortBuiltin.class.getName();
		ITerm term = CONCRETE.createShort((short) 34);

		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsIntBuiltin.class.getName(), IsIntegerBuiltin.class.getName(),
				IsLongBuiltin.class.getName(), IsNumericBuiltin.class.getName());
	}
}