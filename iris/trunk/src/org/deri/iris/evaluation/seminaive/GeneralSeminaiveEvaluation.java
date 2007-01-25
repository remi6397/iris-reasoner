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
package org.deri.iris.evaluation.seminaive;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.deri.iris.api.IProgram;
import org.deri.iris.api.basics.IPredicate;
import org.deri.iris.api.basics.IQuery;
import org.deri.iris.api.evaluation.IBottomUpEvaluator;
import org.deri.iris.api.evaluation.IResultSet;
import org.deri.iris.api.evaluation.algebra.IComponent;
import org.deri.iris.api.evaluation.algebra.IExpressionEvaluator;
import org.deri.iris.evaluation.algebra.Rule2Relation;
import org.deri.iris.exception.DataModelException;
import org.deri.iris.factory.Factory;

/**
 * <p>
 * This is an abstract implementation of  <code>IEvaluator<code> 
 * interface used as a super class for the naïve and 
 * semi-naïve evaluations.
 * </p>
 * 
 * @author Darko Anicic, DERI Innsbruck
 * @author Paco Garcia, University of Murcia
 * @date 08-sep-2006
 */
public abstract class GeneralSeminaiveEvaluation implements IBottomUpEvaluator {
	
	protected IExpressionEvaluator method;

	protected IProgram p;
	
	/** Translator to the relational algebra model. */
	protected Rule2Relation rr = null;

	/** Map of idb literals and coresponding algebra expressions */
	protected Map<IPredicate, IComponent> idbMap;

	protected IResultSet results = null;
	
	GeneralSeminaiveEvaluation(final IExpressionEvaluator e, final IProgram p) {

		this.method = e;
		this.p = p;
		this.rr = new Rule2Relation();
		this.idbMap = rr.translateRules(this.p.getRules());
		this.results = Factory.EVALUATION.createResultSet();
	}
	
	/** Evaluate query */
	public boolean runQuery(IQuery q) 
		throws DataModelException {
		
		/** EVAL (pi, R1,..., Rk, Q1,..., Qm); */
		this.results.getResults().put(
				q.getQueryLiteral(0).getPredicate(), 
				this.method.evaluate(this.rr.translateQuery(q), this.p));
		
		return true;
	}
	
	/** Evaluate queries */
	public boolean runQueries(Set<IQuery> queries) 
		throws DataModelException{
		
		Set<Entry<IPredicate, IComponent>> entrySet = this.rr.translateQueries(queries).entrySet();
		for(Entry<IPredicate, IComponent> entry : entrySet){
			this.results.getResults().put(
					entry.getKey(), 
					this.method.evaluate(entry.getValue(), this.p));
		}	
		return true;
	}
	
	public IResultSet getResultSet() {
		return this.results;
	}
}
