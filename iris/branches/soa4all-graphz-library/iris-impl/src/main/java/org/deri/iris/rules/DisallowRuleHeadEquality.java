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
package org.deri.iris.rules;

import java.util.List;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.facts.IFacts;

/**
 * This pre-processor throws an exception if the specified list of rules
 * contains a rule with rule head equality.
 * 
 * @author Adrian Marte
 */
public class DisallowRuleHeadEquality implements IRuleHeadEqualityPreProcessor {

	public List<IRule> process(List<IRule> rules, IFacts facts)
			throws EvaluationException {
		// Check if any rule with rule head equality is present and, if found,
		// throw an exception.
		for (IRule rule : rules) {
			if (RuleHeadEquality.hasRuleHeadEquality(rule)) {
				throw new EvaluationException(
						"Rules with rule head equality are not supported.");
			}
		}

		// Otherwise, return the rules as they are.
		return rules;
	}

}
