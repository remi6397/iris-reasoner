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
package org.deri.iris.builtins;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.builtins.IBuiltinAtom;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.compiler.BuiltinRegister;
import org.deri.iris.compiler.Parser;
import org.deri.iris.factory.Factory;

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

	public static Test suite() {
		return new TestSuite(FahrenheitBuiltinTest.class, FahrenheitBuiltinTest.class.getSimpleName());
	}

	public void testAdding() {
		
		final BuiltinRegister reg = new BuiltinRegister();
		reg.registerBuiltin( instance );
		final IPredicate ftoc = instance.getBuiltinPredicate();
		assertNotNull("It seems that the builtin wasn't registered correctly", 
				reg.getBuiltinClass(ftoc.getPredicateSymbol()));
		assertEquals("The class of the builtin wasn't returned correctly", 
				FahrenheitToCelsiusBuiltin.class, reg.getBuiltinClass(ftoc.getPredicateSymbol()));
		assertEquals("The arity of the builtin wasn't returned correctly", 
				ftoc.getArity(), reg.getBuiltinArity(ftoc.getPredicateSymbol()));
	}

	public void testParsing() throws Exception {
		final BuiltinRegister reg = new BuiltinRegister();
		reg.registerBuiltin( instance );

		Parser parser = new Parser( reg );
		parser.parse("fahrenheit(?X) :- ftoc(?X, 10).");
		final ILiteral b = parser.getRules().iterator().next().getBody().get(0);
		assertTrue("The atom must be a IBuiltInAtom", b.getAtom() instanceof IBuiltinAtom);
	}

	private final ITerm t1 = Factory.TERM.createVariable( "a" );
	private final ITerm t2 = Factory.TERM.createVariable( "a" );
	private final FahrenheitToCelsiusBuiltin instance = new FahrenheitToCelsiusBuiltin( t1, t2 );
}
