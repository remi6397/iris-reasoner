package at.sti2.streamingiris.parser;

import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.rules.RuleHeadEquality;

/**
 * Test for correct parsing of rules with rule head equality.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualityParserTest extends TestCase {

	private Parser parser;

	private List<IRule> rules;

	public RuleHeadEqualityParserTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		parser = new Parser();

		String program = "?X = ?Y :- foo(?X), bar(?Y), ?X = ?Y.";
		program += "'A' = 'B' :- bar(?X).";
		program += "'A' = 'B' :- .";

		parser.parse(program);
		rules = parser.getRules();
	}

	public void testPredicate() {
		IRule rule = rules.get(0);

		assertTrue("Rule head equality not recognized.",
				RuleHeadEquality.hasRuleHeadEquality(rule));

		rule = rules.get(1);

		assertTrue("Rule head equality not recognized.",
				RuleHeadEquality.hasRuleHeadEquality(rule));

		rule = rules.get(2);

		assertTrue("Rule head equality not recognized.",
				RuleHeadEquality.hasRuleHeadEquality(rule));
	}

}
