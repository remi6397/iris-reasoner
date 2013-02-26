package at.sti2.streamingiris.rules.compiler;

import java.util.List;

import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;

/**
 * Interface for a compiled rule.
 */
public interface ICompiledRule {
	/**
	 * Evaluate rule with all known facts.
	 * 
	 * @return The result relation for this rule.
	 * @throws EvaluationException
	 */
	IRelation evaluate() throws EvaluationException;

	/**
	 * Evaluate the rule using deltas (see semi-naive evaluation) to more
	 * intelligently seek out tuples that have not already been computed.
	 * 
	 * @param deltas
	 *            The collection of recently discovered facts.
	 * @return The result relation for this rule.
	 * @throws EvaluationException
	 */
	IRelation evaluateIteratively(IFacts deltas) throws EvaluationException;

	/**
	 * If this compiled rule represents a rule, then return the head predicate.
	 * 
	 * @return The head predicate.
	 */
	IPredicate headPredicate();

	/**
	 * If this compiled rule represents a query, then return the variables
	 * bindings of the result relation.
	 * 
	 * @return The list of variables in the order in which they are bound to
	 *         terms of the result relation.
	 */
	List<IVariable> getVariablesBindings();
}
