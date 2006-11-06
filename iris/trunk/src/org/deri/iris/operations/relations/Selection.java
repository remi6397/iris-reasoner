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
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.basics.seminaive.NonEqualityTerm;
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
	private ITuple equalityTuple = null;
	private ITuple nonEqualityTuple = null;
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
		if(this.pattern != null) processPattern();
		
		/*	Possibilities:
		  * 
		  * 	?X  = 'a', ?Y  = 'b', ...
		  * 	?X != ?Y,  ?Z  = ?W, ...
		  * 	?X != ?Y,  ?Z  = ?W, ?X  = 'a', ?Y  = 'b', ...
		  * 	?X != 'a', ?Y  = 'b', ...
		  * 	?X != ?Y,  ?Z  = ?W, ?X != 'a', ?Y  = 'b'
		  * 
		  */ 
		if(equalityTuple != null && nonEqualityTuple == null && indexes == null)
			return select0();
		if(equalityTuple == null && nonEqualityTuple == null && indexes != null)
			return select1();
		if(equalityTuple != null && nonEqualityTuple == null && indexes != null)
			return select2();
		if(equalityTuple != null && nonEqualityTuple != null && indexes == null)
			return select3();
		if(equalityTuple != null && nonEqualityTuple != null && indexes != null)
			return select4();
		return null;
	}
	
	/**
	 * Case: ?X  = 'a', ?Y  = 'b'
	 *
	 * @return Selected tuples based on a given condition
	 */
	public IRelation select0() {
		// Sort relation on tupples defined by the pattern
		int[] indexes = this.getIndexes(this.equalityTuple);
		SelectionFullComparator comparator = new SelectionFullComparator(indexes);
		IRelation rel = new Relation(comparator);
		
		rel.add(this.equalityTuple);
		rel.addAll(this.relation);
		if(! isValid(this.equalityTuple)) 
			rel.remove(this.equalityTuple);
		
		return rel;
	}
	
	/**
	 * Case: ?X != ?Y,  ?Z  = ?W
	 *
	 * @return Selected tuples based on a given condition
	 */
	public IRelation select1() {
		// Sort relation on tupples defined by the indexes
		SelectionComparator comparator = new SelectionComparator(this.indexes);
		IRelation rel = new Relation(comparator);
		Set<ITuple> relTrash = new HashSet<ITuple>();
		
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
	
	/**
	 * Case: ?X != ?Y,  ?Z  = ?W, ?X  = 'a', ?Y  = 'b'
	 *
	 * @return Selected tuples based on a given condition
	 */
	public IRelation select2() {
		this.relation = select1();
		IRelation rel = select0();
		
		return rel;
	}
	
	/**
	 * Case: ?X != 'a', ?Y  = 'b'
	 * 
	 * TODO: select3() is expencive operation - try
	 * to implement it more efficiently!
	 *  
	 * Namely selection0() is called as many times as
	 * a condition pattern contains non equality terms.
	 *
	 * @return Selected tuples based on a given condition
	 */
	public IRelation select3() {
		this.relation = select0();
		ITuple tup = null;
		IRelation rel = new Relation(this.relation.getArity());
		List<ITerm> l = this.nonEqualityTuple.getTerms();
		ITerm t = null;
		
		for(int i=0; i<l.size(); i++){
			t = l.get(i);
			if(t != null){
				tup = nextTuple(i, this.relation.getArity(), t);
				rel = select0(tup, this.relation);
				this.relation.removeAll(rel);
			}
		}
		
		return this.relation;
	}
	
	public IRelation select0(ITuple eqTuple, IRelation r) {
		// Sort relation on tupples defined by the pattern
		int[] indexes = this.getIndexes(eqTuple);
		SelectionFullComparator comparator = new SelectionFullComparator(indexes);
		IRelation rel = new Relation(comparator);
		
		rel.add(eqTuple);
		rel.addAll(r);
		if(! isValid(eqTuple)) 
			rel.remove(eqTuple);
		
		return rel;
	}
	
	/**
	 * Case: ?X != ?Y,  ?Z  = ?W, ?X != 'a', ?Y  = 'b'
	 *
	 * @return Selected tuples based on a given condition
	 */
	public IRelation select4() {
		this.relation = select1();
		IRelation rel = select3();
		
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
					indexes[j] = 0;
			}else
				indexes[j] = 0;
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

	private void processPattern(){
		List<ITerm> terms = this.pattern.getTerms();
		List<ITerm> eqTerms = new ArrayList<ITerm>();
		List<ITerm> neqTerms = new ArrayList<ITerm>();
		boolean equalTermExist = false;
		boolean nonEqualTermExist = false;
		
		for(ITerm t : terms){
			if(t instanceof NonEqualityTerm){
				neqTerms.add(t);
				eqTerms.add(null);
				if(t != null && t.getValue() != null)
					nonEqualTermExist = true;
			}else{
				neqTerms.add(null);
				eqTerms.add(t);
				if(t != null && t.getValue() != null)
					equalTermExist = true;
			}
		}
		if(equalTermExist)
			this.equalityTuple = BASIC.createTuple(eqTerms);
		else
			this.equalityTuple = null;
		
		if(nonEqualTermExist)
			this.nonEqualityTuple = BASIC.createTuple(neqTerms);
		else
			this.nonEqualityTuple = null;
	}
	
	private ITuple nextTuple(final int i, final int a, final ITerm t){
		List<ITerm> terms = new ArrayList<ITerm>(a);
		NonEqualityTerm t0 = (NonEqualityTerm)t;
		ITerm t1 = t0.getTerm();
		
		for(int j=0; j<a; j++){
			if(j != i)
				terms.add(null);
			else
				terms.add(i, t1);
		}
		return BASIC.createTuple(terms);
	}
}
