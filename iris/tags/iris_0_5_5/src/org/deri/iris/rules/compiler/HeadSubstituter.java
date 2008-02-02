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

import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.TermMatchingAndSubstitution;

/**
 * A compiled rule element representing the substitution of variable bindings in to the rule head.
 */
public class HeadSubstituter extends RuleElement
{
	/**
	 * Constructor.
	 * @param variables The variables from the rule body.
	 * @param headTuple The tuple from the rule head.
	 */
	public HeadSubstituter( List<IVariable> variables, ITuple headTuple, Configuration configuration )
	{
		assert variables != null;
		assert headTuple != null;
		assert configuration != null;
		
		mConfiguration = configuration;
		
		mHeadTuple = headTuple;

		// Work out the indices of variables in substitution order
		List<IVariable> variablesToSubstitute = TermMatchingAndSubstitution.getVariables( mHeadTuple, false );
		mIndices = new int[ variablesToSubstitute.size() ];
		
		int i = 0;
		for( IVariable variable : variablesToSubstitute )
		{
			int index = variables.indexOf( variable );
			assert index >= 0;
			mIndices[ i++ ] = index;
		}
		
	}
	
	@Override
	public IRelation process( IRelation previous )
	{
		IRelation result = mConfiguration.relationFactory.createRelation();
		
		for( int i = 0; i < previous.size(); ++i )
		{
			ITuple tuple = previous.get( i );
			
			ITuple outputTuple = TermMatchingAndSubstitution.substituteVariablesInToTuple( mHeadTuple, tuple, mIndices );
			
			result.add( outputTuple );
		}
		
		return result;
	}

	/** The rule head tuple. */
	private final ITuple mHeadTuple;
	
	/** The indices of variables in substitution order. */
	private final int[] mIndices;
	
	private final Configuration mConfiguration;
}
