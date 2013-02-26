package at.sti2.streamingiris.rules.stratification;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;
import at.sti2.streamingiris.rules.IRuleStratifier;

/**
 * A test for correct stratificiation of rules with head equality.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualityStratificationTest extends TestCase {

	private Parser parser;

	private List<IRule> rules;

	public RuleHeadEqualityStratificationTest(String name) {
		super(name);
	}

	public void setUp() {
		parser = new Parser();
		rules = new ArrayList<IRule>();
	}

	public void testSuccessful() throws ParserException {
		String program = "";
		program += "q(?X) :- r(?X), t(?X).";
		program += "s(?X, ?Y) :- t(?X, ?Y), q(?X).";
		program += "?X = ?Y :- s(?X, ?Y).";

		parser.parse(program);
		rules = parser.getRules();

		stratify(rules, true);
	}

	public void testUnsuccessful() throws ParserException {
		String program = "";
		program += "q(?X) :- r(?X), t(?X).";
		program += "s(?X, ?Y) :- t(?X, ?Y), not q(?X).";
		program += "?X = ?Y :- s(?X, ?Y).";

		parser.parse(program);
		rules = parser.getRules();

		stratify(rules, false);
	}

	public void testNegatedLiteral() throws ParserException {
		String program = "?X = ?Y :- not c(?X), a(?X), b(?Y).";

		parser.parse(program);
		rules = parser.getRules();

		stratify(rules, false);
	}

	public static void stratify(List<IRule> rules, boolean succeeds) {
		IRuleStratifier stratifier = new LocalStratifier(true);
		List<List<IRule>> result = stratifier.stratify(rules);

		if (succeeds) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}

		stratifier = new LocalStratifier(false);
		result = stratifier.stratify(rules);

		if (succeeds) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}

		stratifier = new GlobalStratifier();
		result = stratifier.stratify(rules);

		if (succeeds) {
			assertNotNull(result);
		} else {
			assertNull(result);
		}
	}

}
