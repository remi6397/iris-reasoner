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
package org.deri.iris;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.IEvaluator;
import org.deri.iris.facts.Facts;
import org.deri.iris.facts.FactsWithExternalData;
import org.deri.iris.facts.FiniteUniverseFacts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.RuleBase;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.storage.IRelation;

/**
 * The concrete knowledge-base.
 */
public class KnowledgeBase implements IKnowledgeBase
{
	/**
	 * Constructor.
	 * @param facts The starting facts for the knowledge-base.
	 * @param rules The rules of the knowledge-base.
	 * @param configuration The configuration object for the knowledge-base.
	 */
	public KnowledgeBase( Map<IPredicate,IRelation> inputFacts, List<IRule> rules, Configuration configuration )
	{
		if( inputFacts == null )
			inputFacts = new HashMap<IPredicate,IRelation>();
		
		if( rules == null )
			rules = new ArrayList<IRule>();
		
		if( configuration == null )
			configuration = new Configuration();
		
		mConfiguration = configuration;

		// Set up the rule-base
		mRuleBase = new RuleBase( configuration, rules );
		
		// Set up the facts object(s)
		IFacts facts = new Facts( inputFacts, mConfiguration.relationFactory );
		
		if( mConfiguration.externalDataSources.size() > 0 )
			facts = new FactsWithExternalData( facts, mConfiguration.externalDataSources );
		
//		If we allow unsafe rules then we need to use the finite universe facts adaptor.
		if( mConfiguration.ruleSafetyProcessor instanceof AugmentingRuleSafetyProcessor )
			facts = new FiniteUniverseFacts( facts, rules );
		
		mFacts = facts;
		
		mEvaluator = mConfiguration.evaluationTechnique.createEvaluator( mFacts, mRuleBase, mConfiguration );
	}
	
	public IRelation execute( IQuery query, List<IVariable> variableBindings ) throws EvaluationException
	{
		return mEvaluator.evaluateQuery( query, variableBindings );
	}

	public IRelation execute( IQuery query ) throws EvaluationException
    {
		return execute( query, null );
    }
	
	public Set<IRule> getRules()
    {
	    return mRuleBase.getRules();
    }
	
	@Override
    public String toString()
    {
	    return mRuleBase.toString() + mFacts.toString();
    }

	/** The facts of the knowledge-base. */
	private final IFacts mFacts;

	/** The rules of the knowledge-base. */
	private final RuleBase mRuleBase;
	
	/** The configuration object for the knowledge-base. */
	private final Configuration mConfiguration;
	
	/** The evaluator for the knowledge-base. */
	private final IEvaluator mEvaluator;
}
