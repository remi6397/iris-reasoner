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

import java.util.Iterator;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.IJoin;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.operations.tuple.Concatenation;
import org.deri.iris.operations.tuple.TupleComparator;
import org.deri.iris.storage.Relation;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   24.05.2006 09:26:43
 */
public class Join implements IJoin{
	
	private IRelation relation0 = null;
	private IRelation relation1 = null;
	
	@SuppressWarnings("unchecked")
	public IRelation join(IRelation relation0, IRelation relation1, int[] indexes) {
		if (((ITuple)relation0.first()).getArity() != ((ITuple)relation1.first()).getArity()) {
			throw new IllegalArgumentException("Couldn't join due to different arity of tuples.");
		} 
		TupleComparator comparator = new TupleComparator(indexes);
		IRelation joinRelation = new Relation(indexes.length*2);
		
		if(!((TupleComparator)relation0.comparator()).getSortIndexes().equals(indexes)){
			this.relation0 = new Relation(comparator);
		}
		this.relation0.addAll(relation0);
		if(!((TupleComparator)relation1.comparator()).getSortIndexes().equals(indexes)){
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
				joinRelation.addAll(findAndJoin(relation1, tuple, indexes, order, comparator));
			}
		}else{
			iterator = relation1.iterator();
			while(iterator.hasNext()){
				tuple = iterator.next();
				joinRelation.addAll(findAndJoin(relation0, tuple, indexes, order, comparator));
			}
		}
			
		return joinRelation;
	}
	
	@SuppressWarnings("unchecked")
	private IRelation findAndJoin(IRelation relation, ITuple tuple, int[] indexes, 
			boolean order, TupleComparator comparator){
		
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
				if(comparator.joinCompare(tuple, tmpTuple) == 0){
					// create joinTuple = tuple + tmpTuple
					concatenatedTuple = concatenator.concatenate(tuple, tmpTuple);
					joinElements.add(concatenatedTuple);
				}
			}
			else{
				if(comparator.joinCompare(tmpTuple, tuple) == 0){
					// create joinTuple = tmpTuple + tuple
					concatenatedTuple = concatenator.concatenate(tmpTuple, tuple);
					joinElements.add(concatenatedTuple);
				}
			}
		}
		return joinElements;
	}
	
}
