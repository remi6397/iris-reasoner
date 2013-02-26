package at.sti2.streamingiris.evaluation.equivalence;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.evaluation.EvaluationTest;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.rules.IgnoreRuleHeadEquality;
import at.sti2.streamingiris.rules.RuleHeadEqualityRewriter;
import at.sti2.streamingiris.rules.safety.AugmentingRuleSafetyProcessor;
import at.sti2.streamingiris.utils.equivalence.IgnoreTermEquivalenceFactory;
import at.sti2.streamingiris.utils.equivalence.TermEquivalenceFactory;

/**
 * Test that compares the evaluation of a set of rules containing rules with
 * head equality using different evaluation techniques.
 * 
 * @author Adrian Marte
 */
public class EquivalenceStressTest extends EvaluationTest {

	private RandomProgramBuilder builder;

	private Program program;

	public EquivalenceStressTest(String name) {
		super(name);

		builder = new RandomProgramBuilder();
	}

	@Override
	protected void setUp() throws Exception {
		program = builder.build();

		super.setUp();
	}

	@Override
	protected IFacts createFacts() {
		return program.getFacts();
	}

	@Override
	protected List<IQuery> createQueries() {
		return program.getQueries();
	}

	@Override
	protected List<IRule> createRules() {
		return program.getRules();
	}

	public void testQuery1() throws Exception {
		executeQuery(queries.get(0));
	}

	private void executeQuery(IQuery query) throws Exception {
		Configuration config = new Configuration();

		// Use rewriting technique.
		config.equivalentTermsFactory = new IgnoreTermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new RuleHeadEqualityRewriter();
		config.ruleSafetyProcessor = new AugmentingRuleSafetyProcessor();

		evaluate(query, config);
		System.out.println("Rewriter: " + getDuration());

		config = new Configuration();

		// Use integrated rule head equality support.
		config.equivalentTermsFactory = new TermEquivalenceFactory();
		config.ruleHeadEqualityPreProcessor = new IgnoreRuleHeadEquality();

		evaluate(query, config);
		System.out.println("Integrated: " + getDuration());
	}

}
