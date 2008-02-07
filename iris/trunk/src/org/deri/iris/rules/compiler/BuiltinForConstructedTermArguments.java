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
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.ExactEqualBuiltin;
import org.deri.iris.builtins.NotEqualBuiltin;
import org.deri.iris.builtins.NotExactEqualBuiltin;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.TermMatchingAndSubstitution;

/**
 * A compiled rule element representing a built-in predicate with constructed terms arguments.
 */
public class BuiltinForConstructedTermArguments extends RuleElement
{
	/**
	 * Constructor.
	 * @param inputVariables The variables from proceeding literals. Can be null if this is the first literal.
	 * @param builtinAtom The built-in atom object at this position in the rule.
	 * @param positive true, if the built-in is positive, false if it is negative.
	 * @throws EvaluationException If constructed terms are used with a built-in or there are unbound variables.
	 */
	public BuiltinForConstructedTermArguments( List<IVariable> inputVariables, IBuiltinAtom builtinAtom, boolean positive, Configuration configuration ) throws EvaluationException
	{
		assert inputVariables != null;
		assert builtinAtom != null;
		assert configuration != null;
		
		mBuiltinAtom = builtinAtom;
		mPositive = positive;
		mConfiguration = configuration;
		
		if( mBuiltinAtom instanceof EqualBuiltin ||
			mBuiltinAtom instanceof NotEqualBuiltin ||
			mBuiltinAtom instanceof ExactEqualBuiltin ||
			mBuiltinAtom instanceof NotExactEqualBuiltin )
		{
			// These built-ins can use function symbols
		}
		else
		{
			throw new EvaluationException(
							"Only equality, inequality and assignment built-in predicates can " +
							"have constructed terms as arguments." );
		}
		
		// So at this point, we know that we have a built-in that can use constructed terms as argument.
		// Next, we have to classify the arguments as either:
		// 		a) grounded or groundable (so need indices from input relation for each variable)
		//		b) a simple variable term
		//		c) something else => can't handle, e.g. a constructed term with unbound variables
		
		ITuple builtinTuple = mBuiltinAtom.getTuple();
		
		// First go, ensure all terms are groundable
		if( ! inputVariables.containsAll( builtinTuple.getVariables() ) )
			throw new EvaluationException( "Non-groundable term in built-in" );
		
		List<IVariable> variablesInBuiltinTuple = TermMatchingAndSubstitution.getVariables( builtinTuple, true );
		List<Integer> indicesOfBuiltinVariablesFromInputRelation = new ArrayList<Integer>();
		
		for( IVariable builtinVariable : variablesInBuiltinTuple )
		{
			int index = inputVariables.indexOf( builtinVariable );
			assert index >= 0;
			indicesOfBuiltinVariablesFromInputRelation.add( index );
		}
		
		mIndicesOfBuiltinVariablesFromInputRelation = Utils.integerListToArray( indicesOfBuiltinVariablesFromInputRelation );

		mOutputVariables = inputVariables;
	}

	@Override
	public IRelation process( IRelation leftRelation )
	{
		assert leftRelation != null;

		IRelation result = mConfiguration.relationFactory.createRelation();
		
		for( int i = 0; i < leftRelation.size(); ++i )
		{
			ITuple inputTuple = leftRelation.get( i );
			
			ITuple builtinInputTuple =
				TermMatchingAndSubstitution.substituteVariablesInToTuple(
								mBuiltinAtom.getTuple(), inputTuple, mIndicesOfBuiltinVariablesFromInputRelation );
	
			ITuple builtinOutputTuple = mBuiltinAtom.evaluate( builtinInputTuple );
			
			if( mPositive )
			{
				if( builtinOutputTuple != null )
					result.add( inputTuple );
			}
			else
			{
				if( builtinOutputTuple == null )
					result.add( inputTuple );
			}
		}
		
		return result;
	}

	/** The built-in atom at this position in the rule. */
	private final IBuiltinAtom mBuiltinAtom;
	
	/** Indicator of this literal is positive or negated. */
	private final boolean mPositive;
	
	private final Configuration mConfiguration;
	
	private final int[] mIndicesOfBuiltinVariablesFromInputRelation;
}
