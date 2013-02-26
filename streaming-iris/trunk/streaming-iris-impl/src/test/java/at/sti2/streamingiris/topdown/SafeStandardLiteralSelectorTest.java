package at.sti2.streamingiris.topdown;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.ILiteral;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.evaluation.topdown.ILiteralSelector;
import at.sti2.streamingiris.evaluation.topdown.SafeStandardLiteralSelector;

/**
 * Test class for SafeStandardLiteralSelector
 * 
 * @author gigi
 * @see SafeStandardLiteralSelector
 */
public class SafeStandardLiteralSelectorTest extends TestCase {

	public void testSinglePositive() throws ParserException {
		String program = "?- p(?X).";

		Parser parser = new Parser();
		parser.parse(program);

		IQuery query = parser.getQueries().get(0);

		ILiteralSelector standardSelector = new SafeStandardLiteralSelector();
		ILiteral selectedLiteral = standardSelector.select(query.getLiterals());

		assertEquals("p(?X)", selectedLiteral.toString());
	}

	public void testSingleNegative() throws ParserException {
		String program = "?- not p(1).";

		Parser parser = new Parser();
		parser.parse(program);

		IQuery query = parser.getQueries().get(0);

		ILiteralSelector standardSelector = new SafeStandardLiteralSelector();
		ILiteral selectedLiteral = standardSelector.select(query.getLiterals());

		assertEquals("!p(1)", selectedLiteral.toString());
	}

	public void testSingleNegativeNotPossible() throws ParserException {
		String program = "?- not p(?X).";

		Parser parser = new Parser();
		parser.parse(program);

		IQuery query = parser.getQueries().get(0);

		ILiteralSelector standardSelector = new SafeStandardLiteralSelector();
		ILiteral selectedLiteral = standardSelector.select(query.getLiterals());

		assertEquals(null, selectedLiteral);
	}

}
