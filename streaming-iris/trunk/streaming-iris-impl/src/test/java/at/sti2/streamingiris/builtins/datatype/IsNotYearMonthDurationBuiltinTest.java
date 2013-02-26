package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotYearMonthDurationBuiltinTest extends
		AbstractBooleanBuiltinTest {

	public IsNotYearMonthDurationBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#YearMonthDuration";
		String builtinName = IsNotYearMonthDurationBuiltin.class.getName();
		ITerm term = CONCRETE.createYearMonthDuration(true, 1889, 4);

		checkBuiltin(iri, term, builtinName,
				IsNotDurationBuiltin.class.getName());
	}
}