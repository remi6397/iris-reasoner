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
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.equivalence.IEquivalentTerms;

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
