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

import static org.deri.iris.factory.Factory.BASIC;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IProjection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.operations.tuple.BasicComparator;
import org.deri.iris.operations.tuple.IndexComparator;
import org.deri.iris.storage.Relation;

/**
 * The implementation of IProjection interface meant to be used for doing
 * projection on a relation (tree) as well as for the tuple reordering in a
 * relation.
 * 
 * The projection is defined by a pattern. The pattern is an array with its
 * length equals to the relation’s arity. If the array contains -1 on a certain
 * position, a term with that position in a tuple (that is stored in the
 * relation) will not be considered during the projection operation. For
 * example, if the pattern array takes the following values: [2, -1, 0] than the
 * projection will be performed on 0th and 2nd (as we have values different from
 * -1 on 0th and 2nd index of the array) arguments of each tuple in the
 * relation.
 * 
 * The result will be a relation where each tuple has arity two. Each tuple in
 * the relation is created so that:
 * 
 * term with index 0 will be replaced at the position 2; term with index 1 will
 * be ignored (due to -1 index vale); term with index 2 will be replaced at the
 * position 0;
 * 
 * For instance for a tuple: <a, b, c> and projection indexes: <2, -1, 0> we
 * would get a tuple: <c, null, a>. As we do not need null values (supposed to
 * be ignored), we will finally get: <c, a>.
 * 
 * Note: The implementation of IJoin (Join and JoinSimpleExtended) using
 * projectIndexes can handle projection operation too! Use this option in cases
 * where the join operation is needed following with the projection operation
 * just after the join operation.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 30.05.2006 10:44:38
 */
public class Projection implements IProjection {
	private IRelation relation = null;

	private int[] indexes = null;

	private int arity = 0;

	private IRelation projectionRelation = null;

	private BasicComparator comparator = null;

	/**
	 * @param relation
	 * @param pattern
	 *            define indexes which the projection operation will be applied
	 *            on.
	 */
	Projection(final IRelation relation, final int[] pattern) {
		if (relation == null || pattern == null) {
			throw new IllegalArgumentException("All construcotr "
					+ "parameters must not be specified (non null values");
		}
		this.relation = relation;
		this.indexes = pattern;
		this.arity = this.getRelationArity();
		projectionRelation = new Relation(this.arity);
	}

	public IRelation project() {
		if (this.relation.size() == 0)
			return this.relation;
		/**
		 * Sort relation on those tupples defined by indexes. This will create
		 * duplicate nodes for equivalent tuples (those that have equal terms on
		 * project indexes). Thus the computation of the projectTuple method
		 * will be done only on non equivalent tuples.
		 */
		this.comparator = new IndexComparator(this.transformIndexes());
		IRelation rel = new Relation(comparator);
		rel.addAll(this.relation);
		this.relation = rel;

		Iterator iterator = this.relation.iterator();
		ITuple tuple;

		while (iterator.hasNext()) {
			tuple = (ITuple) iterator.next();
			tuple = projectTuple(tuple);
			projectionRelation.add(tuple);
		}

		return this.projectionRelation;
	}

	private int getRelationArity() {
		int j = 0;
		for (int i = 0; i < this.indexes.length; i++) {
			if (this.indexes[i] != -1)
				j++;
		}
		return j;
	}

	/**
	 * Transforms indexes according to the following examples: [ 0, -1, -1] to [
	 * 0, -1, -1] [-1, 0, -1] to [-1, 1, -1] [-1, -1, 0] to [-1, -1, 2] [ 1, -1,
	 * 0] to [ 0, -1, 2]
	 * 
	 * @return transformed indexes
	 */
	private int[] transformIndexes() {
		int[] tranformedIndexes = new int[indexes.length];
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1)
				tranformedIndexes[i] = i;
			else
				tranformedIndexes[i] = -1;
		}
		return tranformedIndexes;
	}

	private ITuple projectTuple(ITuple tuple) {
		ITerm[] terms = new ITerm[tuple.getArity()];
		List<ITerm> termList = new ArrayList<ITerm>(this.getRelationArity());
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1)
				terms[indexes[i]] = tuple.getTerm(i);
		}
		for (int j = 0; j < indexes.length; j++) {
			if (terms[j] != null)
				termList.add(terms[j]);
		}
		return BASIC.createTuple(termList);
	}
	
	/**
	 * <p>Creates a default projection indexes.</p>
	 *  
	 * @param arity	Arity of the default projection indexes.
	 * @return		The default projection indexes.
	 */
	public static int[] getInitIndexes(int arity) {
		int[] i = new int[arity];
		for (int j = 0; j < arity; j++){
			i[j] = -1;
		}
		return i;
	}
}
