/*
 * MINS (Mins Is Not Silri) A Prolog Egine based on the Silri  
 * 
 * Copyright (C) 1999-2005  Juergen Angele and Stefan Decker
 *                          University of Innsbruck, Austria  
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.deri.iris.api;

import java.util.Set;
import java.util.Vector;

import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.basics.ITuple;
import org.deri.iris.api.terms.ITerm;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.exception.DataNotFoundException;

/**
 * @author Darko Anicic, DERI Innsbruck
 * @date   10.11.2005 
 */
public interface IProgram {
	
	/********************************/
	/*		methods for the EDB     */
	/*      (handling of facts      */
	/********************************/
	
    /**
     * Adds a fact. If rollback is enabled fact is added temporarily. Otherwise
     * the fact is added to the database.
     * DataModelException is thrown if the fact already exists in the knowledge base. 
     */
    public boolean addFact(final IAtom fact); 
   
    /**
     * adds a set of ground facts to the predicate extension
     * @param the set of facts to be added
     * @return has fact been added
     */
    public boolean addFacts(final Set<IAtom> facts); 
    
    /** Removes a fact from the knowledge database */
    public boolean removeFact(final IAtom fact) throws DataNotFoundException;
    
    public boolean removeFacts(final Set<IAtom> facts) throws DataNotFoundException;
    
    public boolean hasFact(final IAtom fact);
    
    /**
     * are there facts for predicate p available
     * @return are there facts for predicate p
     */
    public boolean hasFacts(final IPredicate p);
    
    /**
     * are there facts for predicate p and selection filter f available
     * @param predicat p
     * @param set of selection tuples
     */
    public boolean hasFacts(final IPredicate p, final Set<ITuple> filter);
    
    /**
     * Get all instantiated predicates (predicates with facts)
     * @return Set of all predicates
     */
	public Set<IPredicate> getPredicates();
    
	public int getNumberOfFacts(final IPredicate predicate);
	
	/**
     * returns the number of filtered facts for predicate p from the EDB
     * with selection tuples filter
     * @param predicate p
     * @param set of selection tuples
     * @return the number of facts
     */
    public int getNumberOfFacts(final IPredicate p, final Set<ITuple> filter);
    
    /**
     * returns all facts of a predicate
     * @param predicate p
     * @return an set for the facts of p
     */
    public Set<IAtom> getFacts(final IPredicate p);
    
    /**
     * returns all facts from the Knowledge Base
     * @return an set for the facts
     */
    public Set<IAtom> getFacts();
    
    /**
     * is the edb empty, i.e. does it contain no facts
     * @return it the edb empty
     */
    public boolean isEmpty();
    
    /**
     * does term t exist in edb
     * @param term t
     * @return is term t in edb?
     */
    public boolean existsTerm(final ITerm t);
    
    
    /********************************/
	/*		rules                   */
	/********************************/
	
    /**
	 * does the idb contain stratified rules
	 * @return is the ruleset stratified?
	 */
    public boolean isStratified();
    
    /**
	 * does the idb contain only horn rules
	 * @return is it a horn rule?
	 */
    //public boolean isHorn();
	
	/**
	 * does idb have rules with negated bodies
	 * @return are there negated bodies?
	 */
	public boolean hasNegation();
	
	/**
	 * does idb have rules with constructed terms
	 * @return are there constructed terms involved?
	 */
	public boolean hasConstructedTerms();
    
    /**
     * returns the number of rules in the program.
     * @return the number of rules in program
     */
    public int ruleCount();
    
    /**
     * Adds a rule r to the ruleset. To get correct evaluation results afterwards
     * intermediate results have to be deleted using method ClearRuleSet
     */
    public boolean addRule(final IRule r);
    
    /** 
     * Deletes a rule r from the Ruleset 
     */
    public boolean removeRule(final IRule r) throws DataNotFoundException;
    
    public Set<IRule> getRules();
    
    
    /********************************/
	/*		queries                 */
	/********************************/
	 
    public boolean addQuery(final IQuery q);
    
    /**
     * Returns subsequently all queries.<br>
     * If query==null first query is returned.<br>
     * Throws DataNotFoundException if no further
     * element is available.
     */
    public IQuery nextQuery(final IQuery q) throws DataNotFoundException;
    
    /** Deletes a query q from the Ruleset */
    public boolean removeQuery(final IQuery q) throws DataNotFoundException;
    
    
    /********************************/
	/*		query evaluation        */
	/********************************/
    
    public enum EvaluationMethod {
        SEMI_NAIVE_EVALUATION, 
        DYNAMIC_FILTERING_EVALUATION, 
        WELLFOUNDED_withAFP_EVALUATION, 
        WELLFOUNDED_EVALUATION
    };
    
    /**
     * Determines the evaluation method.<BR>
     * 0: Naive Evaluation (only stratified prorgams)<BR>
     * 1: Dynamic Filtering Evaluation (only stratified prorgams)<BR>
     * 2: Wellfounded Evaluation with alternating fixed point<BR>
     * 3: Wellfounded Evaluation 
     */
    public void setEvaluationMethod(final EvaluationMethod method);
     
    /**
     * Evaluates all queries which have been added to the ruleset. <br>
     * until answers have been derived in case of <br>
     * wellfounded and dynamic evaluation.<br>
     * Returns true if more answers are expected. Must be called until
     * evaluation is finished. <br>
     * The new answers are returned by "LastResult" or "LastSubstitution". All
     * answers up to now are returned by "Result" or "Substitution".
     */
    public boolean evaluate() throws DataModelException;
    
    /**
     * Compute substitutions of the evaluation result for a query q.<br>
     */
    public Vector computeSubstitution(final IQuery q);
    
    /**
     * Returns all the evaluation results of all queries.<br>
     * The result is a vector where each object is a vector of variable<br> 
     * substitutions of the variables in each of queries<br>
     */
    public Vector computeSubstitutions();

}
