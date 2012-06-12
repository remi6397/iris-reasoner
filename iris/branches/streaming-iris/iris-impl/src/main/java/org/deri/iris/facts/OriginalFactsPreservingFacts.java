/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions.
 * 
 * Copyright (C) 2008 Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
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
package org.deri.iris.facts;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.storage.IRelation;
import org.deri.iris.storage.IRelationFactory;

/**
 * A facts adaptor that allows modification (i.e. adding more facts) without
 * modifying the original facts.
 */
public class OriginalFactsPreservingFacts implements IFacts {
	/**
	 * Constructor. Decorate the given facts object.
	 * 
	 * @param original
	 *            The original facts that will be preserved, i.e. not altered by
	 *            this class.
	 * @param relationFactory
	 *            The relation factory to use.
	 */
	public OriginalFactsPreservingFacts(IFacts original,
			IRelationFactory relationFactory) {
		mOriginalFacts = original;
		mRelationFactory = relationFactory;
	}

	public IRelation get(IPredicate predicate) {
		IRelation adaptor = mPredicateRelationMap.get(predicate);

		if (adaptor == null) {
			adaptor = new OriginalPreservingAdaptor(
					mOriginalFacts.get(predicate));

			mPredicateRelationMap.put(predicate, adaptor);
		}

		return adaptor;
	}

	public Set<IPredicate> getPredicates() {
		return mOriginalFacts.getPredicates();
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();

		for (IPredicate predicate : getPredicates()) {
			IRelation relation = get(predicate);
			for (int t = 0; t < relation.size(); ++t) {
				ITuple tuple = relation.get(t);
				result.append(predicate.getPredicateSymbol());
				result.append(tuple);
				result.append('.');
			}
		}

		return result.toString();
	}

	/**
	 * Original preserving relation adaptor.
	 */
	private class OriginalPreservingAdaptor implements IRelation {

		private final IRelation mOriginal;
		private final IRelation mAddedFacts;

		OriginalPreservingAdaptor(IRelation original) {
			mOriginal = original;
			mAddedFacts = mRelationFactory.createRelation();
		}

		public boolean add(ITuple tuple) {
			if (mOriginal.contains(tuple))
				return false;

			return mAddedFacts.add(tuple);
		}

		public boolean add(ITuple tuple, long timestamp) {
			if (mOriginal.contains(tuple))
				return false;

			return mAddedFacts.add(tuple, timestamp);
		}

		public boolean addAll(IRelation relation) {
			boolean changed = false;
			for (int t = 0; t < relation.size(); ++t) {
				if (add(relation.get(t)))
					changed = true;
			}

			return changed;
		}

		public boolean addAll(IRelation relation, long timestamp) {
			boolean changed = false;
			for (int t = 0; t < relation.size(); ++t) {
				if (add(relation.get(t), timestamp))
					changed = true;
			}

			return changed;
		}

		public ITuple get(int index) {
			if (index < mOriginal.size())
				return mOriginal.get(index);

			return mAddedFacts.get(index - mOriginal.size());
		}

		public int size() {
			return mOriginal.size() + mAddedFacts.size();
		}

		public boolean contains(ITuple tuple) {
			return mOriginal.contains(tuple) || mAddedFacts.contains(tuple);
		}

		@Override
		public String toString() {
			return mOriginal.toString() + mAddedFacts.toString();
		}

		@Override
		public long getTimestamp(ITuple tuple) {
			long timestamp = mOriginal.getTimestamp(tuple);
			if (timestamp != -1) {
				return timestamp;
			} else {
				return mAddedFacts.getTimestamp(tuple);
			}
		}

		@Override
		public void clean(long timestamp) {
			// TODO Norbert: also for added facts?
			mAddedFacts.clean(timestamp);
		}
	}

	/** The map storing the predicate-relation relationship. */
	private final Map<IPredicate, IRelation> mPredicateRelationMap = new HashMap<IPredicate, IRelation>();

	/** The relation factory for new relations. */
	private final IRelationFactory mRelationFactory;

	/** The decorated original facts. */
	private final IFacts mOriginalFacts;

	@Override
	public void addFacts(Map<IPredicate, IRelation> newFacts, long timestamp) {
		mOriginalFacts.addFacts(newFacts, timestamp);
	}

	@Override
	public void clean(long timestamp) {
		mOriginalFacts.clean(timestamp);
	}

}
