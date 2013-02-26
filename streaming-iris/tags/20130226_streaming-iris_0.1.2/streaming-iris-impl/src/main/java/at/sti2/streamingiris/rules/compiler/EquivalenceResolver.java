package at.sti2.streamingiris.rules.compiler;

import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.ITuple;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.utils.equivalence.IEquivalentTerms;

/**
 * This rule element creates all possible combinations of an input relation
 * using the equivalent terms, and adds these combinations to the output
 * relation.
 * 
 * @author Adrian Marte
 */
public class EquivalenceResolver extends RuleElement {

	private final IEquivalentTerms equivalentTerms;

	private final Configuration configuration;

	public EquivalenceResolver(List<IVariable> inputVariables,
			IEquivalentTerms equivalentTerms, Configuration configuration) {
		this.equivalentTerms = equivalentTerms;
		this.configuration = configuration;

		// We do not make any changes to the input/output variables.
		mOutputVariables = inputVariables;
	}

	@Override
	public IRelation process(IRelation input) throws EvaluationException {
		// Create the output relation.
		IRelation relation = configuration.relationFactory.createRelation();

		for (int i = 0; i < input.size(); i++) {
			ITuple tuple = input.get(i);

			// Create all combinations using the equivalent terms.
			List<ITuple> combinations = Utils.createAllCombinations(tuple,
					equivalentTerms);

			// Add combinations to output relation.
			for (ITuple combination : combinations) {
				relation.add(combination);
			}
		}

		return relation;
	}

}
