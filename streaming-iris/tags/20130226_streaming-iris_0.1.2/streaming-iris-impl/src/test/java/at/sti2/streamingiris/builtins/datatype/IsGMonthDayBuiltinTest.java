package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsGMonthDayBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsGMonthDayBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#GMonthDay";
		String builtinName = IsGMonthDayBuiltin.class.getName();
		ITerm term = CONCRETE.createGMonthDay(10, 16);

		checkBuiltin(iri, term, builtinName);
	}
}
