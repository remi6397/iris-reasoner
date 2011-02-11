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
package org.deri.iris.evaluation.topdown;

import java.util.Set;

import org.deri.iris.api.basics.IPredicate;

/**
 * Used by OLDT evaluation to tag predicates as so-called 'memo predicates'
 * If no predicate is tagged as a memo predicate, OLDT resolution behaves like OLD
 * resolution (equals SLD with standard literal selector).
 *  
 * @author gigi
 *
 */
public interface IPredicateTagger {

	/**
	 * Returns a set of tagged predicates.
	 * 
	 * @return set of tagged predicates.
	 */
	public Set<IPredicate> getMemoPredicates();
	
}
