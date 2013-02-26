package at.sti2.streamingiris.evaluation;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;

/**
 * An abstract class for evaluation tests, where only the evaluations of queries
 * against a specific program have to be tested.
 * 
 * @author Adrian Marte
 */
public abstract class EvaluationTest extends TestCase {

	protected List<IRule> rules;

	protected List<IQuery> queries;

	protected IFacts facts;

	protected Configuration defaultConfiguration;

	private long duration;

	public EvaluationTest(String name) {
		super(name);
	}

	protected void setUp() throws Exception {
		// Set up the knowledge base consisting of a set of facts and a set of
		// rules.

		// Create the default configuration.
		defaultConfiguration = new Configuration();

		// Create the facts.
		facts = createFacts();

		// Create the rules.
		rules = createRules();

		// Create the queries.
		queries = createQueries();
	}

	protected abstract IFacts createFacts();

	protected abstract List<IRule> createRules();

	protected abstract List<IQuery> createQueries();

	protected IRelation evaluate(IQuery query) throws Exception {
		// Use default configuration.
		return evaluate(query, new ArrayList<IVariable>(), defaultConfiguration);
	}

	protected IRelation evaluate(IQuery query, Configuration configuration)
			throws Exception {
		return evaluate(query, new ArrayList<IVariable>(), configuration);
	}

	protected IRelation evaluate(IQuery query, List<IVariable> outputVariables)
			throws Exception {
		// Use default configuration.
		return evaluate(query, outputVariables, defaultConfiguration);
	}

	protected IRelation evaluate(IQuery query, List<IVariable> outputVariables,
			Configuration configuration) throws Exception {
		// Create strategy using factory.
		long begin = System.currentTimeMillis();
		IEvaluationStrategy strategy = configuration.evaluationStrategyFactory
				.createEvaluator(facts, rules, configuration);

		IRelation relation = strategy.evaluateQuery(query, outputVariables);

		duration = System.currentTimeMillis() - begin;

		return relation;
	}

	/**
	 * Returns the time in milliseconds it took to evaluate the previous query.
	 * 
	 * @return The time in milliseconds it took to evaluate the previous query.
	 */
	protected long getDuration() {
		return duration;
	}

}
