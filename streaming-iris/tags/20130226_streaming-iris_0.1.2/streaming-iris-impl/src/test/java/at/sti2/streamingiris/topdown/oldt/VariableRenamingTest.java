package at.sti2.streamingiris.topdown.oldt;

import java.util.Map;

import junit.framework.TestCase;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.ITerm;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.evaluation.topdown.TopDownHelper;

/**
 * Test class for TopDownHelper.getVariableMapForVariableRenaming(rule, query)
 * which is used by SLDNF and OLDT evaluation strategies.
 * 
 * @author gigi
 * @see TopDownHelper.getVariableMapForVariableRenaming(IRule rule, IQuery
 *      query)
 */
public class VariableRenamingTest extends TestCase {

	/**
	 * Tests if variables get substituted correctly when the same variable
	 * appears in the rule head and the current evaluation body.
	 * 
	 * @throws ParserException
	 *             on failure
	 * @throws RuleUnsafeException
	 *             when the program contains unsafe rules
	 */
	public void testVariableClash() throws ParserException, RuleUnsafeException {

		String program = "p(?x, ?y) :- ?x > ?y." + "?- 2 + ?y = ?x, p(2, ?x).";

		Parser parser = new Parser();
		parser.parse(program);
		IQuery query = parser.getQueries().get(0);
		IRule rule = parser.getRules().get(0);

		Map<IVariable, ITerm> map = TopDownHelper
				.getVariableMapForVariableRenaming(rule, query);

		System.out.println(map);
		assertEquals(false, map.isEmpty());
	}

}
