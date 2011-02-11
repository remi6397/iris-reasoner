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
package org.deri.iris.rules.optimisation;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.rules.IRuleOptimiser;
import org.deri.iris.rules.RuleManipulator;

/**
 * Replace variables with constants where possible:
 * e.g. p(?X,?Y) :- q(?X, ?Z), ?Z = 't'
 * ==>> p(?X,?Y) :- q(?X, 't')
 * 
 * This should have the effect of pushing selection criteria in to the evaluation of a relation,
 * such that fewer tuples are processed.
 * 
 * Problem!
 * p(?X), ?X = 2 is not the same as p(2), at least at the moment,
 * because with p(2) the evaluator looks for only p( integer( 2 ) ) in the relation,
 * whereas p(?X), ?X = 2 will return the entire relation and filter out what equals 2,
 * i.e. any numeric value.
 * 
 * Not sure how to fix this, but it will involve changing MixedDatatypeRelation.
 */
public class ReplaceVariablesWithConstantsOptimiser implements IRuleOptimiser
{
	public IRule optimise( IRule rule )
	{
		rule = mManipulator.replaceVariablesWithConstants( rule, true );
		rule = mManipulator.removeDuplicateLiterals( rule );
		rule = mManipulator.removeUnnecessaryEqualityBuiltins( rule );
		
		return rule;
	}

	private static RuleManipulator mManipulator = new RuleManipulator();
}
