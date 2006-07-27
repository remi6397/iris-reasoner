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
package org.deri.iris.api;

import java.util.Iterator;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.exception.DataNotFoundException;

/**
 * Interface of an EDB - Extensional DataBase used to promote modularity 
 * of the inference engine.
 * 
 * This interface defines an EDB (knowledge base) of a (logic) program. 
 * The knowledge base includes:
 * 	facts
 * 	rules
 * 	queries.
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date   26.07.2006 16:45:49
 */
public interface IEDB {

	/** ***************************** */
	/* methods for the EDB */
	/* (handling facts)    */
	/** ***************************** */

	/**
	 * Adds a fact. If rollback is enabled fact is added temporarily. Otherwise
	 * the fact is added to the database.
	 */
	public boolean addFact(final IAtom fact);

	/**
	 * adds a set of ground facts to the predicate extension
	 * 
	 * @param the
	 *            set of facts to be added
	 * @return has fact been added
	 */
	public boolean addFacts(final Set<IAtom> facts);

	/** Removes a fact from the knowledge database */
	public boolean removeFact(final IAtom fact);

	public boolean removeFacts(final Set<IAtom> facts);

	public boolean hasFact(final IAtom fact);

	/**
	 * are there facts for predicate p available
	 * 
	 * @return are there facts for predicate p
	 */
	public boolean hasFacts(final IPredicate p);

	/**
	 * are there facts for predicate p and selection filter f available
	 * 
	 * @param predicat
	 *            p
	 * @param set
	 *            of selection tuples
	 */
	public boolean hasFacts(final IPredicate p, final Set<ITuple> filter);

	/**
	 * Get all instantiated predicates (predicates with facts)
	 * 
	 * @return Set of all predicates
	 */
	public Set<IPredicate> getPredicates();

	public int getNumberOfFacts(final IPredicate predicate);

	/**
	 * returns the number of filtered facts for predicate p from the EDB with
	 * selection tuples filter
	 * 
	 * @param predicate
	 *            p
	 * @param set
	 *            of selection tuples
	 * @return the number of facts
	 */
	public int getNumberOfFacts(final IPredicate p, final Set<ITuple> filter);

	/**
	 * returns all facts of a predicate
	 * 
	 * @param predicate
	 *            p
	 * @return an set for the facts of p
	 */
	public Set<IAtom> getFacts(final IPredicate p);

	/**
	 * returns all facts from the Knowledge Base
	 * 
	 * @return an set for the facts
	 */
	public Set<IAtom> getFacts();

	/**
	 * is the edb empty, i.e. does it contain no facts
	 * 
	 * @return it the edb empty
	 */
	public boolean isEmpty();

	/**
	 * does term t exist in edb
	 * 
	 * @param term
	 *            t
	 * @return is term t in edb?
	 */
	public boolean existsTerm(final ITerm t);

	/** ***************************** */
	/* rules */
	/** ***************************** */

	/**
	 * Adds a rule r to the ruleset. To get correct evaluation results
	 * afterwards intermediate results have to be deleted using method
	 * ClearRuleSet
	 */
	public boolean addRule(final IRule r);

	/**
	 * Deletes a rule r from the Ruleset
	 */
	public boolean removeRule(final IRule r);

	public Set<IRule> getRules();
	
	/**
	 * does the idb contain stratified rules
	 * 
	 * @return is the ruleset stratified?
	 */
	public boolean isStratified();

	/**
	 * does the idb contain only horn rules
	 * 
	 * @return is it a horn rule?
	 */
	// public boolean isHorn();
	/**
	 * does idb have rules with negated bodies
	 * 
	 * @return are there negated bodies?
	 */
	public boolean hasNegation();

	/**
	 * does idb have rules with constructed terms
	 * 
	 * @return are there constructed terms involved?
	 */
	public boolean hasConstructedTerms();

	/**
	 * returns the number of rules in the program.
	 * 
	 * @return the number of rules in program
	 */
	public int ruleCount();


	/** ***************************** */
	/* queries */
	/** ***************************** */

	public boolean addQuery(final IQuery q);
	
	/**
     * Returns subsequently all queries.<br>
     */
    public Iterator queryIterator();

	/** Deletes a query q from the Ruleset */
	public boolean removeQuery(final IQuery q);

}
