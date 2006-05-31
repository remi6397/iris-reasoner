/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package org.deri.iris.operations.relations;

import java.util.Arrays;
import java.util.Iterator;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.operations.tuple.Concatenation;
import org.deri.iris.operations.tuple.TupleComparator;
import org.deri.iris.storage.Relation;

//TODO: indexes.length*2 should be replaced be r0.size() + r1.size() (what if
//joining a relation of arity 2 to a relation of arity 6?)
/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.05.2006 09:26:43
 */
public class Join implements IJoin{
	
	private IRelation relation0 = null;
	private IRelation relation1 = null;
	private TupleComparator comparator = null;
	private JoinCondition condition = JoinCondition.EQUALS;
	
	public IRelation join(IRelation relation0, IRelation relation1, int[] indexes, JoinCondition condition) {
		this.condition = condition;
		return join(relation0, relation1, indexes);
	}
	
	public IRelation equiJoin(IRelation arg0, IRelation arg1, int[] indexes) {
		this.condition = JoinCondition.EQUALS;
		return join(relation0, relation1, indexes);
	}
	
	@SuppressWarnings("unchecked")
	private IRelation join(IRelation relation0, IRelation relation1, int[] indexes) {
		if (((ITuple)relation0.first()).getArity() != ((ITuple)relation1.first()).getArity()) {
			throw new IllegalArgumentException("Couldn't join due to different arity of tuples.");
		} 
		this.comparator = new TupleComparator(indexes);
		IRelation joinRelation = new Relation(indexes.length*2);
		
		if(!Arrays.equals(((TupleComparator)relation0.comparator()).getSortIndexes(), indexes)){
			this.relation0 = new Relation(comparator);
		}
		this.relation0.addAll(relation0);
		if(!Arrays.equals(((TupleComparator)relation1.comparator()).getSortIndexes(), indexes)){
			this.relation1 = new Relation(comparator);
		}
		this.relation1.addAll(relation1);
		
		Iterator<ITuple> iterator;
		ITuple tuple;
		boolean order = false;
		
		if(relation0.size() < relation1.size()){
			order = true;
			iterator = relation0.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(findAndJoin(relation1, tuple, indexes, order));
			}
		}else{
			iterator = relation1.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(findAndJoin(relation0, tuple, indexes, order));
			}
		}
			
		return joinRelation;
	}
	
	@SuppressWarnings("unchecked")
	private IRelation findAndJoin(IRelation relation, ITuple tuple, int[] indexes, 
			boolean order){
		
		IRelation joinElements = new Relation(indexes.length*2);
		Concatenation concatenator = new Concatenation();
		SortedSet subSet = null;
		
		if(order){
			subSet = relation.tailSet(tuple);
		}else{
			subSet = relation.subSet(relation.first(), tuple);
		}
		Iterator<ITuple> iterator = subSet.iterator(); 
		ITuple tmpTuple, concatenatedTuple = null;
		
		while(iterator.hasNext()){
			tmpTuple = iterator.next();
			if(order){
				if(isConditionSatisfied(tuple, tmpTuple, this.condition)){
					//create joinTuple = tuple + tmpTuple
					concatenatedTuple = concatenator.concatenate(tuple, tmpTuple);
					joinElements.add(concatenatedTuple);
				}
			}
			else{
				if(isConditionSatisfied(tmpTuple, tuple, this.condition)){
					//create joinTuple = tuple + tmpTuple
					concatenatedTuple = concatenator.concatenate(tmpTuple, tuple);
					joinElements.add(concatenatedTuple);
				}
			}
		}
		return joinElements;
	}
	
	private boolean isConditionSatisfied(ITuple t0, ITuple t1, JoinCondition condition){
		switch (condition){
			case EQUALS:
				if(comparator.joinCompare(t0, t1) == 0)
					return true;
				break;
			case NOT_EQUAL:
				if(comparator.joinCompare(t0, t1) != 0)
					return true;
				break;
			case LESS_THAN:
				if(comparator.joinCompare(t0, t1) < 0)
					return true;
				break;
			case GREATER_THAN:
				if(comparator.joinCompare(t0, t1) > 0)
					return true;
				break;
			case LESS_OR_EQUAL:
				if(comparator.joinCompare(t0, t1) < 0 ||
						comparator.joinCompare(t0, t1) == 0)
					return true;
				break;
			case GREATER_OR_EQUAL:
				if(comparator.joinCompare(t0, t1) > 0 ||
						comparator.joinCompare(t0, t1) == 0)
					return true;
		}
		return false;
	}

}
