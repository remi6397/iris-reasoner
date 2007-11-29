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

package org.deri.iris.storage;

import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;

import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;

/**
 * <p>
 * Relation to store tuples with various datatypes.
 * </p>
 * <p>
 * <b>The <code>first()</code> and <code>last()</code> methods might not work as
 * expeced.</b> This is because at the moment it is not defined how to
 * compare e.g. iris with integers. The results of this methods might differ from
 * call to call. This is because of the internal storing algorithm of the
 * internal <code>HashMap</code>. Also the iterator might not seem to have the
 * correct order, since it groups the tuples in potions with the same datatypes.
 * </p>
 * <p>
 * Another limitation of this relation is, that if you retrieve a subset with
 * a given datatypeorder the subset will only contain and accept tuples of the 
 * same datatypeorder.
 * </p>
 * <p>
 * <code>null</code> is not permitted by this relation, nor by its subsets.
 * </p>
 * <p>
 * $Id: MixedDatatypeRelation.java,v 1.6 2007-11-06 20:19:45 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @version $Revision: 1.6 $
 */
public class MixedDatatypeRelation extends AbstractSet<ITuple> implements IMixedDatatypeRelation {

	/**
	 * Datatype to relation mappings.
	 * <ul>
	 * <li>key: the hash code of the classes of the terms in the tuple</li>
	 * <li>value: the relation with the tuples only of the given datatypes</li>
	 * </ul>
	 */
	private final Map<Integer, IRelation> datatypeRelations = new HashMap<Integer, IRelation>();

	/**
	 * Map holding the class hash to class array mappings.
	 * <ul>
	 * <li>key: the hash code of the classes of the terms in the tuple</li>
	 * <li>value: the class array out of which the hash was constructed.
	 * This mappings are needed when we want to use the sub sets of the
	 * IMixedDatatypeRelation interface.</li>
	 * </ul>
	 */
	private final Map<Integer, Class<?>[]> datatypeIndex = new HashMap<Integer, Class<?>[]>();

	/** The arity of this relation. */
	private final int arity;

	/** The default sorting index. */
	private final int[] idx;

	/**
	 * Constructs a relation with the given arity.
	 * @param arity the arity of this relation
	 * @throws IllegalArgumentException if the arity is negative
	 */
	MixedDatatypeRelation(final int arity) {
		if (arity < 0) {
			throw new IllegalArgumentException(
					"The arity of the relation must not be negative, but was: " + arity);
		}
		// constructing the default index
		idx = new int[arity];
		if (arity > 0) {
			idx[0] = 1;
		}
		this.arity = arity;
	}

	/**
	 * Internal remove method for this relation.
	 * @param o the object which should be removed
	 * @return <code>true</code> if call of this method had any effects on
	 * the relation, otherwise </code>false</code>
	 */
	private boolean intRemove(final Object o) {
		if ((o == null) || (!(o instanceof ITuple))) {
			return false;
		}
		return getDatatypeRelation((ITuple) o).remove(o);
	}

	/**
	 * Returns the relation for storing the tuples of the datatyps of the
	 * given tuple.
	 * @param t the tuple which should be stored in the relation
	 * @return the relation for the given datatype
	 * @throws NullPointerException if the tuple is <code>null</code>
	 */
	private IRelation getDatatypeRelation(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final Integer hash = classHash(t);
		IRelation rel = datatypeRelations.get(hash);
		if (rel == null) { // create new relation if none was found according to the datatypes
			rel = new IndexingOnTheFlyRelation(arity);
			datatypeRelations.put(hash, rel);
			// constructing and storing the class array
			final Class<?>[] classes = new Class<?>[arity];
			int i = 0;
			for (final ITerm term : t) {
				classes[i++] = (term == null) ? null : term.getClass();
			}
			datatypeIndex.put(hash, classes);
		}
		return rel;
	}

	/**
	 * Creates the hash code for the datatypes in this tuple.
	 * @param t the tuple containing the terms with the datatypes
	 * @return the hash code
	 * @throws NullPointerException if the tuple is <code>null</code>
	 */
	private static Integer classHash(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		int res = 17;
		for (final ITerm term : t) {
			res = res * 37 + ((term != null) ? term.getClass().hashCode() : 0);
		}
		return new Integer(res);
	}

	public boolean add(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The tuple must not ne null");
		}
		if (t.size() != arity) {
			throw new IllegalArgumentException("The arity of the relation (" + 
					arity + ") and of the tuple (" + t.size() + ") don't match");
		}
		return getDatatypeRelation(t).add(t);
	}

	public int size() {
		int size = 0;
		for( Map.Entry<Integer, IRelation> e : datatypeRelations.entrySet() )
			size += e.getValue().size();
		return size;
	}

	public Iterator<ITuple> iterator() {
		return new CompoundIterator(datatypeRelations.values());
	}
	
	public ITuple first() {
		for (final IRelation r : datatypeRelations.values()) {
			if (!r.isEmpty()) {
				return r.first();
			}
		}
		throw new NoSuchElementException("There is no element in this relation");
	}

	public ITuple last() {
		ITuple last = null;
		for (final IRelation r : datatypeRelations.values()) {
			if (!r.isEmpty()) {
				last = r.last();
			}
		}
		if (last != null) {
			return last;
		}
		throw new NoSuchElementException("There is no element in this relation");
	}

	public SortedSet<ITuple> tailSet(final ITuple from) {
		if (from == null) {
			throw new NullPointerException("The from tuple must not be null");
		}
		return new CompoundRelation(idx, from, null);
	}

	public SortedSet<ITuple> headSet(final ITuple to) {
		if (to == null) {
			throw new NullPointerException("The to tuple must not be null");
		}
		return new CompoundRelation(idx, null, to);
	}

	public SortedSet<ITuple> subSet(final ITuple from, final ITuple to) {
		if ((from == null) || (to == null)) {
			throw new NullPointerException("The from and to tuple must not be null");
		}
		// asserting the datatypes of both tuples
		if (!classHash(from).equals(classHash(to))) {
			throw new IllegalArgumentException("The datatypes of both tuples (from: " + 
					from + " to: " + to + ")must match");
		}
		return new CompoundRelation(idx, from, to);
	}

	public Comparator<? super ITuple> comparator() {
		return datatypeRelations.values().iterator().next().comparator();
	}

	public IMixedDatatypeRelation indexOn(int[] idx) {
		return new CompoundRelation(idx);
	}

	public int getArity() {
		return arity;
	}

	/**
	 * Determines the internal relations which would match the datatypes of
	 * the given tuple. A term in the tuple which is <code>null</code> would
	 * match all datatypes.
	 * @param t the tuple to match
	 * @return The collection of classHashes which would match the tuple
	 * @throws NullPointerException if the tuple is <code>null</code>
	 */
	private Collection<Integer> matchingIndexes(final ITuple t) {
		if (t == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final Class<?>[] classes = new Class<?>[arity];
		for (int i = 0; i < arity; i++) {
			classes[i] = (t.get(i) == null) ? null : t.get(i).getClass();
		}
		final Set<Integer> res = new HashSet<Integer>();
		for (final Entry<Integer, Class<?>[]> e : datatypeIndex.entrySet()) {
			final Class<?>[] others = e.getValue();
			boolean matches = true;
			for (int i = 0; (i < arity) && matches; i++) {
				matches &= (classes[i] == null) || 
					(classes[i] == others[i]) || 
					classes[i].equals(others[i]);
			}
			if (matches) {
				res.add(e.getKey());
			}
		}
		return res;
	}

	public Collection<SortedSet<ITuple>> separatedHeadSet(final ITuple to) {
		if (to == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final List<SortedSet<ITuple>> res = new ArrayList<SortedSet<ITuple>>();
		for (final Integer i : matchingIndexes(to)) {
			res.add(datatypeRelations.get(i).headSet(to));
		}
		return res;
	}

	public Collection<SortedSet<ITuple>> separatedTailSet(final ITuple from) {
		if (from == null) {
			throw new NullPointerException("The tuple must not be null");
		}
		final List<SortedSet<ITuple>> res = new ArrayList<SortedSet<ITuple>>();
		for (final Integer i : matchingIndexes(from)) {
			res.add(datatypeRelations.get(i).tailSet(from));
		}
		return res;
	}

	public Collection<SortedSet<ITuple>> separatedSubSet(final ITuple from, final ITuple to) {
		if ((to == null) || (from == null)) {
			throw new NullPointerException("The tuple must not be null");
		}
		final List<SortedSet<ITuple>> res = new ArrayList<SortedSet<ITuple>>();
		for (final Integer i : matchingIndexes(to)) {
			res.add(datatypeRelations.get(i).subSet(from, to));
		}
		return res;
	}

	/**
	 * <p>
	 * Iterator designed to iterate over a set of Relations.
	 * </p>
	 * <p>
	 * $Id: MixedDatatypeRelation.java,v 1.6 2007-11-06 20:19:45 bazbishop237 Exp $
	 * </p>
	 * @version $Revision: 1.6 $
	 * @author Richard Pöttler (richard dot poettler at deri dot at)
	 */
	private class CompoundIterator implements Iterator<ITuple> {

		/** Iterator over all relation we got to iterate over. */
		final Iterator<? extends SortedSet<ITuple>> rels;

		/** The iterator over the actual relation. */
		Iterator<ITuple> actual;

		/** The relation we are actually iterating over. */
		SortedSet<ITuple> actualSet;

		/** The last tuple returned by this iterator. */
		ITuple last = null;

		/**
		 * Constructs the iterator for a collection of relations.
		 * @param c the collection to iterate over
		 * @throws NullPointerException if the collection is
		 * <code>null</code>
		 */
		public CompoundIterator(final Collection<? extends SortedSet<ITuple>> c) {
			if (c == null) {
				throw new NullPointerException(
						"The collection we should iterate over must not be null");
			}
			rels = c.iterator();
			actualSet = rels.hasNext() ? rels.next() : null;
			actual = (actualSet != null) ? actualSet.iterator() : null;
		}

		public boolean hasNext() {
			while ((actual != null) && !actual.hasNext()) {
				actualSet = rels.hasNext() ? rels.next() : null;
				actual = (actualSet != null) ? actualSet.iterator() : null;
			}
			return actual != null;
		}

		public ITuple next() {
			if (!hasNext()) {
				throw new NoSuchElementException("There are no items left to iterate over");
			}
			return last = actual.next();
		}

		public void remove() {
			MixedDatatypeRelation.this.intRemove(last);
			// renewing the iterator, to avoid
			// ConcurrentModificationException
			actual = actualSet.tailSet(last).iterator();
		}
	}

	/**
	 * <p>
	 * View/proxy class for the the MixedDatatypeRelation. This class was
	 * designed to serve as proxy for most SortedSets gained from the
	 * MixedDatatypeRelation. It handeles which tuples should be shown, removed
	 * and added internally.
	 * </p>
	 * <p>
	 * $Id: MixedDatatypeRelation.java,v 1.6 2007-11-06 20:19:45 bazbishop237 Exp $
	 * </p>
	 * @version $Revision: 1.6 $
	 * @author Richard Pöttler (richard dot poettler at deri dot at)
	 */
	private class CompoundRelation extends AbstractSet<ITuple> implements IMixedDatatypeRelation {

		/**
		 * Datatype to relation mappings.
		 * <ul>
		 * <li>key: the hash code of the classes of the terms in the tuple</li>
		 * <li>value: the relation with the tuples only of the given datatypes</li>
		 * </ul>
		 */
		private final Map<Integer, SortedSet<ITuple>> datatypeRelations = 
			new HashMap<Integer, SortedSet<ITuple>>();

		/** The indexes to sort on. */
		private final int[] idx;

		/** 
		 * The lower bound of this set. Is <code>null</code> if there
		 * is no lower bound
		 */
		private final ITuple from;

		/** 
		 * The upper bound of this set. Is <code>null</code> if there
		 * is no upper bound
		 */
		private final ITuple to;

		/**
		 * The class/datatype patter of this relation. Is
		 * <code>null</code> if there is no constraint.
		 */
		private final Class<?>[] classPattern;

		/**
		 * Constructs a set which is sorted on the given idexes.
		 * @param idx the indexes to sort on
		 * @throws NullPointerException if idx is <code>null</code>
		 */
		public CompoundRelation(final int[] idx) {
			this(idx, null, null);
		}

		/**
		 * Constructs a set which is sorted on the given idexes with the
		 * given upper and lower bounds.
		 * @param idx the indexes to sort on
		 * @param from the lower bound (inclusive), or <code>null</code>
		 * if there is no lower bound
		 * @param to the upper bound (exclusive), or <code>null</code>
		 * if there is no upper bound
		 * @throws NullPointerException if idx is <code>null</code>
		 * @throws IllegalArgumentException if the lenght of the index
		 * array doesn't match the arity of the relation
		 */
		public CompoundRelation(final int[] idx, final ITuple from, final ITuple to) {
			if (idx == null) {
				throw new NullPointerException("The index array must not be null");
			}
			if (idx.length != MixedDatatypeRelation.this.arity) {
				throw new IllegalArgumentException("The arity of the index (" + idx.length + 
						") must match the arity of the relation (" + 
						MixedDatatypeRelation.this.arity + ")");
			}

			if ((from == null) && (to == null)) { // no upper and lower bound 
				for (final Entry<Integer, IRelation> e : 
						MixedDatatypeRelation.this.datatypeRelations.entrySet()) {
					datatypeRelations.put(e.getKey(), e.getValue().indexOn(idx));
				}
				classPattern = null;
			} else if (to == null) { // construct the tail set for this tuple
				classPattern = classPattern(from);
				for (final Integer i : matchingIndexes(from)) {
					datatypeRelations.put(i, MixedDatatypeRelation.this.datatypeRelations.get(i).indexOn(idx).tailSet(from));
				}
			} else if (from == null) { // construct the head set for this tuple
				classPattern = classPattern(to);
				for (final Integer i : matchingIndexes(to)) {
					datatypeRelations.put(i, MixedDatatypeRelation.this.datatypeRelations.get(i).indexOn(idx).headSet(to));
				}
			} else { // construct the sub set for this tuples
				if (!classHash(from).equals(classHash(to))) { // assert the tuple term classes
					throw new IllegalArgumentException("The datatypes of both tuples (from: " + 
							from + " to: " + to + ")must match");
				}
				classPattern = classPattern(from);
				for (final Integer i : matchingIndexes(to)) {
					datatypeRelations.put(i, MixedDatatypeRelation.this.datatypeRelations.get(i).indexOn(idx).subSet(from, to));
				}
			}
			this.idx = idx;
			this.from = from;
			this.to = to;
		}

		/**
		 * Determines the class/datatype pattern of a tuple. Tuples with
		 * terms which are <code>null</code> are also allowed.
		 * @param t the tuple
		 * @return the class pattern
		 * @throws NullPointerException if the tuple is
		 * <code>null</code>
		 */
		private Class<?>[] classPattern(final ITuple t) {
			if (t == null) {
				throw new NullPointerException("The tuple must not be null");
			}
			final List<Class<?>> res = new ArrayList<Class<?>>();
			for (final ITerm term : t) {
				res.add((term == null) ? null : term.getClass());
			}
			return res.toArray(new Class[res.size()]);
		}

		/**
		 * Checks the class/datatype pattern of a tuple against the
		 * pattern of this relation.
		 * @param t the tuple whose datatype should be checked
		 * @return <code>true</code> if the pattern would match,
		 * otherwise <code>false</code>
		 * @throws NullPointerException if the tuple is
		 * <code>null</code>
		 */
		private boolean checkClassPattern(final ITuple t) {
			if (t == null) {
				throw new NullPointerException("The tuple must not be null");
			}
			if (classPattern == null) {
				return true;
			}
			final Class<?>[] others = classPattern(t);
			if (classPattern.length != others.length) {
				return false;
			}
			for (int i = 0; i < classPattern.length; i++) {
				if ((classPattern[i] != null) && !classPattern[i].equals(others[i])) {
					return false;
				}
			}
			return true;
		}

		public boolean add(final ITuple t) {
			if (!checkClassPattern(t)) {
				throw new IllegalArgumentException("The tuple to add (" + t + 
					") does not match the datatype pattern " + 
					Arrays.toString(classPattern));
			}
			if (classPattern == null) { // if we don't have any bounds for this 
				// relation it is save any datatypes -> use the outher relation
				// FIXME: if the datatypes are new, we won't see it in THIS relation
				return MixedDatatypeRelation.this.add(t);
			}
			// the sub relations of the IndexingOnTheFlyRelation
			// will take care about the thresholds...
			return datatypeRelations.get(classHash(t)).add(t);
		}

		public int size() {
			int size = 0;
			for (final SortedSet<ITuple> s : datatypeRelations.values()) {
				size += s.size();
			}
			return size;
		}

		public Iterator<ITuple> iterator() {
			return new CompoundIterator(datatypeRelations.values());
		}

		public Comparator<? super ITuple> comparator() {
			if (MixedDatatypeRelation.this.datatypeRelations.isEmpty()) {
				return (new IndexingOnTheFlyRelation(arity)).comparator();
			}
			return MixedDatatypeRelation.this.datatypeRelations.values().iterator().next().comparator();
		}

		public ITuple first() {
			for (final SortedSet<ITuple> r : datatypeRelations.values()) {
				if (!r.isEmpty()) {
					return r.first();
				}
			}
			throw new NoSuchElementException("There is no element in this relation");
		}

		public ITuple last() {
			ITuple last = null;
			for (final SortedSet<ITuple> r : datatypeRelations.values()) {
				if (!r.isEmpty()) {
					last = r.last();
				}
			}
			if (last != null) {
				return last;
			}
			throw new NoSuchElementException("There is no element in this relation");
		}

		public SortedSet<ITuple> tailSet(final ITuple from) {
			if (from == null) {
				throw new NullPointerException("The from tuple must not be null");
			}
			return new CompoundRelation(idx, determineFrom(from), null);
		}

		public SortedSet<ITuple> headSet(final ITuple to) {
			if (to == null) {
				throw new NullPointerException("The to tuple must not be null");
			}
			return new CompoundRelation(idx, null, determineTo(to));
		}

		public SortedSet<ITuple> subSet(final ITuple from, final ITuple to) {
			if ((from == null) || (to == null)) {
				throw new NullPointerException("The from and to tuple must not be null");
			}
			if (!classHash(from).equals(classHash(to))) {
				throw new IllegalArgumentException("The datatypes of the from " + 
						from + " and to " + to + " tuple must match");
			}
			return new CompoundRelation(idx, determineFrom(from), determineTo(to));
		}

		public int getArity() {
			return MixedDatatypeRelation.this.arity;
		}

		public IMixedDatatypeRelation indexOn(final int[] idx) {
			// parameter checking will be done in the CompoundRelation
			// constructor
			return new CompoundRelation(idx, from, to);
		}

		public Collection<SortedSet<ITuple>> separatedTailSet(final ITuple from) {
			if (from == null) {
				throw new NullPointerException("The tuple must not be null");
			}
			final List<SortedSet<ITuple>> res = new ArrayList<SortedSet<ITuple>>();
			final Collection<Integer> hashes = matchingIndexes(from);
			hashes.retainAll(datatypeRelations.keySet());
			for (final Integer i : hashes) {
				res.add(datatypeRelations.get(i).tailSet(from));
			}
			return res;
		}
		public Collection<SortedSet<ITuple>> separatedHeadSet(final ITuple to) {
			if (to == null) {
				throw new NullPointerException("The tuple must not be null");
			}
			final List<SortedSet<ITuple>> res = new ArrayList<SortedSet<ITuple>>();
			final Collection<Integer> hashes = matchingIndexes(to);
			hashes.retainAll(datatypeRelations.keySet());
			for (final Integer i : hashes) {
				res.add(datatypeRelations.get(i).headSet(to));
			}
			return res;
		}
		public Collection<SortedSet<ITuple>> separatedSubSet(final ITuple from, final ITuple to) {
			if ((from == null) && (to == null)) {
				throw new NullPointerException("The tuples must not be null");
			}
			if (classHash(from) != classHash(to)) {
				throw new IllegalArgumentException("The datatypes of the tuples doesn't match: " + 
						from + " <-> " + to);
			}
			final List<SortedSet<ITuple>> res = new ArrayList<SortedSet<ITuple>>();
			final Collection<Integer> hashes = matchingIndexes(from);
			hashes.retainAll(datatypeRelations.keySet());
			for (final Integer i : hashes) {
				res.add(datatypeRelations.get(i).subSet(from, to));
			}
			return res;
		}

		/**
		 * Determines the most restrective from tuple out of the
		 * submitted one and the one of the actual subrelation.
		 * @param from tuple which should be compared with the from
		 * tuple of <code>this</code> subset.
		 * @return the most restrective tuple
		 * @throws NullPointerException if the submitted tuple is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the classpattern doesn't
		 * match
		 */
		private ITuple determineFrom(final ITuple from) {
			if (from == null) {
				throw new NullPointerException("The tuple must not be null");
			}
			if (!checkClassPattern(from)) {
				throw new IllegalArgumentException("The class pattern of the from tuple " + 
						from + " doesn't match " + Arrays.toString(classPattern));
			}
			if (this.from == null) {
				return from;
			}
			return comparator().compare(this.from, from) < 0 ? from : this.from;
		}

		/**
		 * Determines the most restrective to tuple out of the
		 * submitted one and the one of the actual subrelation.
		 * @param to tuple which should be compared with the to
		 * tuple of <code>this</code> subset.
		 * @return the most restrective tuple
		 * @throws NullPointerException if the submitted tuple is
		 * <code>null</code>
		 * @throws IllegalArgumentException if the classpattern doesn't
		 * match
		 */
		private ITuple determineTo(final ITuple to) {
			if (to == null) {
				throw new NullPointerException("The tuple must not be null");
			}
			if (!checkClassPattern(to)) {
				throw new IllegalArgumentException("The class pattern of the from tuple " + 
						to + " doesn't match " + Arrays.toString(classPattern));
			}
			if (this.to == null) {
				return to;
			}
			return comparator().compare(this.to, to) > 0 ? to : this.to;
		}
	}
}
