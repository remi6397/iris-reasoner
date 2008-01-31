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

package org.deri.iris.api.storage_old;

import java.util.Collection;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * Interface for relations accepting tuples with various datatypes at different
 * positions. This interface was defined because in java it is not possible to
 * compare apples with organges (e.g. strings with dates), but there is a need
 * to access the sorted subsets with the different datatypes in a fast way.
 * </p>
 * <p>
 * The methods defined here are related to the methods in the
 * <code>java.util.SortedSet</code> interface.
 * <p>
 * Every method defined in this interface will retrun a collection of sorted
 * sets according to the tuple mask passed to it.
 * A <code>null</code> term represents the minimal term for all datatypes.
 * E.g. if you ask for a tail set
 * with the tuple mask of <code>&lt;int(10), null, null&gt;</code> you might get
 * back a collection of sorted sets where all the first terms are greater or equal to
 * 10, but the other two terms hold various data. Sub sets gained from a
 * <code>IMixedDatatypeRelation</code> don't have to be able to handle tuples
 * with various datatypes at various positions.
 * </p>
 * <p>
 * $Id: IMixedDatatypeRelation.java,v 1.1 2007-06-06 11:44:31 poettler_ric Exp $
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.1 $
 */
public interface IMixedDatatypeRelation extends IRelation {

	/**
	 * Returns the tailset according to the tuple mask.
	 * @param from the upper bounds (inclusive) for the tuples
	 * @return collection of relations which satisfy the bound
	 */
	public abstract Collection<SortedSet<ITuple>> separatedTailSet(final ITuple from);

	/**
	 * Returns the headset according to the tuple mask.
	 * @param to the lower bounds (exclusive) for the tuples
	 * @return collection of relations which satisfy the bound
	 */
	public abstract Collection<SortedSet<ITuple>> separatedHeadSet(final ITuple to);

	/**
	 * Returns the tailset according to the tuple mask.
	 * @param from the upper bounds (inclusive) for the tuples
	 * @param to the lower bounds (exclusive) for the tuples
	 * @return collection of relations which satisfy the bounds
	 */
	public abstract Collection<SortedSet<ITuple>> separatedSubSet(final ITuple from, final ITuple to);

	/**
	 * Returns a relation with indexes created at the specified fields.
	 * @param idx the indexes to create
	 * @return the relation with the created indexes
	 * @throws NullPointerException if the index array is <code>null</code>
	 * @throws IllegalArgumentException if the length of the index array
	 * doesn't match the arity of the relation
	 * @throws UnsupportedOperationException if this operation is not
	 * supported by the relation implementation
	 * @see IRelation#indexOn(Integer[])
	 */
	public abstract IMixedDatatypeRelation indexOn(final int[] idx);
}
