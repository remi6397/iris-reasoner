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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IAtom;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.api.storage.IMixedDatatypeRelation;
import org.deri.iris.builtins.BuiltinRegister;

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

	/** The facts of this program. */
	private Facts mFacts = new Facts();

	/** The rule base. */
	private RuleBase mRuleBase = new RuleBase();
	
	/** The queries of this program. */
	private Queries mQueries = new Queries();
	

	/** The register to hold the information about registered builtins. */
	private final BuiltinRegister builtinReg = new BuiltinRegister();
	 

	/**
	 * Creates an empty extensional database (knowledge base) 
	 * ready to be filled up with facts, rules and queries.
	 */
	public Program() {
	}
	
	/**
	 * Creates an extensional database (knowledge base) with 
	 * predefined facts, rules and queries. This EDB can also
	 * be modified later (e.g. to add/remove facts, rules and 
	 * queries).
	 * 
	 * @param facts
	 * 			a set of ground atoms (facts) to be added into the EDB.
	 * @param rules
	 * 			a set of rules to be added into the EDB.
	 * @param queries
	 * 			a set of queries to be added into the EDB.
	 */
	Program(final Map<IPredicate, IMixedDatatypeRelation> facts, final Set<IRule> rules, final Set<IQuery> queries) {
		if (facts != null) {
			for (final Map.Entry<IPredicate, IMixedDatatypeRelation> predicateRelation : facts.entrySet()) {
				if (predicateRelation.getValue().getArity() != predicateRelation.getKey().getArity()) {
					throw new IllegalArgumentException("The arity of the predicate (" + 
							predicateRelation.getKey().getArity() + ") must match the arity of the relation (" + 
							predicateRelation.getValue().getArity() + ")");
				} else {
					mFacts.setAll(predicateRelation.getKey(), predicateRelation.getValue());
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
				mQueries.addQuery( query );
			}
		}
	}

	public int getRuleStrataSize()
	{
		return mRuleBase.getRuleStrataSize();
	}
	
	public Collection<IRule> getRulesOfStratum( int stratum )
	{
		return mRuleBase.getRulesOfStratum( stratum );
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
		
		return mFacts.addFact( a );
	}

	public boolean addFacts(Set<IAtom> facts) {
		return mFacts.addFacts( facts );
	}
	
	public boolean addFacts(IPredicate p, IMixedDatatypeRelation r) {
		return mFacts.addFacts( p, r );
	}

	public boolean removeFact(IAtom a) {
		return mFacts.removeFact( a );
	}

	public boolean removeFacts(Set<IAtom> f) {
		return mFacts.removeFacts( f );
	}
	
	public boolean hasFact(IAtom a) {
		return mFacts.hasFact( a );
	}
	
	public boolean hasFacts(IPredicate p) {
		return mFacts.getFacts( p ).size() > 0;
	}

	public IMixedDatatypeRelation getFacts(final IPredicate p){
		return mFacts.getFacts( p );
	}
	
//	public Map<IPredicate, IMixedDatatypeRelation> getFacts()
//	{
//		return mFacts.getFacts();
//	}
	
	public Map<IPredicate, IMixedDatatypeRelation> getFacts(){
		final Map<IPredicate, IMixedDatatypeRelation> ret = 
			new HashMap<IPredicate, IMixedDatatypeRelation>();
		for (final IPredicate p : getPredicates()) {
			ret.put(p, getFacts(p));
		}
		return ret;
	}
	
	/********************************/
	/*		rules                   */
	/********************************/
	
	public Set<IPredicate> getPredicates() {
		return mRuleBase.getPredicates();
	}

	public boolean addRule(final IRule r) {
		return mRuleBase.addRule(r);
	}

	public boolean removeRule(IRule r){
		return mRuleBase.removeRule( r );
	}
	
	public Set<IRule> getRules() {
		return mRuleBase.getRules();
	}
	
	public boolean isStratified()
	{
		return mRuleBase.stratify();
	}

	public boolean hasNegation() {
		return mRuleBase.hasNegation();
	}
	
	public boolean hasConstructedTerms() {
		return mRuleBase.hasConstructedTerms();
	}
	
	public int ruleCount() {
		return mRuleBase.ruleCount();
	}
	
	public boolean rulesAreSafe()
	{
		return mRuleBase.checkAllRulesSafe();
	}

	/********************************/
	/*		queries                 */
	/********************************/
	
	public boolean addQuery(final IQuery q) {
		return  mQueries.addQuery( q );
	}

	public Set<IQuery> getQueries() {
		return mQueries.getQueries();
	}
	
	public boolean removeQuery(IQuery q) {
		return mQueries.removeQuery( q );
	}
	
	/********************************/
	/*		program                 */
	/********************************/
	
	public void resetProgram(){
		mFacts.clear();
		mQueries.clear();

		mRuleBase.clear();
	}

	public BuiltinRegister getBuiltinRegister() {
		return builtinReg;
	}
}
