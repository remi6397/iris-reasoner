package at.sti2.streamingiris.builtins.string;

import junit.framework.TestCase;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.factory.Factory;

/**
 */
public class XMLLiteralEqualBuiltinTest extends TestCase {

	private static final ITerm X = Factory.TERM.createVariable("X");

	private static final ITerm Y = Factory.TERM.createVariable("Y");

	private static final ITuple EMPTY_TUPLE = Factory.BASIC.createTuple();

	public XMLLiteralEqualBuiltinTest(String name) {
		super(name);
	}

	public void testBuiltin() throws EvaluationException {

		ITerm term1 = Factory.CONCRETE.createXMLLiteral("XML Literal");
		ITerm term2 = Factory.CONCRETE.createXMLLiteral("XML Literal");
		ITerm term3 = Factory.CONCRETE.createXMLLiteral("blabla");

		ITuple arguments = Factory.BASIC.createTuple(X, Y);

		XMLLiteralEqualBuiltin builtin = new XMLLiteralEqualBuiltin(term1,
				term1);
		ITuple actualTuple = builtin.evaluate(arguments);
		assertEquals(EMPTY_TUPLE, actualTuple);

		builtin = new XMLLiteralEqualBuiltin(term1, term2);
		actualTuple = builtin.evaluate(arguments);
		assertEquals(EMPTY_TUPLE, actualTuple);

		builtin = new XMLLiteralEqualBuiltin(term1, term3);
		actualTuple = builtin.evaluate(arguments);
		assertEquals(null, actualTuple);
	}

}
