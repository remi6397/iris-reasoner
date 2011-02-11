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
package org.deri.iris;

import java.util.List;
import java.util.Map;

import org.deri.iris.api.IKnowledgeBase;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.storage.IRelation;

/**
 * The factory for creating a knowledge-base.
 */
public class KnowledgeBaseFactory
{
	/**
	 * Create a knowledge base with default configuration.
	 * @param facts The starting facts.
	 * @param rules The rules to use.
	 * @return A new knowledge-base instance.
	 * @throws EvaluationException 
	 */
	public static IKnowledgeBase createKnowledgeBase( Map<IPredicate,IRelation> facts, List<IRule> rules ) throws EvaluationException
	{
		return createKnowledgeBase( facts, rules, new Configuration() );
	}
	
	/**
	 * Create a knowledge base with a custom configuration.
	 * @param facts The starting facts.
	 * @param rules The rules to use.
	 * @param configuration The configuration to use for the new knowledge-base.
	 * @return A new knowledge-base instance.
	 * @throws EvaluationException 
	 */
	public static IKnowledgeBase createKnowledgeBase( Map<IPredicate,IRelation> facts, List<IRule> rules, Configuration configuration ) throws EvaluationException
	{
		return new KnowledgeBase( facts, rules, configuration );
	}
	
	/**
	 * Create a new default configuration and return it.
	 * @return The new configuration.
	 */
	public static Configuration getDefaultConfiguration()
	{
		return new Configuration();
	}
}
