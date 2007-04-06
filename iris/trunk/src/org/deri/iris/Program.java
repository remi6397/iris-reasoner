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
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.evaluation.MiscOps;
import org.deri.iris.terms.ConstructedTerm;

/**
 * This is an implementation of the IEDB interface.
 * <br/><br/><b>This implementaion is thread-save.</b>
 * 
 * @author richi
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.07.2006 16:53:36
 */
public class Program implements IProgram{

	private Map<IPredicate, IRelation> facts = new HashMap<IPredicate, IRelation>();
	
	private Set<IQuery> queries;
	
	private Set<IRule> rules;
	 
	/** The Lock to make this set threadsafe */
	private final ReentrantReadWriteLock LOCK = new ReentrantReadWriteLock();

	/** The read lock */
	private final Lock READ = LOCK.readLock();

	/** The write lock */
	private final Lock WRITE = LOCK.writeLock();
	
	
	/**
	 * Creates an empty extensional database (knowledge base) 
	 * ready to be filled up with facts, rules and queries.
	 */
	Program() {
		WRITE.lock();
			this.rules = new HashSet<IRule>();
			this.queries = new HashSet<IQuery>();
		WRITE.unlock();
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
	Program(final Map<IPredicate, IRelation> f, final Set<IRule> r, final Set<IQuery> q) {
		if ((f == null) || (r == null) || (q == null)) {
			throw new IllegalArgumentException("Input parameters must not be null");
		}
		
		for (IPredicate p : f.keySet()){
			if (f.get(p).getArity() != p.getArity())
				throw new IllegalArgumentException("Predicate " + p + " is assigned with " +
						"a relation that has a non-matching arity.");
		}
		WRITE.lock();
			this.facts = f;
			this.rules = r;
			this.queries = q;
		WRITE.unlock();
	}
	
	/** ***************************** */
	/* methods for the EDB */
	/* (handling facts)    */
	/** ***************************** */
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#addFact(org.deri.iris.api.basics.IAtom)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.  
	 */
	public boolean addFact(IAtom a) {
		if(!a.isGround()){
			throw new IllegalArgumentException("The input parameter: " + 
					a.toString() + " needs to be a ground atom (it is not a fact).");
		}
		IPredicate p = a.getPredicate();
		p = registerPredicate(p);
		IRelation rel = facts.get(p);
		if (rel == null) {
			rel = RELATION.getRelation(p.getArity());
			this.facts.put(p, rel);
		}
		return this.facts.get(p).add(a.getTuple());
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#addFacts(java.util.Set)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean addFacts(Set<IAtom> facts) {
		boolean added = false;
		
		for (IAtom f : facts) {
			added = added || addFact(f);
		}
		return added;
	}
	
	public boolean addFacts(IPredicate p, IRelation r) {
		if (r.getArity() != p.getArity())
			throw new IllegalArgumentException("Predicate " + p + " is assigned with " +
					"a relation that has a non-matching arity.");
		
		IPredicate pr = registerPredicate(p);
		Iterator<ITuple> it = r.iterator();
		ITuple t = null;
		boolean added = false, a = false;
		
		while(it.hasNext()){
			t = it.next();
			if(t.isGround()){
				a = this.facts.get(pr).add(t);
				added = added || a;
			}
			else{
				throw new IllegalArgumentException("The input realtion " + 
						"is not a ground relation.");
			}
		}
		return added;
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#removeFact(org.deri.iris.api.basics.IAtom)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean removeFact(IAtom a) {
		if(!a.isGround()){
			throw new IllegalArgumentException("The input parameter: " + 
					a.toString() + " needs to be a ground atom (it is not a fact).");
		}
		IPredicate p = a.getPredicate();
		IRelation r = this.facts.get(p);
		boolean bChanged = r.remove(a.getTuple());
		// Remove this statement for propositional rules:
		if (r.size() <= 0) {
			facts.remove(p);
		}
		return bChanged;
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#removeFacts(java.util.Set)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean removeFacts(Set<IAtom> f) {
		boolean bChanged = false;
		for (IAtom a : f) {
			if (removeFact(a)) {
				bChanged = true;
			}
		}
		return bChanged;
	}
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#hasFact(org.deri.iris.api.basics.IAtom)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean hasFact(IAtom a) {
		if(!a.isGround()){
			throw new IllegalArgumentException("The input parameter: " + 
					a.toString() + " needs to be a ground atom (it is not a fact).");
		}
		return this.facts.get(a.getPredicate()).contains(a.getTuple());
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#hasFacts(org.deri.iris.api.basics.IPredicate)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean hasFacts(IPredicate p) {
		return facts.keySet().contains(p);
	}
	
	public IPredicate registerPredicate(IPredicate p){
		if(! facts.containsKey(p)){
			facts.put(p, RELATION.getRelation(p.getArity()));
		}
		return p;
	}
	
	public Set<IPredicate> getPredicates() {
		return Collections.unmodifiableSet(facts.keySet());
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#getNumberOfFacts(org.deri.iris.api.basics.IPredicate)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public int getNumberOfFacts(IPredicate predicate) {
		return getFacts(predicate).size();
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#getNumberOfFacts(org.deri.iris.api.basics.IPredicate, java.util.Set)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public int getNumberOfFacts(IPredicate p, Set<ITuple> filter) {
		int result = 0;
		for (ITuple t : filter) {
			result += RELATION_OPERATION.createSelectionOperator(facts.get(p), t)
					.select().size();
		}
		return result;
	}
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#getFacts(org.deri.iris.api.basics.IPredicate)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public IRelation getFacts(final IPredicate p){
		return facts.get(p);
	}

	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#getFacts()
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public Map<IPredicate, IRelation> getFacts(){
		return this.facts;
	}
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#isEmpty()
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean isEmpty() {
		return facts.isEmpty();
	}
	
	/* (non-Javadoc)
	 * @see org.deri.iris.api.IEDB#existsTerm(org.deri.iris.api.terms.ITerm)
	 * Note: 
	 * 		org.deri.iris.storage.Relation has already been implemented using the read/write lock,
	 * 		so this method is thread-save.
	 */
	public boolean existsTerm(ITerm t) {
		for (IPredicate p : facts.keySet()) {
			for (ITuple tup : facts.get(p)) {
				if (tup.getTerms().contains(t)) {
					return true;
				}
			}
		}
		return false;
	}
	
	/********************************/
	/*		rules                   */
	/********************************/
	
	public boolean addRule(IRule r) {
		if (r == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		WRITE.lock();
		try {
			return this.rules.add(r);
		} finally {
			WRITE.unlock();
		}
	}
	
	public boolean removeRule(IRule r){
		if (r == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		WRITE.lock();
		try {
			return this.rules.remove(r);
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
	
	public boolean addQuery(IQuery q) {
		if (q == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		WRITE.lock();
		try {
			return this.queries.add(q);
		} finally {
			WRITE.unlock();
		}
	}
	
	public Set<IQuery> getQueries() {
		return Collections.unmodifiableSet(queries);
	}
	
	public boolean removeQuery(IQuery q) {
		if (q == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		WRITE.lock();
		try {
			return this.queries.remove(q);
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
	}
}
