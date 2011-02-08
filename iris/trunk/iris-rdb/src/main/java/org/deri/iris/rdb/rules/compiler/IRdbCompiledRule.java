package org.deri.iris.rdb.rules.compiler;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;
import org.deri.iris.rules.compiler.ICompiledRule;

public interface IRdbCompiledRule extends ICompiledRule {

	/**
	 * Evaluate the rule with all known facts.
	 * 
	 * @return The result relation for this rule.
	 * @throws EvaluationException
	 *             If an error occurs during evaluation.
	 */
	IRdbRelation evaluate() throws EvaluationException;

	/**
	 * Evaluate the rule using deltas (see semi-naive evaluation) to more
	 * intelligently seek out tuples that have not already been computed.
	 * 
	 * @param deltas
	 *            The collection of recently discovered facts.
	 * @return The result relation for this rule.
	 * @throws EvaluationException
	 *             If an error occurs during evaluation.
	 */
	IRdbRelation evaluateIteratively(IFacts deltas) throws EvaluationException;

	/**
	 * Returns the output tuple of the compiled rule. The output tuple specifies
	 * the terms, which can be constants or variables, of the output relation.
	 * In other words, the term at position i of the output tuple, represents
	 * the value of the term at position i of the tuple in the output relation.
	 * 
	 * @return The output tuple.
	 */
	public ITuple getOutputTuple();

}
