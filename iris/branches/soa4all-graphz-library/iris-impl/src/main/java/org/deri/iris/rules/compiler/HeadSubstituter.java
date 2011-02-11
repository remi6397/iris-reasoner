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
package org.deri.iris.rules.compiler;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.RuleUnsafeException;
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
	 * @throws EvaluationException If unbound variables occur.
	 */
	public HeadSubstituter( List<IVariable> variables, ITuple headTuple, Configuration configuration ) throws EvaluationException
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
			if( index < 0 )
				throw new RuleUnsafeException( "Unbound variable in rule head: " + variable );
			mIndices[ i++ ] = index;
		}
	}
	
	@Override
	public IRelation process( IRelation inputRelation )
	{
		assert inputRelation != null;

		IRelation result = mConfiguration.relationFactory.createRelation();
		
		for( int i = 0; i < inputRelation.size(); ++i )
		{
			ITuple inputTuple = inputRelation.get( i );
			
			ITuple outputTuple = TermMatchingAndSubstitution.substituteVariablesInToTuple( mHeadTuple, inputTuple, mIndices );
			
			result.add( outputTuple );
		}
		
		return result;
	}

	/** The rule head tuple. */
	protected final ITuple mHeadTuple;
	
	/** The indices of variables in substitution order. */
	protected final int[] mIndices;
	
	/** The knowledge-base's configuration object. */
	protected final Configuration mConfiguration;
}
