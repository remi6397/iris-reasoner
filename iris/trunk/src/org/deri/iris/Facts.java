/*
 * Integrated Rule Inference System (IRIS):
 * An extensible rule inference system for datalog with extensions by 
 * built-in predicates, default negation (under well-founded semantics), 
 * function symbols and contexts. 
 * 
 * Copyright (C) 2007  Digital Enterprise Research Institute (DERI), 
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
package org.deri.iris;

import static org.deri.iris.factory.Factory.RELATION;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.ITuple;

/**
 * The extensional database - EDB. The set of known facts.
 */
public class Facts
{
	/**
	 * Add a relation of facts for the given predicate.
	 * @param predicate The predicate identifying the facts.
	 * @param relation The relation holding the tuple values.
	 * @return true If anything added.
	 */
	public boolean addFacts(IPredicate predicate, IMixedDatatypeRelation relation) {
		if (predicate == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		if (relation == null) {
			throw new NullPointerException("The relation must not be null");
		}
		if (relation.getArity() != predicate.getArity()) {
			throw new IllegalArgumentException("Predicate " + predicate + " is assigned with " +
					"a relation that has a non-matching arity.");
		}

		boolean modified = false;
		final IMixedDatatypeRelation rel = getRelation( predicate );
		for (final ITuple t : relation) {
			if (!t.isGround()) {
				throw new IllegalArgumentException("The fact to add " + 
						t + " must be ground.");
			}
			modified |= rel.add(t);
		}
		return modified;
	}

	/**
	 * Add a set of facts.
	 * @param facts The facts to add.
	 * @return true If anything added.
	 */
	public boolean addFacts(Set<IAtom> facts) {
		if (facts == null) {
			throw new NullPointerException("The facts must not be null");
		}
		boolean added = false;
		for (IAtom f : facts) {
			added |= addFact(f);
		}
		return added;
	}

	/**
	 * Add a single fact.
	 * @param atom The atomic fact.
	 * @return true If the fact was added (didn't exist before).
	 */
	public boolean addFact(IAtom atom) {
		if(!atom.isGround()){
			throw new IllegalArgumentException("The input parameter: " + 
					atom.toString() + " needs to be a ground atom (it is not a fact).");
		}
		return getRelation( atom.getPredicate() ).add(atom.getTuple());
	}

	/**
	 * Set all the known facts for the given predicate.
	 * @param predicate The predicate identifying the relation.
	 * @param relation The relation with all facts for the given predicate.
	 */
	public void setAll( IPredicate predicate, IMixedDatatypeRelation relation)
	{
		mFacts.put( predicate, relation );
	}
	
	// Not used
	/**
	 * Test if a fact exists in the EDB.
	 * @param a The fact
	 * @return true If the fact exists.
	 */
	public boolean hasFact(IAtom a) {
		if (a == null) {
			throw new NullPointerException("The fact must not be null");
		}

		return getRelation( a.getPredicate() ).contains(a.getTuple());
	}
	
	/**
	 * Get all facts for a predicate.
	 * @param predicate
	 * @return The relation containing all tuples for this predicate.
	 */
	public IMixedDatatypeRelation getFacts(final IPredicate predicate){
		if (predicate == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		
		return getRelation( predicate );
	}
	
	/**
	 * Remove a fact.
	 * @param atom The atom to remove.
	 * @return true If removed.
	 */
	public boolean removeFact(IAtom atom) {
		if (atom == null) {
			return false;
		}
		return getRelation( atom.getPredicate() ).remove(atom.getTuple());
	}

	// Not used
	/**
	 * Remove a set of facts.
	 * @param facts The set of facts to remove.
	 * return true if anything removed.
	 */
	public boolean removeFacts(Set<IAtom> facts) {
		if (facts == null) {
			throw new NullPointerException("The set of facts must not be null");
		}
		boolean bChanged = false;
		for (IAtom a : facts) {
			bChanged |= removeFact(a);
		}
		return bChanged;
	}
	
	/**
	 * Remove all facts.
	 */
	public void clear()
	{
		mFacts.clear();
	}

	/**
	 * Get a relation for the requested predicate.
	 * If such a relation does not exist then create one.
	 * @param predicate The predicate identifying the relation.
	 * @return The relation holding tuple values.
	 */
	private IMixedDatatypeRelation getRelation( IPredicate predicate )
	{
		assert predicate!= null;
		
		IMixedDatatypeRelation result = mFacts.get( predicate );
		if( result == null )
		{
			result = RELATION.getMixedRelation( predicate.getArity() );
			mFacts.put(predicate, result );
		}
		return result;
	}
	
	/** The facts of this program. */
	private final Map<IPredicate, IMixedDatatypeRelation> mFacts = new HashMap<IPredicate, IMixedDatatypeRelation>();
}
