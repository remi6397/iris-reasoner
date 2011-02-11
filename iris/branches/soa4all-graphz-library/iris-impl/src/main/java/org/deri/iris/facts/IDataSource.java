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
package org.deri.iris.facts;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;

/**
 * <p>
 * Interface for pluggable datasources for iris.
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 */
public interface IDataSource {

	/**
	 * <p>
	 * Retrieves some tuples for a given predicate from the data source and
	 * adds it to a given relation.
	 * </p>
	 * <p>
	 * The terms in <code>from</code> and
	 * <code>to</code> set the lower and upper bounds for the terms in the
	 * corresponding columns of the tuples, which should be added to the
	 * tuple collection. <code>null</code> in the <code>from</code> or 
	 * <code>to</code> list, stands for the smallest, respectively 
	 * biggest possible term for this column.
	 * </p>
	 * @param p the predicate for which to retrieve the tuples (because one
	 * data source might hold tuples for multiple predicates)
	 * @param from the lower bound for the tuples which should be added to
	 * the relation (<code>null</code> is equivalent to a tuple containing
	 * only <code>null</code>s)
	 * @param to the upper bound for the tuples which should be added to
	 * the relation (<code>null</code> is equivalent to a tuple containing
	 * only <code>null</code>s)
	 * @param r the relation where to add the tuples
	 */
	public void get(final IPredicate p, final ITuple from, final ITuple to, final IRelation r);
}
