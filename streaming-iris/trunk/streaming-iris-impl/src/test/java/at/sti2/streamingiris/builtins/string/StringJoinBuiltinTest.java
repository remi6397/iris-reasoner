package at.sti2.streamingiris.builtins.string;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IStringTerm;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.factory.Factory;

/**
 * Test for StringJoinBuiltin.
 */
public class StringJoinBuiltinTest extends TestCase {

	private static final IVariable X = Factory.TERM.createVariable("X");

	private static final IVariable Y = Factory.TERM.createVariable("Y");

	private static final IVariable Z = Factory.TERM.createVariable("Z");

	private static final IVariable R = Factory.TERM.createVariable("R");

	public StringJoinBuiltinTest(String name) {
		super(name);
	}

	public void testJoin() throws EvaluationException {
		check("foo,bar", "foo", "bar", ",");
		check("a/*/*c", "a", "", "c", "/*");
	}

	private void check(String expected, String... actual)
			throws EvaluationException {
		ITerm[] terms = new ITerm[actual.length + 1];
		for (int i = 0; i < actual.length; i++) {
			terms[i] = Factory.TERM.createString(actual[i]);
		}
		terms[actual.length] = R;

		List<IVariable> vars = new ArrayList<IVariable>();
		for (int i = 0; i < actual.length; i++) {
			vars.add(Factory.TERM.createVariable("var" + i));
		}
		vars.add(R);

		ITuple arguments = Factory.BASIC.createTuple(vars
				.toArray(new ITerm[] {}));

		StringJoinBuiltin length = new StringJoinBuiltin(terms);

		IStringTerm expectedTerm = Factory.TERM.createString(expected);
		ITuple expectedTuple = Factory.BASIC.createTuple(expectedTerm);

		ITuple actualTuple = length.evaluate(arguments);

		assertEquals(expectedTuple, actualTuple);
	}

}
