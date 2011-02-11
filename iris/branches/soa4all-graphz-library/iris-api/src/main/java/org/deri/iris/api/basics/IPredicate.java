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
package org.deri.iris.api.basics;

/**
 * <p>
 * A predicate is either a relation or the boolean-valued function that amounts
 * to the characteristic function or the indicator function of such a relation.
 * </p>
 * <p>
 * A predicate is characterized by a predicate symbol and an arity of the
 * predicate.
 * </p>
 * <p>
 * $Id: IPredicate.java,v 1.7 2007-07-25 08:16:56 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.7 $
 */

public interface IPredicate extends Comparable<IPredicate> {
	/**
	 * <p>
	 * Returns the predicate symbol. <p/>
	 * 
	 * @return The predicate symbol.
	 */
	public String getPredicateSymbol();

	/**
	 * <p>
	 * Returns the arity of the predicate. <p/>
	 * 
	 * @return The arity.
	 */
	public int getArity();
}
