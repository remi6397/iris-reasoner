package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotByteBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotByteBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Byte";
		String builtinName = IsNotByteBuiltin.class.getName();
		ITerm term = CONCRETE.createByte((byte) 2);

		checkBuiltin(iri, term, builtinName,
				IsNotDecimalBuiltin.class.getName(),
				IsNotIntBuiltin.class.getName(),
				IsNotIntegerBuiltin.class.getName(),
				IsNotLongBuiltin.class.getName(),
				IsNotNumericBuiltin.class.getName(),
				IsNotShortBuiltin.class.getName());
	}
}
