package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotDateTimeStampBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotDateTimeStampBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#DateTimeStamp";
		String builtinName = IsNotDateTimeStampBuiltin.class.getName();
		ITerm term = CONCRETE.createDateTimeStamp(1912, 8, 17, 22, 13, 2.3, 0,
				0);

		checkBuiltin(iri, term, builtinName,
				IsNotDateTimeBuiltin.class.getName());
	}

}
