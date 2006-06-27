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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.MinimalTuple;
import org.deri.iris.operations.tuple.BasicComparator;
import org.deri.iris.operations.tuple.Concatenation;
import org.deri.iris.operations.tuple.IndexComparator;
import org.deri.iris.operations.tuple.JoinComparator;
import org.deri.iris.storage.Relation;

/**
 * Implementation of the sort-merge join operation handling duplicates. 
 * Duplicate tuples are those tuples that have identical terms on 
 * positions defined by sort indexes.  Reference: Join Processing in 
 * Relational Databases, PRITI MISHRA and MARGARET H. EICH
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.05.2006 09:26:43
 */
public class Join implements IJoin{
	private IRelation relation0 = null;
	private IRelation relation1 = null;
	private IRelation joinRelation = null;
	private BasicComparator comparator = null;
	private int[] indexes = null;
	private int[] projectIndexes = null;
	private JoinCondition condition = null;
	
	Join(IRelation arg0, IRelation arg1, int[] indexes){
		if (arg0 == null || arg1 == null || indexes == null) {
			throw new IllegalArgumentException("All construcotr " +
				"parameters must not be specified (non null values");
		}
		constructJoinOperator(arg0, arg1, indexes);
		this.condition = JoinCondition.EQUALS; 
	}
	
	Join(IRelation arg0, IRelation arg1, int[] indexes, 
			JoinCondition condition){
		if (arg0 == null || arg1 == null || 
				indexes == null || condition == null) {
			throw new IllegalArgumentException("All construcotr " +
			"parameters must not be specified (non null values");
		}
		constructJoinOperator(arg0, arg1, indexes);
		this.condition = condition; 
	}
	
	Join(IRelation arg0, IRelation arg1, int[] indexes, 
			JoinCondition condition, int[] projectIndexes){
		if (arg0 == null || arg1 == null || 
				indexes == null || condition == null) {
			throw new IllegalArgumentException("Construcotr " +
			"parameters are not specified correctly");
		}
		this.projectIndexes = projectIndexes;
		constructJoinOperator(arg0, arg1, indexes);
		this.condition = condition; 
	}
	
	private void constructJoinOperator(IRelation arg0, IRelation arg1, int[] indexes){
		this.relation0 = arg0;
		this.relation1 = arg1;
		this.indexes = indexes;
		
		// Sort arg0 on those tupples defined by sort indexes
		this.comparator = new IndexComparator(this.indexTransformer0(indexes));
		IRelation rel0 = new Relation(comparator);
		rel0.addAll(this.relation0);
		this.relation0 = rel0;
		
		// Sort arg1 on those tupples defined by sort indexes
		this.comparator = new IndexComparator(this.indexTransformer1(indexes));
		IRelation rel1 = new Relation(comparator);
		rel1.addAll(this.relation1);
		this.relation1 = rel1;
		/*
		 * If not specified join tuples will be simple merged.
		 */
		if(this.projectIndexes == null){
			this.projectIndexes = this.indexTransformer0(
					new int[this.indexes.length*2]);
		}
		this.joinRelation = new Relation(this.projectIndexes.length);
	}
	
	@SuppressWarnings("unchecked")
	public IRelation join() {
		if (((ITuple)relation0.first()).getArity() != ((ITuple)relation1.first()).getArity()) {
			throw new IllegalArgumentException("Couldn't join due to different arity of tuples.");
		} 
		Iterator<ITuple> iterator;
		ITuple tuple;
		boolean order = false;
		if(this.relation0.size() < this.relation1.size()){
			order = true;
			iterator = this.relation0.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(selectAndJoin(tuple, order));
			}
		}else{
			iterator = this.relation1.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(selectAndJoin(tuple, order));
			}
		}
		return joinRelation;
	}
	
	/**
	 * Select portion of the tree that is relevant for ongoing join operation
	 * regarding the tuple and perform the join operation using a helper
	 * method checkAndJoin0 or checkAndJoin1 (depending whether 
	 * relations0/relations1 is an outer/inner relation or vise versa).
	 * 
	 * @param tuple
	 * @param order
	 * @return
	 */
	private IRelation selectAndJoin(ITuple tuple, boolean order){
		if(relation1.first() == null) return null;
		IRelation joinElements = 
			new Relation(((ITuple)relation1.first()).getArity()*2);
		SortedSet subSet = null;
		ITuple transformedTuple;
		if(order){
			transformedTuple = this.getMinimalTupleValue1(tuple, indexes);
			subSet = relation1.tailSet(transformedTuple);
			
			return checkAndJoin1(tuple, subSet);
		}else{
			transformedTuple = this.getMinimalTupleValue0(tuple, indexes);
			subSet = relation0.tailSet(transformedTuple);
			
			return checkAndJoin0(tuple, subSet);
		}
	}
	
	/**
	 * Check which tuples from the selected portion of the tree (relation0) satisfies
	 * the join condition and concatenate them with the tuple.
	 * 
	 * @param tuple
	 * @param subSet
	 * @return
	 */
	private IRelation checkAndJoin0(ITuple tuple, SortedSet subSet){
		IRelation joinElements = 
			new Relation(((ITuple)relation1.first()).getArity()*2);
		
		Concatenation concatenator = new Concatenation();
		ITuple tmpTuple, copyTuple, copyTmpTuple, concatenatedTuple = null;
		Iterator<ITuple> iterator = subSet.iterator(); 
		JoinComparator comparator = new JoinComparator(this.indexes);
		boolean checkNext = true;
		int comparison = 0;
		
		while(checkNext && iterator.hasNext()){
			tmpTuple = iterator.next();
			copyTuple = tuple;
			copyTmpTuple = tmpTuple;
			comparison = comparator.compare(copyTmpTuple, copyTuple);
			
			switch(isConditionSatisfied(comparison)){
				case 0:
					// create joinTuple = tuple + tmpTuple
					while(copyTuple != null){	
						while(copyTmpTuple != null){
							if(!(copyTmpTuple instanceof MinimalTuple)){
								concatenatedTuple = concatenator.concatenate(copyTmpTuple, copyTuple);
								joinElements.add(concatenatedTuple);
							}
							copyTmpTuple = copyTmpTuple.getDuplicate();
						}
						copyTuple = copyTuple.getDuplicate();
						copyTmpTuple = tmpTuple;
					}
					break;
				default:
					checkNext = false;
				break;
			}
		}
		
		return joinElements;
	}
	
	/**
	 * Check which tuples from the selected portion of the tree (relation1) satisfies
	 * the join condition and concatenate them with the tuple.
	 * 
	 * @param tuple
	 * @param subSet
	 * @return
	 */
	private IRelation checkAndJoin1(ITuple tuple, SortedSet subSet){
		IRelation joinElements = 
			new Relation(((ITuple)relation1.first()).getArity()*2);
		
		Concatenation concatenator = new Concatenation();
		ITuple tmpTuple, copyTuple, copyTmpTuple, concatenatedTuple = null;
		Iterator<ITuple> iterator = subSet.iterator(); 
		JoinComparator comparator = new JoinComparator(this.indexes);
		boolean checkNext = true;
		int comparison = 0;
		
		while(checkNext && iterator.hasNext()){
			tmpTuple = iterator.next();
			copyTuple = tuple;
			copyTmpTuple = tmpTuple;
			comparison = comparator.compare(copyTuple, copyTmpTuple);
			
			switch(isConditionSatisfied(comparison)){
				case 0:
					// create joinTuple = tuple + tmpTuple
					while(copyTuple != null){
						while(copyTmpTuple != null){
							if(!(copyTmpTuple instanceof MinimalTuple)){
								concatenatedTuple = concatenator.concatenate(copyTuple, copyTmpTuple);
								joinElements.add(concatenatedTuple);
							}
							copyTmpTuple = copyTmpTuple.getDuplicate();
						}
						copyTuple = copyTuple.getDuplicate();
						copyTmpTuple = tmpTuple;
					}
					break;
				default:
					checkNext = false;
				break;
			}
		}
		
		return joinElements;
	}
	
	/**
	 * if boolean order = true then 
	 * isConditionSatisfied = 
	 *  0	    means join condition is satisfied for the two compared tupels
	 *  -1 or 1	means join condition is not satisfied for the two compared tupels
	 *  
	 * @param comparison
	 * @return
	 */
	private int isConditionSatisfied(int comparison){
		switch (condition){
			case EQUALS:
				if(comparison == 0) return 0;
				if(comparison < 0) return -1;
				break;
			case NOT_EQUAL:
				if(comparison != 0) return 0;
				//if(comparison < 0)return -1;
				break;
			case LESS_THAN:
				if(comparison < 0) return 0;
				if(comparison > 0) return -1;
				break;
			case GREATER_THAN:
				if(comparison > 0) return 0;
				if(comparison < 0) return -1;
				break;
			case LESS_OR_EQUAL:
				if(comparison < 0 || comparison == 0) return 0;
				if(comparison > 0) return -1;
				break;
			case GREATER_OR_EQUAL:
				if(comparison > 0 || comparison == 0) return 0;
				if(comparison < 0) return -1;
				break;
		}
		return 1;
	}
	
	/**
	 * Transforms indexes according to the following examples:
		[-1,-1, 0] to [-1,-1, 2]
		[-1, 1, 0] to [-1, 1, 2]
		[ 2, 1, 0] to [ 0, 1, 2]
		
	 * @param indexes
	 * @return transformed indexes
	 */
	private int[] indexTransformer0(int[] indexes){
		int[] tranformedIndexes = new int [indexes.length];
		for(int i=0; i<indexes.length; i++){
			if(indexes[i] != -1)tranformedIndexes[i]= i;
			else tranformedIndexes[i] = -1;
		}
		return tranformedIndexes;
	}
	
	/**
	 * Transforms indexes according to the following examples:
		[-1,-1, 0] to [0,-1,-1]
		[-1, 1, 0] to [0, 1,-1]
		[ 2, 1, 0] to [0, 1, 2]

	 * @param indexes
	 * @return transformed indexes
	 */
	private int[] indexTransformer1(int[] indexes){
		int[] tranformedIndexes = new int [indexes.length];
		for(int i=0; i<indexes.length; i++){
			tranformedIndexes[i] = -1;
		}
		for(int i=0; i<indexes.length; i++){
			if(indexes[i] != -1){
				tranformedIndexes[indexes[i]]=indexes[i];
			}
		}
		return tranformedIndexes;
	}
	
	private ITuple getMinimalTupleValue0(ITuple tuple, int[] indexes){
		ITerm[] terms = new ITerm[tuple.getArity()];
		List<ITerm> termList = new LinkedList();
		for(int i=0; i<indexes.length; i++){
			if(indexes[i] != -1)
				terms[i] = tuple.getTerm(indexes[i]);
		}		
		for(int j=0; j<indexes.length; j++){
			if(terms[j] == null)
				terms[j] = tuple.getTerm(j).getMinValue();
		}
		return BASIC.createMinimalTuple(terms);
	}
	
	private ITuple getMinimalTupleValue1(ITuple tuple, int[] indexes){
		ITerm[] terms = new ITerm[tuple.getArity()];
		List<ITerm> termList = new LinkedList();
		for(int i=0; i<indexes.length; i++){
			if(indexes[i] != -1)
				terms[indexes[i]] = tuple.getTerm(i);
		}		
		for(int j=0; j<indexes.length; j++){
			if(terms[j] == null)
				terms[j] = tuple.getTerm(j).getMinValue();
		}
		return BASIC.createMinimalTuple(terms);
	}
}

