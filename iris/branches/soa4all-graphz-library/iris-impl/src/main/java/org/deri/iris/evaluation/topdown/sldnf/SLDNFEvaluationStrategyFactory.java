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
package org.deri.iris.evaluation.topdown.sldnf;

import java.util.List;

import org.deri.iris.Configuration;
import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.evaluation.IEvaluationStrategy;
import org.deri.iris.evaluation.IEvaluationStrategyFactory;
import org.deri.iris.facts.IFacts;

/**
 * Factory for SLDNF evaluation strategy
 * 
 * @author gigi
 * 
 */
public class SLDNFEvaluationStrategyFactory implements IEvaluationStrategyFactory
{
	public IEvaluationStrategy createEvaluator( IFacts facts, List<IRule> rules, Configuration configuration )
	                throws EvaluationException
	{
		return new SLDNFEvaluationStrategy( facts, rules, configuration );
	}

}
