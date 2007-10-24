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
package org.deri.iris.dbstorage;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IMixedDatatypeRelation;

/**
 * <p>
 * This is an implementation of the IEDB interface.
 * </p>
 * <p>
 * This implementaion is thread-save.
 * </p>
 * <p>
 * $Id: Program.java,v 1.10 2007-10-24 15:06:58 bazbishop237 Exp $
 * </p>
 * 
 * @author Richard Pöttler (richard dot poettler at deri dot at)
 * @author Darko Anicic, DERI Innsbruck
 * @version $Revision: 1.10 $
 */
public class Program extends org.deri.iris.Program implements IProgram {

	private DbStorageManager dbm = null;
	
	/**
	 * Creates an empty extensional database (knowledge base) ready to be filled
	 * up with facts, rules and queries.
	 */
	Program(Map<?,?> conf) {
		super();
		
		try {
			dbm = new DbStorageManager(conf);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Creates an extensional database (knowledge base) with predefined facts,
	 * rules and queries. This EDB can also be modified later (e.g. to
	 * add/remove facts, rules and queries).
	 * 
	 * @param f
	 *            a set of ground atoms (facts) to be added into the EDB.
	 * @param r
	 *            a set of rules to be added into the EDB.
	 * @param q
	 *            a set of queries to be added into the EDB.
	 */
	Program(Map<?,?> conf, final Map<IPredicate, IMixedDatatypeRelation> f,
			final Set<IRule> rules, final Set<IQuery> queries) {
		super();
		
		try {
			dbm = new DbStorageManager(conf);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
		
		if (f != null) {
			for (final Map.Entry<IPredicate, IMixedDatatypeRelation> e : f
					.entrySet()) {
				if (e.getValue().getArity() != e.getKey().getArity()) {
					throw new IllegalArgumentException(
							"The arity of the predicate ("
									+ e.getKey().getArity()
									+ ") must match the arity of the relation ("
									+ e.getValue().getArity() + ")");
				} else {
					registerPredicate(e.getKey());
					addFacts(e.getKey(), e.getValue());
				}
			}
		}

		if (rules != null) {
			for (final IRule rule : rules) {
				addRule(rule);
			}
		}
		if (queries != null) {
			for (final IQuery query : queries) {
				addQuery( query );
			}
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
		try {
			if(!dbm.isPredicateRegistered(p))
				dbm.registerPredicate(p);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
	}

	/** ***************************** */
	/* methods for the EDB */
	/* (handling facts) */
	/* storage of facts only in the db */
	/** ***************************** */


	/* modified db commit only after parsing all facts */
	
	public boolean addFact(IAtom a) {
		if (!a.isGround()) {
			throw new IllegalArgumentException("The input parameter: "
					+ a.toString()
					+ " needs to be a ground atom (it is not a fact).");
		}
		IPredicate p = a.getPredicate();
		registerPredicate(p);
		try {
			return dbm.addFact(a);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
	}

	/* modified db commit only after parsing all facts */
	
	public boolean addFacts(Set<IAtom> facts) {

		boolean added = false;
		try {
			dbm.getConnection().setAutoCommit(false);
			for (IAtom f : facts) {
				added |= addFact(f);
			}
			dbm.getConnection().commit();
			dbm.getConnection().setAutoCommit(true);
		} catch (SQLException e) {
			//	silent exception
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
			throw new IllegalArgumentException("Predicate " + p
					+ " is assigned with "
					+ "a relation that has a non-matching arity.");
		}
		registerPredicate(p);
		boolean modified = false;
		try {
			dbm.getConnection().setAutoCommit(false);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		for (final ITuple t : r) {
			if (!t.isGround()) {
				throw new IllegalArgumentException("The fact to add " + t
						+ " must be ground.");
			}
			try {
				modified |= dbm.addTuple(t,p);
			} catch (DbStorageManagerException e) {
				throw new RuntimeException(e);
			} finally{
				try {
					dbm.getConnection().commit();
					dbm.getConnection().setAutoCommit(true);
				} catch (SQLException e) {
					//silent exception
				}
				
			}
		}
		return modified;
	}

	public boolean removeFact(IAtom a) {
		if (a == null) {
			return false;
		}
		IPredicate p = a.getPredicate();
		registerPredicate(p);
		try {
			return dbm.removeFact(a);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
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
		
		IPredicate p = a.getPredicate();
		registerPredicate(p);

		try {
			return dbm.hasFact(a);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
	}

	public IMixedDatatypeRelation getFacts(final IPredicate p) {
		if (p == null) {
			throw new NullPointerException("The predicate must not be null");
		}
		
		registerPredicate(p);
		
		try {
			return dbm.getFacts(p);
		} catch (DbStorageManagerException e) {
			throw new RuntimeException(e);
		}
	}

	public Map<IPredicate, IMixedDatatypeRelation> getFacts() {
		final Map<IPredicate, IMixedDatatypeRelation> ret = new HashMap<IPredicate, IMixedDatatypeRelation>();
		for (final IPredicate p : getPredicates()) {
			ret.put(p, getFacts(p));
		}
		return ret;
	}

	public void resetProgram() {
		//this.facts.clear();
		try {
			// take care it deletes all the data in the db!
			dbm.clear();
		} catch (DbStorageManagerException e) {
			//	silent exception
		}
		super.resetProgram();
	}
}
