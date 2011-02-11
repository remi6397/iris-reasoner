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
 * An atom (an atomic formula) is a formula that has no subformulas. Atom
 * consists of a predicate symbol and a tuple (a list of arguments), e.g.,
 * p(A1,...,An), where p is a predicate symbol and A1,...,An is a tuple (a list
 * of arguments).
 * </p>
 * 
 * <p>
 * This interface is used to promote modularity of the inference engine.
 * </p>
 * <p>
 * $Id: IAtom.java,v 1.6 2007-10-09 20:17:49 bazbishop237 Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @version $Revision: 1.6 $
 */
public interface IAtom extends Comparable<IAtom> {

	/**
	 * <p>
	 * Returns the predicate symbol of the atom.
	 * </p>
	 * 
	 * @return The predicate symbol.
	 */
	public IPredicate getPredicate();

	/**
	 * <p>
	 * Returns the tuple of the atom.
	 * </p>
	 * 
	 * @return The tuple.
	 */
	public ITuple getTuple();

	/**
	 * <p>
	 * Checks whether the atom is grounded (tuple contains no variables).
	 * </p>
	 * 
	 * @return True if the atom is grounded, otherwise false.
	 */
	public boolean isGround();

	/**
	 * Returns whether this atom is a builtin one, or not.
	 * @return <code>true</code> if it is builtin, otherwise
	 * <code>false</code>
	 * @since 0.3
	 */
	public boolean isBuiltin();
}
