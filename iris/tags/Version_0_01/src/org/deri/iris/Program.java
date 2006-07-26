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
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.exception.DataNotFoundException;
import org.deri.iris.storage.Relation;
import org.deri.iris.terms.ConstructedTerm;

/**
 * 
 * <br/><br/>$Id$
 * 
 * @author richi
 * @version $Revision$
 * @date $Date$
 */
public class Program implements IProgram {

	private UnsupportedOperationException SHOULD_BE_IMMUTABLE = new UnsupportedOperationException(
			"Only new Facts should be added/removed");

	private Map<IPredicate, IRelation<ITuple>> facts = new HashMap<IPredicate, IRelation<ITuple>>();

	private EvaluationMethod method;

	private IQuery query;

	private Set<IRule> rules;

	public Program(final Set<IRule> r, final IQuery q, final Set<IAtom> f) {
		if ((r == null) || (q == null) || (f == null)) {
			throw new NullPointerException();
		}
		rules = r;
		query = q;

		for (IAtom a : f) {
			addFact(a);
		}
	}

	public boolean addFact(IAtom a) {
		IPredicate p = a.getPredicate();
		IRelation<ITuple> rel = facts.get(p);
		if (rel == null) {
			rel = new Relation(p.getArity());
			facts.put(p, rel);
		}
		return rel.add(createTupleForAtom(a));
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

	public boolean addQuery(IQuery q) {
		throw SHOULD_BE_IMMUTABLE;
	}

	public boolean addRule(IRule r) {
		throw SHOULD_BE_IMMUTABLE;
	}

	public Vector computeSubstitution(IQuery q) {
		// TODO Auto-generated method stub
		return null;
	}

	public Vector computeSubstitutions() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean evaluate() throws DataModelException {
		// TODO Auto-generated method stub
		return false;
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

	public Set<IAtom> getFacts() {
		Set<IAtom> atoms = new HashSet<IAtom>();
		for (IPredicate p : facts.keySet()) {
			atoms.addAll(creatAtomSetfor(p, facts.get(p)));
		}
		return Collections.unmodifiableSet(atoms);
	}

	public Set<IAtom> getFacts(IPredicate p) {
		return Collections.unmodifiableSet(creatAtomSetfor(p, facts.get(p)));
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

	public Set<IPredicate> getPredicates() {
		return Collections.unmodifiableSet(facts.keySet());
	}

	public IQuery getQuery(IQuery q) {
		return query;
	}

	public Set<IRule> getRules() {
		return Collections.unmodifiableSet(rules);
	}

	public boolean hasConstructedTerms() {
		for (IRule r : rules) {
			for (ILiteral l : r.getBodyLiterals()) {
				for (Object t : l.getTerms()) {
					if (t instanceof ConstructedTerm) {
						return true;
					}
				}
			}
			// TODO: don't know whether to check the head, too
			for (ILiteral l : r.getHeadLiterals()) {
				for (Object t : l.getTerms()) {
					if (t instanceof ConstructedTerm) {
						return true;
					}
				}
			}
		}
		return false;
	}

	public boolean hasFact(IAtom a) {
		return facts.get(a.getPredicate()).contains(createTupleForAtom(a));
	}

	public boolean hasFacts(IPredicate p) {
		return facts.keySet().contains(p);
	}

	public boolean hasFacts(IPredicate p, Set<ITuple> filter) {
		return (getNumberOfFacts(p, filter) > 0);
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

	public boolean isEmpty() {
		return facts.isEmpty();
	}

	public boolean isStratified() {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean removeFact(IAtom a) {
		IPredicate p = a.getPredicate();
		IRelation r = facts.get(p);
		boolean bChanged = r.remove(createTupleForAtom(a));
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

	public boolean removeQuery(IQuery q) throws DataNotFoundException {
		throw SHOULD_BE_IMMUTABLE;
	}

	public boolean removeRule(IRule r) throws DataNotFoundException {
		throw SHOULD_BE_IMMUTABLE;
	}

	public int ruleCount() {
		return rules.size();
	}

	public void setEvaluationMethod(EvaluationMethod method) {
		// TODO Auto-generated method stub

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
			Collection<ITuple> t) {
		if ((p == null) || (t == null)) {
			throw new NullPointerException();
		}
		Set<IAtom> atoms = new HashSet<IAtom>();
		for (ITuple tup : t) {
			atoms.add(BASIC.createAtom(p, tup.getTerms()));
		}
		return atoms;
	}

	/**
	 * Creates a tuple out of the terms of a atom.
	 * 
	 * @param a
	 *            the atom from which to take the terms
	 * @return the created tuple
	 * @throws NullPointerException
	 *             if the atom is null
	 */
	private static ITuple createTupleForAtom(final IAtom a) {
		if (a == null) {
			throw new NullPointerException();
		}
		return BASIC.createTuple(a.getTerms());
	}
}
