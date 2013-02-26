package at.sti2.streamingiris.builtins;

import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.ITerm;

/**
 * Base class of all boolean built-in predicates. This kind of built-in ... a)
 * can have any arity b) can only be evaluated when all the terms are known
 * (i.e. no unknown variables) c) evaluates to true or false
 */
public abstract class BooleanBuiltin extends AbstractBuiltin {
	/**
	 * Constructor.
	 * 
	 * @param predicate
	 *            The predicate for this built-in.
	 * @param terms
	 *            The collection of terms, must be length 2 for comparisons.
	 */
	public BooleanBuiltin(IPredicate predicate, final ITerm... terms) {
		super(predicate, terms);
	}

	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes) {
		assert variableIndexes.length == 0;

		return computeResult(terms) ? EMPTY_TERM : null;
	}

	/**
	 * Compute the result of the comparison.
	 * 
	 * @param terms
	 *            The terms
	 * @return The result of the comparison.
	 */
	protected abstract boolean computeResult(ITerm[] terms);
}
