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

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.operations.tuple.SelectionComparator;
import org.deri.iris.operations.tuple.SelectionFullComparator;
import org.deri.iris.storage.Relation;

/**
 * Implementation of the ISelection interface
 * 
 * The Selection operation is meant to be used for selecting a portion of
 * a relation (tree). Basically the functionality of this operation is to
 * select all tuples, from a relation, that are equal regarding the 
 * condition defined by a certain pattern (tuple).
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   31.05.2006 10:38:40
 */
public class Selection implements ISelection{
	private IRelation relation = null;
	private ITuple pattern = null;
	private int[] indexes = null;
	
	Selection(IRelation relation, ITuple pattern){
		if (relation == null || pattern == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		if (relation.size() == 0) {
			throw new IllegalArgumentException("Cannot do selection on " +
					"an empty realtion!");
		}
		this.relation = relation;
		this.pattern = pattern;
	}
	
	Selection(IRelation relation, int[] indexes){
		if (relation == null || indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		if (relation.size() == 0) {
			throw new IllegalArgumentException("Cannot do selection on " +
					"an empty realtion!");
		}
		this.relation = relation;
		this.indexes = indexes;
	}
	
	Selection(IRelation relation, ITuple pattern, int[] indexes){
		if (relation == null || pattern == null || indexes == null) {
			throw new IllegalArgumentException("All constructor " +
					"parameters must not be specified (non null values");
		}
		if (relation.size() == 0) {
			throw new IllegalArgumentException("Cannot do selection on " +
					"an empty realtion!");
		}
		this.relation = relation;
		this.pattern = pattern;
		this.indexes = indexes;
	}
	
	public IRelation select() {
		if(pattern != null && indexes == null)
			return select0();
		if(pattern == null && indexes != null)
			return select1();
		else
			return select2();
	}
	
	public IRelation select0() {
		// Sort relation on tupples defined by the pattern
		int[] indexes = this.getIndexes(this.pattern);
		SelectionFullComparator comparator = new SelectionFullComparator(indexes);
		IRelation rel = new Relation(comparator);
		
		rel.add(pattern);
		rel.addAll(this.relation);
		if(! isValid(pattern)) 
			rel.remove(pattern);
		
		return rel;
	}
	
	public IRelation select1() {
		// Sort relation on tupples defined by the indexes
		SelectionComparator comparator = new SelectionComparator(this.indexes);
		IRelation rel = new Relation(comparator);
		IRelation relTrash = new Relation(comparator);
		
		Iterator i = this.relation.iterator();
		ITuple t = null;
		while(i.hasNext()){
			t = (ITuple)i.next();
			if(comparator.getQuota() != comparator.checkTerms(t))
				relTrash.add(t);
			else
				break;
		}
		this.relation.removeAll(relTrash);
		rel.addAll(this.relation);
		
		return rel;
	}
	
	public IRelation select2() {
		this.relation = select1();
		IRelation rel = select0();
		
		return rel;
	}
	
	private int[] getIndexes(ITuple pattern){
		Map<ITerm, Integer> termSet = new HashMap<ITerm, Integer>();
		int[] indexes = new int[pattern.getArity()];
		List<ITerm> terms = pattern.getTerms();
		int i = 0;
		ITerm t = null;
		
		for(int j=0; j<terms.size(); j++){
			t = terms.get(j);
			if(t != null){
				if(t.getValue() != null){
					if(termSet.containsKey(t))
						indexes[j] = termSet.get(t);
					else{
						termSet.put(t, ++i);
						indexes[j] = i;
					}
				}else
					indexes[j] = -1;
			}else
				indexes[j] = -1;
		}
		return indexes;
	}
	
	private boolean isValid(ITuple tup){
		List<ITerm> terms = tup.getTerms();
		for(ITerm t : terms){
			if(t == null || t.getValue() == null)
				return false;
		}
		return true;
	}
	
}
