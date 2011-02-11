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
package org.deri.iris.evaluation.topdown;

import java.util.List;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.terms.IVariable;
import org.deri.iris.storage.IRelation;

/**
 * Interface for top-down evaluators
 * 
 * @author gigi
 */
public interface ITopDownEvaluator {

	/**
	 * Evaluate a query
	 * @param query a query
	 * @return relation containing all resolved tuples.
	 * @throws EvaluationException thrown on error
	 */
	public IRelation evaluate(IQuery query) throws EvaluationException;
	
	/**
	 * Returns a list of output variables, i.e. the variables of the initial query.
	 * @return list of output variables
	 */
	public List<IVariable> getOutputVariables();
	
}
