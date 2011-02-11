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

import org.deri.iris.api.basics.ITuple;

/**
 * The interface to all relation classes.
 * The order of the tuples is given by the order of insertion. 
 * The access via index position is intended to allow for smart indexing, caching etc when
 * using relation classes that support large amounts of data.
 */
public interface IRelation
{
	/**
	 * Add a tuple to the relation.
	 * The tuple MUST have the same arity as all other tuples in the relation.
	 * @param tuple The (unique) tuple to add.
	 * @return true, if it was added, false if a tuple already exists in the relation with
	 * the same term values.
	 */
	boolean add( ITuple tuple );

	/**
	 * Add all tuples in relation 'relation' to this relation. 
	 * The tuples in 'relation' MUST have the same arity as all other tuples in this relation.
	 * @param relation The relation containing tuples to add.
	 * @return true if any tuples were actually added.
	 */
	boolean addAll( IRelation relation );
	
	/**
	 * Get the current number of tuples in this relation.
	 * @return The number of tuples in the relation.
	 */
	int size();
	
	/**
	 * Get a tuple at a specific index.
	 * @param index The index of the tuple in the relation, 0 <= index < size().
	 * @return The tuple at the given index position.
	 */
	ITuple get( int index );
	
	boolean contains( ITuple tuple );
}
