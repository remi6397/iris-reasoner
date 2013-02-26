package at.sti2.streamingiris.rules.compiler;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

/**
 * A compiled rule element representing the first literal in a rule body, when
 * that literal is a positive ordinary predicate.
 */
public class FirstSubgoal extends RuleElement {
	/**
	 * Constructor.
	 * 
	 * @param predicate
	 *            The predicate for this literal.
	 * @param relation
	 *            The relation for this literal.
	 * @param viewCriteria
	 *            The tuple from the sub-goal in the rule.
	 * @param equivalentTerms
	 *            The equivalent terms.
	 */
	public FirstSubgoal(IPredicate predicate, IRelation relation,
			ITuple viewCriteria, IEquivalentTerms equivalentTerms,
			Configuration configuration) {
		assert predicate != null;
		assert relation != null;
		assert viewCriteria != null;
		assert configuration != null;

		mConfiguration = configuration;

		mView = new View(relation, viewCriteria, equivalentTerms,
				mConfiguration.relationFactory);

		mPredicate = predicate;
		mViewCriteria = viewCriteria;
		mOutputVariables = mView.variables();
		mEquivalentTerms = equivalentTerms;
	}

	/**
	 * Constructor used for iterative evaluation.
	 * 
	 * @param predicate
	 *            The predicate for this literal.
	 * @param relation
	 *            The relation for this literal.
	 * @param viewCriteria
	 *            The tuple from the sub-goal in the rule.
	 * @param variables
	 *            Calculated variables.
	 * @param simple
	 *            Indicator if the view is a simple one (only unique variables).
	 * @param equivalentTerms
	 *            The equivalent terms.
	 */
	public FirstSubgoal(IPredicate predicate, IRelation relation,
			ITuple viewCriteria, List<IVariable> variables, boolean simple,
			IEquivalentTerms equivalentTerms, Configuration configuration) {
		assert predicate != null;
		assert relation != null;
		assert viewCriteria != null;
		assert configuration != null;

		mConfiguration = configuration;

		mView = new View(relation, viewCriteria, variables, simple,
				equivalentTerms, mConfiguration.relationFactory);

		mPredicate = predicate;
		mViewCriteria = viewCriteria;
		mOutputVariables = mView.variables();
		mEquivalentTerms = equivalentTerms;
	}

	@Override
	public IRelation process(IRelation leftRelation) {
		assert leftRelation != null;
		assert leftRelation.size() == 1; // i.e. there is no left relation, just
											// a starting point.

		return mView;
	}

	@Override
	public RuleElement getDeltaSubstitution(IFacts deltas) {
		IRelation delta = deltas.get(mPredicate);

		if (delta == null || delta.size() == 0)
			return null;

		return new FirstSubgoal(mPredicate, delta, mViewCriteria,
				mView.variables(), mView.isSimple(), mEquivalentTerms,
				mConfiguration);
	}

	/** The equivalent terms. */
	private IEquivalentTerms mEquivalentTerms;

	/** Predicate of the literal. */
	private final IPredicate mPredicate;

	/** The tuple from the sub-goal in the rule. */
	private final ITuple mViewCriteria;

	/** The view on this literal. */
	private final View mView;

	private final Configuration mConfiguration;
}
