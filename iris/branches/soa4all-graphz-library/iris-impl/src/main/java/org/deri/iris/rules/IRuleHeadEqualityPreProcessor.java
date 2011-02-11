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
 * An interface for rule head equality pre-processors.
 * 
 * @author Adrian Marte
 */
public interface IRuleHeadEqualityPreProcessor {

	/**
	 * Pre-process the given rules and facts.
	 * 
	 * @param rules The rule to pre-process.
	 * @param facts The facts to pre-process.
	 * @throws EvaluationException If an error occurs, or rule head equality is not
	 *             supported.
	 * @return The resulting rules after pre-processing.
	 */
	public List<IRule> process(List<IRule> rules, IFacts facts)
			throws EvaluationException;

}
