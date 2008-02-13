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
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.builtins.EqualBuiltin;
import org.deri.iris.builtins.ExactEqualBuiltin;
import org.deri.iris.builtins.NotEqualBuiltin;
import org.deri.iris.builtins.NotExactEqualBuiltin;
import org.deri.iris.factory.Factory;
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
							"have constructed terms as arguments. Problem atom: " + builtinAtom );
		}
		
		// So at this point, we know that we have a built-in that can use constructed terms as arguments.
		// Next, we have to classify the arguments as either:
		// 		a) grounded or groundable (so need indices from input relation for each variable)
		//		b) a simple variable term
		//		c) something else => can't handle, e.g. a constructed term with unbound variables
		
		ITuple builtinTuple = mBuiltinAtom.getTuple();
		
		// One more check, all these built-ins are binary
		assert builtinTuple.size() == 2;

		ITerm t0 = builtinTuple.get( 0 );
		ITerm t1 = builtinTuple.get( 1 );
		
		List<IVariable> tList0 = TermMatchingAndSubstitution.getVariables( t0, true );
		List<IVariable> tList1 = TermMatchingAndSubstitution.getVariables( t1, true );
		
		boolean grounded0 = inputVariables.containsAll( tList0 );
		boolean grounded1 = inputVariables.containsAll( tList1 );
		
		mOutputVariables = new ArrayList<IVariable>( inputVariables );

		if( grounded0 && grounded1 )
		{
			// Fine.
			mType = TYPE.NORMAL;
			
			assert inputVariables.containsAll( builtinTuple.getVariables() );
		}
		else if ( grounded0 || grounded1 )
		{
			// Can only be assignment and then only if one term is a pure variable
			if( ! (mBuiltinAtom instanceof EqualBuiltin) &&
				! (mBuiltinAtom instanceof ExactEqualBuiltin) )
				throw new EvaluationException(
								"Not enough grounded variables for in-equality with constructed terms. Problem atom: " + builtinAtom );
			
			if( ! (t0 instanceof IVariable) &&
				! (t1 instanceof IVariable) )
				throw new EvaluationException(
						"Assignment with constructed terms can only be to a plain variable. Problem atom: " + builtinAtom );

			// Assignment ok
			if( t0 instanceof IVariable )
			{
				mType = TYPE.ASSIGNMENT_TO_T0;
				mOutputVariables.add( (IVariable) t0 );
			}
			else
			{
				mOutputVariables.add( (IVariable) t1 );
				mType = TYPE.ASSIGNMENT_TO_T1;
			}
		}
		else
		{
			// Not allowed
			throw new EvaluationException(
				"Not enough grounded variables in built-in with constructed terms. Problem atom: " + builtinAtom );
		}

		List<IVariable> variablesInBuiltinTuple = TermMatchingAndSubstitution.getVariables( builtinTuple, true );
		List<Integer> indicesOfBuiltinVariablesFromInputRelation = new ArrayList<Integer>();
		
		for( IVariable builtinVariable : variablesInBuiltinTuple )
		{
			int index = inputVariables.indexOf( builtinVariable );

			indicesOfBuiltinVariablesFromInputRelation.add( index );
		}
		
		mIndicesOfBuiltinVariablesFromInputRelation = Utils.integerListToArray( indicesOfBuiltinVariablesFromInputRelation );
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
				{
						
					result.add( makeResultTuple( inputTuple, builtinOutputTuple ) );
				}
			}
			else
			{
				if( builtinOutputTuple == null )
				{
					result.add( inputTuple );
				}
			}
		}
		
		return result;
	}
	
	/**
	 * Create the results tuple for assignment.
	 * @param inputTuple Input tuple from previous sub-goals
	 * @param builtinOutputTuple Output of 'this' built-in.
	 * @return The new output tuple.
	 */
	protected ITuple makeResultTuple( ITuple inputTuple, ITuple builtinOutputTuple )
	{
		switch( mType )
		{
		default:
		case NORMAL:
			return inputTuple;
			
		case ASSIGNMENT_TO_T0:
		case ASSIGNMENT_TO_T1:
			{
				ITerm[] terms = new ITerm[ inputTuple.size() + 1 ];
				for( int i = 0; i < inputTuple.size(); ++i )
					terms[ i ] = inputTuple.get( i );
				terms[ inputTuple.size() ] = builtinOutputTuple.get( 0 );	// <<== Get the t0 output term
				return Factory.BASIC.createTuple( terms );
			}
		}
	}
	
	/** A handy reminder of what this built-in is doing. */
	private static enum TYPE { ASSIGNMENT_TO_T0, ASSIGNMENT_TO_T1, NORMAL };
	
	/** Indicates how to process the built-in. */
	private final TYPE mType;

	/** The built-in atom at this position in the rule. */
	private final IBuiltinAtom mBuiltinAtom;
	
	/** Indicator of this literal is positive or negated. */
	private final boolean mPositive;
	
	/** The knowledge-base configuration object. */
	private final Configuration mConfiguration;
	
	/** Indices from input relation used to populate the built-in's input tuple. */
	private final int[] mIndicesOfBuiltinVariablesFromInputRelation;
}
