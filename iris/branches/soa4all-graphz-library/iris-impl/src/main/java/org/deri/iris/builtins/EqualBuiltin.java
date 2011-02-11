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

import static org.deri.iris.factory.Factory.BASIC;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.terms.IConcreteTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * Built-in to either: a) compare two terms for equality, OR b) assign a
 * constant expression to a variable
 */
public class EqualBuiltin extends ArithmeticBuiltin {
	/**
	 * Construct a new EqualBuiltin for the specific predicate and terms.
	 * 
	 * @param predicate The predicate of the built-in.
	 * @param terms The terms.
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	protected EqualBuiltin(IPredicate predicate, ITerm... terms) {
		super(predicate, terms);
	}

	/**
	 * Constructor.
	 * 
	 * @param terms The terms, must be two of these
	 * @throws NullPointerException If the predicate or one of the terms is
	 *             <code>null</code>.
	 * @throws IllegalArgumentException If the length of the terms and the arity
	 *             of the predicate do not match.
	 */
	public EqualBuiltin(final ITerm... t) {
		super(PREDICATE, t);
	}

	protected ITerm computeMissingTerm(int missingTermIndex, ITerm[] terms) {
		return terms[missingTermIndex == 0 ? 1 : 0];
	}

	/** The predicate defining this built-in. */
	public static final IPredicate PREDICATE = BASIC.createPredicate("EQUAL",
			2);
	
	protected boolean checkTypes(int missingTermIndex, ITerm[] terms, 
			Class<? extends IConcreteTerm> expectedClass) {
		int termIndex = (missingTermIndex == 0) ? 1 : 0;
		
		if (!expectedClass.isInstance(terms[termIndex]))
			return false;
		
		if (expectedClass.isInstance(terms[missingTermIndex]) || 
				IVariable.class.isInstance(terms[missingTermIndex])) {
			return true;
		}
		
		return false;
	}
}
