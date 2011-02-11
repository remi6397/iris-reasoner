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
package org.deri.iris.builtins.numeric;

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.CONCRETE;

import java.math.BigDecimal;
import java.math.BigInteger;

import org.deri.iris.EvaluationException;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.INumericTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.DivideBuiltin;

/**
 * <p>
 * Represents a integer divide operation, i.e. divides the first argument by the
 * second, and returns the integer obtained by truncating the fractional part of
 * the result. At the evaluation time there must only be one variable left for
 * computation, otherwise an exception will be thrown.
 * </p>
 * 
 * @author Adrian Marte
 */
public class NumericIntegerDivideBuiltin extends DivideBuiltin {

	/** The predicate defining this builtin. */
	private static final IPredicate PREDICATE = BASIC.createPredicate(
			"NUMERIC_INTEGER_DIVIDE", 3);

	/**
	 * Constructs a builtin. Three terms must be passed to the constructor,
	 * otherwise an exception will be thrown.
	 * 
	 * @param terms The terms.
	 * @throws NullPointerException If one of the terms is <code>null</code>.
	 * @throws IllegalArgumentException If the number of terms submitted is not
	 *             3.
	 * @throws NullPointerException If <code>t</code> is <code>null</code>.
	 */
	public NumericIntegerDivideBuiltin(ITerm... terms) {
		super(PREDICATE, terms);
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms)
			throws EvaluationException {
		ITerm result = super.computeMissingTerm(missingTermIndex, terms);

		// Truncate the fractional part of the result.
		if (result != null && result instanceof INumericTerm) {
			BigDecimal value = ((INumericTerm) result).getValue();
			BigInteger truncatedResult = value.toBigInteger();
			result = CONCRETE.createInteger(truncatedResult);
		}

		return result;
	}
}
