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
package org.deri.iris;

import static org.deri.iris.factory.Factory.RELATION;
import static org.deri.iris.factory.Factory.RELATION_OPERATION;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BuiltinRegister;
import org.deri.iris.evaluation.MiscOps;
import org.deri.iris.terms.ConstructedTerm;

/**
 * <p>
 * This is an implementation of the IEDB interface.
 * </p>
 * <p>
 * This implementaion is thread-save.
 * </p>
 * <p>
 * $Id$
 * </p>
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision$
 */
public class Program implements IProgram{

	private Map<IPredicate, IMixedDatatypeRelation> facts = new HashMap<IPredicate, IMixedDatatypeRelation>();
	
	private Set<IQuery> queries = new HashSet<IQuery>();
	
	private Set<IRule> rules = new HashSet<IRule>();

	/** The register to hold the information about registered builtins. */
	private final BuiltinRegister builtinReg = new BuiltinRegister();
	 
	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The read lock */
	private final Lock READ = LOCK.readLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();

	/** Map to count the occurences of a predicate in rules and queries. */
	private final Map<IPredicate, Integer> predicateCount = new HashMap<IPredicate, Integer>();

	/** Map for the strata of the different predicates. */
	private final Map<IPredicate, Integer> strata = new HashMap<IPredicate, Integer>();
	
	/**
	 * Whether the program (only the rules) has changed since the latest
	 * stratum computation. 
	 */
	private boolean dirtyStratum = true;

	/**
	 * Creates an empty extensional database (knowledge base) 
	 * ready to be filled up with facts, rules and queries.
	 */
	Program() {
	}
	
	/**
	 * Creates an extensional database (knowledge base) with 
	 * predefined facts, rules and queries. This EDB can also
	 * be modified later (e.g. to add/remove facts, rules and 
	 * queries).
	 * 
	 * @param f
	 * 			a set of ground atoms (facts) to be added into the EDB.
	 * @param r
	 * 			a set of rules to be added into the EDB.
	 * @param q
	 * 			a set of queries to be added into the EDB.
	 */
	Program(final Map<IPredicate, IMixedDatatypeRelation> f, final Set<IRule> r, final Set<IQuery> q) {
		if ((f == null) || (r == null) || (q == null)) {
			throw new NullPointerException("Input parameters must not be null");
		}
		
		for (IPredicate p : f.keySet()){
			if (f.get(p).getArity() != p.getArity())
				throw new IllegalArgumentException("Predicate " + p + " is assigned with " +
						"a relation that has a non-matching arity.");
		}
		for (final IPredicate pred : f.keySet()) {
			registerPredicate(pred);
		}
		facts = f;
		for (final IRule rule : r) {
			_addRule(rule);
		}
		for (final IQuery query : q) {
			_addQuery(query);
		}
	}

	/**
	 * Registeres a predicate in the program. <b>This method must be called
	 * every time a new predicate (in a rule, fact or query) is added to the
	 * program</b>. The maps for the facts, predicateCount and strata will
	 * also be created, if they don't already exist.
	 * @param p the predicate to register
	 * @throws NullPointerException if the predicate is <code>null</code>
	 */
	private void registerPredicate(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		WRITE.lock();
		try {
			if (!facts.keySet().contains(p)) {
				facts.put(p, RELATION.getMixedRelation(p.getArity()));
				strata.put(p, Integer.valueOf(-1));
				predicateCount.put(p, Integer.valueOf(0));
			}
		} finally {
			WRITE.unlock();
		}
	}

	/**
	 * Increases the occurence count for the given predicate. <b>This method
	 * must be called for every predicate occurence in a rule or query,
	 * which will be added to the program</b>.
	 * @param p the predicate for which to increase the count
	 * @throws NullPointerException if the predicate is <code>null</code>
	 */
	private void increasePredicateCount(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		WRITE.lock();
		try {
			registerPredicate(p);
			predicateCount.put(p, Integer.valueOf(predicateCount.get(p) + 1));
			dirtyStratum = true;
		} finally {
			WRITE.unlock();
		}
	}

	/**
	 * Decreases the occurence count for the given predicate. <b>This method
	 * must be called for every predicate occurence in a rule or query,
	 * which will be removed from the program</b>.
	 * @param p the predicate for which to decrease the count
	 * @throws NullPointerException if the predicate is <code>null</code>
	 */
	private void decreasePredicateCount(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		if (!predicateCount.keySet().contains(p)) {
			throw new NoSuchElementException("The predicate " + p + 
					" has not been registered, yet");
		}
		WRITE.lock();
		try {
			predicateCount.put(p, Integer.valueOf(predicateCount.get(p) - 1));
			dirtyStratum = true;
		} finally {
			WRITE.unlock();
		}
	}

	/**
	 * Cleans up the predicate maps. <b>This method should be called before
	 * all predicates or the number of predicates is retrieved, to get a
	 * correct result</b>. With this method all predicate mappings will be
	 * removed, where the predicate count is less, or equal to 0 and no
	 * facts are given for this predicate.
	 */
	private void cleanupPredicates() {
		final Set<IPredicate> toRemove = new HashSet<IPredicate>();
		WRITE.lock();
		try {
			for (final IPredicate p : facts.keySet()) {
				if ((predicateCount.get(p) <= 0) && facts.get(p).isEmpty()) {
					toRemove.add(p);
				}
			}
			for (final IPredicate p : toRemove) {
				facts.remove(p);
				predicateCount.remove(p);
				strata.remove(p);
			}
		} finally {
			WRITE.unlock();
		}
	}

	public int getStratum(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		if (!predicateCount.keySet().contains(p)) {
			throw new NoSuchElementException("The predicate " + p + 
					" has not been registered, yet");
		}
		WRITE.lock();
		try {
			if (dirtyStratum) { // recompute the stratum if a rule updated
				dirtyStratum = false;
				cleanupPredicates();
				isStratified();
			}
			READ.lock();
		} finally {
			WRITE.unlock();
		}
		try {
			return strata.get(p).intValue();
		} finally {
			READ.unlock();
		}
	}

	public void setStratum(final IPredicate p, final int s) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		if (!predicateCount.keySet().contains(p)) {
			throw new NoSuchElementException("The predicate " + p + 
					" has not been registered, yet");
		}
		if (s < 0) {
			throw new IllegalArgumentException("The statum must not be negative, but was: " + s);
		}
		WRITE.lock();
		try {
			strata.put(p, Integer.valueOf(s));
		} finally {
			WRITE.unlock();
		}
	}
	
	/** ***************************** */
	/* methods for the EDB */
	/* (handling facts)    */
	/** ***************************** */
	
	public boolean addFact(IAtom a) {
		if(!a.isGround()){
			throw new IllegalArgumentException("The input parameter: " + 
					a.toString() + " needs to be a ground atom (it is not a fact).");
		}
		IPredicate p = a.getPredicate();
		registerPredicate(p);
		return this.facts.get(p).add(a.getTuple());
	}

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
	
	public boolean addFacts(IPredicate p, IMixedDatatypeRelation r) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		if (r == null) {
			throw new NullPointerException("The relation must not be null");
		}
		if (r.getArity() != p.getArity()) {
			throw new IllegalArgumentException("Predicate " + p + " is assigned with " +
					"a relation that has a non-matching arity.");
		}
		registerPredicate(p);
		boolean modified = false;
		final IMixedDatatypeRelation rel = facts.get(p);
		for (final ITuple t : r) {
			if (!t.isGround()) {
				throw new IllegalArgumentException("The fact to add " + 
						t + " must be ground.");
			}
			modified |= rel.add(t);
		}
		return modified;
	}

	public boolean removeFact(IAtom a) {
		if (a == null) {
			return false;
		}
		return facts.get(a.getPredicate()).remove(a.getTuple());
	}

	public boolean removeFacts(Set<IAtom> f) {
		if (f == null) {
			throw new NullPointerException("The set of facts must not be null");
		}
		boolean bChanged = false;
		for (IAtom a : f) {
			bChanged |= removeFact(a);
		}
		return bChanged;
	}
	
	public boolean hasFact(IAtom a) {
		if (a == null) {
			throw new NullPointerException("The fact must not be null");
		}
		return this.facts.get(a.getPredicate()).contains(a.getTuple());
	}

	public boolean hasFacts(IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		cleanupPredicates();
		return facts.keySet().contains(p);
	}
	
	public Set<IPredicate> getPredicates() {
		cleanupPredicates();
		return Collections.unmodifiableSet(facts.keySet());
	}

	public IMixedDatatypeRelation getFacts(final IPredicate p){
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		return facts.get(p);
	}

	public Map<IPredicate, IMixedDatatypeRelation> getFacts(){
		return Collections.unmodifiableMap(facts);
	}
	
	/********************************/
	/*		rules                   */
	/********************************/
	
	public boolean addRule(final IRule r) {
		return _addRule(r);
	}

	/**
	 * Adds a rule to the program. The predicate count will be increased,
	 * too.
	 * @param r the rule to add
	 * @return <code>false</code> if the rule was already in the program,
	 * otherwise <code>true</code>
	 * @throws NullPointerException if the rule was <code>null</code>
	 */
	private boolean _addRule(final IRule r) {
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		WRITE.lock();
		try {
			if(rules.add(r)) {
				for (final ILiteral l : r.getHeadLiterals()) {
					increasePredicateCount(l.getPredicate());
				}
				for (final ILiteral l : r.getBodyLiterals()) {
					increasePredicateCount(l.getPredicate());
				}
				return true;
			}
			return false;
		} finally {
			WRITE.unlock();
		}
	}
	
	public boolean removeRule(IRule r){
		if (r == null) {
			throw new NullPointerException("The rule must not be null");
		}
		WRITE.lock();
		try {
			if(rules.remove(r)) {
				for (final ILiteral l : r.getHeadLiterals()) {
					decreasePredicateCount(l.getPredicate());
				}
				for (final ILiteral l : r.getBodyLiterals()) {
					decreasePredicateCount(l.getPredicate());
				}
				return true;
			}
			return false;
		} finally {
			WRITE.unlock();
		}
	}
	
	public Set<IRule> getRules() {
		READ.lock();
		try {
			return Collections.unmodifiableSet(rules);
		} finally {
			READ.unlock();
		}
	}
	
	public boolean isStratified() {
		WRITE.lock();
		try {
			return MiscOps.stratify(this);
		} finally {
			WRITE.unlock();
		}
	}
	
	public boolean hasNegation() {
		READ.lock();
		try {
			for (IRule r : rules) {
				for (ILiteral l : r.getBodyLiterals()) {
					if (!l.isPositive()) {
						return true;
					}
				}
			}
			return false;
		} finally {
			READ.unlock();
		}
	}
	
	public boolean hasConstructedTerms() {
		READ.lock();
		try {
			for (IRule r : rules) {
				for (ILiteral l : r.getBodyLiterals()) {
					for (Object t : l.getTuple().getTerms()) {
						if (t instanceof ConstructedTerm) {
							return true;
						}
					}
				}
				for (ILiteral l : r.getHeadLiterals()) {
					for (Object t : l.getTuple().getTerms()) {
						if (t instanceof ConstructedTerm) {
							return true;
						}
					}
				}
			}
			return false;
		} finally {
			READ.unlock();
		}
	}
	
	public int ruleCount() {
		READ.lock();
		try {
			return rules.size();
		} finally {
			READ.unlock();
		}
	}
	
	
	/********************************/
	/*		queries                 */
	/********************************/
	
	public boolean addQuery(final IQuery q) {
		return _addQuery(q);
	}

	/**
	 * Adds a query to the program. The predicate count will be increased,
	 * too.
	 * @param q the query to add
	 * @return <code>false</code> if the query was already in the program,
	 * otherwise <code>true</code>
	 * @throws NullPointerException if the query was <code>null</code>
	 */
	private boolean _addQuery(final IQuery q) {
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		WRITE.lock();
		try {
			if (queries.add(q)) {
				for (final ILiteral l : q.getQueryLiterals()) {
					increasePredicateCount(l.getPredicate());
				}
				return true;
			}
			return false;
		} finally {
			WRITE.unlock();
		}
	}
	
	public Set<IQuery> getQueries() {
		return Collections.unmodifiableSet(queries);
	}
	
	public boolean removeQuery(IQuery q) {
		if (q == null) {
			throw new NullPointerException("The query must not be null");
		}
		WRITE.lock();
		try {
			if (queries.remove(q)) {
				for (final ILiteral l : q.getQueryLiterals()) {
					decreasePredicateCount(l.getPredicate());
				}
				return true;
			}
			return false;
		} finally {
			WRITE.unlock();
		}
	}
	
	/********************************/
	/*		program                 */
	/********************************/
	
	public void resetProgram(){
		this.facts.clear();
		this.rules.clear();
		this.queries.clear();
		predicateCount.clear();
		strata.clear();
		dirtyStratum = true;
	}

	public BuiltinRegister getBuiltinRegister() {
		return builtinReg;
	}
}
