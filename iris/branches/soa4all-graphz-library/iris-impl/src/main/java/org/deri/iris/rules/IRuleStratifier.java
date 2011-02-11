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

import org.deri.iris.api.basics.IRule;

/**
 * Interface to all rule stratifiers.
 */
public interface IRuleStratifier
{
	/**
	 * Stratify the rules, i.e. arrange them in to groups such that each
	 * increasing level of rules can be evaluated before the next higher
	 * level of dependent rules. 
	 * @return The rules arranged in to strata. The number of rules
	 * returned may be different to the number provided, because the
	 * stratification technique might require the rules to be re-written.
	 * @param rules The collection of rules to stratify
	 * @return A set of stratified rules, or null if the rules can not be
	 * stratified with this algorithm.
	 */
	List<List<IRule>> stratify( List<IRule> rules );
}
