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
 * This optimiser attempts to use the same variable for join conditions.
 * e.g. t(?X,?Y,?Z) :- p(?X), q(?Y), r(?Z), ?X = ?Y, ?Y = ?Z.
 * ==>> t(?X,?X,?X) :- p(?X), q(?X), r(?X).
 */
public class JoinConditionOptimiser implements IRuleOptimiser
{
	public IRule optimise( IRule rule )
	{
		rule = mManipulator.replaceVariablesWithVariables( rule );
		rule = mManipulator.removeUnnecessaryEqualityBuiltins( rule );
		
		return rule;
	}

	private static RuleManipulator mManipulator = new RuleManipulator();
}
