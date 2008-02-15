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
import java.util.List;
import org.deri.iris.api.IProgramOptimisation;
import org.deri.iris.evaluation.IEvaluatorFactory;
import org.deri.iris.evaluation.bottomup.compiledrules.seminaive.SemiNaiveEvaluatorFactory;
import org.deri.iris.facts.IDataSource;
import org.deri.iris.rules.IRuleOptimiser;
import org.deri.iris.rules.IRuleReOrderingOptimiser;
import org.deri.iris.rules.IRuleSafetyProcessor;
import org.deri.iris.rules.IRuleStratifier;
import org.deri.iris.rules.optimisation.JoinConditionOptimiser;
import org.deri.iris.rules.optimisation.ReOrderLiteralsOptimiser;
import org.deri.iris.rules.optimisation.RemoveDuplicateLiteralOptimiser;
import org.deri.iris.rules.optimisation.ReplaceVariablesWithConstantsOptimiser;
import org.deri.iris.rules.ordering.SimpleReOrdering;
import org.deri.iris.rules.safety.AugmentingRuleSafetyProcessor;
import org.deri.iris.rules.safety.StandardRuleSafetyProcessor;
import org.deri.iris.rules.stratification.GlobalStratifier;
import org.deri.iris.rules.stratification.LocalStratifier;
import org.deri.iris.storage.IIndexFactory;
import org.deri.iris.storage.IRelationFactory;
import org.deri.iris.storage.simple.SimpleIndexFactory;
import org.deri.iris.storage.simple.SimpleRelationFactory;

/**
 * This class holds all configuration data for a knowledge base.
 */
public class Configuration
{
	/** The evaluation strategy to use. */
	public IEvaluatorFactory evaluationTechnique = new SemiNaiveEvaluatorFactory();
	
	/** The evaluation timeout in milliseconds. Zero means no timeout. */
	public int evaluationTimeoutMilliseconds = 0;

	/**
	 * The maximum number of tuples that are allowed to be generated during evaluation
	 * before evaluation is terminated.
	 * Zero means that there is no maximum.
	 */
	public int evaluationMaxTuples = 0;
	
	/**
	 * The maximum complexity (tree depth) of inferred constructed terms.
	 * This is a constraint that can avoid the non-convergence problem
	 * for knowledge-bases containing rules such as:
	 * p( f(X) ) :- p( X )
	 */
	public int evaluationMaxComplexity = 0;

	/**
	 * The different options for handling divide by zero in arithmetic built-ins during evaluation.
	 */
	public static enum DivideByZeroBehaviour
	{
		STOP,
		DISCARD_AND_IGNORE
	}
	
	/** The desired divide-by-zero behaviour. */
	public DivideByZeroBehaviour evaluationDivideByZeroBehaviour = DivideByZeroBehaviour.DISCARD_AND_IGNORE;

	/** The factory for creating relations required during evaluation. */
	public IRelationFactory relationFactory = new SimpleRelationFactory();
	
	/** The factory for creating indexes required during evaluation. */
	public IIndexFactory indexFactory = new SimpleIndexFactory();

	/** The number of bits of precision to use for comparing double term values. */ 
	public int floatingPointDoublePrecision = 42;

	/** The number of bits of precision to use for comparing float term values. */ 
	public int floatingPointFloatPrecision = 19;
	
	/** Add external data sources here. */
	public final List<IDataSource> mExternalDataSources = new ArrayList<IDataSource>();
	
	/** The collection of rule set stratifiers. */
	public final List<IRuleStratifier> mStratifiers = new ArrayList<IRuleStratifier>();
	
	/** The collection of rule optimisers. */
	public final List<IRuleOptimiser> mRuleOptimisers = new ArrayList<IRuleOptimiser>();
	
	/** The collection of rule-reordering optimisers. */
	public final List<IRuleReOrderingOptimiser> mReOrderingOptimisers = new ArrayList<IRuleReOrderingOptimiser>();
	
	/** Collection of program optimisations. */
	public final List<IProgramOptimisation> programOptmimisers = new ArrayList<IProgramOptimisation>();
	
	/** Rule safety processors (e.g. standard rule-safety check and augmented-unsafe-rule modifier). */
	public final List<IRuleSafetyProcessor> ruleSafetyProcessors = new ArrayList<IRuleSafetyProcessor>();
	
	/**
	 * Constructor.
	 */
	public Configuration()
	{
		mStratifiers.add( new GlobalStratifier() );
		mStratifiers.add( new LocalStratifier( true ) );
		mStratifiers.add( new LocalStratifier( false ) );
		
		mReOrderingOptimisers.add( new SimpleReOrdering() ); 

		mRuleOptimisers.add( new JoinConditionOptimiser() );
		mRuleOptimisers.add( new ReplaceVariablesWithConstantsOptimiser() );
		mRuleOptimisers.add( new ReOrderLiteralsOptimiser() );
		mRuleOptimisers.add( new RemoveDuplicateLiteralOptimiser() );
		
		/*
		 * Indicates if ternary arithmetic built-ins can be used to deduce limited variables, e.g.
		 * p(Z) :- q(X, Y), X + Y = Z
		 * if true, then Z would be considered limited.
		 */ 
		boolean ruleSafetyTernaryTargetsImplyLimited = true;
		
		/*
		 * Indicates if a rule can still be considered safe if one or more variables occur
		 * in negative ordinary predicates and nowhere else, e.g.
		 * p(X) :- q(X), not r(Y)
		 * if true, the above rule would be safe
		 */
		boolean ruleSafetyAllowUnlimitedVariablesInNegatedOrdinaryPredicates = true;

		// standard rule-safety processor.
		ruleSafetyProcessors.add( new StandardRuleSafetyProcessor( ruleSafetyAllowUnlimitedVariablesInNegatedOrdinaryPredicates, ruleSafetyTernaryTargetsImplyLimited ) );

		// Use this rule safety processor in combination with HerbrandShrinkingFacts adaptor (and probably well-found semantics too).
		// ruleSafetyProcessors.add( new AugmentingRuleSafetyProcessor() );
	}

	/**
	 * Copy constructor.
	 * @param rhs The object to copy from.
	 */
	public Configuration( Configuration rhs )
	{
		
	}
}
