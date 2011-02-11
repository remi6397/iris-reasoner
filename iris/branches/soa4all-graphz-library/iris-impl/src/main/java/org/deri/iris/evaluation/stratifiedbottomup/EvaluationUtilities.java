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
package org.deri.iris.evaluation.stratifiedbottomup;

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.rules.IRuleOptimiser;
import org.deri.iris.rules.IRuleStratifier;

/**
 * A collection of useful evaluation steps.
 */
public class EvaluationUtilities
{
	/**
	 * Constructor.
	 * @param configuration The knowledge-base configuration object.
	 */
	public EvaluationUtilities( Configuration configuration )
	{
		mConfiguration = configuration;
	}
	
	/**
	 * Apply each of the stratifiers in turn until stratification is achieved. 
	 * @param rules The input rules.
	 * @return A collection of collections of rules arranged in to strata.
	 * The rules in each stratum might be different from the imput rules. 
	 * @throws ProgramNotStratifiedException If none of the stratification algorithms successfully
	 * stratified the rule set.
	 */
	public List<List<IRule>> stratify( List<IRule> rules ) throws ProgramNotStratifiedException
	{
		for( IRuleStratifier stratifier : mConfiguration.stratifiers )
		{
			List<List<IRule>> ruleStrata = stratifier.stratify( rules );
			if( ruleStrata != null )
				return ruleStrata;
		}

		throw new ProgramNotStratifiedException( "The input program is not stratified" );
	}
	
	/**
	 * Attempt to re-order the rules of a stratum such that the dependent rules are evaluated first.
	 * @param rules The strata of rules to re-order.
	 * @return The re-ordered rules.
	 */
	public List<IRule> reOrderRules( List<IRule> rules )
	{
		if( mConfiguration.reOrderingOptimiser == null )
			return rules;
		else
			return mConfiguration.reOrderingOptimiser.reOrder( rules );
	}
	
	/**
	 * Optimise a collection of rules by applying each of the rule optimisations to each rule in turn.
	 * @param rules A collection of rules to optimise.
	 * @return A list of optimised rules.
	 */
	public List<IRule> applyRuleOptimisers( final List<IRule> rules )
	{
		List<IRule> optimisedRules = new ArrayList<IRule>();
		
		for( IRule rule : rules )
		{
			IRule optimisedRule = rule;
			
			for( IRuleOptimiser optimiser : mConfiguration.ruleOptimisers )
				optimisedRule = optimiser.optimise( optimisedRule );
			
			optimisedRules.add( optimisedRule );
		}
			
		return optimisedRules;
	}
	
	/**
	 * Check the given rules for rule-safety (unbound variables).
	 * In the future, this method will be allowed to modify the input rules.
	 * @param rules The rules to check.
	 * @return The collection of safe rules.
	 * @throws RuleUnsafeException
	 */
	public List<IRule> applyRuleSafetyProcessor( List<IRule> rules ) throws RuleUnsafeException
	{
		if( mConfiguration.ruleSafetyProcessor == null )
			return rules;
		
		List<IRule> safeRules = new ArrayList<IRule>();
		
		for( IRule rule : rules )
			safeRules.add( mConfiguration.ruleSafetyProcessor.process( rule ) );
		
		return safeRules;
	}
	
	/** The knowledge-base configuration object. */
	protected final Configuration mConfiguration;
}
