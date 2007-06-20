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

import java.util.Map;
import java.util.Set;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.builtins.BuiltinRegister;

/**
 * <p>
 * Interface of an EDB and IDB (extensional and intensional database). This
 * interface defines a set of methods for managing a logic program.
 * </p>
 * 
 * <p>
 * The interface defines a knowledgebase of a (logic) program. The knowledge
 * base includes:
 * 
 * <ul>
 * <li> facts</li>
 * <li> rules</li>
 * <li> queries</li>
 * </ul>
 * </p>
 * 
 * <p>
 * This interface is used to promote modularity of the inference engine.
 * </p>
 * <p>
 * $Id: IProgram.java,v 1.10 2007-06-14 21:13:40 darko_anicic Exp $
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @date 14.06.2007 16:45:49
 */
public interface IProgram {

	/**
	 * Set of methods for handling the facts:
	 */

	/**
	 * <p>
	 * Adds a fact to the knowledgebase.
	 * </p>
	 * <p>
	 * A fact is a ground atom.
	 * </p>
	 * 
	 * @param f
	 *            A fact to be added.
	 * @return True if a fact has successfully been added, otherwise false.
	 */
	public boolean addFact(final IAtom f);

	/**
	 * <p>
	 * Adds a set of facts to the knowledgebase.
	 * </p>
	 * 
	 * @param facts
	 *            The set of facts to be added.
	 * @return True if any fact from the set of facts have been added, otherwise
	 *         false.
	 */
	public boolean addFacts(final Set<IAtom> facts);

	/**
	 * <p>
	 * Adds a set of facts represented as a relation (with a particular
	 * predicate assigned to) to the knowledgebase.
	 * </p>
	 * 
	 * @param p
	 *            The common predicate for facts from the relation r.
	 * @param r
	 *            The relation which contains facts (tuples) to be stored in the
	 *            knowledgebase.
	 * @return True if any fact from the relation r have been added, otherwise
	 *         false.
	 */
	public boolean addFacts(final IPredicate p, final IMixedDatatypeRelation r);

	/**
	 * <p>
	 * Removes a fact from the knowledgebase.
	 * </p>
	 * 
	 * @param f
	 *            The fact to be removed.
	 * @return True if the fact f has successfully been removed, otherwise
	 *         false.
	 */
	public boolean removeFact(final IAtom f);

	/**
	 * <p>
	 * Removes a set of facts from the knowledgebase.
	 * </p>
	 * 
	 * @param facts
	 *            The facts to be removed.
	 * @return True if the knowledgebase does not contain any fact from the set
	 *         of facts (all facts have been removed from the knowledgebase),
	 *         otherwise false.
	 */
	public boolean removeFacts(final Set<IAtom> facts);

	/**
	 * <p>
	 * Checks whether the knowledgebase contains a fact f.
	 * </p>
	 * 
	 * @param f
	 *            The fact to be checked.
	 * @return True if the knowledgebase contains the fact f, otherwise false.
	 */
	public boolean hasFact(final IAtom f);

	/**
	 * <p>
	 * Checks whether the knowledgebase contains any fact with a predicate p.
	 * </p>
	 * 
	 * @return True if the knowledgebase contains a fact with a predicate p,
	 *         otherwise false.
	 */
	public boolean hasFacts(final IPredicate p);

	/**
	 * <p>
	 * Returns all instantiated predicates from the knowledgebase.
	 * </p>
	 * 
	 * @return Set of all predicates from the knowledgebase.
	 */
	public Set<IPredicate> getPredicates();

	/**
	 * <p>
	 * Register a predicate p. Checks whether the predicate p already exists in
	 * the knowledgebase and returns it. Otherwise it will register the
	 * predicate and return the registered one. The purpos of this mehtod
	 * is, to keep only one instance of each predicate in the vm, so that
	 * the statification works correctly.
	 * </p>
	 * 
	 * @param p
	 *            The predicate to be checked.
	 * @return Returns the registered predicate.
	 */
	public IPredicate registerPredicate(final IPredicate p);

	/**
	 * <p>
	 * Returns the number of facts, for a predicate p, from the knowledgebase.
	 * </p>
	 * 
	 * @param p
	 *            The common predicate of facts which will be retrieved.
	 * @return The number of facts.
	 */
	public int getNumberOfFacts(final IPredicate p);

	/**
	 * <p>
	 * Returns the number of filtered facts, for a predicate from the
	 * knowledgebase, with the selection tuples filter.
	 * </p>
	 * 
	 * @param p
	 *            The common predicate for facts to be retrieved.
	 * @param filter
	 *            Set of selection tuples to be used as a filter (a selection
	 *            pattern).
	 * @return The number of selected facts.
	 */
	public int getNumberOfFacts(final IPredicate p, final Set<ITuple> filter);

	/**
	 * <p>
	 * Returns all facts, from the knowledgebase, for a predicate p.
	 * </p>
	 * 
	 * @param p
	 *            The common predicate for facts to be retrieved.
	 * @return Set of facts (a relation with the predicate p).
	 */
	public IMixedDatatypeRelation getFacts(final IPredicate p);

	/**
	 * <p>
	 * Returns all facts from the knowledgebase.
	 * </p>
	 * 
	 * @return Map of all predicates from the knowledgebase with corresponding
	 *         relations.
	 */
	public Map<IPredicate, IMixedDatatypeRelation> getFacts();

	/**
	 * <p>
	 * Checks whether the knowledgebase contains no facts.<p/>
	 * 
	 * @return True if the knowledgebase contains no facts, otherwise false.
	 */
	public boolean isEmpty();

	/**
	 * <p>
	 * Checks whether the knowledgebase contains a particular term.
	 * </p>
	 * 
	 * @param t
	 *            A term to be checked.
	 * @return True if the knowledgebase contains the terms t, otherwise false.
	 */
	public boolean existsTerm(final ITerm t);

	/**
	 * Set of methods for handling the rules:
	 */

	/**
	 * <p>
	 * Adds a rule to the set of rules of the knowledgebase.
	 * </p>
	 * 
	 * @param r
	 *            A rule to be added.
	 * @return True if the rule r is successfully added into the knowledgebase,
	 *         otherwise false.
	 */
	public boolean addRule(final IRule r);

	/**
	 * <p>
	 * Removes a rule r from the set of rules of the knowledgebase.
	 * </p>
	 * 
	 * @param r
	 *            A rule to be removed.
	 * @return True if the rule r is successfully removed from the
	 *         knowledgebase, otherwise false.
	 */
	public boolean removeRule(final IRule r);

	/**
	 * <p>
	 * Returns all rules from the knowledgebase.
	 * </p>
	 * 
	 * @return Returns set of all rules from the knowledgebase.
	 */
	public Set<IRule> getRules();

	/**
	 * <p>
	 * Checks whether the knowledgebase contains only stratified rules.
	 * </p>
	 * 
	 * @return True if all rules from the knowledgebase are stratified,
	 *         otherwise false.
	 */
	public boolean isStratified();

	/**
	 * <p>
	 * Checks whether the knowledgebase contains rules with negated atoms.
	 * </p>
	 * 
	 * @return True if the knowledgebase contains rules with negated atoms,
	 *         otherwise false.
	 */
	public boolean hasNegation();

	/**
	 * <p>
	 * Checks whether the knowledgebase contains rules with constructed terms.
	 * </p>
	 * 
	 * @return True if the knowledgebase contains constructed terms, otherwise
	 *         false.
	 */
	public boolean hasConstructedTerms();

	/**
	 * <p>
	 * Returns the number of rules in the knowledgebase.
	 * </p>
	 * 
	 * @return The number of rules.
	 */
	public int ruleCount();

	/**
	 * Set of methods for handling the queries:
	 */

	/**
	 * <p>
	 * Adds a query to the knowledgebase.
	 * </p>
	 * 
	 * @param q
	 *            A query to be added.
	 * @return True if the query q has been successfully added, otherwise false.
	 */
	public boolean addQuery(final IQuery q);

	/**
	 * <p>
	 * Returns all queries from the knowledgebase.
	 * </p>
	 * 
	 * @return Returns set of all queries from the knowledgebase.
	 */
	public Set<IQuery> getQueries();

	/**
	 * <p>
	 * Removes a query from the knowledgebase.
	 * </p>
	 * 
	 * @param q
	 *            A query to be removed.
	 * @return True if a query has been successfully removed from the
	 *         knowledgebase, otherwise false.
	 */
	public boolean removeQuery(final IQuery q);
	
	/**
	 * <p>
	 * Removes all facts, rules and queries from the entire program.
	 * </p>
	 */
	public void resetProgram();

	/**
	 * Returns the BuiltinRegister for this program.
	 * @return the register
	 */
	public BuiltinRegister getBuiltinRegister();
}
