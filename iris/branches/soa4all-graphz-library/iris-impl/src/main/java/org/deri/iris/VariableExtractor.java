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
package org.deri.iris;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.IConstructedTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.api.terms.IVariable;

/**
 * <p>
 * Helpermethods to extract variables from terms, tuples and literals.
 * </p>
 * <p>
 * $Id: VariableExtractor.java,v 1.1 2007-10-30 09:29:25 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public class VariableExtractor {

	private VariableExtractor() {
		// prevent subclassing
	}

	/**
	 * Retrieves the variables out of a collection of terms.
	 * @param from the terms
	 * @param to the collection where to add the variables to
	 * @return the reference to <code>to</code>
	 * @throws IllegalArgumentException if <code>t</code> is
	 * <code>null</code>
	 * @throws IllegalArgumentException if <code>to</code> is
	 * <code>null</code>
	 */
	public static Collection<IVariable> getTermVariables(final Collection<? extends ITerm> from, 
			final Collection<IVariable> to) {
		if (from == null) {
			throw new IllegalArgumentException("The term collection must not be null");
		}

		for (final ITerm term : from) {
			if (term instanceof IConstructedTerm) {
				getTermVariables(((IConstructedTerm) term).getParameters(), to);
			} else if (term instanceof IVariable) {
				to.add((IVariable) term);
			}
		}
		return to;
	}

	/**
	 * Retrieves the variables out of a collection of tuples.
	 * @param from the tuples
	 * @param to the collection where to add the variables to
	 * @return the reference to <code>to</code>
	 * @throws IllegalArgumentException if <code>t</code> is
	 * <code>null</code>
	 * @throws IllegalArgumentException if <code>to</code> is
	 * <code>null</code>
	 */
	public static Collection<IVariable> getTupleVariables(final Collection<? extends ITuple> from, 
			final Collection<IVariable> to) {
		if (from == null) {
			throw new IllegalArgumentException("The tuple collection must not be null");
		}

		for (final ITuple tuple : from) {
			getTermVariables(tuple, to);
		}
		return to;
	}

	/**
	 * Retrieves the variables out of a collection of literals.
	 * @param from the literals
	 * @param to the collection where to add the variables to
	 * @return the reference to <code>to</code>
	 * @throws IllegalArgumentException if <code>t</code> is
	 * <code>null</code>
	 * @throws IllegalArgumentException if <code>to</code> is
	 * <code>null</code>
	 */
	public static Collection<IVariable> getLiteralVariables(final Collection<? extends ILiteral> from, 
			final Collection<IVariable> to) {
		if (from == null) {
			throw new IllegalArgumentException("The literal collection must not be null");
		}

		for (final ILiteral literal : from) {
			getTupleVariables(Collections.singleton(literal.getAtom().getTuple()), to);
		}
		return to;
	}

	/**
	 * Retrieves the variables out of a collection of terms.
	 * @param t the terms
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static List<IVariable> getTermVariablesList(final Collection<? extends ITerm> t) {
		if (t == null) {
			throw new IllegalArgumentException("The term collection must not be null");
		}

		return (List<IVariable>) getTermVariables(t, new ArrayList<IVariable>());
	}

	/**
	 * Retrieves the variables out of a collection of tuples.
	 * @param t the tuples
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static List<IVariable> getTupleVariablesList(final Collection<? extends ITuple> t) {
		if (t == null) {
			throw new IllegalArgumentException("The tuple collection must not be null");
		}

		return (List<IVariable>) getTupleVariables(t, new ArrayList<IVariable>());
	}

	/**
	 * Retrieves the variables out of a collection of literals.
	 * @param t the literals
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static List<IVariable> getLiteralVariablesList(final Collection<? extends ILiteral> l) {
		if (l == null) {
			throw new IllegalArgumentException("The literal collection must not be null");
		}

		return (List<IVariable>) getLiteralVariables(l, new ArrayList<IVariable>());
	}

	/**
	 * Retrieves the variables out of a collection of terms.
	 * @param t the terms
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static Set<IVariable> getTermVariables(final Collection<? extends ITerm> t) {
		if (t == null) {
			throw new IllegalArgumentException("The term collection must not be null");
		}

		return (Set<IVariable>) getTermVariables(t, new HashSet<IVariable>());
	}

	/**
	 * Retrieves the variables out of a collection of tuples.
	 * @param t the tuples
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static Set<IVariable> getTupleVariables(final Collection<? extends ITuple> t) {
		if (t == null) {
			throw new IllegalArgumentException("The tuple collection must not be null");
		}

		return (Set<IVariable>) getTupleVariables(t, new HashSet<IVariable>());
	}

	/**
	 * Retrieves the variables out of a collection of literals.
	 * @param t the literals
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static Set<IVariable> getLiteralVariables(final Collection<? extends ILiteral> l) {
		if (l == null) {
			throw new IllegalArgumentException("The literal collection must not be null");
		}

		return (Set<IVariable>) getLiteralVariables(l, new HashSet<IVariable>());
	}

	/**
	 * Retrieves the variables out of a array of terms.
	 * @param t the terms
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static List<IVariable> getVariablesList(final ITerm... t) {
		if (t == null) {
			throw new IllegalArgumentException("The term array must not be null");
		}

		return getTermVariablesList(Arrays.asList(t));
	}

	/**
	 * Retrieves the variables out of a array of tuples.
	 * @param t the tuples
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static List<IVariable> getVariablesList(final ITuple... t) {
		if (t == null) {
			throw new IllegalArgumentException("The tuple array must not be null");
		}

		return getTupleVariablesList(Arrays.asList(t));
	}

	/**
	 * Retrieves the variables out of a array of literals.
	 * @param t the literals
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static List<IVariable> getVariablesList(final ILiteral... l) {
		if (l == null) {
			throw new IllegalArgumentException("The literal array must not be null");
		}

		return getLiteralVariablesList(Arrays.asList(l));
	}

	/**
	 * Retrieves the variables out of a array of terms.
	 * @param t the terms
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static Set<IVariable> getVariables(final ITerm... t) {
		if (t == null) {
			throw new IllegalArgumentException("The term array must not be null");
		}

		return getTermVariables(Arrays.asList(t));
	}

	/**
	 * Retrieves the variables out of a array of tuples.
	 * @param t the tuples
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static Set<IVariable> getVariables(final ITuple... t) {
		if (t == null) {
			throw new IllegalArgumentException("The tuple array must not be null");
		}

		return getTupleVariables(Arrays.asList(t));
	}

	/**
	 * Retrieves the variables out of a array of literals.
	 * @param t the literals
	 * @return the list of variables
	 * @throws IllegalArgumentException if the collection is
	 * <code>null</code>
	 */
	public static Set<IVariable> getVariables(final ILiteral... l) {
		if (l == null) {
			throw new IllegalArgumentException("The literal array must not be null");
		}

		return getLiteralVariables(Arrays.asList(l));
	}
}
