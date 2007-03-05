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

import java.util.ArrayList;
import java.util.List;

import org.deri.iris.api.operations.relation.IUnion;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic
 * @date 23.09.2006
 * 
 */
public class Union implements IUnion {

	private List<IRelation> rels = null;

	private IRelation unionRel = null;

	Union(List<IRelation> arg) {
		if (arg == null) {
			throw new IllegalArgumentException(
					"Input parameter must not be null");
		}
		int arity = 0;
		int prevArity = 0;
		int start = 0;
		this.rels = new ArrayList<IRelation>();
		for (IRelation r : arg) {
			// Get non-empty relations from the input relation list
			if (r != null && r.size() > 0) {
				arity = r.getArity();
				if (arity == prevArity || start == 0) {
					this.rels.add(r);
					start++;
				} else
					throw new IllegalArgumentException(
							"Cannot do union due to different arities");

				prevArity = arity;
			}
		}
		this.unionRel = RELATION.getRelation(arity);
	}

	Union(IRelation... args) {
		if (args == null) {
			throw new IllegalArgumentException(
					"Input parameter must not be null");
		}
		int arity = 0;
		int prevArity = 0;
		int start = 0;
		this.rels = new ArrayList<IRelation>();
		for (IRelation r : args) {
			// Get non-empty relations from the input relation list
			if (r != null && r.size() > 0) {
				arity = r.getArity();
				if (arity == prevArity || start == 0) {
					this.rels.add(r);
					start++;
				} else
					throw new IllegalArgumentException(
							"Cannot do union due to different arities");

				prevArity = arity;
			}
		}
		this.unionRel = RELATION.getRelation(arity);
	}

	public IRelation union() {
		for (IRelation r : this.rels) {
			this.unionRel.addAll(r);
		}
		return this.unionRel;
	}

}
