/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 * 
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, 
 * MA  02110-1301, USA.
 */
package at.sti2.streamingiris.factory;


import at.sti2.streamingiris.api.factory.IBasicFactory;
import at.sti2.streamingiris.api.factory.IBuiltinsFactory;
import at.sti2.streamingiris.api.factory.IConcreteFactory;
import at.sti2.streamingiris.api.factory.IGraphFactory;
import at.sti2.streamingiris.api.factory.ITermFactory;
import at.sti2.streamingiris.basics.BasicFactory;
import at.sti2.streamingiris.builtins.BuiltinsFactory;
import at.sti2.streamingiris.graph.GraphFactory;
import at.sti2.streamingiris.terms.TermFactory;
import at.sti2.streamingiris.terms.concrete.ConcreteFactory;

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
