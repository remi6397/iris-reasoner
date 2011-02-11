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
package org.deri.iris.storage;

import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;

/**
 * Represents an index on something, usually a relation.
 * This index interface can be used when sort order is not relevant.
 */
public interface IIndex
{
	/**
	 * Return all tuples matching the given key.
	 * @param key The collection (possibly empty) of terms in the key.
	 * @return The list of matching tuples.
	 * TODO This might change to returning Iterator<ITuple> to allow for very large data sets.
	 */
	List<ITuple> get( List<ITerm> key );
}
