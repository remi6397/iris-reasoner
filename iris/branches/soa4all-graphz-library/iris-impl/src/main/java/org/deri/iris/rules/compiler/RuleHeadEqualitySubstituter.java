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
package org.deri.iris.rules.compiler;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.equivalence.IEquivalentTerms;

/**
 * A compiled rule element representing the substitution of variable bindings in
 * to the rule head for rules with rule head equality. This substituter sets all
 * pairs that are substituted into the head as equivalent in the specified term
 * equivalence relation. It then adds all possible combinations of the two terms
 * and its equivalent terms to the relation.
 * 
 * @author Adrian Marte
 */
public class RuleHeadEqualitySubstituter extends HeadSubstituter {

	/**
	 * The equivalent terms.
	 */
	private IEquivalentTerms equivalentTerms;

	/**
	 * Constructor.
	 * 
	 * @param variables The variables from the rule body.
	 * @param headTuple The tuple from the rule head.
	 * @param equivalentTerms The equivalent terms.
	 * @param configuration The configuration.
	 * @throws EvaluationException If unbound variables occur.
	 */
	public RuleHeadEqualitySubstituter(List<IVariable> variables,
			ITuple headTuple, IEquivalentTerms equivalentTerms,
			Configuration configuration) throws EvaluationException {
		super(variables, headTuple, configuration);

		assert headTuple.size() == 2 : "Only works on binary tuples.";

		this.equivalentTerms = equivalentTerms;
	}

	@Override
	public IRelation process(IRelation inputRelation) {
		// Do standard head substitution.
		IRelation relation = super.process(inputRelation);

		// Create a new relation which only contains valid equivalence
		// relations. For instance, an equivalence relation of two numeric terms
		// is invalid.
		IRelation result = mConfiguration.relationFactory.createRelation();

		for (int i = 0; i < relation.size(); i++) {
			ITuple tuple = relation.get(i);

			assert tuple.size() == 2 : "Only works on binary tuples.";

			ITerm x = tuple.get(0);
			ITerm y = tuple.get(1);

			// ?X and ?Y are equivalent.
			equivalentTerms.setEquivalent(x, y);
			result.add(tuple);
		}

		return result;
	}

}
