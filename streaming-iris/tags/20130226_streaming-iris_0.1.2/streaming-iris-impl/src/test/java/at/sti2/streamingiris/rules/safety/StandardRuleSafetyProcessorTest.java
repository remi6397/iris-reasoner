package at.sti2.streamingiris.rules.safety;

import junit.framework.TestCase;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.compiler.Parser;
import at.sti2.streamingiris.compiler.ParserException;

public class StandardRuleSafetyProcessorTest extends TestCase {
	StandardRuleSafetyProcessor mProcessor;

	@Override
	protected void setUp() throws Exception {
		mProcessor = new StandardRuleSafetyProcessor(true, true);
	}

	public void testSimpleSafe() throws Exception {
		IRule rule = makeRule("p(?x) :- q(?x).");

		mProcessor.process(rule);
	}

	public void testConstantsInHeadAndEmptyBodySafe() throws Exception {
		IRule rule = makeRule("p(2) :- .");

		mProcessor.process(rule);
	}

	public void testConstantsInHeadAndNegationInBodySafe() throws Exception {
		IRule rule = makeRule("p(2) :- not q(3).");

		mProcessor.process(rule);
	}

	public void testConstantsInHeadAndNegationWithVariablesInBodySafe()
			throws Exception {
		IRule rule = makeRule("p(2) :- not q(?x).");

		mProcessor.process(rule);
	}

	public void testThroughEqualitySafe() throws Exception {
		IRule rule = makeRule("p(?y) :- q(?x), ?x = ?y.");

		mProcessor.process(rule);
	}

	public void testThroughInequalityUnSafe() throws Exception {
		IRule rule = makeRule("p(?y) :- q(?x), ?x != ?y.");

		checkUnsafe(rule);
	}

	public void testArithmeticTernarySafe() throws Exception {
		IRule rule = makeRule("p(?z) :- q(?x, ?y), ?x + ?y = ?z.");

		mProcessor.process(rule);
	}

	public void testArithmeticTernaryUnSafe() throws Exception {
		IRule rule = makeRule("p(?z) :- q(?x, ?y), ?x + ?y = ?z.");

		mProcessor = new StandardRuleSafetyProcessor(true, false);
		checkUnsafe(rule);
	}

	public void testSimpleUnSafe() throws Exception {
		IRule rule = makeRule("p(?x) :- q(?y).");

		checkUnsafe(rule);
	}

	public void testNegatedVariablesSafe() throws Exception {
		IRule rule = makeRule("p(?x) :- q(?x), not r(?y).");

		mProcessor.process(rule);
	}

	public void testNegatedVariablesUnSafe() throws Exception {
		IRule rule = makeRule("p(?x) :- q(?x), not r(?y).");

		mProcessor = new StandardRuleSafetyProcessor(false, true);
		checkUnsafe(rule);
	}

	private void checkUnsafe(IRule rule) {
		try {
			mProcessor.process(rule);
			fail("RuleUnsafeException not thrown.");
		} catch (RuleUnsafeException e) {
		}
	}

	private IRule makeRule(String strRule) throws ParserException {
		Parser parser = new Parser();
		parser.parse(strRule);

		return parser.getRules().get(0);
	}
}
