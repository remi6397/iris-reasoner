/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2007 Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package org.deri.iris.rules.compiler;

import java.util.ArrayList;
import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IIndex;
import org.deri.iris.storage.IRelation;

/**
 * A compiled rule element representing a negated ordinary literal.
 */
public class Differ extends RuleElement
{
	/**
	 * Constructor.
	 * @param inputVariables The variable bindings from previous rule elements.
	 * @param thisLiteralsRelation The relation to use for this literal.
	 * @param viewCriteria The view criteria (tuple in the sub-goal instance in the rule).
	 */
	public Differ( List<IVariable> inputVariables, IRelation thisLiteralsRelation, ITuple viewCriteria, Configuration configuration )
	{
		assert inputVariables != null;
		assert thisLiteralsRelation != null;
		assert viewCriteria != null;
		assert configuration != null;
		
		mConfiguration = configuration;
		
//		mThisLiteralsRelation = thisLiteralsRelation;
		
		mView = new View( thisLiteralsRelation, viewCriteria, mConfiguration.relationFactory );
		
		// Find the indices of the variables used in the natural join
		List<Integer> join1 = new ArrayList<Integer>();
		List<Integer> join2 = new ArrayList<Integer>();
		
		List<IVariable> vars2 = mView.variables();
		
		for( int i1 = 0; i1 < inputVariables.size(); ++i1 )
		{
			IVariable var1 = inputVariables.get( i1 );
			
			for( int i2 = 0; i2 < vars2.size(); ++i2 )
			{
				IVariable var2 = vars2.get( i2 );
				
				if( var1.equals( var2 ) )
				{
					join1.add( i1 );
					join2.add( i2 );
					
					// NB Variables in views occur only once
					break;
				}
			}
		}
		
		mJoinIndices1 = Utils.integerListToArray( join1 );
		mJoinIndices2 = Utils.integerListToArray( join2 );

		// Create the index for the second relation
		mIndex2 = mConfiguration.indexFactory.createIndex( mView.getView(), mJoinIndices2 );
		
		mOutputVariables = inputVariables;
	}

	@Override
    public IRelation process( IRelation previous )
    {
		assert previous != null;
		
		IRelation result = mConfiguration.relationFactory.createRelation();

		for( int f = 0; f < previous.size(); ++f )
		{
			ITuple first = previous.get( f );
			
			List<ITuple> seconds = mIndex2.get( Utils.makeKey( first, mJoinIndices1 ) );

			if( seconds == null || seconds.size() == 0 )
				result.add( first );
		}
		
		return result;
	}
	
	/** The view on the relation (as seen with the view criteria). */
	private final View mView;

	/** The relation for this literal. */
//	private final IRelation mThisLiteralsRelation;
	
	/** The join indices from the previous rule element's output tuple. */
	private final int[] mJoinIndices1;

	/** The join indices from this rule element's view of its relation. */
	private final int[] mJoinIndices2;
	
	/** An index on the view of this literal's relation. */
	private final IIndex mIndex2;
	
	private final Configuration mConfiguration;
}
