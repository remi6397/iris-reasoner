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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeMap;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.operations.relation.ISelection;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Implementation of the ISelection interface.
 * </p>
 * <p>
 * The Selection operation is meant to be used for selecting a portion of a
 * relation (tree). Basically the functionality of this operation is to select
 * all tuples, from a relation, that qualify w.r.t to the condition defined by a
 * certain pattern (tuple), selection indexes or both.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 11.06.2007 10:42:40
 */
public class GeneralSelection implements ISelection {

	/**
	 * <p>
	 * Selection mode, e.g.:
	 * </p>
	 * <ul>
	 * <li> INDEX: [1,2,1,2] (terms with index 0th and 2nd must be equal as well as
	 * those on 1st and 3rd position.) </li>
	 * <li> PATTERN: [null,'a',null,null] (second term in each tuple must be 'a').</li>
	 * <li> COMBO: A combination of the previous two cases.</li>
	 * </ul>
	 */
	public enum SelectionMode {
		INDEX, PATTERN, COMBO;
	}

	/** A relation on which the selection operation needs to be performed. */
	private IMixedDatatypeRelation relation = null;

	/**
	 * A collection of sub relations produced by partitioning the input relation
	 * by different data types. Each sub relation contains terms of the same
	 * data type in one column.
	 */
	private Collection<SortedSet<ITuple>> subRels = null;

	private ITuple pattern = null;

	private int[] indexes = null;

	private SelectionMode mode = null;

	public GeneralSelection(IMixedDatatypeRelation relation, ITuple pattern) {
		if (relation == null || pattern == null) {
			throw new IllegalArgumentException("All constructor "
					+ "parameters must be specified (non null values");
		}
		this.pattern = pattern;
		this.relation = relation;
		this.mode = SelectionMode.PATTERN;
	}

	public GeneralSelection(IMixedDatatypeRelation relation, int[] indexes) {
		if (relation == null || indexes == null) {
			throw new IllegalArgumentException("All constructor "
					+ "parameters must be specified (non null values");
		}
		this.indexes = indexes;
		this.relation = relation;
		this.mode = SelectionMode.INDEX;
	}

	public GeneralSelection(IMixedDatatypeRelation relation, ITuple pattern,
			int[] indexes) {
		if (relation == null || pattern == null || indexes == null) {
			throw new IllegalArgumentException("All constructor "
					+ "parameters must be specified (non null values");
		}
		this.relation = relation;
		this.pattern = pattern;
		this.indexes = indexes;
		this.mode = SelectionMode.COMBO;
	}

	/**
	 * <p>
	 * Possibilities:
	 * </p>
	 * <ul>
	 * <li> PATTERN: ?X = 'a', ?Y = 10; </li>
	 * <li> INDEX: ?X = ?Y, ?Z = ?W;</li>
	 * <li> COMBO: ?X = 'a', ?Y = 10, ?X = ?Y, ?Z = ?W</li>
	 * </ul>
	 */
	public IMixedDatatypeRelation select() {
		switch (this.mode) {
		case PATTERN:
			return select_0();
		case INDEX:
			return select_1();
		case COMBO:
			this.relation = select_0();
			return select_1();
		default:
			throw new IllegalArgumentException("Couldn't handle the case "
					+ this.mode);
		}
	}

	/**
	 * <p>
	 * Case PATTERN: ?X = 'a', ?Y = 'b'.
	 * </p>
	 * @return Selected tuples based on a given condition
	 */
	private IMixedDatatypeRelation select_0() {
		IMixedDatatypeRelation result = RELATION.getMixedRelation(this.pattern
				.getArity());
		ITuple tup = null;
		Iterator<Integer> posIterator = null;

		// Sort the input relation on required indexes
		IMixedDatatypeRelation tmpRel = this.relation
				.indexOn(sortPatternIndexes());
		// Extract sub relations w.r.t different datatypes
		subRels = tmpRel.separatedTailSet(this.pattern);
		
		Iterator<SortedSet<ITuple>> subRelIt = this.subRels.iterator();
		while (subRelIt.hasNext()) {
			SortedSet<ITuple> st = subRelIt.next();
			// Get the tail set starting with the first tuple that satisfies the
			// selection condition.
			final SortedSet<ITuple> tailRel = st.tailSet(this.pattern);
			Iterator<ITuple> it = tailRel.iterator();
			Map<Integer, ITerm> selMap = getSelectionMap();

			// Scan the SortedSet until the selection condition is no longer
			// satisfied.
			while (it.hasNext()) {
				boolean toAdd = true;
				tup = it.next();
				posIterator = selMap.keySet().iterator();
				while (posIterator.hasNext()) {
					Integer i = posIterator.next();
					if (!tup.getTerm(i).equals(selMap.get(i))) {
						toAdd = false;
						break;
					}
				}
				// Put qualified tuples in the result of the selection
				// operation.
				if (toAdd)
					result.add(tup);
			}
		}
		return result;
	}

	/**
	 * <p>
	 * Case INDEX: ?X = ?Y in p(?X, ?Z, ?Y).
	 * </p>
	 * @return Selected tuples based on a given condition
	 */
	private IMixedDatatypeRelation select_1() {
		IMixedDatatypeRelation result = RELATION
				.getMixedRelation(this.indexes.length);
		Iterator<Integer> it, posIterator = null;
		Map<Integer, Set<Integer>> selIndexes = getSelectionIndexes();
		ITerm t = null;

		// Scan the input relation until the selection condition is no longer
		// satisfied (two or more terms are equal on specified positions).
		// It loops through each tuple from the relation.
		for (ITuple tup : this.relation) {
			boolean toAdd = true;
			it = selIndexes.keySet().iterator();
			while (it.hasNext()) {
				int key = it.next();
				posIterator = selIndexes.get(key).iterator();
				if (posIterator.hasNext())
					t = tup.getTerm(posIterator.next());
				while (posIterator.hasNext()) {
					if(key > 0){
						if (!t.equals(tup.getTerm(posIterator.next()))) {
							toAdd = false;
							break;
						}
					} else {
						if (t.equals(tup.getTerm(posIterator.next()))) {
							toAdd = false;
							break;
						}
					}
				}
				if (!toAdd) break;
			}
			// Put qualified tuples in the result of the selection operation.
			if (toAdd)
				result.add(tup);
		}
		return result;
	}

	/**
	 * <p>
	 * Returns selection indexes. For example, if this.indexes = [1,2,1,2,1], a
	 * map would be:
	 * </p>
	 * <ul>
	 * <li> [1] [0,2,4]; </li>
	 * <li> [2] [1,3]</li>
	 * </ul>
	 * <p>
	 * This means that first and third terms in a tuple must be equal as well as
	 * those on 0th, 2nd and 4th postion (e.g., t = [11, 22, 11, 22, 11]).
	 * Remark: 0 as an index means that atribute is ignored.
	 * </p>
	 * 
	 * @return A map with selection indexes.
	 */
	public Map<Integer, Set<Integer>> getSelectionIndexes() {
		Map<Integer, Set<Integer>> indexes = new HashMap<Integer, Set<Integer>>(
				this.relation.getArity());
		Set<Integer> eqInds = null;
		for (int j = 0; j < this.relation.getArity(); j++) {
			if (this.indexes[j] != 0) {
				if (!indexes.containsKey(this.indexes[j])) {
					eqInds = new HashSet<Integer>();
					eqInds.add(j);
					indexes.put(this.indexes[j], eqInds);
				} else {
					indexes.get(this.indexes[j]).add(j);
				}
			}
		}
		return indexes;
	}

	private Map<Integer, ITerm> getSelectionMap() {
		Map<Integer, ITerm> selMap = new TreeMap<Integer, ITerm>();
		Integer i = 0;
		for (ITerm t : this.pattern.getTerms()) {
			if (t != null)
				selMap.put(i, t);
			i++;
		}
		return selMap;
	}

	/**
	 * <p>
	 * Determines the indexes the relation should be <code>indexOn</code>
	 * w.r.t the ongoing select operation that needs to be performed.
	 * </p>
	 * 
	 * @return array with the sort indexes required for the ongoing select
	 *         operation based on pattern case.
	 */
	private Integer[] sortPatternIndexes() {
		final Integer[] res = new Integer[this.pattern.getArity()];
		Arrays.fill(res, 0);
		int j = 1;
		for (int i = 0; i < this.pattern.getTerms().size(); i++) {
			if (this.pattern.getTerm(i) != null) {
				res[i] = j;
				j++;
			}
		}
		return res;
	}
}
