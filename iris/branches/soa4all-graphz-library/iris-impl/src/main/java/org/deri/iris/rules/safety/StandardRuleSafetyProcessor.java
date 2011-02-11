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
package org.deri.iris.rules.safety;

import java.util.List;

import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.rules.IRuleSafetyProcessor;
import org.deri.iris.rules.RuleValidator;

/**
 * A standard rule-safety processor that checks if all variables are limited, a la Ullman.
 * If not, a rule unsafe exception is thrown.
 */
public class StandardRuleSafetyProcessor implements IRuleSafetyProcessor
{
	/**
	 * Default constructor. Initialises with most flexible rule-safety parameters.
	 */
	public StandardRuleSafetyProcessor()
	{
		this( true, true );
	}
	
	/**
	 * Constructor.
	 * @param allowUnlimitedVariablesInNegatedOrdinaryPredicates Indicates if a rule can still be
	 * considered safe if one or more variables occur
	 * in negative ordinary predicates and nowhere else, e.g.
	 * p(X) :- q(X), not r(Y)
	 * if true, the above rule would be safe
	 * @param ternaryTargetsImplyLimited Indicates if ternary arithmetic built-ins can be
	 * used to deduce limited variables, e.g.
	 * p(Z) :- q(X, Y), X + Y = Z
	 * if true, then Z would be considered limited.
	 */
	public StandardRuleSafetyProcessor(
					boolean allowUnlimitedVariablesInNegatedOrdinaryPredicates,
					boolean ternaryTargetsImplyLimited )
	{
		mAllowUnlimitedVariablesInNegatedOrdinaryPredicates = allowUnlimitedVariablesInNegatedOrdinaryPredicates;
		mTernaryTargetsImplyLimited = ternaryTargetsImplyLimited;
	}

	public IRule process( IRule rule ) throws RuleUnsafeException
	{
		RuleValidator validator = new RuleValidator( rule,
						mAllowUnlimitedVariablesInNegatedOrdinaryPredicates, 
						mTernaryTargetsImplyLimited );
		
		List<IVariable> unsafeVariables = validator.getAllUnlimitedVariables();
		
		if( unsafeVariables.size() > 0 )
		{
			StringBuilder buffer = new StringBuilder();
			buffer.append( rule ).append( " contains unlimited variable(s): " );
			
			boolean first = true;
			for( IVariable variable : unsafeVariables )
			{
				if( first )
					first = false;
				else
					buffer.append( ", " );
				buffer.append( variable );
			}
			
			throw new RuleUnsafeException( buffer.toString() );
		}
		
		return rule;
	}

	private final boolean mAllowUnlimitedVariablesInNegatedOrdinaryPredicates;
	private final boolean mTernaryTargetsImplyLimited;
}
