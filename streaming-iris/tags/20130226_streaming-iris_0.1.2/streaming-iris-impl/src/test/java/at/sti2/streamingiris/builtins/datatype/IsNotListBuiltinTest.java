package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.IConcreteTerm;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotListBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotListBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {
		String iri = "http://www.w3.org/2007/rif#List";
		String builtinName = IsNotListBuiltin.class.getName();
		IConcreteTerm term = CONCRETE.createInt(1);
		ITerm list = CONCRETE.createList(term);

		checkBuiltin(iri, list, builtinName);
	}
}