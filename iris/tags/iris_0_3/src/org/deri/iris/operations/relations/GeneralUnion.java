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

import java.util.List;

import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * Implementation of the IUnion interface.
 * </p>
 * <p>
 * The union operation put tuples from all input relations in one collection of
 * tuples which can further be indexed as necessary.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 13.06.2007. 14:40:40
 */
public class GeneralUnion implements IUnion {

	/** Input relations. */
	private IMixedDatatypeRelation[] rels = null;

	/** Result relation which is a union of all input relations. */
	private IMixedDatatypeRelation unionRel = null;

	GeneralUnion(List<IMixedDatatypeRelation> arg) {
		if (arg == null) {
			throw new IllegalArgumentException(
					"Input parameter must not be null");
		}
		this.rels = arg.toArray(new IMixedDatatypeRelation[arg.size()]);
	}

	GeneralUnion(IMixedDatatypeRelation... args) {
		if (args == null) {
			throw new IllegalArgumentException(
					"Input parameter must not be null");
		}
		this.rels = args;
	}

	public IMixedDatatypeRelation union() {
		int arity = 0;
		IMixedDatatypeRelation r = null;
		if (this.rels.length > 0) {
			int prevArity = this.rels[0].getArity();
			unionRel = RELATION.getMixedRelation(prevArity);
			for (int i = 0; i < this.rels.length; i++) {
				r = this.rels[i];
				// Get non-empty relations from the input relation list/array
				if (r != null && r.size() > 0) {
					arity = r.getArity();
					if (arity == prevArity) {
						unionRel.addAll(r);
					} else
						throw new IllegalArgumentException(
								"Cannot do union due to different arities");
				}
			}
		} else {
			throw new IllegalArgumentException(
					"Cannot define arity of the union relation.");
		}
		return this.unionRel;
	}

}