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

import static org.deri.iris.builtins.BuiltinHelper.add;
import static org.deri.iris.builtins.BuiltinHelper.divide;
import static org.deri.iris.builtins.BuiltinHelper.multiply;
import static org.deri.iris.builtins.BuiltinHelper.subtract;
import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * A simple fahrenheit to celsius builtin. The first index is the fahrenheit
 * value, the second is the celsius value.
 * </p>
 * <p>
 * $Id: FahrenheitToCelsiusBuiltin.java,v 1.8 2007-10-12 12:52:13 bazbishop237 Exp $
 * </p>
 * @version $Revision: 1.8 $
 * @author Richard Pöttler (richard dot poettler at deri dot org)
 */
public class FahrenheitToCelsiusBuiltin extends ArithmeticBuiltin {

	/** Predicate holding the information about this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate("ftoc", 2);

	/** Term representing an int(5). */
	private static final ITerm t5 = CONCRETE.createInteger(5);

	/** Term representing an int(9). */
	private static final ITerm t9 = CONCRETE.createInteger(9);

	/** Term representing an int(32). */
	private static final ITerm t32 = CONCRETE.createInteger(32);

	/**
	 * Constructs this builtin.
	 * @param t the terms for this builtin. The first index is the
	 * fahrenheit value, the second is the celsius value.
	 */
	public FahrenheitToCelsiusBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	@Override
    protected ITerm computeMissingTerm( int missingTermIndex, ITerm[] terms ) throws EvaluationException
    {
		if( missingTermIndex == 0 ) // fahrenheit are requested
			return add(divide(multiply(terms[ 1 ], t9), t5), t32);
		else // celsius are requested
			return divide(multiply(subtract(terms[ 0 ], t32), t5), t9);
    }

	public IPredicate getBuiltinPredicate() {
		return PREDICATE;
	}
}
