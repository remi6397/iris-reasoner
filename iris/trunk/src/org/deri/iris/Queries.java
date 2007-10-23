/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2007  Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.deri.iris.api.basics.IQuery;

/**
 * A container for the program's set of queries.
 */
public class Queries
{
	/**
	 * Adds a query.
	 * @param query The query to add
	 * @return <code>false</code> if the query was already in the program,
	 * otherwise <code>true</code>
	 * @throws NullPointerException if the query was <code>null</code>
	 */
	boolean addQuery(final IQuery query) {
		if (query == null)
			throw new NullPointerException("The query must not be null");

		return mQueries.add( query );
	}
	
	/**
	 * Get the set of all queries.
	 * @return The set of all queries.
	 */
	public Set<IQuery> getQueries() {
		return Collections.unmodifiableSet(mQueries);
	}
	
	/**
	 * Remove a single query.
	 * @param query The query to remove
	 * @return true If the query was removed.
	 */
	public boolean removeQuery(IQuery query) {
		if (query == null) {
			throw new NullPointerException("The query must not be null");
		}
		return mQueries.remove( query );
	}
	
	/**
	 * Remove all the queries.
	 */
	void clear()
	{
		mQueries.clear();
	}

	/** The set of queries. */
	final Set<IQuery> mQueries = new HashSet<IQuery>();
}
