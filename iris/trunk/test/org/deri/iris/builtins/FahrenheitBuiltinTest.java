/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2006  Digital Enterprise Research Institute (DERI), 
 * Leopold-Franzens-Universitaet Innsbruck, Technikerstrasse 21a, 
 * A-6020 Innsbruck. Austria.
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
package org.deri.iris.builtins;

import static org.deri.iris.factory.Factory.PROGRAM;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.builtins.IBuiltInAtom;
import org.deri.iris.compiler.Parser;

/**
 * <p>
 * Test the possibility of custom made builtins.
 * </p>
 * <p>
 * $Id: FahrenheitBuiltinTest.java,v 1.6 2007-10-30 08:28:31 poettler_ric Exp $
 * </p>
 * @version $Revision: 1.6 $
 * @author Richard Pöttler, richard dot poettler at deri dot org
 */
public class FahrenheitBuiltinTest extends TestCase {

	private IProgram p;

	public static Test suite() {
		return new TestSuite(FahrenheitBuiltinTest.class, FahrenheitBuiltinTest.class.getSimpleName());
	}

	public void setUp() {
		p = PROGRAM.createProgram();
	}

	public void testAdding() {
		final BuiltinRegister reg = p.getBuiltinRegister();
		reg.registerBuiltin(FahrenheitToCelsiusBuiltin.class);
		final IPredicate ftoc = FahrenheitToCelsiusBuiltin.getBuiltinPredicate();
		assertNotNull("It seems that the builtin wasn't registered correctly", 
				reg.getBuiltinClass(ftoc.getPredicateSymbol()));
		assertEquals("The class of the builtin wasn't returned correctly", 
				FahrenheitToCelsiusBuiltin.class, reg.getBuiltinClass(ftoc.getPredicateSymbol()));
		assertEquals("The arity of the builtin wasn't returned correctly", 
				ftoc.getArity(), reg.getBuiltinArity(ftoc.getPredicateSymbol()));
	}

	public void testParsing() throws Exception {
		p.getBuiltinRegister().registerBuiltin(FahrenheitToCelsiusBuiltin.class);
		Parser.parse("fahrenheit(?X) :- ftoc(?X, 10).", p);
		final ILiteral b = p.getRules().iterator().next().getBody().get(0);
		assertTrue("The atom must be a IBuiltInAtom", b.getAtom() instanceof IBuiltInAtom);
	}
}
