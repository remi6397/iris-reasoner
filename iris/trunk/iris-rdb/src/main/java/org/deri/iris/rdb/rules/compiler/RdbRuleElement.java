package org.deri.iris.rdb.rules.compiler;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rdb.storage.IRdbRelation;

public abstract class RdbRuleElement {

	/**
	 * Default constructor.
	 */
	public RdbRuleElement() {
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
	public abstract IRdbRelation process(IRdbRelation input)
			throws EvaluationException;

	public abstract RdbRuleElement getDeltaSubstitution(IFacts deltas);

	public abstract ITuple getOutputTuple();
	
	public abstract void dispose();

}
