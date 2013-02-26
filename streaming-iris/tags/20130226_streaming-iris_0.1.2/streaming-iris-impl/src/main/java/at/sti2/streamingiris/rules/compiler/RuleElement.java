package at.sti2.streamingiris.rules.compiler;

import java.util.List;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;

/**
 * A base class for all compiled rule elements.
 */
public abstract class RuleElement {
	/**
	 * Default constructor.
	 */
	public RuleElement() {
	}

	/**
	 * Called to process tuples from previous literals.
	 * 
	 * @param previous
	 *            The relation of tuples from the previous rule element. This
	 *            should be null if this element represents the first literal.
	 * @return The output relation for this literal.
	 * @throws EvaluationException
	 */
	public abstract IRelation process(IRelation input)
			throws EvaluationException;

	/**
	 * Create a substitute rule element that will use the corresponding delta if
	 * it exists.
	 * 
	 * @param deltas
	 *            The Deltas from the last round of iterative evaluation.
	 * @return A substitute rule element if possible.
	 */
	public RuleElement getDeltaSubstitution(IFacts deltas) {
		return null;
	}

	/**
	 * Get the variable bindings for tuples output from this rule element.
	 * 
	 * @return The list of variables in term order.
	 */
	public List<IVariable> getOutputVariables() {
		return mOutputVariables;
	}

	/** The variable bindings for tuples output from this rule element. */
	protected List<IVariable> mOutputVariables;
}
