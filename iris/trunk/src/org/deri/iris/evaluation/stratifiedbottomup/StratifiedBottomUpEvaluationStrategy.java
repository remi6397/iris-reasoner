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
package org.deri.iris.evaluation.stratifiedbottomup;

import java.util.ArrayList;
import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.EvaluationUtilities;
import org.deri.iris.evaluation.IEvaluationStrategy;
import org.deri.iris.evaluation.IEvaluator2;
import org.deri.iris.facts.FiniteUniverseFacts;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.deri.iris.rules.compiler.RuleCompiler;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.storage.IRelation;

/**
 * A strategy that uses bottom up evaluation on a stratified rule set.
 */
public class StratifiedBottomUpEvaluationStrategy implements IEvaluationStrategy
{
	StratifiedBottomUpEvaluationStrategy( IFacts facts, List<IRule> rules, Configuration configuration ) throws EvaluationException
	{
		mConfiguration = configuration;

		mFacts = facts;

		if( mConfiguration.ruleSafetyProcessor instanceof AugmentingRuleSafetyProcessor )
			facts = new FiniteUniverseFacts( facts, rules );
		
		EvaluationUtilities utils = new EvaluationUtilities( mConfiguration );
		
		// Rule safety processing
		List<IRule> safeRules = utils.checkRuleSafety( rules );

		// Stratify
		List<List<IRule>> stratifiedRules = utils.stratify( safeRules );
		
		RuleCompiler rc = new RuleCompiler( facts, mConfiguration );
		
		for( List<IRule> stratum : stratifiedRules )
		{
			// Re-order stratum
			List<IRule> reorderedRules = utils.reOrderRules( stratum );
			
			// Rule optimisation
			List<IRule> optimisedRules = utils.optimiseRules( reorderedRules );
			
			List<ICompiledRule> compiledRules = new ArrayList<ICompiledRule>();
			
			for( IRule rule : optimisedRules )
				compiledRules.add( rc.compile( rule ) );
			
			IEvaluator2 eval = mConfiguration.evaluatorFactory.createEvaluator();
			eval.evaluateRules( compiledRules, facts, configuration );
		}
		
	}

	public IRelation evaluateQuery( IQuery query, List<IVariable> outputVariables ) throws EvaluationException
	{
		IRelation result = null;
		
		if( query != null )
		{
			RuleCompiler compiler = new RuleCompiler( mFacts, mConfiguration );
			
			ICompiledRule compiledQuery = compiler.compile( query );
			
			result = compiledQuery.evaluate();
			
			if( outputVariables != null )
			{
				outputVariables.clear();
				outputVariables.addAll( compiledQuery.getVariablesBindings() );
			}
		}		
		return result;
	}

	protected final Configuration mConfiguration;
	protected final IFacts mFacts;
}
