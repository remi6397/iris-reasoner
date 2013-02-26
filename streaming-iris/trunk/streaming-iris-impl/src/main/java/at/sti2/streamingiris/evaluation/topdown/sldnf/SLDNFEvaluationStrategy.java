package at.sti2.streamingiris.evaluation.topdown.sldnf;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.ProgramNotStratifiedException;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.IQuery;
import at.sti2.streamingiris.api.basics.IRule;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.evaluation.IEvaluationStrategy;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Implementation of the SLDNF evaluation strategy. For details see 'Deduktive
 * Datenbanken' by Cremers, Griefahn and Hinze (ISBN 978-3528047009).
 * 
 * @author gigi
 * 
 */
public class SLDNFEvaluationStrategy implements IEvaluationStrategy {

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
	public SLDNFEvaluationStrategy(IFacts facts, List<IRule> rules,
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

		SLDNFEvaluator evaluator = new SLDNFEvaluator(mFacts, mRules);
		IRelation relation = evaluator.evaluate(query);
		outputVariables.addAll(evaluator.getOutputVariables());

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
