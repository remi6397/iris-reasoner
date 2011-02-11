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
package org.deri.iris.api.factory;

import java.util.Collection;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.graph.IPredicateGraph;

/**
 * <p>
 * An interface that can be used to create a predicate graph.
 * </p>
 * <p>
 * $Id$
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public interface IGraphFactory {

	/**
	 * Constructs a new empty predicate graph.
	 * @return the constructed graph
	 */
	public IPredicateGraph createPredicateGraph();

	/**
	 * Constructs a new predicate graph initialized with a collection of
	 * rules.
	 * @param r the rules with which to initialize the graph
	 * @return the constructed graph
	 */
	public IPredicateGraph createPredicateGraph(final Collection<IRule> r);
}
