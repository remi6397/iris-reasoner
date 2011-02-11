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
package org.deri.iris.rules.compiler;

import java.util.List;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.facts.IFacts;
import org.deri.iris.storage.IRelation;

/**
 * Interface for a compiled rule.
 */
public interface ICompiledRule
{
	/**
	 * Evaluate rule with all known facts.
	 * @return The result relation for this rule.
	 * @throws EvaluationException 
	 */
	IRelation evaluate() throws EvaluationException;

	/**
	 * Evaluate the rule using deltas (see semi-naive evaluation) to more intelligently seek out
	 * tuples that have not already been computed.
	 * @param deltas The collection of recently discovered facts.
	 * @return The result relation for this rule.
	 * @throws EvaluationException 
	 */
	IRelation evaluateIteratively( IFacts deltas ) throws EvaluationException;
	
	/**
	 * If this compiled rule represents a rule, then return the head predicate.
	 * @return The head predicate.
	 */
	IPredicate headPredicate();
	
	/**
	 * If this compiled rule represents a query, then return the variables bindings of the
	 * result relation.
	 * @return The list of variables in the order in which they are bound to terms of the result relation. 
	 */
	List<IVariable> getVariablesBindings();
}
