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

import java.util.Iterator;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.operations.tuple.BasicComparator;
import org.deri.iris.operations.tuple.Concatenation;
import org.deri.iris.operations.tuple.IndexComparator;
import org.deri.iris.storage.Relation;

/**
 * Implementation of the sort-merge join operation handling duplicates.
 * Duplicate tuples are those tuples that have identical terms on positions
 * defined by sort indexes. Reference: JoinSimpleExtended Processing in Relational Databases,
 * PRITI MISHRA and MARGARET H. EICH
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 21.09.2006 11:55:43
 */
public class Join implements IJoin{

	private IRelation relation0 = null;

	private IRelation relation1 = null;

	private IRelation joinRelation = null;

	private BasicComparator comparator = null;

	private int[] indexes = null;

	private int[] projectIndexes = null;

	private JoinCondition condition = null;

	Join(IRelation arg0, IRelation arg1, int[] indexes) {
		if (arg0 == null || arg1 == null || indexes == null) {
			throw new IllegalArgumentException(
					"Input parameters must not be null");
		}
		int[] i = null;
		int[] j = null;
		this.indexes = indexes;
		i = this.transformIndexes0(this.indexes);
		j = this.transformIndexes1(this.indexes);
		if(! checkIndexes(i, ((ITuple)arg0.first()).getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		if(! checkIndexes(j, ((ITuple)arg1.first()).getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		
		setJoinOperator(arg0, i, arg1, j);
		this.condition = JoinCondition.EQUALS;
	}

	Join(IRelation arg0, IRelation arg1, int[] indexes,
			JoinCondition condition) {
		if (arg0 == null || arg1 == null || indexes == null
				|| condition == null) {
			throw new IllegalArgumentException("All constructor "
					+ "parameters must not be specified (non null values");
		}
		int[] i = null;
		int[] j = null;
		this.indexes = indexes;
		i = this.transformIndexes0(this.indexes);
		j = this.transformIndexes1(this.indexes);
		if(! checkIndexes(i, ((ITuple)arg0.first()).getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		if(! checkIndexes(j, ((ITuple)arg1.first()).getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		
		setJoinOperator(arg0, i, arg1, j);
		this.condition = condition;
	}

	/**
	 * @param arg0
	 * @param arg1
	 * @param indexes
	 * @param condition
	 * @param projectIndexes
	 *            define indexes which the projection operation will be applied
	 *            on. For example, if set of tuples of arity 3 needs to be
	 *            projected on the first and last term, the projectIndexes will
	 *            look like: [1, -1, 1]. -1 means that terms with that index
	 *            will be omitted. Note that an equivalent array for the project
	 *            indexes could be also: [0, -1, 2], in which case the array
	 *            values represent the term indexes in a tuple. If not specified
	 *            join tuples will be simple merged.
	 */
	Join(IRelation arg0, IRelation arg1, int[] indexes,
			JoinCondition condition, int[] projectIndexes) {
		if (arg0 == null || arg1 == null || indexes == null
				|| condition == null) {
			throw new IllegalArgumentException("Constructor "
					+ "parameters are not specified correctly");
		}
		int[] i = null;
		int[] j = null;
		this.indexes = indexes;
		i = this.transformIndexes0(this.indexes);
		j = this.transformIndexes1(this.indexes);
		if(! checkIndexes(i, ((ITuple)arg0.first()).getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		if(! checkIndexes(j, ((ITuple)arg1.first()).getArity()))
			throw new IllegalArgumentException("Indexes are not specified" +
					" correctly.");
		
		this.projectIndexes = projectIndexes;
		setJoinOperator(arg0, i, arg1, j);
		this.condition = condition;
	}

	private void setJoinOperator(IRelation arg0, int[] inds0, IRelation arg1, int[] inds1) {
		this.relation0 = arg0;
		this.relation1 = arg1;
		
		// Sort arg0 on those tupples defined by sort indexes
		this.comparator = new IndexComparator(inds0);
		IRelation rel0 = new Relation(comparator);
		rel0.addAll(this.relation0);
		this.relation0 = rel0;
		
		// Sort arg1 on those tupples defined by sort indexes
		this.comparator = new IndexComparator(inds1);
		IRelation rel1 = new Relation(comparator);
		rel1.addAll(this.relation1);
		this.relation1 = rel1;
		
		/*
		 * If project indexes are not specified, join tuples will be simple
		 * merged.
		 */
		if (this.projectIndexes == null) {
			this.projectIndexes = this
			.transformIndexes0(new int[((ITuple)this.relation0.first()).getArity() + 
			                           ((ITuple)this.relation1.first()).getArity()]);
		}
		this.joinRelation = new Relation(this.getRelationArity());
	}

	public IRelation join() {
		Concatenation concatenator = new Concatenation();
		Iterator<ITuple> it0, it1;
		ITuple t1, t0, concatenatedTuple, copyTuple0 = null, copyTuple1 = null, copyTuple2 = null;
		int comp = 0;
		
		it0 = this.relation0.iterator();
		it1 = this.relation1.iterator();
		t1 = it1.next();
		
		while(it0.hasNext()){
			t0 = (ITuple) it0.next();
			while((comp = checkJointness(t0, t1)) >= 0){
				if(comp == 0){
					copyTuple1 = t1;
					copyTuple2 = copyTuple1;
					while(t0 != null){
						while(copyTuple1 != null){
							concatenatedTuple = concatenator.concatenate(
									t0, copyTuple1, this.projectIndexes);
							
							this.joinRelation.add(concatenatedTuple);
							copyTuple1 = copyTuple1.getDuplicate();
						}
						t0 = t0.getDuplicate();
						if(t0 != null) copyTuple1 = copyTuple2;
					}
				}
				if(comp > 0){
					if(it1.hasNext()){
						t1 = (ITuple) it1.next();
					}	
					else
						t1 = null;
				}
				if(t1 == null)
					break;
			}
			if(t1 == null)
				break;
		}
		return joinRelation;
	}
	
	/**
	 * Transforms indexes according to the following examples: 
	 * [-1,-1, 0] to [-1,-1, 2]; 
	 * [-1, 1, 0] to [-1, 1, 2];
	 * [ 2, 1, 0] to [ 0, 1, 2].
	 * 
	 * @param indexes
	 * @return transformed indexes
	 */
	private int[] transformIndexes0(int[] indexes) {
		int[] tranformedIndexes = new int[indexes.length];
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1)
				tranformedIndexes[i] = i;
			else
				tranformedIndexes[i] = -1;
		}
		return tranformedIndexes;
	}

	/**
	 * Transforms indexes according to the following examples: [-1,-1, 0] to
	 * [0,-1,-1] [-1, 1, 0] to [0, 1,-1] [ 2, 1, 0] to [0, 1, 2]
	 * 
	 * @param indexes
	 * @return transformed indexes
	 */
	private int[] transformIndexes1(int[] indexes) {
		int[] tranformedIndexes = new int[indexes.length];
		for (int i = 0; i < indexes.length; i++) {
			tranformedIndexes[i] = -1;
		}
		for (int i = 0; i < indexes.length; i++) {
			if (indexes[i] != -1) {
				tranformedIndexes[indexes[i]] = indexes[i];
			}
		}
		return tranformedIndexes;
	}
	
	private int getRelationArity(){
		int j=0;
		for(int i=0; i<this.projectIndexes.length; i++){
			if(this.projectIndexes[i] != -1) j++;
		}
		return j;
	}

	/**
	 * @param inds indexes
	 * @param a arity
	 * @return true for consistant indexes (each index is 
	 * 	not bigger than the relation arity), otherwise false
	 */
	private boolean checkIndexes(int[] inds, int a){
		int j=0;
		for(int i=0; i<inds.length; i++){
			if(inds[i]+1 > a) return false;
		}
		return true;
	}
	
	private int checkJointness(ITuple t0, ITuple t1){
		int[] indexes = this.indexes;
		int comp = 0;
		
		if(t0 != null && t1 != null){
			for(int i=0; i<indexes.length; i++){
				if(indexes[i] != -1){
					comp = t0.getTerm(i).compareTo(t1.getTerm(indexes[i]));
					if(comp != 0) 	
						return comp;
				}
			}
			return 0;
		}
		if(t0 != null && t1 == null)
			return 1;
		if(t0 == null && t1 != null)
			return -1;
		
		return 0;
	}
}
