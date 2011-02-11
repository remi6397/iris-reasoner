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
package org.deri.iris.basics;

import org.deri.iris.api.basics.IPredicate;

/**
 * <p>
 * This is a simple IPredicate implementation.
 * </p>
 * <p>
 * NOTE: This implementation is immutable
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision$
 */
public class Predicate implements IPredicate {

	private final String symbol;
	
	/** A (unique) string containing the predicate name and arity. */
	private final String symbolPlusArity;

	private final int arity;
	
	Predicate(final String symbol, final int arity) {
		this.symbol = symbol;
		this.arity = arity;
		
		StringBuilder b = new StringBuilder();
		
		b.append( symbol ).append( '$' ).append( arity );
		symbolPlusArity = b.toString();
	}

	public String getPredicateSymbol() {
		return symbol;
	}

	public int getArity() {
		return arity;
	}

	public int hashCode() {
		return symbolPlusArity.hashCode();
	}

	public boolean equals(final Object o) {
		if (o == this) {
			return true;
		}
		if (!(o instanceof Predicate)) {
			return false;
		}
		Predicate p = (Predicate) o;
		return symbolPlusArity.equals(p.symbolPlusArity);
	}

	public int compareTo(IPredicate o) {
		Predicate predicate = (Predicate) o;
		return symbolPlusArity.compareTo( predicate.symbolPlusArity );
	}

	public String toString() {
		return symbol;
	}
}
