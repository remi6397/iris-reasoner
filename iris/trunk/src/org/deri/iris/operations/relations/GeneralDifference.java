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
package org.deri.iris.operations.relations;

import static org.deri.iris.factory.Factory.RELATION;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IDifference;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * Implementation of the IDifference interface.
 * </p>
 * <p>
 * The difference operation removes all tuples in the first argument (relation)
 * contained in the second argument (relation). Both relations must contain 
 * tuples with the same arities and can have terms of different data types.
 * </p>
 * 
 * @author Darko Anicic
 * @date $Date: 13.06.2007 16:26:30
 */
public class GeneralDifference implements IDifference {

	private IMixedDatatypeRelation rel0 = null;

	private IMixedDatatypeRelation rel1 = null;

	/**
	 * Constructs the difference operator.
	 * 
	 * @param arg0	A minuend relation.
	 * @param arg1	A subtrahend relation.
	 */
	GeneralDifference(IMixedDatatypeRelation arg0, IMixedDatatypeRelation arg1) {
		if ((arg0 == null) || (arg1 == null)) {
			throw new IllegalArgumentException(
					"Input parameters must not be null");
		}
		if (!arg0.isEmpty() && !arg1.isEmpty()) {
			if (((ITuple) arg0.first()).size() != ((ITuple) arg1.first())
					.size()) {
				throw new IllegalArgumentException(
						"Arity of input parameter do not match: "
								+ ((ITuple) arg0.first()).size() + " != "
								+ ((ITuple) arg1.first()).size());
			}
		}
		this.rel0 = arg0;
		this.rel1 = arg1;
	}

	public IMixedDatatypeRelation difference() {
		IMixedDatatypeRelation differenceRel = RELATION
				.getMixedRelation(this.rel0.getArity());
		differenceRel.addAll(this.rel0);
		differenceRel.removeAll(this.rel1);

		return differenceRel;
	}

}
