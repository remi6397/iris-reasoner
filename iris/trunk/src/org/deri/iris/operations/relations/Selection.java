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
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.IStringTerm;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.MinimalTuple;
import org.deri.iris.operations.tuple.IndexComparator;
import org.deri.iris.operations.tuple.SelectionComparator;
import org.deri.iris.storage.Relation;

/**
 * Implementation of the Selection operation meant to be used for 
 * selecting a portion of a relation (tree). Basically the functionality of 
 * this operation is to select all tuples, from a relation, that are equal
 * regarding the condition defined by a certain pattern (tuple).
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 10:38:40
 */
public class Selection implements ISelection{
	private IRelation relation = null;
	private ITuple pattern = null;
	private IndexComparator indexComparator = null;
	private SelectionComparator selectionComparator = null;
	
	public IRelation select(IRelation relation, ITuple pattern) {
		IRelation selectRelation = 
			new Relation(((ITuple)relation.first()).getArity());
		
		this.pattern = pattern;
		
		// Sort relation on tupples defined by the pattern
		//this.comparator = new SelectionComparator(createIndexes(this.pattern));
		
		int[] indexes = this.getIndexes(this.pattern);
		this.indexComparator = new IndexComparator(indexes);
		this.relation = new Relation(this.indexComparator);
		this.relation.addAll(relation);
		
		ITuple transformedTuple = this.getMinimalTupleValue(pattern, indexes);
		SortedSet set = relation.tailSet(pattern);
		
		Iterator<ITuple> iterator = set.iterator();
		ITuple tuple;
		selectionComparator = new SelectionComparator(indexes);
		while(iterator.hasNext()){
			tuple = iterator.next();
			if(selectionComparator.compare(tuple, transformedTuple) == 0){
				selectRelation.add(tuple);
			}else{
				return selectRelation;
			}	
		}
		return null;
	}

	private int[]getIndexes(ITuple pattern){
		int[] indexes = new int[pattern.getArity()];
		List<ITerm> terms = pattern.getTerms();
		Iterator<ITerm> i = terms.iterator();
		int j=0;
		ITerm term;
		while(i.hasNext()){
			term = i.next();
			if(term.getValue() == null)
				indexes[j] = -1;
			else
				indexes[j] = j;
			j++;
		}
		return indexes;
	}
	
	// Correct it! (create a helper class and merge this method with one used in the Join)
	private ITuple getMinimalTupleValue(ITuple tuple, int[] indexes){
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
		
		return new MinimalTuple(termList);
	}
}
