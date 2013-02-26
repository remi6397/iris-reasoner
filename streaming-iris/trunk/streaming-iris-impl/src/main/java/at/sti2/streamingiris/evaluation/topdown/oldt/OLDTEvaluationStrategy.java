package at.sti2.streamingiris.evaluation.topdown.oldt;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.ProgramNotStratifiedException;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.evaluation.IEvaluationStrategy;
import at.sti2.streamingiris.evaluation.topdown.ITopDownEvaluator;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;

public class OLDTEvaluationStrategy implements IEvaluationStrategy {

	/**
	 * Constructor
	 * 
	 * @param facts
	 *            Given facts.
	 * @param rules
	 *            Given rules.
	 * @param configuration
	 *            Configuration
	 * @throws EvaluationException
	 */
	public OLDTEvaluationStrategy(IFacts facts, List<IRule> rules,
			Configuration configuration) throws EvaluationException {
		mFacts = facts;
		mRules = rules;
		mConfiguration = configuration;
	}

	/**
	 * Evaluate the query
	 */
	public IRelation evaluateQuery(IQuery query, List<IVariable> outputVariables)
			throws ProgramNotStratifiedException, RuleUnsafeException,
			EvaluationException {
		if (query == null)
			throw new IllegalArgumentException(
					"SLDEvaluationStrategy.evaluateQuery() - query must not be null.");

		ITopDownEvaluator evaluator = new OLDTEvaluator(mFacts, mRules);
		IRelation relation = evaluator.evaluate(query);
		outputVariables = evaluator.getOutputVariables();

		return relation;
	}

	protected final IFacts mFacts;
	protected final List<IRule> mRules;
	protected final Configuration mConfiguration;

	@Override
	public void evaluateRules(IFacts facts, long timestamp)
			throws EvaluationException {
		// FIXME Norbert: not implemented
	}

}
