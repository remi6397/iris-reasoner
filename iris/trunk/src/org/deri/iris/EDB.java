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

import static org.deri.iris.factory.Factory.BASIC;
import static org.deri.iris.factory.Factory.RELATION;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.deri.iris.api.IEDB;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.exception.DataNotFoundException;
import org.deri.iris.storage.Relation;
import org.deri.iris.terms.ConstructedTerm;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.07.2006 16:53:36
 */
public class EDB implements IEDB{

	private Map<IPredicate, IRelation<ITuple>> facts = new HashMap<IPredicate, IRelation<ITuple>>();
	
	private Set<IQuery> queries;
	
	private Set<IRule> rules;
	
	
	EDB(final Set<IAtom> f, final Set<IRule> r, final Set<IQuery> q) {
		if ((f == null) || (r == null) || (q == null)) {
			throw new NullPointerException("Input parameters must not be null");
		}
		for (IAtom a : f) {
			addFact(a);
		}
		this.rules = r;
		this.queries = q;
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
		IRelation<ITuple> rel = facts.get(p);
		if (rel == null) {
			rel = new Relation(p.getArity());
			this.facts.put(p, rel);
		}
		return rel.add(a.getTuple());
	}

	public boolean addFacts(Set<IAtom> facts) {
		boolean bReturn = false;
		for (IAtom f : facts) {
			if (addFact(f)) {
				bReturn = true;
			}
		}
		return bReturn;
	}

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

	public boolean removeFacts(Set<IAtom> f) {
		boolean bChanged = false;
		for (IAtom a : f) {
			if (removeFact(a)) {
				bChanged = true;
			}
		}
		return bChanged;
	}
	
	public boolean hasFact(IAtom a) {
		if(!a.isGround()){
			throw new IllegalArgumentException("The input parameter: " + 
					a.toString() + " needs to be a ground atom (it is not a fact).");
		}
		return this.facts.get(a.getPredicate()).contains(a.getTuple());
	}

	public boolean hasFacts(IPredicate p) {
		return facts.keySet().contains(p);
	}

	public boolean hasFacts(IPredicate p, Set<ITuple> filter) {
		return (getNumberOfFacts(p, filter) > 0);
	}
	
	public Set<IPredicate> getPredicates() {
		return Collections.unmodifiableSet(facts.keySet());
	}

	public int getNumberOfFacts(IPredicate predicate) {
		return getFacts(predicate).size();
	}

	public int getNumberOfFacts(IPredicate p, Set<ITuple> filter) {
		int result = 0;
		for (ITuple t : filter) {
			result += RELATION.createSelectionOperator(facts.get(p), t)
					.select().size();
		}
		return result;
	}
	
	public Set<IAtom> getFacts(IPredicate p) {
		return Collections.unmodifiableSet(creatAtomSetfor(p, facts.get(p)));
	}
	
	public Set<IAtom> getFacts() {
		Set<IAtom> atoms = new HashSet<IAtom>();
		for (IPredicate p : facts.keySet()) {
			atoms.addAll(creatAtomSetfor(p, facts.get(p)));
		}
		return Collections.unmodifiableSet(atoms);
	}
	
	public boolean isEmpty() {
		return facts.isEmpty();
	}
	
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

	/**
	 * Creates a set of atoms out of a set of tuples and a given predicate
	 * 
	 * @param p
	 *            for which to create the atoms
	 * @param t
	 *            the tupleset forwhich to create the atoms
	 * @return the generated set
	 * @throws NullPointerException
	 *             if the predicate of the set are null
	 */
	private static Set<IAtom> creatAtomSetfor(final IPredicate p,
			Collection<ITuple> tuple) {
		if ((p == null) || (tuple == null)) {
			throw new NullPointerException();
		}
		Set<IAtom> atoms = new HashSet<IAtom>();
		for (ITuple t : tuple) {
			atoms.add(BASIC.createAtom(p, 
						BASIC.createTuple(t.getTerms())));
		}
		return atoms;
	}
	
	/********************************/
	/*		rules                   */
	/********************************/
	
	public boolean addRule(IRule r) {
		if (r == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		return this.rules.add(r);
	}
	
	public boolean removeRule(IRule r){
		if (r == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		return this.rules.remove(r);
	}
	
	public Set<IRule> getRules() {
		return Collections.unmodifiableSet(rules);
	}
	
	public boolean isStratified() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean hasNegation() {
		for (IRule r : rules) {
			for (ILiteral l : r.getBodyLiterals()) {
				if (!l.isPositive()) {
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean hasConstructedTerms() {
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
	}
	
	public int ruleCount() {
		return rules.size();
	}
	
	
	/********************************/
	/*		queries                 */
	/********************************/
	
	public boolean addQuery(IQuery q) {
		if (q == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		return this.queries.add(q);
	}

	public Iterator queryIterator() throws DataNotFoundException {
		return this.queries.iterator();
	}
	
	public boolean removeQuery(IQuery q) {
		if (q == null) {
			throw new NullPointerException(
					"The input parameter must not be null");
		}
		return this.queries.remove(q);
	}

}
