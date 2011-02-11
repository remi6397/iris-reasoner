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

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.factory.Factory;
import org.deri.iris.facts.FiniteUniverseFacts;
import org.deri.iris.rules.IRuleSafetyProcessor;
import org.deri.iris.rules.RuleValidator;

/**
 * Uses the trick of augmenting rules to artificially limit variables.
 * If any head variable is found that does not occur in a positive ordinary predicate
 * then a literal is added to the rule body $UNIVERSE$( variable ).
 * This has the effect of binding the variable to the 'universe' of known ground terms.
 */
public class AugmentingRuleSafetyProcessor implements IRuleSafetyProcessor
{
	public IRule process( IRule rule ) throws RuleUnsafeException
	{
		RuleValidator validator = new RuleValidator( rule, true, true );
		
		List<IVariable> unlimitedVariables = validator.getAllUnlimitedVariables();
		
		if( unlimitedVariables.size() > 0 )
		{
			List<ILiteral> body = new ArrayList<ILiteral>();
			
			for( ILiteral literal : rule.getBody() )
				body.add( literal );
			
			for( IVariable variable : unlimitedVariables )
			{
				ILiteral newLiteral = Factory.BASIC.createLiteral( true, FiniteUniverseFacts.UNIVERSE, Factory.BASIC.createTuple( variable ) );
				body.add( newLiteral );
			}
			
			return Factory.BASIC.createRule( rule.getHead(), body );
		}
		else
			return rule;
	}
}
