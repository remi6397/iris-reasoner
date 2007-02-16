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
package org.deri.iris.api.storage;

import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;

/**
 * <p>
 * An interface for a relation in general. A relation is a set of tuples.
 * Particular implementations of this interface may be based on different data
 * structures (e.g. red-black trees, AVL trees etc.) or different way of
 * indexing.
 * </p>
 * <p>
 * $Id: IRelation.java,v 1.10 2007-02-16 08:48:15 poettler_ric Exp $
 * </p>
 * 
 * @author Darko Anicic
 * @author Richard Pöttler, richard dot poettler at deri dot org
 * @date 11.04.2006
 * @time 14:55:12
 * 
 * @version $Revision: 1.10 $ $Date: 2007-02-16 08:48:15 $
 */

public interface IRelation extends SortedSet<ITuple>, Cloneable {

	/**
	 * @return Arity of tuples contained in this relation.
	 */
	public int getArity();

	/**
	 * <p>
	 * Returns a sorted set which is sorted on the specified indexes.
	 * </p>
	 * <p>
	 * The indedexes are specified as follows: All indexes you don't want to
	 * sort on are 0, the index you want to sort on mainly is 1, the second
	 * index (which will be taken into account if the terms at the given
	 * possition of the previous index are equal) is 2, and so on.
	 * </p>
	 * <p>
	 * e.g.: If you want a set of tuples first sorted on the third and then
	 * sorted on the first term you give an index of <code>[2,0,1]</code>.
	 * </p>
	 * 
	 * @param idx
	 *            the index on which should be sorted
	 * @return an <b>unmodifiable</b> set sorted on the given indexes
	 * @throws NullPointerException
	 *             if the indexes is {@code null}
	 * @throws IllegalArgumentException
	 *             if the length of the indexes doesn't match the arity of the
	 *             relation
	 * @throws UnsupportedOperationException
	 *             if the actual relation implementation doesn't support this
	 *             method
	 */
	public abstract SortedSet<ITuple> indexOn(final Integer[] idx);
}
