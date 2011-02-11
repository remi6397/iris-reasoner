/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.builtins;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * Base class of functional built-in predicates. For this kind of built-in, the
 * following properties must hold:
 * <ol>
 * <li>Can have any arity.</li>
 * <li>Can be evaluated with up to 1 unknown variable.</li>
 * <li>The unknown variable only occurs at the last position of the tuple.</li>
 * <li>If all terms are known at evaluation time, the given term is checked
 * against the computed term to indicate <code>true</code> or <code>false</code>
 * .</li>
 * </ol>
 */
public abstract class FunctionalBuiltin extends ArithmeticBuiltin {

	/**
	 * Creates a new functional builtin.
	 * 
	 * @param predicate The predicate that identifies this built-in.
	 * @param terms The terms of the instance.
	 */
	public FunctionalBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	@Override
	protected ITerm evaluateTerms(ITerm[] terms, int[] variableIndexes)
			throws EvaluationException {
		assert variableIndexes.length == 0 || variableIndexes.length == 1;

		// Run the evaluation.
		if (variableIndexes.length == 0) {
			int resultIndex = getPredicate().getArity() - 1;

			// Perform the operation.
			ITerm result = computeResult(terms);

			// Return nothing if the operation is invalid.
			if (result == null)
				return null;

			// Indicate TRUE if the operation is equal to the constant result.
			return testForEquality(terms[resultIndex], result) ? EMPTY_TERM
					: null;
		} else {
			assert variableIndexes[0] >= 0;
			assert variableIndexes[0] == getPredicate().getArity() - 1;

			return computeMissingTerm(variableIndexes[0], terms );
		}
	}

	/**
	 * Tests for equality of two terms.
	 * 
	 * @param term1 The first term.
	 * @param term2 The second term.
	 * @return <code>true</code> if both terms are equal, <code>false</code>
	 *         otherwise.
	 */
	protected boolean testForEquality(ITerm term1, ITerm term2) {
		return BuiltinHelper.equal(term1, term2, getEquivalenceClasses());
	}

	/**
	 * Computes the result when all terms but the term at the last position are
	 * known.
	 * 
	 * @param terms The terms, where the terms at the last position is a
	 *            variable representing the result.
	 * @return The computed term, or <code>null</code> if the operation is
	 *         unsuccessful.
	 * @throws EvaluationException If an error occurs.
	 */
	protected abstract ITerm computeResult(ITerm[] terms)
			throws EvaluationException;

	@Override
	protected ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms ) throws EvaluationException {
		return computeResult(terms);
	}
	
	@Override
	public int maxUnknownVariables() {
		return 1;
	}

}
