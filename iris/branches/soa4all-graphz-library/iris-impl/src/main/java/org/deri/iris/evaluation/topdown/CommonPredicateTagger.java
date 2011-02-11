/*
 * Copyright 2011, Semantic Technology Institute (STI) Innsbruck, 
 * University of Innsbruck, Technikerstrasse 21a, 6020 Innsbruck, Austria.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.deri.iris.evaluation.topdown;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.deri.iris.api.basics.ILiteral;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;

public class CommonPredicateTagger implements IPredicateTagger {
	
	private List<IRule> mRules;
	private Set<IPredicate> mMemoPredicates;

	public CommonPredicateTagger(List<IRule> rules, IQuery query) {
		mRules = rules;
		mMemoPredicates = tagMemoPredicates(query);
	}
	
	private Set<IPredicate> tagMemoPredicates(IQuery query) {
		Set<IPredicate> headPredicates = new HashSet<IPredicate>();
		Set<IPredicate> memoPredicates = new HashSet<IPredicate>();
		List<IPredicate> bodyPredicates = new LinkedList<IPredicate>();
		
		for (ILiteral queryLiteral : query.getLiterals()) {
			headPredicates.add(queryLiteral.getAtom().getPredicate());
		}
		
		for (IPredicate hp : headPredicates) {
			bodyPredicates.addAll( getBodyPredicatesAsList(hp) );
		}
		
		if (!bodyPredicates.isEmpty()) {
			Map<IPredicate, Integer> occurrence = new HashMap<IPredicate, Integer>(); 
			for (IPredicate bp : bodyPredicates) {
				Integer value = occurrence.get(bp);
				value = (value == null) ? 0 : value;
				occurrence.put(bp, ++value);
			}
			
			Map<Integer, IPredicate> counts = new HashMap<Integer, IPredicate>();
			for (Entry<IPredicate, Integer> entry : occurrence.entrySet()) {
				counts.put(entry.getValue(), entry.getKey());
			}
			
			Integer max = Collections.max(counts.keySet());
			if (counts.containsKey( max )) {
				IPredicate mostCommonPredicate = counts.get( max );		
				memoPredicates.add(mostCommonPredicate);
			}
		}
		
		return memoPredicates;
	}
	
	/**
	 * Get body predicates of all rules that match the given predicate.
	 * @param headPredicate predicate
	 * @return list of predicates that depend on <code>headPredicate</code>
	 */
	private List<IPredicate> getBodyPredicatesAsList(IPredicate headPredicate) {
		List<IPredicate> bodyPredicates = new LinkedList<IPredicate>();
		for (IRule rule : mRules) {
			if (getHeadPredicate(rule).equals( headPredicate )) { // Matching rule
				for (ILiteral bodyLiteral : rule.getBody()) { // Add predicates
					bodyPredicates.add( bodyLiteral.getAtom().getPredicate() );
				}
			}
		}		
		return bodyPredicates;
	}
	
	private IPredicate getHeadPredicate(IRule rule) {
		return rule.getHead().get(0).getAtom().getPredicate();
	}

	public List<IRule> getRules() {
		return mRules;
	}

	public Set<IPredicate> getMemoPredicates() {
		return mMemoPredicates;
	}

}