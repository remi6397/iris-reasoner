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
package org.deri.iris.evaluation.equivalence;

import java.util.List;

import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.basics.IRule;
import org.deri.iris.facts.IFacts;

/**
 * @author Adrian Marte
 */
public class Program {

	private List<IRule> rules;

	private IFacts facts;

	private List<IQuery> queries;

	public Program(IFacts facts, List<IRule> rules, List<IQuery> queries) {
		this.rules = rules;
		this.facts = facts;
		this.queries = queries;
	}

	public List<IRule> getRules() {
		return rules;
	}

	public void setRules(List<IRule> rules) {
		this.rules = rules;
	}

	public IFacts getFacts() {
		return facts;
	}

	public void setFacts(IFacts facts) {
		this.facts = facts;
	}

	public List<IQuery> getQueries() {
		return queries;
	}

	public void setQueries(List<IQuery> queries) {
		this.queries = queries;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("Facts: ");
		buffer.append(facts);
		buffer.append("\n");
		
		buffer.append("Rules: ");
		buffer.append(rules);
		buffer.append("\n");
		
		buffer.append("Queries: ");
		buffer.append(queries);
		buffer.append("\n");
		
		return buffer.toString();
	}
	
}
