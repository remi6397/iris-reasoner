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

import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IRule;

/**
 * Represents all classes that can optimise the order of rule evaluation.
 * Ideally, the rule with the fewest or no dependencies will be evaluated first
 * and so on up to the rule with the most dependencies.
 * Cycles, branches and independent networks will have to be taken in to account.
 */
public interface IRuleReOrderingOptimiser
{
	/**
	 * Re-order the rules.
	 * The returned collection will have an implied ordering.
	 * @param rules The rules to re-order.
	 * @return The same rules, but in a more efficient order for evaluation.
	 */
	List<IRule> reOrder( Collection<IRule> rules );
}
