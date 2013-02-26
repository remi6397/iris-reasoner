package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotDecimalBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotDecimalBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#Decimal";
		String builtinName = IsNotDecimalBuiltin.class.getName();
		ITerm term = CONCRETE.createDecimal(435234523.45);

		checkBuiltin(iri, term, builtinName,
				IsNotNumericBuiltin.class.getName());
	}
}
