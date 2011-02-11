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
package org.deri.iris.graph;

import java.util.Collection;

import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.factory.IGraphFactory;
import org.deri.iris.api.graph.IPredicateGraph;

/**
 * <p>
 * A simple predicate graph implementation.
 * </p>
 * <p>
 * $Id$
 * </p>
 *
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 * @version $Revision$
 */
public class GraphFactory implements IGraphFactory {

	private static final IGraphFactory FACTORY = new GraphFactory();
	
	private GraphFactory() {
		// this is a singelton
	}
	
	public static IGraphFactory getInstance() {
		return FACTORY;
	}
	
	public IPredicateGraph createPredicateGraph() {
		return new PredicateGraph();
	}
	
	public IPredicateGraph createPredicateGraph(final Collection<IRule> r) {
		return new PredicateGraph(r);
	}

}
