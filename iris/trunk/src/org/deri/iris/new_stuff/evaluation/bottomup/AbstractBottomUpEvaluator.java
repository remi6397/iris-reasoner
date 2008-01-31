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
package org.deri.iris.new_stuff.evaluation.bottomup;

import java.util.List;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.new_stuff.Configuration;
import org.deri.iris.new_stuff.evaluation.IEvaluator;
import org.deri.iris.new_stuff.facts.IFacts;
import org.deri.iris.new_stuff.storage.IRelation;
import org.deri.iris.rules.RuleBase;

/**
 * Base class for all naive evaluator implementations.
 */
public abstract class AbstractBottomUpEvaluator implements IEvaluator
{
	/**
	 * Constructor.
	 * @param facts The facts to use.
	 * @param ruleBase The rule-base to use.
	 * @param configuration The configuration to use.
	 */
	public AbstractBottomUpEvaluator( IFacts facts, RuleBase ruleBase, Configuration configuration )
	{
		mFacts = facts;
		mRuleBase = ruleBase;
		mConfiguration = configuration;
	}
	
	public IRelation evaluateQuery( IQuery query ) throws EvaluationException
	{
		return evaluateQuery( query, null );
	}

	public IRelation evaluateQuery( IQuery query, List<IVariable> outputVariables ) throws EvaluationException
	{
		if( ! mMinimalModelCalculated )
		{
			mRuleBase.initialise();
			calculateMinimalModel( mFacts, mRuleBase, mConfiguration );
			mMinimalModelCalculated = true;
		}
		
		IRelation result = null;

		if( query != null )
			result = executeQueryAgainstMinimalModel( query, outputVariables, mFacts, mConfiguration );

		return result;
	}
	
	/**
	 * Calculate the minimal model.
	 * This involves initialising the rule-base (stratifying, optimising etc) and evaluating
	 * every rule until no more facts can be inferred.  
	 * @throws EvaluationException If a rule can not be compiled, a rule is unsafe or the program is not stratified.
	 */
	protected abstract void calculateMinimalModel( IFacts facts, RuleBase ruleBase, Configuration configuration ) throws EvaluationException;
	
	/**
	 * Execute the query against all the known facts (minimal model).
	 * @param query The query to execute
	 * @param outputVariables The out variables for the query (ordered collection of unique variables that appear in the query).
	 */
	protected abstract IRelation executeQueryAgainstMinimalModel( IQuery query, List<IVariable> outputVariables, IFacts facts, Configuration configuration ) throws EvaluationException;

	/** The original facts of the knowledge base. */
	private  final IFacts mFacts;
	
	/** The rule-base of the knowledge-base. */
	private final RuleBase mRuleBase;
	
	/** Flag to indicate of the minimal model has yet been calculated. */
	private boolean mMinimalModelCalculated = false;
	
	/** The configuration for this knowledge base. */
	private final Configuration mConfiguration;
}
