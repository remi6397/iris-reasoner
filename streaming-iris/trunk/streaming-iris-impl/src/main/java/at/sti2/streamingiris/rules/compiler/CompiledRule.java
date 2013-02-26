package at.sti2.streamingiris.rules.compiler;

import java.util.ArrayList;
import java.util.List;

import at.sti2.streamingiris.Configuration;
import at.sti2.streamingiris.EvaluationException;
import at.sti2.streamingiris.api.basics.IPredicate;
import at.sti2.streamingiris.api.terms.IVariable;
import at.sti2.streamingiris.factory.Factory;
import at.sti2.streamingiris.facts.IFacts;
import at.sti2.streamingiris.storage.IRelation;
import at.sti2.streamingiris.storage.simple.SimpleRelationFactory;

/**
 * A compiled rule.
 */
public class CompiledRule implements ICompiledRule {
	/**
	 * Constructor.
	 * 
	 * @param elements
	 *            The rule elements produced by the rule compiler.
	 * @param headPredicate
	 *            The head predicate of the original rule.
	 */
	public CompiledRule(List<RuleElement> elements, IPredicate headPredicate,
			Configuration configuration) {
		assert elements.size() > 0;
		assert configuration != null;

		mConfiguration = configuration;

		mHeadPredicate = headPredicate;

		mElements = elements;
	}

	/**
	 * Evaluate the rule. Each element is called in turn to produce tuples to
	 * pass on to the next rule element. If any rule element outputs an empty
	 * relation, then stop.
	 * 
	 * @throws EvaluationException
	 */
	public IRelation evaluate() throws EvaluationException {
		// The first literal receives the starting relation (which has one zero
		// length tuple in it). */
		IRelation output = mStartingRelation;

		for (RuleElement element : mElements) {
			output = element.process(output);

			// Must always get some output relation, even if it is empty.
			assert output != null;

			// All literals are conjunctive, so if any literal produces no
			// results,
			// then the whole rule produces no results.
			if (output.size() == 0)
				break;
		}

		return output;
	}

	public IRelation evaluateIteratively(IFacts deltas)
			throws EvaluationException {
		IRelation union = mConfiguration.relationFactory.createRelation();

		/*
		 * for each literal (rule element) for which there exists a delta
		 * substitution substitute the rule element with the delta evaluate the
		 * whole rule store the results combine all the results and return
		 */
		for (int r = 0; r < mElements.size(); ++r) {
			RuleElement original = mElements.get(r);

			RuleElement substitution = original.getDeltaSubstitution(deltas);

			if (substitution != null) {
				mElements.set(r, substitution);

				// Now just evaluate the modified rule
				IRelation output = evaluate();

				for (int t = 0; t < output.size(); ++t)
					union.add(output.get(t));

				// Put the original rule element back the way it was
				mElements.set(r, original);
			}
		}

		return union;
	}

	public IPredicate headPredicate() {
		return mHeadPredicate;
	}

	public List<IVariable> getVariablesBindings() {
		if (mElements.size() > 0)
			return mElements.get(mElements.size() - 1).getOutputVariables();
		else
			return new ArrayList<IVariable>();
	}

	/** The starting relation for evaluating each sub-goal. */
	private static final IRelation mStartingRelation = new SimpleRelationFactory()
			.createRelation();

	static {
		// Start the evaluation with a single, zero-length tuple.
		mStartingRelation.add(Factory.BASIC.createTuple());
	}

	/** The rule elements in order. */
	private final List<RuleElement> mElements;

	/** The head predicate. */
	private final IPredicate mHeadPredicate;

	private final Configuration mConfiguration;
}
