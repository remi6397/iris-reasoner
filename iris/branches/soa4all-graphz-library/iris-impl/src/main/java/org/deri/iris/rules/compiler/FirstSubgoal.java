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
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.facts.IFacts;
import org.deri.iris.storage.IRelation;
import org.deri.iris.utils.equivalence.IEquivalentTerms;

/**
 * A compiled rule element representing the first literal in a rule body,
 * when that literal is a positive ordinary predicate.
 */
public class FirstSubgoal extends RuleElement
{
	/**
	 * Constructor.
	 * @param predicate The predicate for this literal.
	 * @param relation The relation for this literal.
	 * @param viewCriteria The tuple from the sub-goal in the rule.
	 * @param equivalentTerms The equivalent terms.
	 */
	public FirstSubgoal( IPredicate predicate, IRelation relation, ITuple viewCriteria, 
			IEquivalentTerms equivalentTerms, Configuration configuration )
	{
		assert predicate != null;
		assert relation != null;
		assert viewCriteria != null;
		assert configuration != null;
		
		mConfiguration = configuration;
		
		mView = new View( relation, viewCriteria, equivalentTerms, mConfiguration.relationFactory );

		mPredicate = predicate;
		mViewCriteria = viewCriteria;
		mOutputVariables = mView.variables();
		mEquivalentTerms = equivalentTerms;
	}
	
	/**
	 * Constructor used for iterative evaluation.
	 * @param predicate The predicate for this literal.
	 * @param relation The relation for this literal.
	 * @param viewCriteria The tuple from the sub-goal in the rule.
	 * @param variables Calculated variables.
	 * @param simple Indicator if the view is a simple one (only unique variables).
	 * @param equivalentTerms The equivalent terms.
	 */
	public FirstSubgoal(	IPredicate predicate, IRelation relation,
			ITuple viewCriteria, List<IVariable> variables, boolean simple,
			IEquivalentTerms equivalentTerms, Configuration configuration )
	{
		assert predicate != null;
		assert relation != null;
		assert viewCriteria != null;
		assert configuration != null;
		
		mConfiguration = configuration;
		
		mView = new View( relation, viewCriteria, variables, simple, equivalentTerms, mConfiguration.relationFactory );
		
		mPredicate = predicate;
		mViewCriteria = viewCriteria;
		mOutputVariables = mView.variables();
		mEquivalentTerms = equivalentTerms;
	}

	@Override
	public IRelation process( IRelation leftRelation )
	{
		assert leftRelation != null;
		assert leftRelation.size() == 1;	// i.e. there is no left relation, just a starting point.
		
		return mView;
	}
	
	@Override
    public RuleElement getDeltaSubstitution( IFacts deltas )
    {
		IRelation delta = deltas.get( mPredicate );
		
		if( delta == null || delta.size() == 0 )
			return null;

	    return new FirstSubgoal( mPredicate, delta, mViewCriteria, mView.variables(), mView.isSimple(), mEquivalentTerms, mConfiguration );
    }

	/** The equivalent terms. */
	private IEquivalentTerms mEquivalentTerms;
	
	/** Predicate of the literal. */
	private final IPredicate mPredicate;
	
	/** The tuple from the sub-goal in the rule. */
	private final ITuple mViewCriteria;
	
	/** The view on this literal. */
	private final View mView;
	
	private final Configuration mConfiguration;
}
