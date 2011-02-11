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
package org.deri.iris.evaluation;

import java.util.List;

import org.deri.iris.EvaluationException;
import org.deri.iris.ProgramNotStratifiedException;
import org.deri.iris.RuleUnsafeException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;

/**
 * Interface for all evaluation strategy implementations.
 */
public interface IEvaluationStrategy
{
	/**
	 * Evaluate a query and optionally return the variable bindings.
	 * @param query The query to evaluate.
	 * @param outputVariables If this is not null, it will be filled with the variable bindings
	 * of the result relation, i.e. there will be one variable instance for each term
	 * (in one row) of the results set
	 * @return The relation of results.
	 * @throws ProgramNotStratifiedException If the program (knowledge-base)can not be stratified
	 * @throws RuleUnsafeException If the program (knowledge-base) contains an unsafe rule.
	 * @throws EvaluationException If the evaluation fails for any other reason.
	 */
	IRelation evaluateQuery( IQuery query, List<IVariable> outputVariables ) throws ProgramNotStratifiedException, RuleUnsafeException, EvaluationException;
}
