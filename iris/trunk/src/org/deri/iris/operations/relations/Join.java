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
import java.util.LinkedList;
import java.util.List;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.factory.Factory;
import org.deri.iris.operations.tuple.BasicComparator;
import org.deri.iris.operations.tuple.Concatenation;
import org.deri.iris.operations.tuple.IndexComparator;
import org.deri.iris.operations.tuple.JoinComparator;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.05.2006 09:26:43
 */
public class Join implements IJoin{
	
	private IRelation relation0 = null;
	private IRelation relation1 = null;
	private BasicComparator comparator = null;
	private int[] indexes = null;
	
	private JoinCondition condition = JoinCondition.EQUALS;
	
	public IRelation join(IRelation relation0, IRelation relation1, int[] indexes, JoinCondition condition) {
		this.condition = condition;
		this.indexes = indexes;
		return join(relation0, relation1);
	}
	
	public IRelation equiJoin(IRelation arg0, IRelation arg1, int[] indexes) {
		this.condition = JoinCondition.EQUALS;
		this.indexes = indexes;
		return join(relation0, relation1);
	}
	
	@SuppressWarnings("unchecked")
	private IRelation join(IRelation relation0, IRelation relation1) {
		if (((ITuple)relation0.first()).getArity() != ((ITuple)relation1.first()).getArity()) {
			throw new IllegalArgumentException("Couldn't join due to different arity of tuples.");
		} 
		IRelation joinRelation = new Relation(this.indexes.length*2);
		
		// Sort relation0 on those tupples defined by sort indexes
		this.comparator = new IndexComparator(this.indexTransformer0(indexes));
		this.relation0 = new Relation(comparator);
		this.relation0.addAll(relation0);
	
		// Sort relation1 on those tupples defined by sort indexes
		this.comparator = new IndexComparator(this.indexTransformer1(indexes));
		this.relation1 = new Relation(comparator);
		this.relation1.addAll(relation1);
		
		Iterator<ITuple> iterator;
		ITuple tuple;
		boolean order = false;
		
		if(this.relation0.size() < this.relation1.size()){
			order = true;
			iterator = this.relation0.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(findAndJoin(tuple, order));
			}
		}else{
			iterator = this.relation1.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(findAndJoin(tuple, order));
			}
		}
			
		return joinRelation;
	}
	
	@SuppressWarnings("unchecked")
	private IRelation findAndJoin(ITuple tuple, boolean order){
		
		if(relation1.first() == null) return null;
		IRelation joinElements = 
			new Relation(((ITuple)relation1.first()).getArity()*2);
		Concatenation concatenator = new Concatenation();
		SortedSet subSet = null;
		
		ITuple transformedTuple;
		if(order){
			transformedTuple = this.transformTuple1(tuple, indexes);
			subSet = relation1.tailSet(transformedTuple);
		}else{
			transformedTuple = this.transformTuple0(tuple, indexes);
			subSet = relation0.tailSet(transformedTuple);
		}
		
		ITuple tmpTuple, concatenatedTuple = null;
		
		/*
		 * No duplicates handled
		*/
		Iterator<ITuple> iterator = subSet.iterator(); 
		JoinComparator comparator = new JoinComparator(this.indexes);
		boolean checkNext = true;
		int comparison = 0;
		
		while(checkNext && iterator.hasNext()){
			tmpTuple = iterator.next();
			if(order){
				comparison = comparator.compare(tuple, tmpTuple);
			}else{
				comparison = comparator.compare(tmpTuple, tuple);
			}
			switch(isConditionSatisfied(comparison)){
				case -1:
					if(order)checkNext = false;
					break;
				case 0:
					// create joinTuple = tuple + tmpTuple
					if(order){
						concatenatedTuple = concatenator.concatenate(tuple, tmpTuple);
					}else{
						concatenatedTuple = concatenator.concatenate(tmpTuple, tuple);
					}
					joinElements.add(concatenatedTuple);
					break;
				case 1:
					if(!order)checkNext = false;
					break;
			}
		}
		
		/*
		 * Duplicates handling
		*/
		/*ITuple joinTuple = (ITuple)subSet.first();
		while(joinTuple != null){
			// Correct it! (Carefull)
			if(!joinTuple.equals(transformedTuple)){
				concatenatedTuple = concatenator.concatenate(tuple, joinTuple);
			}
			joinElements.add(concatenatedTuple);
			joinTuple = joinTuple.getDuplicate();
		}*/
		
		return joinElements;
	}
	
	/*
	 * if boolean order = true then 
	 * isConditionSatisfied = 
	 * -1	means relevant part of the tree is handled, stop further checking
	 *  0	means join condition is satisfied for the two compared tupels
	 *  1	means join condition is not satisfied for the two compared tupels
	 *  
	 * if boolean order = flase then
	 * isConditionSatisfied = 
	 *  -1	means join condition is not satisfied for the two compared tupels
	 *  0	means join condition is satisfied for the two compared tupels
	 *  1	means relevant part of the tree is handled, stop further checking
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
	
	private int[] indexTransformer0(int[] indexes){
		int[] tranformedIndexes = new int [indexes.length];
		for(int i=0; i<indexes.length; i++){
			if(indexes[i] != -1)tranformedIndexes[i]= i;
			else tranformedIndexes[i] = -1;
		}
		return tranformedIndexes;
	}
	
	private int[] indexTransformer1(int[] indexes){
		int[] tranformedIndexes = new int [indexes.length];
		for(int i=0; i<indexes.length; i++){
			if(indexes[i] != -1){
				tranformedIndexes[indexes[i]]=indexes[i];
				tranformedIndexes[i] = -1;
			}else if(tranformedIndexes[i] == 0)
				tranformedIndexes[i] = -1;
		}
		return tranformedIndexes;
	}
	
	// Refactor it!
	private ITuple transformTuple0(ITuple tuple, int[] indexes){
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
		for(int i=0; i<tuple.getArity(); i++)
			termList.add(terms[i]);
		return Factory.BASIC.createTuple(termList);
	}
	
	// Refactor it!
	private ITuple transformTuple1(ITuple tuple, int[] indexes){
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
		for(int i=0; i<tuple.getArity(); i++)
			termList.add(terms[i]);
		return Factory.BASIC.createTuple(termList);
	}
}

