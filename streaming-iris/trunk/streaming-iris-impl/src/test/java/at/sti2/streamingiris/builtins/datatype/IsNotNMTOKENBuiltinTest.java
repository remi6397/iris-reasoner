package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 */
public class IsNotNMTOKENBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotNMTOKENBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException {

		String iri = "http://www.w3.org/2001/XMLSchema#NMTOKEN";
		String builtinName = "at.sti2.streamingiris.builtins.datatype.IsNotNMTOKENBuiltin";
		ITerm term = CONCRETE.createNMTOKEN("NnnnMmmmmTOOOOKEEEEENNNN");

		checkBuiltin(
				iri,
				term,
				builtinName,
				"at.sti2.streamingiris.builtins.datatype.IsNotNormalizedStringBuiltin",
				"at.sti2.streamingiris.builtins.datatype.IsNotStringBuiltin",
				"at.sti2.streamingiris.builtins.datatype.IsNotTokenBuiltin");
	}
}
