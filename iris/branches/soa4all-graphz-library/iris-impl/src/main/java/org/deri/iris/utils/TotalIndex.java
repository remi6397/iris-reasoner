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
package org.deri.iris.utils;

import java.util.HashSet;
import java.util.Set;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;

/**
 * Helper class for the semi-naive evaluator.
 * An object of this class indexes an entire relation on every term to enable fast detection
 * of the existence of a tuple within a relation.
 */
public class TotalIndex
{
	/**
	 * Constructor.
	 * @param relation The relation to index.
	 */
	public TotalIndex( IRelation relation )
	{
		mRelation = relation;
	}
	
	/**
	 * Discover if the indexed relation contains a specific tuple.
	 * @param tuple The tuple to test for.
	 * @return true, if a tuple with identical term values already exists in the relation.
	 */
	public boolean contains( ITuple tuple )
	{
		update();
		
		return mBag.contains( tuple );
	}
	
	/**
	 * Update the index with tuples not already seen by the index.
	 */
	private void update()
	{
		for( ; mLastIndexOfView < mRelation.size(); ++mLastIndexOfView )
		{
			ITuple viewTuple = mRelation.get( mLastIndexOfView );
			
			mBag.add( viewTuple );
		}
	}
	
	/** The index in to the relation of the last seen tuple. */
	private int mLastIndexOfView = 0;
	
	/** The set of tuples from the relation. */
	private final Set<ITuple> mBag = new HashSet<ITuple>();
	
	/** The relation being indexed. */
	private final IRelation mRelation;
}
