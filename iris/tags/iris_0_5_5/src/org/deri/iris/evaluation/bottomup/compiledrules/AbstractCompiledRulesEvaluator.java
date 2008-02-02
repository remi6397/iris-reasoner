package org.deri.iris.evaluation.bottomup.compiledrules;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.evaluation.bottomup.AbstractBottomUpEvaluator;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.RuleBase;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.deri.iris.rules.compiler.RuleCompiler;
import org.deri.iris.storage.IRelation;

public abstract class AbstractCompiledRulesEvaluator extends AbstractBottomUpEvaluator
{
	/**
	 * Constructor.
	 * @param facts The facts to use.
	 * @param ruleBase The rule-base to use.
	 * @param configuration The configuration to use.
	 */
	public AbstractCompiledRulesEvaluator( IFacts facts, RuleBase ruleBase, Configuration configuration )
	{
		super( facts, ruleBase, configuration );
	}

	protected void calculateMinimalModel( IFacts facts, RuleBase ruleBase, Configuration configuration ) throws EvaluationException
	{
		RuleCompiler compiler = new RuleCompiler( facts, configuration );

		for( int stratum = 0; stratum < ruleBase.getRuleStrataSize(); ++stratum )
		{
			Collection<IRule> stratumRules = ruleBase.getRulesOfStratum( stratum );
			
			List<ICompiledRule> stratumCompiledRules = new ArrayList<ICompiledRule>();
			for( IRule rule : stratumRules )
			{
				ICompiledRule compiledRule = compiler.compile( rule );
				stratumCompiledRules.add( compiledRule );
			}
			
			evaluateRules( stratumCompiledRules, facts, configuration );
		}
	}	

	protected IRelation executeQueryAgainstMinimalModel( IQuery query, List<IVariable> outputVariables, IFacts facts, Configuration configuration ) throws EvaluationException
	{
		RuleCompiler compiler = new RuleCompiler( facts, configuration );
		
		ICompiledRule compiledQuery = compiler.compile( query );
		
		IRelation result = compiledQuery.evaluate();
		
		if( outputVariables != null )
		{
			outputVariables.clear();
			outputVariables.addAll( compiledQuery.getVariablesBindings() );
		}
		
		return result;
	}

	/**
	 * Infer all possible facts from the original facts and the rule-base.
	 * 
	 * Implementations of this method should compute all possible facts
	 * by the application of the known facts to the rules.
	 * This should be done iteratively until no more new facts can be produced.
	 * 
	 * The new facts should be added to the starting facts so that when this
	 * method returns, 'facts' will hold the computed minimal model. 
	 * @param rules The rule-base.
	 * @param facts The starting facts. On exit this will hold the minimal model.
	 * @param configuration The configuration for thi knowledge base.
	 */
	protected abstract void evaluateRules( Collection<ICompiledRule> rules, IFacts facts, Configuration configuration );
}
