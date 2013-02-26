package at.sti2.streamingiris.rules;

import java.util.Collection;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;

/**
 * Test for EquivalenceRuleRewriter.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualityRewriterTest extends TestCase {

	private RuleHeadEqualityRewriter rewriter;

	public RuleHeadEqualityRewriterTest(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		rewriter = new RuleHeadEqualityRewriter(false, true);
	}

	// Just a simple test, to test if enough rules are created.
	public void testRewrite() throws ParserException {
		String program = "q(?X, ?Y, ?Z) :- p(?X, ?Y), r(?Z).";

		Parser parser = new Parser();
		parser.parse(program);

		Collection<IRule> rules = parser.getRules();
		Collection<IRule> newRules = rewriter.rewrite(rules);

		assertEquals("Incorrect number of rules created.", 10, newRules.size());
	}

}
