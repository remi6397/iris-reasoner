package at.sti2.streamingiris.builtins.datatype;

import static at.sti2.streamingiris.factory.Factory.CONCRETE;

import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.ITerm;

public class IsNotAnyURIBuiltinTest extends AbstractBooleanBuiltinTest {

	public IsNotAnyURIBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws SecurityException,
			IllegalArgumentException, EvaluationException,
			ClassNotFoundException, NoSuchMethodException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException, URISyntaxException {

		String iri = "http://www.w3.org/2001/XMLSchema#AnyURI";
		String builtinName = IsNotAnyURIBuiltin.class.getName();
		ITerm term = CONCRETE.createAnyURI(new URI("http://sti.uri.at"));

		checkBuiltin(iri, term, builtinName);
	}
}
