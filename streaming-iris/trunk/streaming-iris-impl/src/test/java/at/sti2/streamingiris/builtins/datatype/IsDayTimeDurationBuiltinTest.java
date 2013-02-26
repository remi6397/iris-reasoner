package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsDayTimeDurationBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsDayTimeDurationBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#DayTimeDuration";
		String builtinName = IsDayTimeDurationBuiltin.class.getName();

		ITerm term = CONCRETE.createDayTimeDuration(true, 4, 11, 34, 12.5);

		checkBuiltin(iri, term, builtinName, IsDurationBuiltin.class.getName());
	}

}
