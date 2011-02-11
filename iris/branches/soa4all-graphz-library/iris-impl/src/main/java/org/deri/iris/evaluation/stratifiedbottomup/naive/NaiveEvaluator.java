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
package org.deri.iris.evaluation.stratifiedbottomup.naive;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.evaluation.stratifiedbottomup.IRuleEvaluator;
import org.deri.iris.facts.IFacts;
import org.deri.iris.rules.compiler.ICompiledRule;
import org.deri.iris.storage.IRelation;

/**
 * Naive evaluation. see Ullman, Vol. 1
 */
public class NaiveEvaluator implements IRuleEvaluator
{
	public void evaluateRules( List<ICompiledRule> rules, IFacts facts, Configuration configuration ) throws EvaluationException
	{
		boolean cont = true;
		while( cont )
		{
			cont = false;
			
			// For each rule in the collection (stratum)
			for (final ICompiledRule rule : rules )
			{
				IRelation delta = rule.evaluate();

				if( delta != null && delta.size() > 0 )
				{
					IPredicate predicate = rule.headPredicate();
					if( facts.get( predicate ).addAll( delta ) )
						cont = true;
				}
			}
		}
	}
}
