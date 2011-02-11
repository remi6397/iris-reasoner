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
package org.deri.iris.api;

import java.util.Collection;
import java.util.List;

import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;

/**
 * <p>
 * Interface for a rule optimisation algorithm.
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @since 0.6
 */
public interface IProgramOptimisation {

	/**
	 * Applies the optimisation algorithm to a set of rules. A 
	 * {@link IProgramOptimisation.Result Result} which 
	 * contains the transformed rules and the adjusted query.  
	 * @param rules the rules to transform 
	 * @param query the query for which to transform the rules
	 * @return a optimisation result or <code>null</code>, if the
	 * transformation failed.
	 */
	public Result optimise(final Collection<IRule> rules, final IQuery query);

	/**
	 * Represents the result of a rule optimisation. <b>The
	 * <code>rules</code> and <code>query</code> of this class 
	 * are non-final mutable fields.</b>
	 * @author Richard Pöttler (richard dot poettler at sti2 dot at)
	 * @since 0.6
	 */
	public static class Result {

		/**
		 * Constructor to create and initialize a result in one line.
		 * @param r the rules to set
		 * @param q the query to set
		 */
		public Result(final List<IRule> r, final IQuery q) {
			rules = r;
			query = q;
		}

		/** The transformed rules. */
		public List<IRule> rules;

		/** The adjusted query. */
		public IQuery query;
	}
}
