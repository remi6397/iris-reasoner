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
package org.deri.iris.factory;

import org.deri.iris.api.factory.IBasicFactory;
import org.deri.iris.api.factory.IBuiltinsFactory;
import org.deri.iris.api.factory.IConcreteFactory;
import org.deri.iris.api.factory.IGraphFactory;
import org.deri.iris.api.factory.ITermFactory;
import org.deri.iris.basics.BasicFactory;
import org.deri.iris.builtins.BuiltinsFactory;
import org.deri.iris.graph.GraphFactory;
import org.deri.iris.terms.TermFactory;
import org.deri.iris.terms.concrete.ConcreteFactory;

/**
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 */
public class Factory {
	public static final IBasicFactory BASIC = BasicFactory.getInstance();

	public static final ITermFactory TERM = TermFactory.getInstance();

	public static final IConcreteFactory CONCRETE = ConcreteFactory
			.getInstance();

	public static final IGraphFactory GRAPH = GraphFactory.getInstance();

	public static final IBuiltinsFactory BUILTIN = BuiltinsFactory
			.getInstance();
}
