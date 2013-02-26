package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsDateTimeStampBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsDateTimeStampBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#DateTimeStamp";
		String builtinName = IsDateTimeStampBuiltin.class.getName();

		// also dateTime is possible
		ITerm term = CONCRETE
				.createDateTimeStamp(2010, 10, 6, 3, 23, 0.0, 0, 0);

		checkBuiltin(iri, term, builtinName, IsDateTimeBuiltin.class.getName());
	}

}
