package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsByteBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsByteBuiltinTest(String name) {
		super(name);
	}

	public void testByte() throws SecurityException, IllegalArgumentException,
			EvaluationException, ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#byte";
		String builtinName = IsByteBuiltin.class.getName();

		ITerm term = CONCRETE.createByte((byte) 1);

		// is also numeric
		checkBuiltin(iri, term, builtinName, IsDecimalBuiltin.class.getName(),
				IsLongBuiltin.class.getName(), IsByteBuiltin.class.getName(),
				IsIntBuiltin.class.getName(), IsIntegerBuiltin.class.getName(),
				IsNumericBuiltin.class.getName(),
				IsShortBuiltin.class.getName());
	}

}
