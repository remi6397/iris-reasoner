package at.sti2.streamingiris.rules.compiler;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.RuleUnsafeException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.utils.TermMatchingAndSubstitution;

/**
 * A compiled rule element representing the substitution of variable bindings in
 * to the rule head.
 */
public class HeadSubstituter extends RuleElement {
	/**
	 * Constructor.
	 * 
	 * @param variables
	 *            The variables from the rule body.
	 * @param headTuple
	 *            The tuple from the rule head.
	 * @throws EvaluationException
	 *             If unbound variables occur.
	 */
	public HeadSubstituter(List<IVariable> variables, ITuple headTuple,
			Configuration configuration) throws EvaluationException {
		assert variables != null;
		assert headTuple != null;
		assert configuration != null;

		mConfiguration = configuration;

		mHeadTuple = headTuple;

		// Work out the indices of variables in substitution order
		List<IVariable> variablesToSubstitute = TermMatchingAndSubstitution
				.getVariables(mHeadTuple, false);
		mIndices = new int[variablesToSubstitute.size()];

		int i = 0;
		for (IVariable variable : variablesToSubstitute) {
			int index = variables.indexOf(variable);
			if (index < 0)
				throw new RuleUnsafeException("Unbound variable in rule head: "
						+ variable);
			mIndices[i++] = index;
		}
	}

	@Override
	public IRelation process(IRelation inputRelation) {
		assert inputRelation != null;

		IRelation result = mConfiguration.relationFactory.createRelation();

		for (int i = 0; i < inputRelation.size(); ++i) {
			ITuple inputTuple = inputRelation.get(i);

			ITuple outputTuple = TermMatchingAndSubstitution
					.substituteVariablesInToTuple(mHeadTuple, inputTuple,
							mIndices);

			result.add(outputTuple);
		}

		return result;
	}

	/** The rule head tuple. */
	protected final ITuple mHeadTuple;

	/** The indices of variables in substitution order. */
	protected final int[] mIndices;

	/** The knowledge-base's configuration object. */
	protected final Configuration mConfiguration;
}
