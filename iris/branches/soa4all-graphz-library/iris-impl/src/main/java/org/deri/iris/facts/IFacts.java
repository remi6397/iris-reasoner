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
package org.deri.iris.facts;

import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.storage.IRelation;

public interface IFacts
{

	/**
	 * Get the relation associated with the given predicate and create one
	 * if one does not already exist.
	 * @param predicate The predicate identifying the relation.
	 * @return The relation associated with the given predicate.
	 */
	IRelation get( IPredicate predicate );

	/**
	 * Get the set of predicate identifying all relations known to this facts object.
	 * @return
	 */
	Set<IPredicate> getPredicates();

}
